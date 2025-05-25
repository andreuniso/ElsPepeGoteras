using RiskClient.Serveis;
using RiskClient.UserControls;
using RiskClient.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using System.Text.Json.Serialization;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Shapes;

namespace RiskClient.Models
{
    public partial class Mapa : Page
    {
        private readonly WebSocketClient wsClient;
        private readonly List<Jugador> jugadors;
        private GameStateData? gameState;
        private Jugador? jugadorSessio;
        private readonly Usuari usuariActual;

        // Controls visuals i mapes de color
        private readonly Dictionary<long, Brush> colorPerJugadorId = new();
        private readonly Dictionary<string, UCJugadorPartida> controlsPerJugador = new();

        // Paleta de colors per jugador
        private readonly List<Brush> paletaColors = new()
        {
            Brushes.Red, Brushes.Green, Brushes.Blue,
            Brushes.Orange, Brushes.Purple, Brushes.Orchid
        };

        public Mapa(List<Jugador> jugadors, JsonElement gameStateJson, WebSocketClient webSocketClient)
        {
            InitializeComponent();

            wsClient = webSocketClient;
            this.jugadors = jugadors;
            usuariActual = UsuariActual.Get()!;

            Debug.WriteLine("🧪 Mode prova actiu: inicialització visual.");

            // 1) Pinta el fons neutre
            InicialitzarMapa();

            // 2) Crea els controls de jugador i assigna colors
            MostrarJugadors(jugadors);

            // 3) Deserialitza el wrapper amb case‐insensitive (mapea "territories" → territories)
            var opts = new JsonSerializerOptions
            {
                PropertyNameCaseInsensitive = true
            };
            var wrapper = JsonSerializer.Deserialize<GameStateWrapper>(
                gameStateJson.GetRawText(),
                opts
            );
            if (wrapper == null)
            {
                Debug.WriteLine("❌ No s'ha pogut deserialitzar el gameState inicial.");
                return;
            }
            gameState = wrapper.data;

            // 4) Identifica el jugador de la sessió
            jugadorSessio = jugadors.Find(j => j.Usuari.Id == usuariActual.Id);
            if (jugadorSessio == null)
            {
                Debug.WriteLine("❌ No s'ha trobat el jugador de la sessió.");
                return;
            }

            // 5) Pintat inicial segons estat
            PintarMapaSegonsGameState();

            // 6) Engega només el listener per a futurs "game_state"
            _ = wsClient.EscoltarAsync(OnWebSocketMessage);
        }

        private void OnWebSocketMessage(JsonElement msg)
        {
            // Només processem els "game_state"
            if (!msg.TryGetProperty("type", out var typeEl) ||
                typeEl.GetString() != "game_state")
                return;

            // Re‐deserialitza amb case‐insensitive
            var opts = new JsonSerializerOptions
            {
                PropertyNameCaseInsensitive = true
            };
            var wrapper = JsonSerializer.Deserialize<GameStateWrapper>(
                msg.GetRawText(),
                opts
            );
            if (wrapper == null) return;

            gameState = wrapper.data;

            // Repinta en el fil de UI
            Dispatcher.Invoke(PintarMapaSegonsGameState);
        }

        private void InicialitzarMapa()
        {
            Debug.WriteLine("🎨 Inicialitzant mapa...");
            foreach (var path in GetAllPaths(MapaCanvas))
            {
                path.Fill = Brushes.LightGray;
            }
        }

        private void MostrarJugadors(List<Jugador> jugadors)
        {
            for (int i = 0; i < jugadors.Count; i++)
            {
                var jugador = jugadors[i];
                var color = paletaColors[i % paletaColors.Count];

                colorPerJugadorId[jugador.Id] = color;

                var control = new UCJugadorPartida();
                control.SetJugador(jugador.Usuari, color);
                controlsPerJugador[jugador.Usuari.Login] = control;
                LlistaJugadorsPanel.Children.Add(control);
            }
            Debug.WriteLine($"👥 {jugadors.Count} jugadors mostrats al panell.");
        }

        private void PintarMapaSegonsGameState()
        {
            if (gameState == null)
            {
                Debug.WriteLine("⚠️ gameState és null; abortant repintat.");
                return;
            }

            Debug.WriteLine($"🧩 Pintant mapa segons estat del joc: {gameState.estat}");

            // 1) Pintem cada territori
            foreach (var t in gameState.territories)
            {
                var tag = t.IdPais.ToString();
                var path = FindPathByTag(tag);
                if (path == null) continue;

                if (t.IdJugador != 0 && colorPerJugadorId.TryGetValue(t.IdJugador, out var brush))
                {
                    path.Fill = brush;
                    var tbName = $"txt_{tag}";
                    if (FindName(tbName) is TextBlock tb)
                    {
                        tb.Text = t.Tropes.ToString();
                        tb.Visibility = Visibility.Visible;
                    }
                }
                else
                {
                    path.Fill = Brushes.LightGray;
                    var tbName = $"txt_{tag}";
                    if (FindName(tbName) is TextBlock tb)
                        tb.Visibility = Visibility.Hidden;
                }
            }

            // 2) Ressalta / mostra badge de torn a la llista de jugadors
            foreach (var kvp in controlsPerJugador)
            {
                var login = kvp.Key;
                var control = kvp.Value;
                var jugador = jugadors.FirstOrDefault(j => j.Usuari.Login == login);
                bool esTorn = jugador != null && jugador.Id == gameState.tornPlayerId;
                control.Resaltar(esTorn);
            }
        }


        private Path? FindPathByTag(string tag)
        {
            foreach (var path in GetAllPaths(MapaCanvas))
            {
                if (path.Tag?.ToString() == tag)
                    return path;
            }
            return null;
        }

        private void country_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            if (gameState == null || jugadorSessio == null)
                return;

            // 1) Només si és el teu torn
            if (jugadorSessio.Id != gameState.tornPlayerId)
            {
                MessageBox.Show(
                    "No és el teu torn — espera que l'altre jugador acabi.",
                    "Torn incorrecte",
                    MessageBoxButton.OK,
                    MessageBoxImage.Information
                );
                return;
            }

            // 2) Extreu l'id del país clicat
            if (!(sender is Path path) || !(path.Tag is string tag) || !int.TryParse(tag, out int idPais))
                return;

            // 3) Decideix quina comanda enviar segons l'estat actual
            string tipus;
            object dades;
            switch (gameState.estat)
            {
                case Estats.COLOCACIO_INICIAL:
                    tipus = "place_troop";
                    dades = new { id_pais = idPais };
                    break;

                case Estats.REFORCAR_PAIS:
                    tipus = "reinforce_countries";
                    dades = new { id_pais = idPais };
                    break;

                default:
                    // Altres estats encara no tenen interacció al mapa
                    MessageBox.Show(
                        "Ara no pots fer aquesta acció.",
                        "Acció no permesa",
                        MessageBoxButton.OK,
                        MessageBoxImage.Warning
                    );
                    return;
            }

            // 4) Munta i envia el missatge
            var msg = new
            {
                type = tipus,
                data = dades
            };
            var json = JsonSerializer.Serialize(msg);
            _ = wsClient.EnviarMissatgeAsync(json);

            Debug.WriteLine($"📤 Enviat '{tipus}' per país {idPais}");
        }


        private void country_MouseEnter(object sender, MouseEventArgs e)
            => Mouse.OverrideCursor = Cursors.Hand;

        private void country_MouseLeave(object sender, MouseEventArgs e)
            => Mouse.OverrideCursor = null;

        private IEnumerable<Path> GetAllPaths(DependencyObject parent)
        {
            int count = VisualTreeHelper.GetChildrenCount(parent);
            for (int i = 0; i < count; i++)
            {
                var child = VisualTreeHelper.GetChild(parent, i);
                if (child is Path p && p.Tag is string)
                    yield return p;
                foreach (var desc in GetAllPaths(child))
                    yield return desc;
            }
        }
    }
}

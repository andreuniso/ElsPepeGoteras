using RiskClient.Serveis;
using RiskClient.UserControls;
using RiskClient.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Shapes;
using System.Net.Http;
using System.Net.Http.Json;
using System.Windows.Media.Imaging;

namespace RiskClient.Models
{
    public partial class Mapa : Page
    {
        private readonly WebSocketClient wsClient;
        private readonly List<Jugador> jugadors;
        private GameStateData? gameState;
        private Jugador? jugadorSessio;
        private readonly Usuari usuariActual;

        private readonly Dictionary<long, Brush> colorPerJugadorId = new();
        private readonly Dictionary<string, UCJugadorPartida> controlsPerJugador = new();

        // Paleta de colors per jugador
        private readonly List<Brush> paletaColors = new()
        {
            Brushes.Red, Brushes.Green, Brushes.Blue,
            Brushes.Orange, Brushes.Purple, Brushes.Orchid
        };

        private int? assignarPaisId = null;
        private int tropesDisponibles = 0;

        private int? origenFortificacio = null;
        private int? destiFortificacio = null;

        private readonly HttpClient http = new HttpClient();
        private Dictionary<int, List<int>> paisosFrontera = new();
        private List<int>? rutaFortificacio = null;
        private List<int>? veinsFortificacio = null;

        private int? origenAtac = null;
        private List<int>? veinsAtac = null;
        private int? destiAtac = null;

        private int tropesAAssignar = 1;      
        private Estats? darreraFaseMostrada = null;

        private readonly Dictionary<Path, Brush> colorsOriginals = new();

        private readonly CancellationTokenSource _cts = new();


        public Mapa(List<Jugador> jugadors, JsonElement gameStateJson, WebSocketClient webSocketClient)
        {
            InitializeComponent();

            wsClient = webSocketClient;
            this.jugadors = jugadors;
            usuariActual = UsuariActual.Get()!;

            _ = LoadAdjacencyAsync();

            InicialitzarMapa();

            MostrarJugadors(jugadors);

            var opts = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
            var wrapper = JsonSerializer.Deserialize<GameStateWrapper>(
                gameStateJson.GetRawText(), opts
            );
            
            if (wrapper == null)
            {
                Debug.WriteLine("❌ No s'ha pogut deserialitzar el gameState inicial.");
                return;
            }
            gameState = wrapper.data;

            jugadorSessio = jugadors.FirstOrDefault(j => j.Usuari.Id == usuariActual.Id);
            if (jugadorSessio == null)
            {
                Debug.WriteLine("❌ No s'ha trobat el jugador de la sessió.");
                return;
            }

            PintarMapaSegonsGameState();

            _ = wsClient.EscoltarAsync(OnWebSocketMessage);
        }

        //aquesta funció carrega les fronteres dels països des del servidor
        private async Task LoadAdjacencyAsync()
        {
            try
            {
                var regions = await http.GetFromJsonAsync<List<RegionInfo>>("http://localhost:8080/api/mapa");
                paisosFrontera = regions
                    .SelectMany(r => r.paisos)
                    .ToDictionary(p => p.id, p => p.fronteres);
            }
            catch (Exception ex)
            {
                Debug.WriteLine("❌ Error carregant /api/mapa: " + ex.Message);
            }
        }

        // Aquesta funció obté una ruta BFS entre dos països, retornant una llista d'IDs de països (m'ha ajudat chatty)
        private List<int>? ObtenirRuta(int origen, int desti)
        {
            var meus = gameState!.territories
                .Where(t => t.IdJugador == jugadorSessio!.Id)
                .Select(t => t.IdPais)
                .ToHashSet();
            if (!meus.Contains(origen) || !meus.Contains(desti))
                return null;

            var prev = new Dictionary<int, int>();
            var q = new Queue<int>();
            q.Enqueue(origen);
            prev[origen] = -1;

            while (q.Count > 0)
            {
                var cur = q.Dequeue();
                if (!paisosFrontera.TryGetValue(cur, out var veïns)) continue;
                foreach (var nxt in veïns)
                {
                    if (!meus.Contains(nxt) || prev.ContainsKey(nxt)) continue;
                    prev[nxt] = cur;
                    if (nxt == desti)
                    {
                        // reconstruir ruta
                        var ruta = new List<int>();
                        for (int p = desti; p != -1; p = prev[p])
                            ruta.Add(p);
                        ruta.Reverse();
                        return ruta;
                    }
                    q.Enqueue(nxt);
                }
            }
            return null;
        }



        private void OnWebSocketMessage(JsonElement msg)
        {
            if (!msg.TryGetProperty("type", out var typeEl) || typeEl.GetString() != "game_state")
            {
                return;
            }
                
            var opts = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
            var wrapper = JsonSerializer.Deserialize<GameStateWrapper>(msg.GetRawText(), opts);

            if (wrapper == null) return;

            gameState = wrapper.data;

            //això és per actualitzar el jugador de la sessió
            Dispatcher.Invoke(() =>
            {
                PintarMapaSegonsGameState();

                if (gameState.DausAtacant != null && gameState.DausDefensor != null)
                {
                    var at = gameState.DausAtacant.ToList();
                    var def = gameState.DausDefensor.ToList();
                    MostrarDaus(at, def);
                }

                ActualitzarOverlayFase(); 

                if (AssignOverlay.Visibility == Visibility.Visible)
                {
                    tropesDisponibles = gameState.availableTroopsActualPlayer;
                    TxtAssignAvailable.Text = $"Disponibles: {tropesDisponibles}";
                }

                if (gameState.estat == Estats.FINAL)
                {
                    _ = Task.Run(async () =>
                    {
                        await Task.Delay(3000); 
                        Dispatcher.Invoke(MostrarFinalPartida);
                    });
                }
            });
        }

        private void MostrarFinalPartida()
        {
            bool heGuanyat = gameState!.territories.All(t => t.IdJugador == jugadorSessio!.Id);

            string imgPath = heGuanyat ? "/Imatges/victoria.png" : "/Imatges/derrota.png";
            ImgFinalResult.Source = new BitmapImage(new Uri(imgPath, UriKind.Relative));

            FinalOverlay.Visibility = Visibility.Visible;
        }

        private async void BtnFinalitzarPartida_Click(object sender, RoutedEventArgs e)
        {
            await SortirISortirAsync();
            NavigationService?.Navigate(new RiskClient.Pagines.PantallaPrincipal());
        }

        private void InicialitzarMapa()
        {
            Debug.WriteLine("🎨 Inicialitzant mapa...");
            foreach (var path in GetAllPaths(MapaCanvas))
                path.Fill = Brushes.LightGray;
        }

        private void MostrarJugadors(List<Jugador> jugadors)
        {
            for (int i = 0; i < jugadors.Count; i++)
            {
                Jugador j = jugadors[i];
                Brush color = paletaColors[i % paletaColors.Count];
                colorPerJugadorId[j.Id] = color;

                UCJugadorPartida ctrl = new UCJugadorPartida();
                ctrl.SetJugador(j.Usuari, color);
                controlsPerJugador[j.Usuari.Login] = ctrl;
                LlistaJugadorsPanel.Children.Add(ctrl);
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

            Debug.WriteLine($"🧩 Pintant mapa segons estat: {gameState.estat}");

            foreach (Okupa t in gameState.territories)
            {
                String tag = t.IdPais.ToString();
                Path? path = FindPathByTag(tag);
                if (path == null) continue;

                if (t.IdJugador != 0 &&
                    colorPerJugadorId.TryGetValue(t.IdJugador, out Brush brush))
                {
                    path.Fill = brush;
                    colorsOriginals[path] = brush;
                    if (FindName($"txt_{tag}") is TextBlock tb)
                    {
                        tb.Text = t.Tropes.ToString();
                        tb.Visibility = Visibility.Visible;
                    }
                }
                else
                {
                    path.Fill = Brushes.LightGray;
                    if (FindName($"txt_{tag}") is TextBlock tb)
                        tb.Visibility = Visibility.Hidden;
                }
            }

            foreach (var kvp in controlsPerJugador)
            {
                String login = kvp.Key;
                UCJugadorPartida ctrl = kvp.Value;
                Jugador? jugador = jugadors.FirstOrDefault(j => j.Usuari.Login == login);
                bool esTorn = jugador != null && jugador.Id == gameState.tornPlayerId;
                ctrl.Resaltar(esTorn);
            }

            if (gameState.estat == Estats.ATAC)
            {
                btnFinishAttack.Content = "Finalitzar atac";
                btnFinishAttack.Visibility = Visibility.Visible;
            }
            else if (gameState.estat == Estats.FORTIFICACIO)
            {
                btnFinishAttack.Content = "Ometre fortificació";
                btnFinishAttack.Visibility = Visibility.Visible;
            }
            else
            {
                btnFinishAttack.Visibility = Visibility.Collapsed;
            }

        }

        private Path? FindPathByTag(string tag)
        {
            foreach (var p in GetAllPaths(MapaCanvas))
                if (p.Tag?.ToString() == tag)
                    return p;
            return null;
        }

        private void country_MouseLeftButtonDown(object sender, MouseButtonEventArgs e)
        {
            if (gameState == null || jugadorSessio == null) return;

            if (jugadorSessio.Id != gameState.tornPlayerId)
            {
                MessageBox.Show(
                    "No és el teu torn — espera l'altre jugador.",
                    "Torn incorrecte",
                    MessageBoxButton.OK,
                    MessageBoxImage.Information
                );
                return;
            }

            if (!(sender is Path path) ||
                !(path.Tag is string tag) ||
                !int.TryParse(tag, out int idPais))
                return;

            switch (gameState.estat)
            {
                case Estats.ASSIGNAR_TROPES:
                    Okupa? territori = gameState.territories
                        .FirstOrDefault(t => t.IdPais == idPais);

                    if (territori == null || territori.IdJugador != jugadorSessio.Id)
                    {
                        MessageBox.Show(
                            "Aquest país no és teu.",
                            "Acció no permesa",
                            MessageBoxButton.OK,
                            MessageBoxImage.Warning
                        );
                        return;
                    }
                        
                    assignarPaisId = idPais;
                    tropesDisponibles = gameState.availableTroopsActualPlayer;
                    TxtAssignInfo.Text = $"Assigna tropes al país {idPais}";
                    TxtAssignAvailable.Text = $"Disponibles: {tropesDisponibles}";
                    SetInitialQty();
                    AssignOverlay.Visibility = Visibility.Visible;
                    break;


                case Estats.COLOCACIO_INICIAL:
                    EnviaMissatge("place_troop", new { id_pais = idPais });
                    break;

                case Estats.REFORCAR_PAIS:
                    EnviaMissatge("reinforce_countries", new { id_pais = idPais });
                    break;

                case Estats.FORTIFICACIO:
                    if (origenFortificacio == null)
                    {
                        var origen = idPais;
                        Okupa tOr = gameState.territories.First(t => t.IdPais == origen);
                        if (tOr.IdJugador != jugadorSessio!.Id || tOr.Tropes < 2) return;

                        origenFortificacio = origen;

                        var meus = gameState.territories
                                          .Where(t => t.IdJugador == jugadorSessio.Id)
                                          .Select(t => t.IdPais)
                                          .ToHashSet();
                        veinsFortificacio = paisosFrontera[origen]
                                                .Where(n => meus.Contains(n) && n != origen)
                                                .ToList();

                        HighlightPath(veinsFortificacio, true);
                    }

                    else if (destiFortificacio == null && idPais != origenFortificacio)
                    {
                        var desti = idPais;
                        HighlightPath(veinsFortificacio, false);

                        var ruta = ObtenirRuta(origenFortificacio.Value, desti);
                        if (ruta == null)
                        {
                            MessageBox.Show("No hi ha cap camí continu pels teus països.",
                                            "No accessible",
                                            MessageBoxButton.OK,
                                            MessageBoxImage.Warning);
                            origenFortificacio = null;
                            veinsFortificacio = null;
                            return;
                        }

                        destiFortificacio = desti;
                        rutaFortificacio = ruta;
                        HighlightPath(ruta, true);

                        var tropesEnt = gameState.territories
                                                .First(t => t.IdPais == origenFortificacio).Tropes;
                        tropesDisponibles = tropesEnt - 1;
                        TxtAssignInfo.Text = $"Mou tropes de {origenFortificacio} → {destiFortificacio}";
                        TxtAssignAvailable.Text = $"Màxim: {tropesDisponibles}";
                        SetInitialQty();
                        AssignOverlay.Visibility = Visibility.Visible;
                    }
                    break;

                case Estats.ATAC:
                    if (origenAtac == null)
                    {
                        var origen = idPais;
                        Okupa toOrigen = gameState.territories.First(t => t.IdPais == origen);
                        if (toOrigen.IdJugador != jugadorSessio.Id || toOrigen.Tropes < 2)
                            return;
                        origenAtac = origen;

                        veinsAtac = paisosFrontera[origen]
                            .Where(n =>
                                gameState.territories.Any(t => t.IdPais == n && t.IdJugador != jugadorSessio.Id))
                            .ToList();

                        HighlightPath(veinsAtac, true);
                    }

                    else if (destiAtac == null)
                    {
                        if (idPais == origenAtac.Value)
                        {
                            HighlightPath(veinsAtac, false);
                            origenAtac = null;
                            veinsAtac = null;
                        }

                        else if (veinsAtac.Contains(idPais))
                        {
                            destiAtac = idPais;
                            HighlightPath(veinsAtac, false);

                            var tropesOrigen = gameState.territories
                                                       .First(t => t.IdPais == origenAtac).Tropes;
                           
                            tropesDisponibles = Math.Min(3, tropesOrigen - 1);

                            TxtAssignInfo.Text = $"Ataca {origenAtac} → {destiAtac}";
                            TxtAssignAvailable.Text = $"Màxim: {tropesDisponibles}";
                            SetInitialQty();                       
                            AssignOverlay.Visibility = Visibility.Visible;
                        }
                        else
                        {
                            HighlightPath(veinsAtac, false);
                            origenAtac = null;
                            veinsAtac = null;

                            var origen = idPais;
                            var toOrigen = gameState.territories.First(t => t.IdPais == origen);
                            if (toOrigen.IdJugador != jugadorSessio.Id || toOrigen.Tropes < 2)
                                return;
                            origenAtac = origen;
                            veinsAtac = paisosFrontera[origen]
                                .Where(n =>
                                    gameState.territories.Any(t => t.IdPais == n && t.IdJugador != jugadorSessio.Id))
                                .ToList();
                            HighlightPath(veinsAtac, true);
                        }
                    }
                    break;

                default:
                    MessageBox.Show(
                        "Ara no pots fer aquesta acció.",
                        "Acció no permesa",
                        MessageBoxButton.OK,
                        MessageBoxImage.Warning
                    );
                    break;
            }
        }

        private void HighlightPath(List<int>? path, bool on)
        {
            if (path == null) return;
            foreach (var paisId in path)
            {
                Path? p = FindPathByTag(paisId.ToString());
                if (p == null) continue;
                if (on)
                {
                    p.Stroke = Brushes.Yellow;
                    p.StrokeThickness = 3;
                }
                else
                {
                    p.Stroke = new SolidColorBrush((Color)ColorConverter.ConvertFromString("#95000000"));
                    p.StrokeThickness = 1;
                }
            }
        }

        private void EnviaMissatge(string tipus, object dades)
        {
            var msg = new { type = tipus, data = dades };
            var json = JsonSerializer.Serialize(msg);
            _ = wsClient.EnviarMissatgeAsync(json);
            Debug.WriteLine($"📤 Enviat '{tipus}'");
        }

        private void country_MouseEnter(object sender, MouseEventArgs e)
        {
            Mouse.OverrideCursor = Cursors.Hand;

            if (sender is Path path)
            {
                if (!colorsOriginals.ContainsKey(path))
                    colorsOriginals[path] = path.Fill.Clone(); 

                var rosaTranslucida = (SolidColorBrush)(new BrushConverter().ConvertFrom("#80FFC0CB")!);
                path.Fill = rosaTranslucida;
            }
        }

        private void country_MouseLeave(object sender, MouseEventArgs e)
        {
            Mouse.OverrideCursor = null;

            if (sender is Path path && colorsOriginals.TryGetValue(path, out var originalBrush))
            {
                path.Fill = originalBrush;
            }
        }

        private IEnumerable<Path> GetAllPaths(DependencyObject parent)
        {
            int n = VisualTreeHelper.GetChildrenCount(parent);
            for (int i = 0; i < n; i++)
            {
                var child = VisualTreeHelper.GetChild(parent, i);
                if (child is Path p && p.Tag is string) yield return p;
                foreach (var desc in GetAllPaths(child))
                    yield return desc;
            }
        }

        private void BtnAssignCancel_Click(object sender, RoutedEventArgs e)
        {
            AssignOverlay.Visibility = Visibility.Collapsed;

            HighlightPath(rutaFortificacio, false);
            HighlightPath(veinsFortificacio, false);

            HighlightPath(veinsAtac, false);

            assignarPaisId = null;
            origenFortificacio = null;
            destiFortificacio = null;
            rutaFortificacio = null;
            veinsFortificacio = null;
            origenAtac = null;
            destiAtac = null;
            veinsAtac = null;
        }

        private void BtnAssignOk_Click(object sender, RoutedEventArgs e)
        {
            int qty = tropesAAssignar;                           
            if (qty < 1 || qty > tropesDisponibles)
            {
                MessageBox.Show($"La quantitat ha d'estar entre 1 i {tropesDisponibles}.");
                return;
            }

            if (qty > tropesDisponibles)
            {
                MessageBox.Show($"Només tens {tropesDisponibles} tropes.");
                return;
            }

            if (gameState.estat == Estats.ASSIGNAR_TROPES && assignarPaisId.HasValue)
            {
                EnviaMissatge("assign_troops", new
                {
                    id_pais = assignarPaisId.Value,
                    qt_tropes = qty
                });
            }
            else if (gameState.estat == Estats.FORTIFICACIO
                     && origenFortificacio.HasValue
                     && destiFortificacio.HasValue)
            {
                EnviaMissatge("fortify", new
                {
                    id_pais_1 = origenFortificacio.Value,
                    id_pais_2 = destiFortificacio.Value,
                    qt_tropes = qty
                });

                HighlightPath(rutaFortificacio, false);
            }
            else if (gameState.estat == Estats.ATAC
             && origenAtac.HasValue
             && destiAtac.HasValue)
            {
                EnviaMissatge("attack", new
                {
                    id_pais_atacant = origenAtac.Value,
                    id_pais_defensiu = destiAtac.Value,
                    qt_tropes_atacant = qty
                });

                HighlightPath(veinsAtac, false);

                origenAtac = null;
                destiAtac = null;
                veinsAtac = null;
            }

            AssignOverlay.Visibility = Visibility.Collapsed;
            assignarPaisId = null;
            origenFortificacio = null;
            destiFortificacio = null;
            rutaFortificacio = null;
        }

        private void btnFinishAttack_Click(object sender, RoutedEventArgs e)
        {
            if (gameState == null || jugadorSessio?.Id != gameState.tornPlayerId)
            {
                MessageBox.Show(
                    "No és el teu torn.",
                    "Atenció",
                    MessageBoxButton.OK,
                    MessageBoxImage.Warning
                );
                return;
            }

            string tipus = gameState.estat switch
            {
                Estats.ATAC => "finish_attack",
                Estats.FORTIFICACIO => "finish_fortify",
                _ => ""
            };

            if (string.IsNullOrEmpty(tipus)) return;

            var msg = new { type = tipus };
            var json = JsonSerializer.Serialize(msg);
            _ = wsClient.EnviarMissatgeAsync(json);

            Debug.WriteLine($"📤 Enviat '{tipus}'");

            btnFinishAttack.Visibility = Visibility.Collapsed;
        }

        private void UpdateQtyDisplay()
        {
            TxtQtyDisplay.Text = tropesAAssignar.ToString();
            BtnAssignOk.IsEnabled = tropesAAssignar >= 1 && tropesAAssignar <= tropesDisponibles;
        }

        private void SetInitialQty()
        {
            tropesAAssignar = 1;
            UpdateQtyDisplay();
        }

        private void BtnQtyDec_Click(object sender, RoutedEventArgs e)
        {
            if (tropesAAssignar > 1)
            {
                tropesAAssignar--;
                UpdateQtyDisplay();
            }
        }

        private void BtnQtyInc_Click(object sender, RoutedEventArgs e)
        {
            if (tropesAAssignar < tropesDisponibles)
            {
                tropesAAssignar++;
                UpdateQtyDisplay();
            }
        }

        private async void ActualitzarOverlayFase()
        {
            if (gameState == null || jugadorSessio == null) return;

            bool esElMeuTorn = jugadorSessio.Id == gameState.tornPlayerId;

            if (!esElMeuTorn)
            {
                if (gameState.estat == Estats.ASSIGNAR_TROPES ||
                    gameState.estat == Estats.ATAC ||
                    gameState.estat == Estats.FORTIFICACIO)
                {
                    if (darreraFaseMostrada != gameState.estat)
                    {
                        MostrarOverlayFase("Esperant el teu torn...");
                        darreraFaseMostrada = gameState.estat;
                    }
                }
                else
                {
                    AmagarOverlayFase();
                    darreraFaseMostrada = null;
                }
                return;
            }

            if (darreraFaseMostrada == gameState.estat)
                return;

            string msg = gameState.estat switch
            {
                Estats.ASSIGNAR_TROPES => "Assignant tropes...",
                Estats.ATAC => "Fase d'atac",
                Estats.FORTIFICACIO => "Fortificant tropes...",
                _ => string.Empty
            };

            if (!string.IsNullOrEmpty(msg))
            {
                darreraFaseMostrada = gameState.estat;
                MostrarOverlayFase(msg);
                await Task.Delay(2000);
                AmagarOverlayFase();
            }
            else
            {
                AmagarOverlayFase();
                darreraFaseMostrada = null;
            }
        }

        private void MostrarOverlayFase(string msg)
        {
            OverlayFaseText.Text = msg;
            OverlayFase.Visibility = Visibility.Visible;
        }

        private void AmagarOverlayFase()
        {
            OverlayFase.Visibility = Visibility.Collapsed;
        }

        private async void MostrarDaus(List<int> atacant, List<int> defensor)
        {
            PanelDausAtacant.Children.Clear();
            PanelDausDefensor.Children.Clear();

            foreach (int val in atacant)
            {
                var img = new Image
                {
                    Source = new BitmapImage(new Uri($"/Recursos/Daus/dau{val}.png", UriKind.Relative)),
                    Width = 40,
                    Height = 40,
                    Margin = new Thickness(5)
                };
                PanelDausAtacant.Children.Add(img);
            }

            foreach (int val in defensor)
            {
                var img = new Image
                {
                    Source = new BitmapImage(new Uri($"/Recursos/Daus/dau{val}.png", UriKind.Relative)),
                    Width = 40,
                    Height = 40,
                    Margin = new Thickness(5)
                };
                PanelDausDefensor.Children.Add(img);
            }

            PanelDaus.Visibility = Visibility.Visible;

            await Task.Delay(2000); 

            PanelDaus.Visibility = Visibility.Collapsed;
        }

        private async Task SortirISortirAsync()
        {
            try
            {
                _cts.Cancel();
                await wsClient.TancarAsync();
            }
            catch (Exception ex)
            {
                Debug.WriteLine($"❌ Error sortint de la partida: {ex.Message}");
            }
        }
    }
}

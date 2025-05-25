using RiskClient.Models;
using RiskClient.Serveis;
using System.Collections.ObjectModel;
using System.Net.WebSockets;
using System.Text.Json;
using System.Text;
using System.Windows;
using System.Windows.Controls;
using RiskClient.UserControls;
using System.Diagnostics;

namespace RiskClient.Pagines
{
    public partial class SalaEspera : Page
    {
        private readonly Jugador jugador;
        private readonly List<Usuari> usuarisActuals = new();
        private readonly WebSocketClient webSocketClient;

        // Per desar l'últim JsonElement de jugadors
        private JsonElement? jugadorsJsonLast = null;

        // CancellationTokenSource per aturar el bucle d'escolta
        private readonly CancellationTokenSource _cts = new();

        public SalaEspera(Jugador jugador, WebSocketClient webSocketClient)
        {
            InitializeComponent();
            this.jugador = jugador;
            this.webSocketClient = webSocketClient;
            DataContext = this;

            TxtNomPartida.Text = jugador.Partida.Nom.ToUpper();
            if (!string.IsNullOrEmpty(jugador.Partida.Token))
            {
                TxtTokenPartida.Text = $"Token: {jugador.Partida.Token}";
                TxtTokenPartida.Visibility = Visibility.Visible;
            }
            else
            {
                TxtTokenPartida.Visibility = Visibility.Collapsed;
            }

            btnIniciar.Visibility = (jugador.Id == jugador.Partida.AdminId)
                ? Visibility.Visible
                : Visibility.Collapsed;

            // Iniciem escolta del WebSocket amb token de cancel·lació
            _ = EscoltarMissatgesAsync(_cts.Token);
        }

        private async Task EscoltarMissatgesAsync(CancellationToken token)
        {
            var buffer = new byte[2048];

            while (webSocketClient.IsConnected && !token.IsCancellationRequested)
            {
                try
                {
                    var result = await webSocketClient.ReceiveAsync(buffer);
                    if (result.MessageType == WebSocketMessageType.Text)
                    {
                        string missatge = Encoding.UTF8.GetString(buffer, 0, result.Count);
                        Debug.WriteLine($"📥 Missatge rebut: {missatge}");
                        ProcessarMissatge(missatge);
                    }
                    else if (result.MessageType == WebSocketMessageType.Close)
                    {
                        Debug.WriteLine("🔴 Connexió WebSocket tancada pel servidor");
                        break;
                    }
                }
                catch (Exception ex)
                {
                    Debug.WriteLine($"❌ Error rebent missatge WebSocket: {ex.Message}");
                    if (!webSocketClient.IsConnected || token.IsCancellationRequested)
                        break;
                }
            }

            Debug.WriteLine("🔴 Bucle d'escolta de SalaEspera finalitzat");
        }

        private void ProcessarMissatge(string missatge)
        {
            Debug.WriteLine("🔵 [SalaEspera] rebent a SalaEspera: " + missatge);

            JsonElement json;
            try
            {
                json = JsonSerializer.Deserialize<JsonElement>(missatge);
            }
            catch (JsonException)
            {
                Debug.WriteLine("❌ JSON invàlid a SalaEspera");
                return;
            }

            if (!json.TryGetProperty("type", out var typeEl))
            {
                Debug.WriteLine("⚠️ Missatge rebut sense camp 'type'");
                return;
            }

            string type = typeEl.GetString()?.ToLower() ?? "";
            Debug.WriteLine($"📌 Tipus de missatge: {type}");

            switch (type)
            {
                case "reload_players":
                    if (json.TryGetProperty("data", out var dataEl) &&
                        dataEl.TryGetProperty("jugadors", out var jugadorsJson))
                    {
                        jugadorsJsonLast = jugadorsJson;
                        Application.Current.Dispatcher.Invoke(() =>
                        {
                            usuarisActuals.Clear();
                            PlayerPanel.Children.Clear();

                            int maxJugadors = jugador.Partida.MaxJugadors;
                            Usuari?[] slots = new Usuari?[maxJugadors];
                            var opts = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };

                            foreach (var jJson in jugadorsJson.EnumerateArray())
                            {
                                var jInfo = JsonSerializer.Deserialize<Jugador>(jJson.GetRawText(), opts);
                                if (jInfo?.Usuari == null) continue;
                                int idx = jInfo.Numero - 1;
                                if (idx >= 0 && idx < maxJugadors)
                                {
                                    slots[idx] = jInfo.Usuari;
                                    usuarisActuals.Add(jInfo.Usuari);
                                }
                            }

                            for (int i = 0; i < maxJugadors; i++)
                            {
                                var u = slots[i] ?? new Usuari { Login = "Esperant...", Wins = 0, Avatar = "" };
                                PlayerPanel.Children.Add(new UCJugador { usuaris = u });
                            }

                            ActualitzaComptadorIControls();
                        });
                    }
                    break;

                case "game_state":
                    Debug.WriteLine("🎮 Rebut primer game_state; navegant al Mapa");

                    // 1) Aturem SalaEspera
                    _cts.Cancel();

                    // 2) Reconstruïm la llista de Jugador a passar
                    var opts2 = new JsonSerializerOptions { PropertyNameCaseInsensitive = true };
                    var jugadorsReals = new List<Jugador>();
                    if (jugadorsJsonLast.HasValue)
                    {
                        foreach (var jJson in jugadorsJsonLast.Value.EnumerateArray())
                        {
                            var jInfo = JsonSerializer.Deserialize<Jugador>(jJson.GetRawText(), opts2);
                            if (jInfo != null) jugadorsReals.Add(jInfo);
                        }
                    }

                    // 3) Naveguem al Mapa amb el JSON sencer (wrapper)
                    Application.Current.Dispatcher.Invoke(() =>
                    {
                        var mapa = new Mapa(jugadorsReals, json, webSocketClient);
                        NavigationService?.Navigate(mapa);
                    });
                    break;

                case "error":
                    if (json.TryGetProperty("error", out var errEl))
                        MessageBox.Show(errEl.GetString() ?? "Error desconegut");
                    break;

                default:
                    Debug.WriteLine($"⚪ Tipus desconegut: {type}");
                    break;
            }
        }


        private void ActualitzaComptadorIControls()
        {
            int actuals = usuarisActuals.Count;
            int maxim = jugador.Partida.MaxJugadors;

            TxtComptadorJugadors.Text = $"{actuals} / {maxim}";
            btnIniciar.IsEnabled = (actuals == maxim);
            ProgressBarCarrega.Visibility = (actuals < maxim) ? Visibility.Visible : Visibility.Collapsed;
            btnIniciar.IsEnabled = (usuarisActuals.Count == jugador.Partida.MaxJugadors) && (jugador.Id == jugador.Partida.AdminId);
        }

        private void btnIniciar_Click(object sender, RoutedEventArgs e)
        {
            if (jugador.Id != jugador.Partida.AdminId)
            {
                MessageBox.Show("Només l'administrador pot iniciar la partida.");
                return;
            }

            // Enviem start_game i esperem que el servidor ens retorni un game_state
            _ = webSocketClient.EnviarMissatgeAsync("{\"type\":\"start_game\"}");
            Debug.WriteLine("📤 Enviat start_game, esperant game_state...");
        }

        private void btnCancelar_Click(object sender, RoutedEventArgs e)
        {
            NavigationService?.GoBack();
        }

        // Aquest mètode es pot cridar des del WebSocket quan s'afegeixi un jugador nou
        public void AfegirJugador(Usuari nouUsuari)
        {
            if (usuarisActuals.Any(u => u.Id == nouUsuari.Id)) return;

            usuarisActuals.Add(nouUsuari);

            var control = new UCJugador
            {
                usuaris = nouUsuari
            };

            PlayerPanel.Children.Add(control);
            ActualitzaComptadorIControls();
        }
    }
}

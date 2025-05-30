using System;
using System.Diagnostics;
using System.Net.WebSockets;
using System.Text;
using System.Text.Json;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;

namespace RiskClient.Serveis
{
    public class WebSocketClient
    {
        private ClientWebSocket _webSocket;
        private bool _isConnecting = false;

        public bool IsConnected => _webSocket != null && _webSocket.State == WebSocketState.Open;

        public async Task ConnectarAsync(long idJugador, long idPartida)
        {
            if (_isConnecting) return;
            _isConnecting = true;

            try
            {
                _webSocket = new ClientWebSocket();
                var uri = new Uri($"ws://localhost:8080/risk?idJugador={idJugador}&idPartida={idPartida}");

                await _webSocket.ConnectAsync(uri, CancellationToken.None);

                // 🔔 Enviem un petit "ping" al servidor per confirmar activitat
                var ping = Encoding.UTF8.GetBytes("{\"type\":\"ping\"}");
                await _webSocket.SendAsync(new ArraySegment<byte>(ping), WebSocketMessageType.Text, true, CancellationToken.None);

                // Esperem un moment per assegurar-nos que la connexió és estable
                await Task.Delay(100);

                if (IsConnected)
                {
                    MessageBox.Show("✅ Connectat al servidor WebSocket!");
                }
                else
                {
                    throw new Exception("No s'ha pogut establir la connexió WebSocket");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"❌ Error connectant: {ex.Message}");
                throw;
            }
            finally
            {
                _isConnecting = false;
            }
        }

        public async Task<WebSocketReceiveResult> ReceiveAsync(byte[] buffer)
        {
            if (!IsConnected)
                throw new InvalidOperationException("WebSocket no està connectat.");

            return await _webSocket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);
        }

        public async Task EnviarMissatgeAsync(string missatge)
        {
            if (IsConnected)
            {
                var buffer = Encoding.UTF8.GetBytes(missatge);
                await _webSocket.SendAsync(new ArraySegment<byte>(buffer), WebSocketMessageType.Text, true, CancellationToken.None);
            }
        }
        public async Task EscoltarAsync(Action<JsonElement> onMissatge)
        {
            var buffer = new byte[4096];

            while (IsConnected)
            {
                try
                {
                    var result = await _webSocket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);
                    Debug.WriteLine("🟣 [EscoltarAsync] Rebut raw message: " + Encoding.UTF8.GetString(buffer, 0, result.Count));


                    if (result.MessageType == WebSocketMessageType.Close)
                    {
                        await _webSocket.CloseAsync(WebSocketCloseStatus.NormalClosure, "Tancat pel servidor", CancellationToken.None);
                        break;
                    }

                    var missatgeJson = Encoding.UTF8.GetString(buffer, 0, result.Count);

                    using var document = JsonDocument.Parse(missatgeJson);
                    JsonElement json = document.RootElement;

                    // Trucar el callback amb el missatge rebut
                    onMissatge?.Invoke(json);
                }
                catch (Exception ex)
                {
                    MessageBox.Show($"❌ Error escoltant missatges: {ex.Message}");
                    break;
                }
            }
        }

        public async Task TancarAsync()
        {
            if (IsConnected)
            {
                await _webSocket.CloseAsync(WebSocketCloseStatus.NormalClosure, "Tancant", CancellationToken.None);
            }
        }
    }
}

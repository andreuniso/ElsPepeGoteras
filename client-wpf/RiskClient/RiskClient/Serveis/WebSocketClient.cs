using System;
using System.Net.WebSockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows;

namespace RiskClient.Serveis
{
    public class WebSocketClient
    {
        private ClientWebSocket _webSocket;

        public async Task ConnectarAsync()
        {
            _webSocket = new ClientWebSocket();

            try
            {
                await _webSocket.ConnectAsync(new Uri("ws://localhost:8080/ws"), CancellationToken.None);
                MessageBox.Show("Connectat al servidor WebSocket!");

                _ = EscoltarMissatgesAsync(); // Escoltem missatges automàticament
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error connectant: {ex.Message}");
            }
        }

        private async Task EscoltarMissatgesAsync()
        {
            var buffer = new byte[1024];

            while (_webSocket.State == WebSocketState.Open)
            {
                var result = await _webSocket.ReceiveAsync(new ArraySegment<byte>(buffer), CancellationToken.None);

                if (result.MessageType == WebSocketMessageType.Text)
                {
                    string missatge = Encoding.UTF8.GetString(buffer, 0, result.Count);
                    MessageBox.Show($"Missatge rebut: {missatge}");
                }
                else if (result.MessageType == WebSocketMessageType.Close)
                {
                    await _webSocket.CloseAsync(WebSocketCloseStatus.NormalClosure, "Tancant", CancellationToken.None);
                }
            }
        }

        public async Task EnviarMissatgeAsync(string missatge)
        {
            if (_webSocket.State == WebSocketState.Open)
            {
                var buffer = Encoding.UTF8.GetBytes(missatge);
                await _webSocket.SendAsync(new ArraySegment<byte>(buffer), WebSocketMessageType.Text, true, CancellationToken.None);
            }
        }

        public async Task TancarAsync()
        {
            if (_webSocket.State == WebSocketState.Open)
            {
                await _webSocket.CloseAsync(WebSocketCloseStatus.NormalClosure, "Tancant", CancellationToken.None);
            }
        }
    }
}

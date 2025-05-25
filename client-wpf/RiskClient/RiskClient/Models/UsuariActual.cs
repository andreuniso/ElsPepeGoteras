using RiskClient.Serveis;
using System;
using System.Threading.Tasks;

namespace RiskClient.Models
{
    public static class UsuariActual
    {
        private static Usuari usuari;
        private static WebSocketClient _webSocketClient;

        public static void Set(Usuari u)
        {
            usuari = u;
        }

        public static Usuari Get()
        {
            return usuari;
        }

        public static void Clear()
        {
            usuari = null;
            _ = _webSocketClient?.TancarAsync(); // Opcional: tancar connexió
            _webSocketClient = null;
        }

        public static bool IsLoggedIn()
        {
            return usuari != null;
        }

        public static WebSocketClient WebSocketClient
        {
            get => _webSocketClient;
            set => _webSocketClient = value;
        }
    }
}

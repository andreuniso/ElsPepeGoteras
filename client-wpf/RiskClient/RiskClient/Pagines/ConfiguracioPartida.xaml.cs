using RiskClient.Models;
using RiskClient.Serveis;
using System.Text.Json;
using System.Windows;
using System.Windows.Controls;

namespace RiskClient.Pagines
{
    public partial class ConfiguracioPartida : Page
    {
        private int quantitatJugadors = 3;

        public ConfiguracioPartida()
        {
            InitializeComponent();
            TxtQuantitatJugadors.Text = quantitatJugadors.ToString();

            BtnDecrementa.Click += BtnDecrementa_Click;
            BtnIncrementa.Click += BtnIncrementa_Click;
        }

        private void BtnDecrementa_Click(object sender, RoutedEventArgs e)
        {
            if (quantitatJugadors > 2)
            {
                quantitatJugadors--;
                TxtQuantitatJugadors.Text = quantitatJugadors.ToString();
            }
        }

        private void BtnIncrementa_Click(object sender, RoutedEventArgs e)
        {
            if (quantitatJugadors < 6)
            {
                quantitatJugadors++;
                TxtQuantitatJugadors.Text = quantitatJugadors.ToString();
            }
        }

        private void BtnCancelar_Click(object sender, RoutedEventArgs e)
        {
            if (this.NavigationService != null && this.NavigationService.CanGoBack)
            {
                this.NavigationService.GoBack();
            }
        }

        private async void btnCrearPartida_Click(object sender, RoutedEventArgs e)
        {
            string nom = TxtNomPartida.Text.Trim();
            if (string.IsNullOrEmpty(nom))
            {
                MessageBox.Show("El nom de la partida no pot estar buit.");
                return;
            }

            Usuari usuari = UsuariActual.Get();
            if (usuari == null)
            {
                MessageBox.Show("No s'ha trobat l'usuari actiu.");
                return;
            }

            bool privada = RbPrivada.IsChecked == true;

            PartidaDTO partidaDTO = new PartidaDTO(nom, quantitatJugadors, privada, usuari.Id);

            try
            {
                PartidaService servei = new PartidaService("http://localhost:8080");
                Jugador? jugador = await servei.CrearPartidaAsync(partidaDTO);

                if (jugador != null)
                {
                    WebSocketClient webSocketClient = new WebSocketClient();

                    try
                    {
                        await webSocketClient.ConnectarAsync(jugador.Id, jugador.Partida.Id);

                        SalaEspera salaEspera = new SalaEspera(jugador, webSocketClient);
                        NavigationService?.Navigate(salaEspera);
                    }
                    catch (Exception wsEx)
                    {
                        MessageBox.Show($"Error connectant al WebSocket: {wsEx.Message}");
                    }
                }
                else
                {
                    MessageBox.Show("❌ No s'ha pogut crear la partida.");
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error: {ex.Message}");
            }
        }
    }
}

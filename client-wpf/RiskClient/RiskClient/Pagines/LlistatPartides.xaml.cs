using RiskClient.Models;
using RiskClient.Serveis;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

namespace RiskClient.Pagines
{
    public partial class LlistatPartides : Page
    {
        private readonly PartidaService partidaService = new PartidaService("http://localhost:8080"); // Canvia URL si cal
        public List<Partida> Partides { get; set; } = new();

        public LlistatPartides()
        {
            InitializeComponent();
            DataContext = this;
        }

        private async void Page_Loaded(object sender, RoutedEventArgs e)
        {
            await CarregarPartides();
        }

        private async Task CarregarPartides()
        {
            try
            {
                Partides = await partidaService.GetPartidesPubliquesAsync() ?? new List<Partida>();
                lvPartides.ItemsSource = Partides;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error en carregar les partides: " + ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private async void lvPartides_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (lvPartides.SelectedItem is Partida partidaSeleccionada)
            {
                await UnirAlaPartidaPublica(partidaSeleccionada);
            }
        }

        private async void btnUnirPrivada_Click(object sender, RoutedEventArgs e)
        {
            string token = txtTokenPrivada.Text.Trim();
            if (string.IsNullOrEmpty(token))
            {
                MessageBox.Show("Has d'introduir un token.", "Advertència", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            var usuari = UsuariActual.Get();
            if (usuari == null)
            {
                MessageBox.Show("No s'ha trobat l'usuari actiu.");
                return;
            }

            var joinDto = new JoinPartidaDTO
            {
                IdPartida = null,
                Token = token,
                IdUsuari = usuari.Id
            };

            try
            {
                var jugador = await partidaService.JoinPartidaAsync(joinDto);
                if (jugador != null)
                {
                    var webSocketClient = new WebSocketClient();
                    await webSocketClient.ConnectarAsync(jugador.Id, jugador.Partida.Id);

                    // Navega a la sala d'espera
                    var salaEspera = new SalaEspera(jugador, webSocketClient);
                    NavigationService?.Navigate(salaEspera);
                }
                else
                {
                    MessageBox.Show("No s'ha pogut unir a la partida privada. Verifica el token.", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error en unir-se a la partida privada: " + ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private async Task UnirAlaPartidaPublica(Partida partida)
        {
            var usuari = UsuariActual.Get();
            if (usuari == null)
            {
                MessageBox.Show("No s'ha trobat l'usuari actiu.");
                return;
            }

            var joinDto = new JoinPartidaDTO
            {
                IdPartida = partida.Id,
                Token = null,
                IdUsuari = usuari.Id
            };

            try
            {
                var jugador = await partidaService.JoinPartidaAsync(joinDto);
                if (jugador != null)
                {
                    var webSocketClient = new WebSocketClient();
                    await webSocketClient.ConnectarAsync(jugador.Id, jugador.Partida.Id);

                    // Navega a la sala d'espera
                    var salaEspera = new SalaEspera(jugador, webSocketClient);
                    NavigationService?.Navigate(salaEspera);
                }
                else
                {
                    MessageBox.Show("No s'ha pogut unir a la partida.", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error en unir-se a la partida: " + ex.Message, "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    }
}
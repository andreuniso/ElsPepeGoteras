using RiskClient.Models;
using RiskClient.Serveis;
using System;
using System.Collections.ObjectModel;
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
        private readonly PartidaService partidaService
            = new PartidaService("http://localhost:8080");

        public ObservableCollection<Partida> Partides { get; }
            = new ObservableCollection<Partida>();

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
                // carrega una llista de les partides públiques 
                List<Partida>? llista = await partidaService.GetPartidesPubliquesAsync()
                            ?? new List<Partida>();

                Partides.Clear();
                foreach (var p in llista)
                    Partides.Add(p);
            }
            catch (Exception ex)
            {
                MessageBox.Show(
                    "Error en carregar les partides: " + ex.Message, "Error", MessageBoxButton.OK,
                    MessageBoxImage.Error
                );
            }
        }

        private async void lvPartides_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (lvPartides.SelectedItem is Partida partidaSeleccionada)
                await UnirAlaPartidaPublica(partidaSeleccionada);
        }

        private async void btnUnirPrivada_Click(object sender, RoutedEventArgs e)
        {
            string token = txtTokenPrivada.Text.Trim().ToUpper();
            if (string.IsNullOrEmpty(token))
            {
                MessageBox.Show("Has d'introduir un token.",
                                "Advertència", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            Usuari usuari = UsuariActual.Get();
            if (usuari == null)
            {
                MessageBox.Show("No s'ha trobat l'usuari actiu.");
                return;
            }

            JoinPartidaDTO joinDto = new JoinPartidaDTO
            {
                IdPartida = null,
                Token = token,
                IdUsuari = usuari.Id
            };

            try
            {
                Jugador? jugador = await partidaService.JoinPartidaAsync(joinDto);
                if (jugador != null)
                {
                    WebSocketClient ws = new WebSocketClient();
                    await ws.ConnectarAsync(jugador.Id, jugador.Partida.Id);
                    NavigationService?.Navigate(new SalaEspera(jugador, ws));
                }
                else
                {
                    MessageBox.Show("Token invàlid o partida no trobada.",
                                    "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(
                    "Error en unir-se a la partida privada: " + ex.Message,
                    "Error", MessageBoxButton.OK, MessageBoxImage.Error
                );
            }
        }

        private async Task UnirAlaPartidaPublica(Partida partida)
        {
            Usuari usuari = UsuariActual.Get();
            if (usuari == null)
            {
                MessageBox.Show("No s'ha trobat l'usuari actiu.");
                return;
            }

            JoinPartidaDTO joinDto = new JoinPartidaDTO
            {
                IdPartida = partida.Id,
                Token = null,
                IdUsuari = usuari.Id
            };

            try
            {
                Jugador? jugador = await partidaService.JoinPartidaAsync(joinDto);
                if (jugador != null)
                {
                    WebSocketClient ws = new WebSocketClient();
                    await ws.ConnectarAsync(jugador.Id, jugador.Partida.Id);
                    NavigationService?.Navigate(new SalaEspera(jugador, ws));
                }
                else
                {
                    MessageBox.Show("No s'ha pogut unir a la partida.",
                                    "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(
                    "Error en unir-se a la partida: " + ex.Message,
                    "Error", MessageBoxButton.OK, MessageBoxImage.Error
                );
            }
        }

        private void btnEnrere_Click(object sender, RoutedEventArgs e)
        {
            if (NavigationService?.CanGoBack == true)
                NavigationService.GoBack();
            else
                NavigationService?.Navigate(new PantallaPrincipal());
        }

    }
}

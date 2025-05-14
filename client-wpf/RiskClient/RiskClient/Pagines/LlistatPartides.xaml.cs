using System.Collections.Generic;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using RiskClient.Models;

namespace RiskClient.Pagines
{
    public partial class LlistatPartides : Page
    {
        public List<Partida> Partides { get; set; } = new List<Partida>();

        //dades de provaaaaa 
        public LlistatPartides()
        {
            InitializeComponent();
            DataContext = this;

            // ➡️ Omplim la llista de partides de prova
            // Omplir la llista de partides de prova
            Partides = new List<Partida>
        {
            new Partida
            {
                Id = 1,
                Nom = "Conquesta Mundial",
                Token = "", // Partida pública
                MaxJugadors = 6,
                AdminId = 123,
                JugadorActualId = 234,
                LlistaJugadorsIds = new List<long> { 234, 567 },
                Estat = Estats.ESPERA,
                DataInici = DateTime.Now.AddMinutes(-10)
            },
            new Partida
            {
                Id = 2,
                Nom = "Batalla Èpica",
                Token = "", // Partida pública
                MaxJugadors = 6,
                AdminId = 124,
                JugadorActualId = 235,
                LlistaJugadorsIds = new List<long> { 234, 235, 678, 789 },
                Estat = Estats.ESPERA,
                DataInici = DateTime.Now.AddMinutes(-5)
            },
            new Partida
            {
                Id = 3,
                Nom = "Privada d'Àlex",
                Token = "ABC123", // Partida privada
                MaxJugadors = 4,
                AdminId = 125,
                JugadorActualId = 236,
                LlistaJugadorsIds = new List<long> { 236 },
                Estat = Estats.ESPERA,
                DataInici = DateTime.Now.AddMinutes(5)
            },
            new Partida
            {
                Id = 4,
                Nom = "Guerra Freda",
                Token = "", // Partida pública
                MaxJugadors = 6,
                AdminId = 126,
                JugadorActualId = 237,
                LlistaJugadorsIds = new List<long> { 237, 238 },
                Estat = Estats.ESPERA,
                DataInici = DateTime.Now.AddMinutes(-20)
            },
            new Partida
            {
                Id = 5,
                Nom = "Partida Misteriosa",
                Token = "XYZ789", // Partida privada
                MaxJugadors = 5,
                AdminId = 127,
                JugadorActualId = 239,
                LlistaJugadorsIds = new List<long> { 239, 240 },
                Estat = Estats.ESPERA,
                DataInici = DateTime.Now.AddMinutes(10)
            }
        };

        }

        private void Page_Loaded(object sender, RoutedEventArgs e)
        {
            // FILTRAR només les partides PÚBLIQUES
            lvPartides.ItemsSource = Partides.Where(p => string.IsNullOrEmpty(p.Token)).ToList();
        }

        private void lvPartides_MouseDoubleClick(object sender, MouseButtonEventArgs e)
        {
            if (lvPartides.SelectedItem is Partida partidaSeleccionada)
            {
                // Unir-se directament a la pública
                UnirAlaPartida(partidaSeleccionada);
            }
        }

        private void btnUnirPrivada_Click(object sender, RoutedEventArgs e)
        {
            string token = txtTokenPrivada.Text.Trim();
            if (!string.IsNullOrEmpty(token))
            {
                Partida? partidaPrivada = Partides.FirstOrDefault(p => p.Token == token);

                if (partidaPrivada != null)
                {
                    UnirAlaPartida(partidaPrivada);
                }
                else
                {
                    MessageBox.Show("No s'ha trobat cap partida amb aquest token.", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                }
            }
            else
            {
                MessageBox.Show("Has d'introduir un token.", "Advertència", MessageBoxButton.OK, MessageBoxImage.Warning);
            }
        }

        private void UnirAlaPartida(Partida partida)
        {
            NavigationService?.Navigate(new SalaEspera());
        }
    }
}

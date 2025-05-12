using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;

namespace RiskClient.Pagines
{
    public partial class PantallaPrincipal : Page
    {
        private bool editant = false;

        public PantallaPrincipal()
        {
            InitializeComponent();
            InicialitzaDades();
        }

        private void InicialitzaDades()
        {
            TxtNomUsuari.Text = "Joan Garcia";
            TxtNickname.Text = "joan123";
            TxtPartidesGuanyades.Text = "3";
            TxtPartidesJugades.Text = "15";
            MostraModeVisualitzacio();
        }

        private void BtnEditaDesa_Click(object sender, RoutedEventArgs e)
        {
            if (!editant)
            {
                ActivaModeEdicio();
            }
            else
            {
                DesaCanvis();
            }
        }

        private void ActivaModeEdicio()
        {
            // Omplim els controls d'edició
            EditNomUsuari.Text = TxtNomUsuari.Text;
            EditNickname.Text = TxtNickname.Text;

            // Amaguem els TextBlock
            TxtNomUsuari.Visibility = Visibility.Collapsed;
            TxtNickname.Visibility = Visibility.Collapsed;

            // Mostrem els controls d'edició
            EditNomUsuari.Visibility = Visibility.Visible;
            EditNickname.Visibility = Visibility.Visible;
            EditContrasenya.Visibility = Visibility.Visible;

            // Actualitzem estat del botó
            IconEditaDesa.Text = "✔";
            AvatarSelector.Visibility = Visibility.Visible;

            editant = true;
        }

        private void DesaCanvis()
        {
            // Guardem les dades
            TxtNomUsuari.Text = EditNomUsuari.Text;
            TxtNickname.Text = EditNickname.Text;

            // Tornem a mostrar els TextBlock
            TxtNomUsuari.Visibility = Visibility.Visible;
            TxtNickname.Visibility = Visibility.Visible;

            // Amaguem els controls d'edició
            EditNomUsuari.Visibility = Visibility.Collapsed;
            EditNickname.Visibility = Visibility.Collapsed;
            EditContrasenya.Visibility = Visibility.Collapsed;

            // Actualitzem estat del botó
            IconEditaDesa.Text = "✎";
            AvatarSelector.Visibility = Visibility.Collapsed;

            editant = false;
        }

        private void MostraModeVisualitzacio()
        {
            TxtNomUsuari.Visibility = Visibility.Visible;
            TxtNickname.Visibility = Visibility.Visible;

            EditNomUsuari.Visibility = Visibility.Collapsed;
            EditNickname.Visibility = Visibility.Collapsed;
            EditContrasenya.Visibility = Visibility.Collapsed;

            IconEditaDesa.Text = "✎";
            AvatarSelector.Visibility = Visibility.Collapsed;
            editant = false;
        }

        private void btnCrearPartida_Click(object sender, RoutedEventArgs e)
        {
            this.NavigationService.Navigate(new ConfiguracioPartida());
        }

        private void AvatarMini_Click(object sender, MouseButtonEventArgs e)
        {
            if (sender is Border border && border.Child is Image miniatura)
            {
                AvatarGran.Source = miniatura.Source;
            }
        }
    }
}

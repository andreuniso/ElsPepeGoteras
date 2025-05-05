using System.Windows;
using System.Windows.Controls;

namespace RiskClient.Pagines
{
    public partial class ConfiguracioPartida : Page
    {
        private int quantitatJugadors = 3; // Valor inicial

        public ConfiguracioPartida()
        {
            InitializeComponent();
            TxtQuantitatJugadors.Text = quantitatJugadors.ToString();

            // Assignem els events als botons
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

    }
}

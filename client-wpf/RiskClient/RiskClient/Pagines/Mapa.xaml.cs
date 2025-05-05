using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;

namespace RiskClient.Models
{
    /// <summary>
    /// Lógica de interacción para Mapa.xaml
    /// </summary>
    public partial class Mapa : Page
    {
        public Mapa()
        {
            InitializeComponent();
        }

        private void country_MouseEnter(object sender, MouseEventArgs e)
        {
            Path clickedPath = (Path)sender; // Això et dona el Path del país que ha estat seleccionat
            clickedPath.Fill = Brushes.LightBlue; // Exemple: canviar el color del país quan el ratolí entra
        }

        private void country_MouseLeave(object sender, MouseEventArgs e)
        {
            Path clickedPath = (Path)sender; // Això et dona el Path del país que ha estat seleccionat
            clickedPath.Fill = Brushes.LightCoral; // Exemple: restaurar el color original quan el ratolí marxa
        }
    }
}

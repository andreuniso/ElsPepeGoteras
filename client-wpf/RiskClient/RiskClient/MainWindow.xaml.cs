using RiskClient.Models;
using System.Windows;

namespace RiskClient
{
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
            // Quan s'obre la finestra, naveguem a la pàgina d'inici de sessió
            MainFrame.Navigate(new IniciSessioRegistre());
        }
    }
}

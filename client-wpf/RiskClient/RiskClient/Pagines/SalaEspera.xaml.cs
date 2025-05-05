using RiskClient.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
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

namespace RiskClient.Pagines
{
    /// <summary>
    /// Lógica de interacción para SalaEspera.xaml
    /// </summary>
    public partial class SalaEspera : Page
    {
        public ObservableCollection<Usuari> Usuaris { get; } = new ObservableCollection<Usuari>
        {
            new Usuari(1, "Alice", "alice123", "pwd", "E:\\M13 - PROJECTE\\RiskClient\\RiskClient\\Imatges\\sala_espera.png", 5, 20),
            new Usuari(2, "Bob", "bob321", "pwd", "E:\\M13 - PROJECTE\\RiskClient\\RiskClient\\Imatges\\sala_espera.png", 3, 12),
            new Usuari(3, "Carol", "carol007", "pwd", "E:\\M13 - PROJECTE\\RiskClient\\RiskClient\\Imatges\\sala_espera.png", 7, 30),
            new Usuari(4, "Dave", "daveX", "pwd", "E:\\M13 - PROJECTE\\RiskClient\\RiskClient\\Imatges\\sala_espera.png", 2, 8),
            new Usuari(5, "Eve", "eve_star", "pwd", "E:\\M13 - PROJECTE\\RiskClient\\RiskClient\\Imatges\\sala_espera.png", 9, 40)
        };
        public SalaEspera()
        {
            InitializeComponent();
            DataContext = this;
        }
    }
}

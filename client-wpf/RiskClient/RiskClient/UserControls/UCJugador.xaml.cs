using RiskClient.Models;
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

namespace RiskClient.UserControls
{
    /// <summary>
    /// Lógica de interacción para UCJugador.xaml
    /// </summary>
    public partial class UCJugador : UserControl
    {
        public UCJugador()
        {
            InitializeComponent();
        }

        public Usuari usuaris
        {
            get { return (Usuari)GetValue(usuarisProperty); }
            set { SetValue(usuarisProperty, value); }
        }

        // Using a DependencyProperty as the backing store for MyProperty.  This enables animation, styling, binding, etc...
        public static readonly DependencyProperty usuarisProperty =
            DependencyProperty.Register("usuaris", typeof(Usuari), typeof(UCJugador), new PropertyMetadata(null));


    }
}

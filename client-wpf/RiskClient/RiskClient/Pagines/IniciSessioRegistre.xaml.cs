<<<<<<< HEAD
﻿using RiskClient.Serveis;
using System;
=======
﻿using System;
>>>>>>> 0ee73eab525e45adc4986e3b0cbe15fd89680742
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
    /// Lógica de interacción para IniciSessioRegistre.xaml
    /// </summary>
    public partial class IniciSessioRegistre : Page
    {
<<<<<<< HEAD
        private WebSocketClient _webSocketClient;

        public IniciSessioRegistre()
        {
            InitializeComponent();
            _webSocketClient = new WebSocketClient();
            _webSocketClient.ConnectarAsync();
        }

        private void BtnInicia_Click(object sender, RoutedEventArgs e)
        {
            NavigationService?.Navigate(new RiskClient.Pagines.PantallaPrincipal());
=======
        public IniciSessioRegistre()
        {
            InitializeComponent();
>>>>>>> 0ee73eab525e45adc4986e3b0cbe15fd89680742
        }
    }
}

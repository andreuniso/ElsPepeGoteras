using RiskClient.Models;
using System.Windows;
using System.Windows.Controls;

namespace RiskClient.UserControls
{
    public partial class UCPartida : UserControl
    {
        public UCPartida()
        {
            InitializeComponent();
        }

        public Partida partida
        {
            get { return (Partida)GetValue(PartidaProperty); }
            set { SetValue(PartidaProperty, value); }
        }

        public static readonly DependencyProperty PartidaProperty =
            DependencyProperty.Register("partida", typeof(Partida), typeof(UCPartida), new PropertyMetadata(null));        
        
    }
}

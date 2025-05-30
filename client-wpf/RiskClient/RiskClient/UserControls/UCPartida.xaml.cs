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
            get { return (Partida)GetValue(partidaProperty); }
            set { SetValue(partidaProperty, value); }
        }

        public static readonly DependencyProperty partidaProperty =
            DependencyProperty.Register("partida", typeof(Partida), typeof(UCPartida), new PropertyMetadata(null));        
        
    }
}

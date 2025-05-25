using RiskClient.Models;
using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media.Imaging;

namespace RiskClient.UserControls
{
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

        public static readonly DependencyProperty usuarisProperty =
            DependencyProperty.Register("usuaris", typeof(Usuari), typeof(UCJugador), new PropertyMetadata(null, OnUsuariChanged));

        private static void OnUsuariChanged(DependencyObject d, DependencyPropertyChangedEventArgs e)
        {
            var control = d as UCJugador;
            control?.ActualitzaAvatar();
            control.DataContext = control.usuaris; // <-- Binding correcte
        }

        private void ActualitzaAvatar()
        {
            if (usuaris != null && !string.IsNullOrEmpty(usuaris.Avatar))
            {
                try
                {
                    ImgAvatar.Source = new BitmapImage(new Uri($"http://localhost:8080/avatars/{usuaris.Avatar}", UriKind.Absolute));
                }
                catch
                {
                    ImgAvatar.Source = null;
                }
            }
            else
            {
                ImgAvatar.Source = null;
            }
        }
    }
}

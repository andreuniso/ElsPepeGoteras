using RiskClient.Models;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using System.Windows.Media.Effects;
using System.Windows.Media.Imaging;

namespace RiskClient.UserControls
{
    public partial class UCJugadorPartida : UserControl
    {
        public UCJugadorPartida()
        {
            InitializeComponent();
        }

        public void SetJugador(Usuari usuari, Brush colorFons)
        {
            // Fons del jugador
            MainBorder.Background = colorFons;

            // Nom
            TxtNom.Text = usuari.Login;

            // Avatar
            if (!string.IsNullOrEmpty(usuari.Avatar))
            {
                try
                {
                    ImgAvatar.Source = new BitmapImage(
                        new System.Uri(
                            $"http://localhost:8080/avatars/{usuari.Avatar}",
                            System.UriKind.Absolute));
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

        public void Resaltar(bool esTorn)
        {
            if (esTorn)
            {
                // Glow entorn
                MainBorder.BorderBrush = Brushes.Black;
                MainBorder.BorderThickness = new Thickness(4);
                MainBorder.Effect = new DropShadowEffect
                {
                    Color = Colors.Black,
                    Direction = 0,
                    ShadowDepth = 0,
                    BlurRadius = 10,
                    Opacity = 0.8
                };
            }
            else
            {
                TurnBadge.Visibility = Visibility.Collapsed;
                MainBorder.Margin = new Thickness(4);

                MainBorder.BorderBrush = Brushes.Transparent;
                MainBorder.BorderThickness = new Thickness(0);
                MainBorder.Effect = null;
            }
        }
    }
}

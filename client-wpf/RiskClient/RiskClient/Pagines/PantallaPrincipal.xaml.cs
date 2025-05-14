using RiskClient.Models;
using RiskClient.Serveis;
using System;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using System.Windows.Media.Imaging;
using System.Windows.Media;

namespace RiskClient.Pagines
{
    public partial class PantallaPrincipal : Page
    {
        private bool editant = false;
        private readonly UserService _userService;
        private List<string> _avatars = new();

        public PantallaPrincipal()
        {
            InitializeComponent();
            _userService = new UserService("http://localhost:8080");
            InicialitzaDades();
        }

        private async void InicialitzaDades()
        {
            var usuari = UsuariActual.Get();
            if (usuari == null)
            {
                MessageBox.Show("Error: no hi ha usuari actiu");
                return;
            }

            TxtNomUsuari.Text = usuari.Nom;
            TxtNickname.Text = usuari.Login;
            TxtPartidesGuanyades.Text = usuari.Wins.ToString();
            TxtPartidesJugades.Text = usuari.GamesPlayed.ToString();

            AvatarGran.Source = new BitmapImage(new Uri($"http://localhost:8080/avatars/{usuari.Avatar}"));

            await CarregaAvatars();
            MostraModeVisualitzacio();
        }

        private async Task CarregaAvatars()
        {
            try
            {
                _avatars = await _userService.GetAvatarsAsync();

                if (_avatars.Count < 4) return;

                AvatarMini0.Source = new BitmapImage(new Uri($"http://localhost:8080/avatars/{_avatars[0]}", UriKind.Absolute));
                AvatarMini1.Source = new BitmapImage(new Uri($"http://localhost:8080/avatars/{_avatars[1]}", UriKind.Absolute));
                AvatarMini2.Source = new BitmapImage(new Uri($"http://localhost:8080/avatars/{_avatars[2]}", UriKind.Absolute));
                AvatarMini3.Source = new BitmapImage(new Uri($"http://localhost:8080/avatars/{_avatars[3]}", UriKind.Absolute));
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error al carregar avatars: {ex.Message}");
            }
        }


        private void BtnEditaDesa_Click(object sender, RoutedEventArgs e)
        {
            if (!editant)
            {
                ActivaModeEdicio();
            }
            else
            {
                DesaCanvis();
            }
        }

        private void ActivaModeEdicio()
        {
            EditNomUsuari.Text = TxtNomUsuari.Text;
            EditNickname.Text = TxtNickname.Text;

            TxtNomUsuari.Visibility = Visibility.Collapsed;
            TxtNickname.Visibility = Visibility.Collapsed;

            EditNomUsuari.Visibility = Visibility.Visible;
            EditNickname.Visibility = Visibility.Visible;
            EditContrasenya.Visibility = Visibility.Visible;

            IconEditaDesa.Text = "✔";
            AvatarSelector.Visibility = Visibility.Visible;

            editant = true;
        }

        private async void DesaCanvis()
        {
            var usuari = UsuariActual.Get();
            if (usuari == null)
            {
                MessageBox.Show("No hi ha cap usuari actiu.");
                return;
            }

            string nouNom = EditNomUsuari.Text.Trim();
            string nouLogin = EditNickname.Text.Trim();
            string novaContrasenya = EditContrasenya.Password.Trim();

            string nouAvatar = _avatars.FirstOrDefault(a => AvatarGran.Source?.ToString().EndsWith(a) == true) ?? usuari.Avatar;

            var usuariActualitzat = new Usuari
            {
                Id = usuari.Id,
                Nom = nouNom,
                Login = nouLogin,
                Contrasenya = string.IsNullOrEmpty(novaContrasenya) ? usuari.Contrasenya : novaContrasenya,
                Avatar = nouAvatar,
                Wins = usuari.Wins,
                GamesPlayed = usuari.GamesPlayed
            };

            try
            {
                var usuariRes = await _userService.ActualitzarUsuariAsync(usuariActualitzat);
                if (usuariRes == null)
                {
                    MessageBox.Show("⚠️ No s'han pogut desar els canvis.");
                }
                else
                {
                    UsuariActual.Set(usuariRes);
                    MessageBox.Show("✅ Canvis desats correctament.");
                }
            }
            catch (Exception)
            {
                MessageBox.Show("❌ Error en desar els canvis.");
            }
            finally
            {
                InicialitzaDades();
                MostraModeVisualitzacio();
            }
        }


        private void MostraModeVisualitzacio()
        {
            TxtNomUsuari.Visibility = Visibility.Visible;
            TxtNickname.Visibility = Visibility.Visible;

            EditNomUsuari.Visibility = Visibility.Collapsed;
            EditNickname.Visibility = Visibility.Collapsed;
            EditContrasenya.Visibility = Visibility.Collapsed;

            IconEditaDesa.Text = "✎";
            AvatarSelector.Visibility = Visibility.Collapsed;
            editant = false;
        }

        private void btnCrearPartida_Click(object sender, RoutedEventArgs e)
        {
            this.NavigationService.Navigate(new ConfiguracioPartida());
        }

        private void AvatarMini_Click(object sender, MouseButtonEventArgs e)
        {
            if (sender is Border border && border.Child is Image miniatura)
            {
                AvatarGran.Source = miniatura.Source;
            }
        }


        private void btnUnirse_Click(object sender, RoutedEventArgs e)
        {
            this.NavigationService.Navigate(new LlistatPartides());
        }
    }
}
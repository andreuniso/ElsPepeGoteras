using RiskClient.Models;
using RiskClient.Serveis;
using System;
using System.Windows;
using System.Windows.Controls;

namespace RiskClient.Models
{
    /// <summary>
    /// Lógica de interacción para IniciSessioRegistre.xaml
    /// </summary>
    public partial class IniciSessioRegistre : Page
    {
        private WebSocketClient _webSocketClient;
        private readonly UserService _userService;

        public IniciSessioRegistre()
        {
            InitializeComponent();
            _userService = new UserService("http://localhost:8080");
        }

        private async void BtnInicia_Click(object sender, RoutedEventArgs e)
        {
            BtnInicia.IsEnabled = false;

            string login = TxtUsuari.Text;
            string password = TxtContrasenya.Password;

            if (string.IsNullOrWhiteSpace(login) || string.IsNullOrWhiteSpace(password))
            {
                MessageBox.Show("El nom d'usuari i la contrasenya no poden estar buits.");
                BtnInicia.IsEnabled = true;
                return;
            }

            Usuari usuari = new Usuari(login, password);
            Usuari? usuariAutenticat;
            try
            {
                usuariAutenticat = await _userService.LoginAsync(usuari);

                if (usuariAutenticat != null)
                {
                    // Desa l'usuari globalment per utilitzar-lo més endavant (crear/join partida)
                    UsuariActual.Set(usuariAutenticat);

                    // 🧭 Navega a la pantalla principal (crear/entrar a partida)
                    NavigationService?.Navigate(new RiskClient.Pagines.PantallaPrincipal());
                }
                else
                {
                    MessageBox.Show("Credencials incorrectes");
                    BtnInicia.IsEnabled = true;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error de connexió: {ex.Message}");
                BtnInicia.IsEnabled = true;
            }
        }

        private void RegistraText_MouseLeftButtonDown(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            LoginPanel.Visibility = Visibility.Collapsed;
            RegistrePanel.Visibility = Visibility.Visible;
        }

        private void IniciaSessioText_MouseLeftButtonDown(object sender, System.Windows.Input.MouseButtonEventArgs e)
        {
            RegistrePanel.Visibility = Visibility.Collapsed;
            LoginPanel.Visibility = Visibility.Visible;
        }

        private async void BtnRegistrar_Click(object sender, RoutedEventArgs e)
        {
            string nom = TxtNom.Text.Trim();
            string login = TxtNickname.Text.Trim();
            string contrasenya = TxtPasswordRegistre.Password;
            string confirmaContrasenya = TxtConfirmaPassword.Password;

            // Validació bàsica
            if (string.IsNullOrEmpty(nom) || string.IsNullOrEmpty(login) || string.IsNullOrEmpty(contrasenya))
            {
                MessageBox.Show("Tots els camps són obligatoris.", "Error", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            if (contrasenya != confirmaContrasenya)
            {
                MessageBox.Show("Les contrasenyes no coincideixen.", "Error", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            try
            {
                Usuari usuari = new Usuari(nom, login, contrasenya);
                UserService userService = new UserService("http://localhost:8080");

                Usuari? usuariRegistrat = await userService.RegisterAsync(usuari);

                if (usuariRegistrat != null)
                {
                    MessageBox.Show("Registre complet! Ja pots iniciar sessió.", "Èxit", MessageBoxButton.OK, MessageBoxImage.Information);

                    RegistrePanel.Visibility = Visibility.Collapsed;
                    LoginPanel.Visibility = Visibility.Visible;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error durant el registre: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    
    }
}

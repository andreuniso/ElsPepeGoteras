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
            // Instanciem el servei HTTP amb la URL base del servidor
            _userService = new UserService("http://localhost:8080");
        }

        private async void BtnInicia_Click(object sender, RoutedEventArgs e)
        {
            // Deshabilitem el botó per evitar múltiples clics
            BtnInicia.IsEnabled = false;

            // Recollim les credencials de la UI
            string login = TxtUsuari.Text;
            string password = TxtContrasenya.Password;

            // Validem que els camps no estiguin buits
            if (string.IsNullOrWhiteSpace(login) || string.IsNullOrWhiteSpace(password))
            {
                MessageBox.Show("El nom d'usuari i la contrasenya no poden estar buits.");
                BtnInicia.IsEnabled = true;
                return;
            }

            // Creem l'usuari i fem login
            var usuari = new Usuari(login, password);
            Usuari? usuariAutenticat;
            try
            {
                // Comprovem si les credencials són vàlides
                usuariAutenticat = await _userService.LoginAsync(usuari);
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Error de connexió: {ex.Message}");
                BtnInicia.IsEnabled = true;
                return;
            }

            if (usuariAutenticat == null)
            {
                MessageBox.Show("Credencials incorrectes");
                BtnInicia.IsEnabled = true;
                return;
            }

            // Un cop login OK, connectem via WebSocket
            _webSocketClient = new WebSocketClient();
            await _webSocketClient.ConnectarAsync();

            // Naveguem a la pantalla principal
            NavigationService?.Navigate(new RiskClient.Pagines.PantallaPrincipal());
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
            
        }
    }
}

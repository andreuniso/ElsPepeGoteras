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
                    UsuariActual.Set(usuariAutenticat); 
                }
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

            //Aixo çes per conectar amb el websockeet
            _webSocketClient = new WebSocketClient();
            await _webSocketClient.ConnectarAsync();

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

using System;
using System.Collections.Generic;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Controls;
using System.Windows.Media.Imaging;
using System.Windows;

namespace RiskClient.UserControls
{
    public partial class UCDaus : UserControl
    {
        private readonly Random _random = new Random();
        private CancellationTokenSource? _ctsAnimacio;

        public UCDaus()
        {
            InitializeComponent();
        }

        public void MostrarDausReals(List<int> valors)
        {
            _ctsAnimacio?.Cancel(); // Atura animació si n'hi havia
            DausPanel.Children.Clear();

            foreach (var valor in valors)
            {
                DausPanel.Children.Add(CrearImatgeDau(valor));
            }
        }

        public async Task IniciarAnimacioAsync(int quantitat)
        {
            _ctsAnimacio = new CancellationTokenSource();
            var token = _ctsAnimacio.Token;

            try
            {
                while (!token.IsCancellationRequested)
                {
                    DausPanel.Children.Clear();
                    for (int i = 0; i < quantitat; i++)
                    {
                        int aleatori = _random.Next(1, 7);
                        DausPanel.Children.Add(CrearImatgeDau(aleatori));
                    }

                    await Task.Delay(150, token);
                }
            }
            catch (TaskCanceledException) { }
        }

        public void AturarAnimacio()
        {
            _ctsAnimacio?.Cancel();
        }

        private Image CrearImatgeDau(int valor)
        {
            return new Image
            {
                Width = 40,
                Height = 40,
                Margin = new Thickness(4),
                Source = new BitmapImage(new Uri($"/Recursos/Daus/dau{valor}.png", UriKind.Relative))
            };
        }
    }
}

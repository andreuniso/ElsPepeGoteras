using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media.Imaging;

namespace RiskClient.Models
{
    public class Usuari
    {
        public int Id { get; set; }
        public string Nom { get; set; }
        public string Login { get; set; }
        public string Password { get; set; }
        public string Avatar { get; set; }
        public int Wins { get; set; }
        public int Games { get; set; }

        public Usuari() { }

        public Usuari(int id, string nom, string login, string password, string avatar, int wins, int games)
        {
            Id = id;
            Nom = nom;
            Login = login;
            Password = password;
            Avatar = avatar;
            Wins = wins;
            Games = games;
        }

        public Usuari(string nom, string login, string password)
        {
            Nom = nom;
            Login = login;
            Password = password;
        }

        public Usuari(string login, string password)
        {
            Login = login;
            Password = password;
        }
    }
}

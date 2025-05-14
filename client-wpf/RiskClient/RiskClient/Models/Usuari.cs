using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RiskClient.Models
{
    public class Usuari
    {
        public int Id { get; set; }
        public string Nom { get; set; }
        public string Login { get; set; }

        public string Contrasenya { get; set; }

        public string Avatar { get; set; }

        public int Wins { get; set; }
        public int GamesPlayed { get; set; }

        public Usuari() { }

        public Usuari(int id, string nom, string login, string contrasenya, string avatar, int wins, int gamesPlayed)
        {
            Id = id;
            Nom = nom;
            Login = login;
            Contrasenya = contrasenya;
            Avatar = avatar;
            Wins = wins;
            GamesPlayed = gamesPlayed;
        }

        public Usuari(string nom, string login, string contrasenya)
        {
            Nom = nom;
            Login = login;
            Contrasenya = contrasenya;
        }

        public Usuari(string login, string contrasenya)
        {
            Login = login;
            Contrasenya = contrasenya;
        }
    }
}

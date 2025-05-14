using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RiskClient.Models
{
    public static class UsuariActual
    {
        private static Usuari usuari;

        public static void Set(Usuari u)
        {
            usuari = u;
        }

        public static Usuari Get()
        {
            return usuari;
        }

        public static void Clear()
        {
            usuari = null;
        }

        public static bool IsLoggedIn()
        {
            return usuari != null;
        }
    }
}


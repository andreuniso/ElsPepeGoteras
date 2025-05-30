using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Policy;
using System.Text;
using System.Threading.Tasks;

namespace RiskClient.Models
{
    public class PartidaDTO
    {
        public String Nom { get; set; }
        public int MaxJugadors { get; set; }
        public Boolean EsPrivada { get; set; }
        public long UserAdminId { get; set; }

        public PartidaDTO(string nom, int maxJugadors, Boolean esPrivada, long userAdminId)
        {
            Nom = nom;
            EsPrivada = esPrivada;
            MaxJugadors = maxJugadors;
            UserAdminId = userAdminId;
        }
    }
    
}

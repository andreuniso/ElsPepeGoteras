using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Media;

namespace RiskClient.Models
{
    public class PaisInfo
    {
        public string Nom { get; set; }
        public int Tropes { get; set; }
        public string JugadorId { get; set; } // Ex: "J1", "J2", etc.
    }

}

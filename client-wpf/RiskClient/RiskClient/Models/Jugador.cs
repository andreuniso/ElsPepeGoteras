using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RiskClient.Models
{
    public class Jugador
    {
        public int Id { get; set; }
        public int UsuariId { get; set; }
        public int PartidaId { get; set; }
        public int Numero { get; set; }
    }
}

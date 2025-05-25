using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace RiskClient.Models
{
    public class JoinPartidaDTO
    {
        public long? IdPartida { get; set; }  
        public string? Token { get; set; }
        public long IdUsuari { get; set; }
    }

}

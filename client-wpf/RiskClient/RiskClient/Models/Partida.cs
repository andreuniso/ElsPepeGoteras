using System;
using System.Collections.Generic;

namespace RiskClient.Models
{
    public class Partida
    {
        public long Id { get; set; }
        public DateTime DataInici { get; set; }
        public string Nom { get; set; }
        public string ?Token { get; set; }
        public int MaxJugadors { get; set; }

        public long AdminId { get; set; }
        public long JugadorActualId { get; set; }
        public List<long> LlistaJugadorsIds { get; set; }

        public Estats Estat { get; set; }  
    }
}

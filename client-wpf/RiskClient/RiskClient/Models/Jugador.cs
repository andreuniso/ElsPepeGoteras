using System.Collections.Generic;

namespace RiskClient.Models
{
    public class Jugador
    {
        public long Id { get; set; }
        public Usuari Usuari { get; set; }
        public Partida Partida { get; set; }
        public int Numero { get; set; }
        public List<Okupa> PaisosOkupats { get; set; }
        public List<Carta> Cartes { get; set; }
    }

    public class Carta
    {
        // afegeix camps reals quan toqui
    }

    

}

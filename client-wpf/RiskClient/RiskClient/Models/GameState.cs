using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.Json.Serialization;
using System.Threading.Tasks;

namespace RiskClient.Models
{
    public class GameStateData
    {
        public int id { get; set; }
        [JsonConverter(typeof(DateTimeOrLongConverter))]
        public DateTime dataInici { get; set; }
        public string nom { get; set; }
        public string? token { get; set; }
        public int maxJugadors { get; set; }
        public int adminId { get; set; }
        public int tornPlayerId { get; set; }

        [JsonConverter(typeof(JsonStringEnumConverter))]
        public Estats estat { get; set; }

        public int availableTroopsActualPlayer { get; set; }

        // Nou: daus de l’atacant i del defensor
        [JsonPropertyName("dausAtacant")]
        public List<int>? DausAtacant { get; set; }

        [JsonPropertyName("dausDefensor")]
        public List<int>? DausDefensor { get; set; }

        [JsonPropertyName("territories")]
        public List<Okupa> territories { get; set; }
    }

    public class GameStateWrapper
    {
        public string type { get; set; }
        public GameStateData data { get; set; }
    }

}

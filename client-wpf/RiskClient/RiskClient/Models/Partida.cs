using System;
using System.Text.Json.Serialization;

namespace RiskClient.Models
{
    public class Partida
    {
        public long Id { get; set; }
        [JsonConverter(typeof(DateTimeOrLongConverter))]
        public DateTime DataInici { get; set; }
        public string Nom { get; set; }
        public string? Token { get; set; }
        public int MaxJugadors { get; set; }
        public long AdminId { get; set; }
        public long? TornPlayerId { get; set; }
        public string Estat { get; set; }
    }
}
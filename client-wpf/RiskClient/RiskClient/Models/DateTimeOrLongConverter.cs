using System;
using System.Text.Json;
using System.Text.Json.Serialization;

namespace RiskClient.Models
{
    public class DateTimeOrLongConverter : JsonConverter<DateTime>
    {
        public override DateTime Read(ref Utf8JsonReader reader, Type typeToConvert, JsonSerializerOptions options)
        {
            if (reader.TokenType == JsonTokenType.String)
            {
                // Format ISO
                var str = reader.GetString();
                if (DateTime.TryParse(str, out var dt))
                    return dt;
                throw new JsonException($"No es pot parsejar la data: {str}");
            }
            else if (reader.TokenType == JsonTokenType.Number)
            {
                // Timestamp en mil·lisegons
                var ms = reader.GetInt64();
                return DateTimeOffset.FromUnixTimeMilliseconds(ms).DateTime;
            }
            throw new JsonException("Format de data no suportat");
        }

        public override void Write(Utf8JsonWriter writer, DateTime value, JsonSerializerOptions options)
        {
            // Escriu com a ISO string
            writer.WriteStringValue(value.ToString("o"));
        }
    }
}
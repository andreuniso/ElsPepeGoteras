using RiskClient.Models;
using System;
using System.Diagnostics;
using System.Net.Http;
using System.Text;
using System.Text.Json;
using System.Threading.Tasks;

namespace RiskClient.Serveis
{
    public class PartidaService
    {
        private readonly HttpClient _httpClient;
        private readonly JsonSerializerOptions _jsonOptions;

        public PartidaService(string baseUrl)
        {
            _httpClient = new HttpClient { BaseAddress = new Uri(baseUrl) };
            _jsonOptions = new JsonSerializerOptions
            {
                PropertyNamingPolicy = JsonNamingPolicy.CamelCase
            };
        }

        public async Task<Jugador?> CrearPartidaAsync(PartidaDTO partidaDTO)
        {
            try
            {
                var json = JsonSerializer.Serialize(partidaDTO, _jsonOptions);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var resp = await _httpClient.PostAsync("/api/partida/crear", content);

                var body = await resp.Content.ReadAsStringAsync();
                Debug.WriteLine("🔄 JSON rebut del servidor:");
                Debug.WriteLine(body);

                if (!resp.IsSuccessStatusCode)
                {
                    throw new Exception($"❌ Error HTTP {resp.StatusCode}: {body}");
                }

                try
                {
                    var jugador = JsonSerializer.Deserialize<Jugador>(body, _jsonOptions);
                    return jugador;
                }
                catch (JsonException jsonEx)
                {
                    Debug.WriteLine("❌ Error de deserialització:");
                    Debug.WriteLine(jsonEx.Message);
                    Debug.WriteLine(jsonEx.StackTrace);
                    throw new Exception("El JSON rebut no és vàlid: " + body, jsonEx);
                }
            }
            catch (Exception ex)
            {
                Debug.WriteLine("❌ EXCEPCIÓ GLOBAL:");
                Debug.WriteLine(ex.Message);
                Debug.WriteLine(ex.StackTrace);
                throw;
            }
        }

        public async Task<List<Partida>?> GetPartidesPubliquesAsync()
        {
            try
            {
                var resp = await _httpClient.GetAsync("/api/partida/public");

                var body = await resp.Content.ReadAsStringAsync();
                Debug.WriteLine("📥 JSON rebut de /public:");
                Debug.WriteLine(body);

                if (!resp.IsSuccessStatusCode)
                {
                    throw new Exception($"❌ Error HTTP {resp.StatusCode}: {body}");
                }

                var partides = JsonSerializer.Deserialize<List<Partida>>(body, _jsonOptions);
                return partides;
            }
            catch (Exception ex)
            {
                Debug.WriteLine("❌ EXCEPCIÓ a GetPartidesPubliquesAsync:");
                Debug.WriteLine(ex.Message);
                Debug.WriteLine(ex.StackTrace);
                return null;
            }
        }

        public async Task<Jugador?> JoinPartidaAsync(JoinPartidaDTO joinDto)
        {
            try
            {
                var json = JsonSerializer.Serialize(joinDto, _jsonOptions);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var resp = await _httpClient.PostAsync("/api/partida/join", content);

                var body = await resp.Content.ReadAsStringAsync();
                Debug.WriteLine("🔗 JSON rebut de /join:");
                Debug.WriteLine(body);

                if (!resp.IsSuccessStatusCode)
                {
                    throw new Exception($"❌ Error HTTP {resp.StatusCode}: {body}");
                }

                var jugador = JsonSerializer.Deserialize<Jugador>(body, _jsonOptions);
                return jugador;
            }
            catch (Exception ex)
            {
                Debug.WriteLine("❌ EXCEPCIÓ a JoinPartidaAsync:");
                Debug.WriteLine(ex.Message);
                Debug.WriteLine(ex.StackTrace);
                return null;
            }
        }

    }
}

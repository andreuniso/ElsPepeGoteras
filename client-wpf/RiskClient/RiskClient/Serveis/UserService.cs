using RiskClient.Models;
using System.Net.Http;
using System.Text.Json;
using System.Text;

namespace RiskClient.Serveis
{
    public class UserService
    {
        private readonly HttpClient _httpClient;
        private readonly JsonSerializerOptions _jsonOptions;

        public UserService(string baseUrl)
        {
            _httpClient = new HttpClient { BaseAddress = new Uri(baseUrl) };
            _jsonOptions = new JsonSerializerOptions
            {
                PropertyNamingPolicy = JsonNamingPolicy.CamelCase
            };
        }

        public async Task<bool> LoginAsync(Usuari usuari)
        {
            try
            {
                // Preparem un objecte amb només login i password
                var loginDTO = new
                {
                    login = usuari.Login,
                    password = usuari.Contrasenya
                };

                var json = JsonSerializer.Serialize(loginDTO, _jsonOptions);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var resp = await _httpClient.PostAsync("/api/usuari/login", content);

                if (!resp.IsSuccessStatusCode)
                {
                    var errorContent = await resp.Content.ReadAsStringAsync();
                    throw new Exception($"Error: {resp.StatusCode}. {errorContent}");
                }

                var body = await resp.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<bool>(body);
            }
            catch (HttpRequestException ex)
            {
                throw new Exception("Error de connexió amb el servidor.", ex);
            }
            catch (Exception ex)
            {
                throw new Exception("S'ha produït un error inesperat.", ex);
            }
        }
    }
}

using RiskClient.Models;
using RiskClient.Serveis;
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

        public async Task<Usuari?> LoginAsync(Usuari usuari)
        {
            try
            {
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
                var usuariAutenticat = JsonSerializer.Deserialize<Usuari>(body, _jsonOptions);
                return usuariAutenticat;
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

        public async Task<Usuari?> ActualitzarUsuariAsync(Usuari usuari)
        {
            try
            {
                var json = JsonSerializer.Serialize(usuari, _jsonOptions);
                var content = new StringContent(json, Encoding.UTF8, "application/json");

                var resp = await _httpClient.PostAsync("/api/usuari", content);

                if (!resp.IsSuccessStatusCode)
                {
                    var errorContent = await resp.Content.ReadAsStringAsync();
                    throw new Exception($"Error al desar l'usuari: {resp.StatusCode}. {errorContent}");
                }

                var body = await resp.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<Usuari>(body, _jsonOptions);
            }
            catch (Exception ex)
            {
                throw new Exception("No s'ha pogut actualitzar l'usuari.", ex);
            }
        }

        public async Task<List<string>> GetAvatarsAsync()
        {
            try
            {
                var resp = await _httpClient.GetAsync("/api/usuari/avatars");

                if (!resp.IsSuccessStatusCode)
                {
                    var errorContent = await resp.Content.ReadAsStringAsync();
                    throw new Exception($"Error al obtenir avatars: {resp.StatusCode}. {errorContent}");
                }

                var body = await resp.Content.ReadAsStringAsync();
                return JsonSerializer.Deserialize<List<string>>(body, _jsonOptions) ?? new List<string>();
            }
            catch (Exception ex)
            {
                throw new Exception("Error al carregar la llista d'avatars.", ex);
            }
        }
    }
}

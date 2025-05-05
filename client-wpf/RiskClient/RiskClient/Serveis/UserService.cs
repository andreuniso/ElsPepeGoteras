using RiskClient.Models;
using System.Net.Http;
using System.Text.Json;
using System.Text;

namespace RiskClient.Serveis
{
    public class UserService
    {
        private readonly HttpClient _httpClient;

        public UserService(string baseUrl)
        {
            _httpClient = new HttpClient { BaseAddress = new Uri(baseUrl) };
        }

        public async Task<bool> LoginAsync(Usuari usuari)
        {
            var json = JsonSerializer.Serialize(usuari);
            var content = new StringContent(json, Encoding.UTF8, "application/json");
            var resp = await _httpClient.PostAsync("/api/login", content);
            if (!resp.IsSuccessStatusCode) return false;
            var body = await resp.Content.ReadAsStringAsync();
            return JsonSerializer.Deserialize<bool>(body);
        }

        // opcional: RegisterAsync
    }
}

## 🌍 Projecte Risk

Aquest projecte està format per tres components principals desenvolupats per diferents membres de l'equip:

*   🧠 **Servidor (Java Spring Boot)** – desenvolupat per Andreu Niso
*   💻 **Client d’escriptori (WPF)** – desenvolupat per Elena Romeu
*   🌐 **Client web** – desenvolupat per Ferran Morgades

---

## 📁 Estructura del projecte

/`ProjecteClientServidor`  
│  
├── `servidor-spring/` ← Backend amb Java Spring  
├── `client-wpf/` ← Aplicació d'escriptori amb C# i WPF  
├── `client-web/` ← Client web (HTML/CSS/JS o framework)  
└── `README.md` ← Aquest fitxer

---

## 🔀 Branques Git utilitzades

`main` → Versió estable del projecte (mai fer commit si no es estable!)

`dev` → Zona per fer merge de les branques de desenvolupament i fer proves abans de passar-ho a main

`feat-servidor-andreu` → Desenvolupament del servidor  
`feat-cliente-wpf-elena` → Desenvolupament del client WPF  
`feat-cliente-web-ferran` → Desenvolupament del client web

---

## 📝 Protocol WebSockets
El protocol WebSockets es basa en missatges JSON. Aquest es el format de missatge:

```json
{
    "type": "event_type",
    "data": { /* contingut específic per cada tipus */ }
}
```

```json
{
    "error": "missatge d'error"
}
```

### 📪 Tipus de missatges 
#### (Server → Client)

*   `reload_players` → S'ha actualitzat la llista de jugadors
*   `data`:
    *   `jugadors` → Llista de jugadors amb tots els seus camps


*   `game_state` → S'ha actualitzat l'estat del joc
*   `data`:
    *   `partida` → Llista de la partida amb tots els seus camps
    *   `availableTroopsActualPlayer` → Nombre de tropes disponibles per al jugador actual
    *   `dausAtacant` → Array amb els daus generats pel pais atacant
    *   `dausDefensor` → Array amb els daus generats pel pais defensor
    *   `territories` → Llista de territoris amb tots els seus camps

#### (Client → Server)

*   `start_game` → L'admin ha iniciat la partida


*   `place_troop` → El jugador ha col·locat una tropa
*  `data`:
    *   `id_pais` → Pais on s'ha col·locat la tropa


*   `reinforce_countries` → El jugador ha reforçat un pais
*   `data`:
    *   `id_pais` → Pais que s'ha reforçat


*   `assign_troops` → El jugador ha assignat tropes
*   `data`:
    *   `id_pais` → Pais on s'han assignat les tropes
    *   `qt_tropes` → Nombre de tropes assignades


*   `attack` → El jugador ha atacat un pais
*   `data`:
    *   `id_pais_atacant` → Pais que ataca
    *   `id_pais_defensiu` → Pais que defensa
    *   `qt_tropes_atacant` → Nombre de tropes que ataca


*   `finish_attack` → El jugador ja no vol atacar més


*   `fortify` → El jugador ha fortificat un pais
*   `data`:
     *   `id_pais_1` → Pais d'on es treuen les tropes
     *   `id_pais_2` → Pais on es posen les tropes
     *   `qt_tropes` → Nombre de tropes que es desplacen
---

## ✅ Versions

Les versions estables es marcaran amb etiquetes com v1.0, v1.1, etc.

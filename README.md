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


*   `game_started` → La partida ha començat
*   `data`:
    *   `partida` → Partida actualitzada amb tots els seus camps (incloent-hi el torn actual i l'estat de la partida)


*   `country_updated` → S'ha actualitzat un pais
*  `data`:
    *   `okupa` → Okupa actualitzat amb tots els seus camps


*   `new_turn` → Canvi de torn en la partida
*   `data`:
    *   `partida` → Partida actualitzada amb tots els seus camps


*   `available_troops` → S'informa del nombre de tropes disponibles AL JUGADOR
*   `data`:
    *   `qt_tropes` → Nombre de tropes disponibles per al jugador actual


*   `change_state` → S'ha canviat l'estat de la partida
*   `data`:
    *   `partida` → Partida actualitzada amb tots els seus camps


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
    *   `qt_tropes_restants` → Nombre de tropes restants per assignar
---

## ✅ Versions

Les versions estables es marcaran amb etiquetes com v1.0, v1.1, etc.

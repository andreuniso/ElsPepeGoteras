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

### 📪 Tipus de missatges 
#### (Server → Client)

*   `player_joined` → Un jugador s'ha unit a la sala
*   `data`:
    *   `idJugador` → ID del jugador
    *   `nom` → Nom del jugador
    *   `numero` → Posició del jugador a la partida


*  `player_left` → Un jugador ha abandonat la sala
*   `data`:
    *   `idJugador` → ID del jugador


#### (Server → Tots els clients)

*   `game_started` → La partida ha començat
*   `data`:
    *   `estat` → Indica la fase de la partida (COLOCACIO_INICIAL)


#### (Client → Server)

*   `start_game` → L'admin ha iniciat la partida
*   `data`:
    *   `idPartida` → ID de la partida

---

## ✅ Versions

Les versions estables es marcaran amb etiquetes com v1.0, v1.1, etc.

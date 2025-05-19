# 🧠 Els Pepe Goteras - Servidor Spring Boot (Risk Edition)

Servidor Java Spring Boot per a gestionar partides, usuaris i comunicació en temps real d'un joc tipus Risk.

## 📁 Estructura de Paquets

### `config`
Conté configuracions globals del servidor.  
🔧 Exemple: configuració CORS, WebSocket, etc.

### `controller`
Conté els controladors REST que exposen endpoints HTTP.  
🎮 Gestionen peticions com crear una partida, registrar un usuari, etc.

### `dto`
Conté les *Data Transfer Objects* que encapsulen les dades d'entrada/sortida.  
🧳 Exemple: `LoginDTO`, `RegisterDTO`, `PartidaDTO`.

### `models`
Conté les entitats JPA que representen les taules de la base de dades.  
🗃️ Exemple: `Usuari`, `Partida`, `Jugador`.

### `repository`
Conté les interfícies que hereten de `JpaRepository`, permetent accedir a la base de dades.  
🔍 Exemple: `UsuariRepository`, `PartidaRepository`.

### `security`
Conté la configuració de seguretat del servidor.  
🔒 Exemple: `generació de tokens`, `encriptació`.

### `service`
Conté la lògica de negoci.  
⚙️ Exemple: crear partida, validar login, afegir jugadors...

### `websocket`
Conté la gestió de connexions WebSocket en temps real.  
💬 Exemple: rebre i enviar missatges entre clients durant una partida.

---

## 🎯 Controladors i els seus Endpoints

### `JugadorController`  
Ruta base: `/api/jugador`  

| Endpoint               | Mètode   | Retorn          | Descripció                               |
|------------------------|----------|-----------------|------------------------------------------|
| `/id/{id}`             | `GET`    | `Jugador`       | Recupera un jugador per ID               |
| `/partida/{idPartida}` | `GET`    | `List<Jugador>` | Recupera tots els jugadors de la partida |

### `PartidaController`
Ruta base: `/api/partida`

| Endpoint         | Mètode   | Retorn          | Descripció                                             |
|------------------|----------|-----------------|--------------------------------------------------------|
| `/id/{id}`       | `GET`    | `Partida`       | Recupera una partida per ID                            |
| `/token/{token}` | `GET`    | `Partida`       | Recupera una partida per token                         |
| `/public`        | `GET`    | `List<Partida>` | Llista totes les partides públiques (token `null`)     |
| `/crear`         | `POST`   | `Jugador`       | Crea una nova partida amb un `PartidaDTO`              |
| `/join`          | `POST`   | `Jugador`       | S'uneix a una partida existent amb un `JoinPartidaDTO` |
| `/actualitzar`   | `PUT`    | `Partida`       | Actualitza una partida existent                        |

### `UsuariController`
Ruta base: `/api/usuari`

| Endpoint       | Mètode   | Retorn         | Descripció                               |
|----------------|----------|----------------|------------------------------------------|
| `/avatars`     | `GET`    | `List<String>` | Retorna tots els avatars disponibles     |
| `/login`       | `POST`   | `Usuari`       | Valida login d'un usuari amb `LoginDTO`  |
| `/register`    | `POST`   | `Usuari`       | Registra un nou usuari amb `RegisterDTO` |
| `/actualitzar` | `PUT`    | `Usuari`       | Actualitza dades d'un usuari             |
| `/{id}`        | `DELETE` | `void`         | Elimina un usuari per ID                 |

---

## 🧪 Com provar-lo

1. Assegura't de tenir una BBDD Oracle configurada.
2. Llença l'app.
3. Pots consumir els endpoints amb Postman o qualsevol client HTTP.
4. WebSocket actiu a: `ws://localhost:8080/risk`

---

## 👨‍💻 Autor

Servidor desenvolupat per Andreu Niso.

---



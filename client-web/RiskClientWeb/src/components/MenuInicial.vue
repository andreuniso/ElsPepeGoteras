<template>
  <div class="main-container">
    <div class="game-background"></div>
    <div class="content-wrapper">
      <header class="game-header">
        <h1 class="game-title">RISK</h1>
      </header>
      <div class="auth-form" v-if="showLogin && !isAuthenticated">
        <h2>Iniciar Sessió</h2>
        <input v-model="loginUser" type="text" placeholder="Usuari">
        <input v-model="loginPassword" type="password" placeholder="Contrasenya">
        <button @click="userLogin" class="login-btn">Entrar</button>
        <p class="auth-switch" @click="switchToRegister">No tens compte? <span>Registra't</span></p>
      </div>

      <div class="auth-form" v-if="!showLogin && !isAuthenticated">
        <h2>Crear Compte</h2>
        <input v-model="registerUser" type="text" placeholder="Usuari">
        <input v-model="registerName" type="text" placeholder="Nom i Cognoms">
        <input v-model="registerPassword" type="password" placeholder="Contrasenya">
        <input v-model="registerConfirmarPassword" type="password" placeholder="Repeteix la contrasenya">
        <button @click="userRegister" class="register-btn">Registrar</button>
        <p class="auth-switch" @click="switchToLogin">Ja tens compte? <span>Inicia sessió</span></p>
      </div>

      <div class="main-menu" v-if="isAuthenticated">
        <div class="user-info">
          <img src="@/assets/Imatges/avatar_test.jpg" class="user-avatar">
          <span class="username">Benvingut/da, {{ currentUser }}</span>
        </div>
        
        <div class="menu-options">
          <button @click="comencarPartida" class="menu-btn">
            <img src="@/assets/Icones/mas.png" class="menu-icon">
            Crear Partida
          </button>
          <button @click="unirsePartida" class="menu-btn">
            <img src="@/assets/Icones/fletxa-icona.png" class="menu-icon">
            Unir-se a Partida
          </button>
          <button @click="modificarUsuari" class="menu-btn">
            <img src="@/assets/Icones/editar-user.png" class="menu-icon">
            Modificar Usuari
          </button>
          <button @click="logout" class="menu-btn logout-btn">
            <img src="@/assets/Icones/cerrar.png" class="menu-icon">
            Tancar Sessió
          </button>
        </div>
      </div>
      <CrearPartida v-if="mostrantCrearPartida" @partidaCreada="handleNovaPartida"/>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      showLogin: true,
      isAuthenticated: false,
      currentUser: '',
      loginUser: '',
      loginPassword: '',
      registerName: '',
      registerUser: '',
      registerPassword: '',
      registerConfirmarPassword: ''
    }
  },
  methods: {
    switchToLogin() {
      this.showLogin = true;
    },
    switchToRegister() {
      this.showLogin = false;
    },
    userLogin() {
      fetch("http://localhost:8080/api/usuari/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          username: this.loginUser,
          password: this.loginPassword
        })
      })
      .then(response => response.json())
      .then(data => {
        if (data == true) {
          this.isAuthenticated = true;
          this.currentUser = this.loginUser;
        } else {
          alert("Credencials incorrectes!");
        }
      })
      .catch(error => {
        console.error("Error al fer login:", error);
      });
    },
    userRegister() {
      if (this.registerPassword !== this.registerConfirmarPassword) {
        alert("Les contrasenyes no coincideixen.");
        this.registerPassword = '';
        this.registerConfirmarPassword = '';
        return;
      }

      fetch("http://localhost:8080/api/usuari/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          nom: this.registerName,
          nickname: this.registerUser,
          password: this.registerPassword
        })
      })
      .then(response => {
        if (!response.ok) {
          throw new Error("Error en el registre");
        }
        return response.json();
      })
      .then(data => {
        // Succes! Ara pots fer login directament o mostrar un missatge.
        alert("Usuari registrat correctament! Inicia sessió.");
        this.switchToLogin();
        this.registerName = '';
        this.registerEmail = '';
        this.registerPassword = '';
        this.registerConfirmPassword = '';
      })
      .catch(error => {
        console.error("Error en el registre:", error);
        alert("No s'ha pogut completar el registre.");
      });
    },
    logout() {
      this.isAuthenticated = false;
      this.currentUser = '';
      this.loginPassword = '';
    },
    comencarPartida() {
      console.log("Iniciant nova partida...");
      this.$router.push('/crear-partida');
    },
    unirsePartida() {
      console.log("Unint-se a partida...");
    },
    modificarUsuari(){
      console.log("Modificant Usuari");
    }
  }
}
</script>

<style scoped>
.main-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: 'Montserrat', sans-serif;
}

.game-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: url('@/assets/imatges/imatge_portada.png');
  background-size: cover;
  background-position: center;
  filter: brightness(0.6);
  z-index: -1;
}

.content-wrapper {
  width: 90%;
  max-width: 500px;
  background-color: rgba(0, 0, 0, 0.7);
  border-radius: 15px;
  padding: 2rem;
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.8);
  backdrop-filter: blur(5px);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.game-header {
  text-align: center;
  margin-bottom: 2rem;
}

.game-title {
  font-family: 'MasterTitol', sans-serif;
  font-size: 8rem;
  margin: 0;
  color: #e74c3c;
  text-shadow: 3px 3px 0 #000;
  letter-spacing: 3px;
}
.auth-form {
  color: white;
}

.auth-form h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #ecf0f1;
}

.auth-form input {
  width: 100%;
  padding: 12px;
  margin-bottom: 1rem;
  border: none;
  border-radius: 5px;
  background-color: rgba(255, 255, 255, 0.9);
  font-size: 1rem;
}

.auth-form button {
  width: 100%;
  padding: 12px;
  border: none;
  border-radius: 5px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
}

.login-btn {
  background-color: #2ecc71;
  color: white;
}

.login-btn:hover {
  background-color: #27ae60;
}

.register-btn {
  background-color: #3498db;
  color: white;
}

.register-btn:hover {
  background-color: #2980b9;
}

.auth-switch {
  text-align: center;
  margin-top: 1rem;
  color: #bdc3c7;
  cursor: pointer;
}

.auth-switch span {
  color: #3498db;
  font-weight: bold;
}

.main-menu {
  color: white;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 2rem;
  padding-bottom: 1rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right: 1rem;
}

.username {
  font-size: 1.2rem;
  font-weight: bold;
}

.menu-options {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.menu-btn {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 15px;
  background-color: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  border-radius: 8px;
  font-size: 1.1rem;
  cursor: pointer;
  transition: all 0.3s;
}

.menu-btn:hover {
  background-color: rgba(255, 255, 255, 0.2);
  transform: translateY(-3px);
}

.menu-icon {
  width: 24px;
  height: 24px;
  margin-right: 10px;
}

.logout-btn {
  margin-top: 2rem;
  background-color: rgba(231, 76, 60, 0.2);
  border-color: rgba(231, 76, 60, 0.4);
}

.logout-btn:hover {
  background-color: rgba(231, 76, 60, 0.3);
}
</style>
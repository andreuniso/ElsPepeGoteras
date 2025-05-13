<template>
  <div class="main-container">
    <div class="game-background"></div>
    <div class="content-wrapper">
      <header class="game-header">
        <h1 class="game-title">RISK</h1>
      </header>
      <div class="auth-form" v-if="showLogin && !userStore.isAuthenticated">
        <h2>Iniciar Sessió</h2>
        <input v-model="loginUser" type="text" placeholder="Usuari">
        <input v-model="loginPassword" type="password" placeholder="Contrasenya">
        <button @click="userLogin" class="login-btn">Entrar</button>
        <p class="auth-switch" @click="switchToRegister">No tens compte? <span>Registra't</span></p>
      </div>

      <div class="auth-form" v-if="!showLogin && !userStore.isAuthenticated">
        <h2>Crear Compte</h2>
        <input v-model="registerUser" type="text" placeholder="Usuari">
        <input v-model="registerName" type="text" placeholder="Nom i Cognoms">
        <input v-model="registerPassword" type="password" placeholder="Contrasenya">
        <input v-model="registerConfirmarPassword" type="password" placeholder="Repeteix la contrasenya">
        <button @click="userRegister" class="register-btn">Registrar</button>
        <p class="auth-switch" @click="switchToLogin">Ja tens compte? <span>Inicia sessió</span></p>
      </div>

      <div class="main-menu" v-if="userStore.isAuthenticated">
        <div class="user-info">
          <img src="@/assets/Imatges/avatar_test.jpg" class="user-avatar">
          <span class="username">Benvingut/da, {{ userStore.currentUser }}</span>
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
<style scoped src="@/assets/styles/menuinicial.css"></style>

<script>
import { useUserStore } from '@/store/user'; 
import { useRouter } from 'vue-router';
import CrearPartida from '@/components/CrearPartida.vue';

export default {
  components: {
    CrearPartida,
  },
  data() {
    return {
      showLogin: true,
      loginUser: '',
      loginPassword: '',
      registerName: '',
      registerUser: '',
      registerPassword: '',
      registerConfirmarPassword: '',
      mostrantCrearPartida: false,
    }
  },
  setup() {
    const userStore = useUserStore();
    const router = useRouter();               
    return { userStore, router };            
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
          login: this.loginUser,
          password: this.loginPassword
        })
      })
      .then(response => response.json())
      .then(data => {
        if (data == true) {
          this.userStore.login(this.loginUser);
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
          login: this.registerUser,
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
        //Ara hauria de poder fer login, quedar registrat i passar a la pantalla de login
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
      this.userStore.logout();
    },
    comencarPartida() {
      console.log("Iniciant nova partida...");
      this.router.push('/crear-partida');
    },
    unirsePartida() {
      console.log("Unint-se a partida...");
      this.router.push('/unirse-partida');
    },
    modificarUsuari(){
      console.log("Modificant Usuari");
      this.router.push('/modificar-usuari');
    }
  }
}
</script>

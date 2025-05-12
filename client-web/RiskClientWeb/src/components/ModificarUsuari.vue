<template>
    <div class="main-container">
      <div class="game-background"></div>
      <div class="content-wrapper">
        <header class="game-header">
          <h1 class="game-title">Perfil</h1>
        </header>
  
        <div class="auth-form">
          <h2>Modificar Usuari</h2>
  
            <div class="avatar-selection">
                <p>Selecciona un avatar:</p>
                <div class="avatar-grid">
                    <img 
                    v-for="(avatar, index) in avatarsDisponibles" 
                    :key="index" 
                    :src="avatar" 
                    class="avatar-option"
                    :class="{ selected: avatar == avatarSeleccionat }"
                    @click="seleccionarAvatar(index)"
                    />
                </div>
            </div>
  
            <label>Nickname</label>
            <input v-model="nouNomUsuari" type="text" placeholder="Nickname" />
    
            <label>Contrasenya</label>
            <input v-model="novaContrasenya" type="password" placeholder="Nova contrasenya" />
    
            <label>Confirmar Contrasenya</label>
            <input v-model="confirmarContrasenya" type="password" placeholder="Repeteix la contrasenya" />
    
            <button class="register-btn" @click="guardarCanvis">Guardar Canvis</button>
            <button class="logout-btn" @click="tornar">Cancel·lar</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { useUserStore } from '@/store/user';
  import { useRouter } from 'vue-router';
  
  export default {
    setup() {
      const userStore = useUserStore();
      const router = useRouter();
  
      return { userStore, router };
    },
    data() {
      return {
        nouNomUsuari: this.userStore.currentUser || '',
        novaContrasenya: '',
        confirmarContrasenya: '',
        avatarsDisponibles: [],
        avatarSeleccionat: null,
      };
    },
    mounted() {
        this.carregarAvatarsDisponibles();
    },
    methods: {
        carregarAvatarsDisponibles() {
            fetch('http://localhost:8080/api/usuari/avatars')
            .then(res => res.json())
            .then(data => {
                this.avatarsDisponibles = data.map(b64 => `data:image/png;base64,${b64}`);
            })
            .catch(err => {
                console.error('Error carregant avatars:', err);
            });
        },
        seleccionarAvatar(index) {
            this.avatarSeleccionat = this.avatarsDisponibles[index];
        },
        guardarCanvis() {
            if (this.novaContrasenya && this.novaContrasenya != this.confirmarContrasenya) {
            alert('Les contrasenyes no coincideixen');
            return;
            }
    
            const formData = new FormData();
            formData.append('nouNom', this.nouNomUsuari);
            if (this.novaContrasenya) {
            formData.append('novaContrasenya', this.novaContrasenya);
            }
            if (this.avatarFitxer) {
            formData.append('avatar', this.avatarFitxer);
            }
    
            fetch('http://localhost:8080/api/usuari/modificar', {
            method: 'POST',
            body: formData
            })
            .then(res => {
                if (!res.ok) throw new Error('Error en modificar el perfil');
                return res.json();
            })
            .then(data => {
                alert('Perfil actualitzat correctament!');
                this.userStore.login(this.nouNomUsuari);
                this.router.push('/');
            })
            .catch(err => {
                console.error(err);
                alert("Error en desar els canvis.");
            });
        },
        tornar() {
            this.router.push('/');
        }
    }
  };
  </script>
  
  <style scoped src="@/assets/styles/menuinicial.css"></style>
  <style scoped src="@/assets/styles/modificarUsuari.css"></style>
  
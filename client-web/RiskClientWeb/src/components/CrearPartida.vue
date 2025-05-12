<template>
  <div class="crear-partida-container">
    <div class="game-background"></div>
    <div class="content-wrapper">
      <h2>Crear Nova Partida</h2>
      <label for="nomPartida">Nom de la Partida:</label>
      <input
        id="nomPartida"
        v-model="nomPartida"
        type="text"
        placeholder="Escriu un nom..."
      />
      <div class="slider-container">
        <label for="jugadorsSlider">Nombre de Jugadors: <span class="slider-value">{{ nombreJugadors }}</span></label>
        <div class="slider-wrapper">
          <input
            id="jugadorsSlider"
            type="range"
            min="2"
            max="4"
            v-model.number="nombreJugadors"
            class="styled-slider"
          />
        </div>
      </div>
      <div class="privadesa-toggle">
        <label>
          <input type="checkbox" v-model="esPrivada" />
          Partida Privada
        </label>
      </div>

      <button class="crear-btn" @click="crearPartida">Crear Partida</button>
      <button class="tornar-btn" @click="tornarMenu">Tornar al Menú</button>
    </div>
  </div>
</template>

<style scoped src="@/assets/styles/creacioPartida.css"></style>

<script>
import { useRouter } from 'vue-router';

export default {
  data() {
    return {
      nomPartida: "",
      nombreJugadors: 2,
      esPrivada: false
    };
  },
  setup() {
    const router = useRouter();
    return { router };              
  },
  methods: {
    crearPartida() {
      if (!this.nomPartida.trim()) {
        alert("Has d'introduir un nom per la partida.");
        return;
      }

      const novaPartida = {
        nom: this.nomPartida,
        jugadors: this.nombreJugadors,
        privada: this.esPrivada
      };

      // Aquí pots enviar-ho al backend o via websocket
      console.log("Creant partida:", novaPartida);

      // Netegem el formulari o notifiquem el component pare
      this.$emit("partidaCreada", novaPartida);
    },
    tornarMenu() {
      this.router.push('/');
    }
  }
};
</script>


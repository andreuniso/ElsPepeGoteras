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

<script>
export default {
  data() {
    return {
      nomPartida: "",
      nombreJugadors: 2,
      esPrivada: false
    };
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
      this.$router.push('/');
    }
  }
};
</script>

<style scoped>
.crear-partida-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
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
  color: white;
}

h2 {
  text-align: center;
  margin-bottom: 1.5rem;
  color: #ecf0f1;
}

input[type="text"],
input[type="range"] {
  padding: 10px;
  border-radius: 5px;
  border: none;
  width: 100%;
  font-size: 1rem;
  margin-bottom: 1rem;
}

.privadesa-toggle {
  margin-bottom: 1.5rem;
}

.crear-btn {
  padding: 12px;
  background-color: #2ecc71;
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s;
  width: 100%;
}

.crear-btn:hover {
  background-color: #27ae60;
}

.tornar-btn {
  padding: 12px;
  background-color: #e74c3c;
  border: none;
  border-radius: 8px;
  color: white;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: background 0.3s;
  width: 100%;
  margin-top: 1rem;
}

.tornar-btn:hover {
  background-color: #c0392b;
}
.slider-container {
  margin-bottom: 1.5rem;
}

.slider-value {
  display: inline-block;
  width: 24px;
  height: 24px;
  background-color: #2ecc71;
  color: white;
  border-radius: 50%;
  text-align: center;
  line-height: 24px;
  font-size: 0.9rem;
  margin-left: 5px;
}

.slider-wrapper {
  position: relative;
  margin-top: 10px;
}

.styled-slider {
  -webkit-appearance: none;
  width: 100%;
  height: 8px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 10px;
  outline: none;
  margin: 15px 0;
}

.styled-slider::-webkit-slider-thumb {
  -webkit-appearance: none;
  width: 24px;
  height: 24px;
  background: #2ecc71;
  border-radius: 50%;
  cursor: pointer;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
  transition: all 0.2s ease;
}

.styled-slider::-moz-range-thumb {
  width: 24px;
  height: 24px;
  background: #2ecc71;
  border-radius: 50%;
  cursor: pointer;
  border: 3px solid white;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
  transition: all 0.2s ease;
}

.styled-slider::-webkit-slider-thumb:hover {
  transform: scale(1.2);
  background:rgb(14, 95, 14);
}
</style>

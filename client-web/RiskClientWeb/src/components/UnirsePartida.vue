<template>
    <div class="unirse-container">
      <h2>Unir-se a una Partida</h2>
  
      <div class="unirse-codi">
        <input v-model="codiPartida" type="text" placeholder="Introdueix el codi de la partida" />
        <button @click="unirAmbCodi">Unir-se</button>
      </div>

      <div class="llistat-partides">
        <h3>Partides Disponibles</h3>
        <div class="partides-scroll">
          <div v-for="partida in partides" :key="partida.id" class="partida-card">
            <p><strong>Nom:</strong> {{ partida.nom }}</p>
            <p><strong>Jugadors:</strong> {{ partida.jugadors.length }} / {{ partida.maxJugadors }}</p>
            <button @click="unirPartida(partida.id)">Unir-se</button>
          </div>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        codiPartida: '',
        partides: []
      };
    },
    mounted() {
      this.carregarPartides();
    },
    methods: {
      carregarPartides() {
        fetch('http://localhost:8080/api/partides/disponibles')
          .then(res => res.json())
          .then(data => {
            this.partides = data;
          })
          .catch(err => console.error("Error carregant partides:", err));
      },
      unirAmbCodi() {
        fetch(`http://localhost:8080/api/partides/unir/${this.codiPartida}`, {
          method: 'POST'
        })
          .then(res => {
            if (!res.ok) throw new Error("No s'ha pogut unir a la partida");
            alert("T'has unit a la partida!");
          })
          .catch(err => alert(err.message));
      },
      unirPartida(idPartida) {
        fetch(`http://localhost:8080/api/partides/unir/${idPartida}`, {
          method: 'POST'
        })
          .then(res => {
            if (!res.ok) throw new Error("No s'ha pogut unir a la partida");
            alert("T'has unit a la partida!");
          })
          .catch(err => alert(err.message));
      }
    }
  };
  </script>
  

  <style scoped src="@/assets/styles/unirsepartida.css"></style>
  
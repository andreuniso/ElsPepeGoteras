import { createRouter, createWebHistory } from 'vue-router';
import MenuInicial from '@/components/MenuInicial.vue';
import CrearPartida from '@/components/CrearPartida.vue';

const routes = [
  {
    path: '/',
    name: 'MenuInicial',
    component: MenuInicial
  },
  {
    path: '/crear-partida',
    name: 'CrearPartida',
    component: CrearPartida
  }
];

const router = createRouter({
  history: createWebHistory('/'),
  routes
});

export default router;

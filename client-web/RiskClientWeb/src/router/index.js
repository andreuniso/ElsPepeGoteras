import { createRouter, createWebHistory } from 'vue-router';
import MenuInicial from '@/components/MenuInicial.vue';
import CrearPartida from '@/components/CrearPartida.vue';
import ModificarUsuari from '@/components/ModificarUsuari.vue';
import UnirsePartida from '@/components/UnirsePartida.vue';

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
  },
  {
    path: '/modificar-usuari',
    name: 'ModificarUsuari',
    component: ModificarUsuari
  },
  {
    path: '/unirse-partida',
    name: 'UnirsePartida',
    component: UnirsePartida,
  },
];

const router = createRouter({
  history: createWebHistory('/'),
  routes
});

//Arreglar això
// router.beforeEach((to, from, next) => {
//   const userStore = useUserStore()
//   if (to.meta.requiresAuth && !userStore.isAuthenticated) {
//     next({ path: '/' });
//   } else {
//     next();
//   }
// })

export default router;

import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import './assets/global.css'
import router from './router';
import '@/assets/fonts.css'

createApp(App).use(router).mount('#app')

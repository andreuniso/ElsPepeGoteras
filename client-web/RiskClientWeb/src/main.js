import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import './assets/global.css'
import router from './router';
import '@/assets/fonts.css'
import { createPinia } from 'pinia'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.mount('#app')

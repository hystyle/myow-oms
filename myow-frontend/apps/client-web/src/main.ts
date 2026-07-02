import { createApp } from 'vue';
import { createPinia } from 'pinia';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import './styles/main.css';
import App from './app/app.vue';
import { router } from './app/router';

createApp(App).use(createPinia()).use(router).use(ElementPlus).mount('#app');

import Vue from 'vue';
import App from './App.vue';

import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';
import 'font-awesome/css/font-awesome.min.css';

Vue.prototype.host = 'http://localhost:8216/antiplag';

import axios from 'axios';
axios.defaults.withCredentials = true;
Vue.prototype.axios = axios;

import uploader from 'vue-simple-uploader';
Vue.use(uploader);

import VueClipboard from 'vue-clipboard2';
Vue.use(VueClipboard);

Vue.config.productionTip = false;

new Vue({
  render: h => h(App),
}).$mount('#app');

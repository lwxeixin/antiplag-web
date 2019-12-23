import Vue from 'vue'
import App from './App.vue'
import axios from 'axios'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.min.js'
import 'font-awesome/css/font-awesome.min.css'

Vue.prototype.host = 'http://localhost:8216/antiplag';

axios.defaults.withCredentials = true;
Vue.prototype.axios = axios;
Vue.config.productionTip = false;

new Vue({
  render: h => h(App),
}).$mount('#app');

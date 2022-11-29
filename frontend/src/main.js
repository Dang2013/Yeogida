import { createApp } from "vue";
import { createPinia } from "pinia";
import App from "./App.vue";
import vuetify from "./plugins/vuetify";
import router from "./router";
import { loadFonts } from "./plugins/webfontloader";
import VueFullPage from "vue-fullpage.js";

loadFonts();

createApp(App)
  .use(router)
  .use(createPinia())
  .use(vuetify)
  .use(VueFullPage)
  .mount("#app");

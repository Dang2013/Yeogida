// https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vite-plugin
import { defineConfig, loadEnv } from "vite";
import vue from "@vitejs/plugin-vue";
import vuetify from "vite-plugin-vuetify";

export default ({ mode }) => {
  process.env = { ...process.env, ...loadEnv(mode, process.cwd()) };
  return defineConfig({
    plugins: [vue(), vuetify({ autoImport: true })],
  });
};

<template>
  <div class="navbar">
    <div class="navbar-logo" @click="toMain()"></div>
    <div>
      <div v-if="accountStore.isLogin" style="display: flex">
        <div class="navbar-btn">
          <a class="navbar-link" @click="toMypage()">마이페이지</a>
        </div>
        <div class="navbar-btn">
          <a class="navbar-link" @click="accountStore.logoutAccount()"
            >로그아웃</a
          >
        </div>
      </div>
      <div v-else style="display: flex">
        <div class="navbar-btn">
          <a
            class="navbar-link"
            @click="
              accountStore.openLoginDialog(), (accountStore.loginModal = true)
            "
            >로그인</a
          >
        </div>
        <div class="navbar-btn">
          <a
            class="navbar-link"
            @click="
              accountStore.openLoginDialog(), (accountStore.loginModal = false)
            "
            >회원가입</a
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import router from "../router";
import { useAccountStore } from "../store";
const accountStore = useAccountStore();
function toMypage() {
  router.push({ name: "MyPage" });
}
function toMain() {
  router.push({ name: "MainPage" });
}
</script>

<style>
.navbar {
  width: 100%;
  height: 80px;
  background-color: white;
  display: flex;
  justify-content: space-between;
  top: 0;
  z-index: 1;
  position: fixed;
}
.navbar-logo {
  width: 180px;
  height: 80px;
  background-image: url("../assets/logo.png");
  cursor: pointer;
}
.navbar-btn {
  width: 200px;
  height: 80px;
  display: flex;
  font-family: "MICEGothic";
  font-size: 23px;
  justify-content: center;
  align-items: center;
}
.navbar-link:hover {
  cursor: pointer;
  color: #6570fc;
  font-size: 28px;
  opacity: 0.8;
}
</style>

<template>
  <div>
    <v-row justify="center">
      <v-dialog v-model="accountStore.dialogModal">
        <v-card>
          <div class="account-modal">
            <div class="account-modal-top">
              <div
                v-if="accountStore.loginModal"
                class="account-modal-top-menu-active"
              >
                로그인
              </div>
              <div v-else class="account-modal-top-menu-inactive">
                <a class="link" @click="accountStore.switchToLogin()">로그인</a>
              </div>
              <div
                v-if="accountStore.loginModal"
                class="account-modal-top-menu-inactive"
              >
                <a class="link" @click="accountStore.switchToSignup()"
                  >회원가입</a
                >
              </div>
              <div v-else class="account-modal-top-menu-active">회원가입</div>
            </div>
            <LoginModal v-if="accountStore.loginModal" />
            <SignupModal v-else />
          </div>
        </v-card>
      </v-dialog>
    </v-row>
  </div>
</template>
<script setup>
import LoginModal from "./LoginModal.vue";
import SignupModal from "./SignupModal.vue";
import { useAccountStore } from "../store";
const accountStore = useAccountStore();
</script>
<style scoped>
.account-modal {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 400px;
  height: 510px;
}
.account-modal-top {
  display: flex;
  height: 50px;
  width: 400px;
}
.account-modal-top-menu-active {
  display: flex;
  height: 50px;
  width: 200px;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-family: "MICEGothic";
  color: #6fc0f7;
}
.account-modal-top-menu-inactive {
  display: flex;
  height: 50px;
  width: 200px;
  justify-content: center;
  align-items: center;
  font-size: 18px;
  font-family: "MICEGothic";
  background-color: #efefef;
  color: #6fc0f7;
}
.link:hover {
  cursor: pointer;
}
</style>

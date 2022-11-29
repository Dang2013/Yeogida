<template>
  <div class="login-modal">
    <div class="login-modal-info">
      <a style="font-size: 19px; color: #5a87e0">여기다</a>에 오신것을
      환영합니다.
    </div>
    <input
      v-model="loginId"
      style="margin-top: 50px"
      type="text"
      placeholder=" 아이디"
      class="login-modal-textinput"
    />
    <input
      v-model="loginPassword"
      style="margin-top: 25px"
      type="password"
      placeholder=" 비밀번호"
      class="login-modal-textinput"
      @keyup.enter="loginEnter()"
    />
    <v-btn
      style="margin-top: 25px; color: white"
      color="#5a87e0"
      width="310px"
      height="45px"
      @click="
        (newPasswordDialog = true),
          (newPasswordMessage = '회원님의 이메일 아이디를 입력해 주세요')
      "
    >
      <a class="btn-text">비밀번호 찾기</a>
    </v-btn>
    <v-btn
      v-if="isValidId"
      style="margin-top: 50px; color: white"
      color="#5a87e0"
      width="310px"
      height="45px"
      @click="loginBtn()"
    >
      <a class="btn-text">로그인</a>
    </v-btn>
    <v-btn
      v-else
      disabled
      style="margin-top: 50px; color: white"
      color="#9d9d9d"
      width="310px"
      height="45px"
    >
      <a class="btn-text">아이디가 올바르지 않습니다.</a>
    </v-btn>
  </div>
  <v-dialog v-model="newPasswordDialog">
    <v-card>
      <v-card-text>
        <div>
          <div class="auth-modal-top">
            <p>임시 비밀 번호를 전송합니다.</p>
            <p style="font-size: 18px; margin-top: 5px">
              {{ newPasswordMessage }}
            </p>
          </div>
          <input
            v-model="newPasswordEmail"
            type="text"
            placeholder=" 이메일"
            class="login-modal-textinput"
            style="width: 330px"
          />
          <div
            style="
              display: flex;
              justify-content: space-around;
              margin-top: 20px;
              margin-bottom: 15px;
            "
          >
            <v-btn
              v-if="isValidEmail"
              color="#5a87e0"
              style="color: white"
              @click="sendNewPassword()"
              >인증하기</v-btn
            >
            <v-btn v-else disabled color="#5a87e0" style="color: white"
              >인증하기</v-btn
            >
            <v-btn @click="newPasswordDialog = false">취소하기</v-btn>
          </div>
        </div>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script setup>
import axios from "axios";
import { ref, watchEffect } from "vue";
import { useAccountStore } from "../store";
const accountStore = useAccountStore();
const loginId = ref("");
const loginPassword = ref("");
const isValidId = ref(false);
const newPasswordDialog = ref(false);
const newPasswordEmail = ref("");
const newPasswordMessage = ref("회원님의 이메일 아이디를 입력해 주세요");
const isValidEmail = ref(false);
const emailRule = /^([0-9a-zA-Z_.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
// const user = {
//   email: "test@test.com",
//   password: "test1234",
// };
function loginBtn() {
  const user = {
    email: loginId.value,
    pw: loginPassword.value,
  };
  accountStore.loginAccount(user);
}
function loginEnter() {
  if (isValidId.value) {
    loginBtn();
  }
}
function sendNewPassword() {
  newPasswordMessage.value = "메일로 발송중입니다....";
  axios({
    url: "http://j7b308.p.ssafy.io:8081/yeogida/members/sendPw",
    method: "GET",
    params: {
      email: newPasswordEmail.value,
    },
  })
    .then((res) => {
      newPasswordMessage.value = "임시 비밀번호 발송이 완료되었습니다.";
      newPasswordEmail.value = "";
      console.log(res);
    })
    .catch((err) => {
      newPasswordMessage.value =
        "발송에 실패했습니다. 주소를 다시 확인해주세요.";
      console.log(err);
    });
}
watchEffect(() => {
  isValidId.value = emailRule.test(loginId.value);
});
watchEffect(() => {
  isValidEmail.value = emailRule.test(newPasswordEmail.value);
});
</script>

<style scoped>
.login-modal {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.login-modal-info {
  margin-top: 50px;
  height: 22px;
  font-family: "MICEGothic";
  color: #6fc0f7;
  text-align: center;
}
.login-modal-textinput {
  width: 310px;
  height: 45px;
  border-radius: 1px;
  border: 1px solid #bcbcbc;
}
.btn-text {
  font-size: 17px;
  font-family: "MICEGothic";
}
.auth-modal-top {
  margin-bottom: 20px;
  font-size: 22px;
  font-family: "MICEGothic";
}
</style>

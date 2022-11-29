<template>
  <div class="signup-modal">
    <div style="margin-top: 24px" class="signup-modal-warning">
      {{ isValidId }}
    </div>
    <div style="display: flex">
      <input
        v-model="signupId"
        type="text"
        placeholder=" 아이디"
        class="signup-modal-input"
        style="width: 220px"
        @blur="checkId()"
      />
      <v-btn
        v-if="isEmailAuth"
        style="margin-left: 20px; color: white"
        disabled
        color="#5a87e0"
        width="70px"
        height="30px"
      >
        <a class="signup-modal-id-btn">인증됨</a>
      </v-btn>
      <div v-else>
        <v-btn
          v-if="!isValidId"
          style="margin-left: 20px; color: white"
          color="#5a87e0"
          width="70px"
          height="30px"
          @click="emailAuthSend()"
        >
          <a class="signup-modal-id-btn">메일 인증</a>
        </v-btn>
        <v-btn
          v-else
          style="margin-left: 20px; color: white"
          color="#555555"
          disabled
          width="70px"
          height="30px"
        >
          <a class="signup-modal-id-btn">메일 인증</a>
        </v-btn>
      </div>
    </div>
    <div class="signup-modal-warning">
      {{ isValidPassword }}
    </div>
    <input
      v-model="signupPassword"
      type="password"
      placeholder=" 비밀번호"
      class="signup-modal-input"
      @blur="checkPassword()"
    />
    <div class="signup-modal-warning">
      {{ isValidPassword2 }}
    </div>
    <input
      v-model="signupPassword2"
      type="password"
      placeholder=" 비밀번호 확인"
      class="signup-modal-input"
      @blur="checkPassword2()"
    />
    <div class="signup-modal-warning">
      {{ isValidName }}
    </div>
    <input
      v-model="signupName"
      type="text"
      placeholder=" 이름"
      class="signup-modal-input"
      @blur="checkName()"
    />
    <div class="signup-modal-userinfo">
      <div style="width: 130px; height: 38px; margin-top: 10px">
        <v-select
          v-model="signupGender"
          :items="gender"
          item-title="KO"
          item-value="EN"
          label="성별"
          variant="underlined"
          density="compact"
        >
        </v-select>
      </div>
      <div style="width: 130px; height: 48px">
        <div class="signup-modal-warning" style="width: 130px; margin-top: 0px">
          {{ isValidAge }}
        </div>
        <input
          v-model="signupAge"
          type="text"
          placeholder=" 나이"
          class="signup-modal-input"
          style="width: 130px"
          @blur="checkAge()"
        />
      </div>
    </div>
    <v-btn
      v-if="totalValid"
      style="margin-top: 41px; color: white"
      color="#5a87e0"
      width="310px"
      height="45px"
      @click="signupBtn()"
    >
      <a class="btn-text">회원가입</a>
    </v-btn>
    <v-btn
      v-else
      disabled
      style="margin-top: 41px; color: white"
      color="#9d9d9d"
      width="310px"
      height="45px"
      @click="signupBtn()"
    >
      <a class="btn-text">필수 입력칸을 채워주세요</a>
    </v-btn>
  </div>
  <v-dialog v-model="emailAuthDialog">
    <v-card>
      <v-card-text>
        <div>
          <div class="auth-modal-top">
            <p>메일 인증 코드를 입력해 주세요</p>
            <p style="font-size: 18px; margin-top: 5px">
              {{ emailAuthMessage }}
            </p>
          </div>
          <input
            v-model="authCode"
            type="text"
            placeholder=" 인증코드를 입력해주세요"
            class="signup-modal-input"
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
              color="#5a87e0"
              style="color: white"
              @click="emailAuthVerify()"
              >인증하기</v-btn
            >
            <v-btn @click="emailAuthDialog = false">취소하기</v-btn>
          </div>
        </div>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script setup>
import axios from "axios";
import { ref } from "vue";
import { useAccountStore } from "../store";
const accountStore = useAccountStore();
const signupId = ref("");
const signupPassword = ref("");
const signupPassword2 = ref("");
const signupName = ref("");
const signupGender = ref("");
const signupAge = ref("");
const isValidId = ref("*필수 입력입니다.");
const isValidPassword = ref("*필수 입력입니다.");
const isEmailAuth = ref(false);
const isValidPassword2 = ref("*필수 입력입니다.");
const isValidName = ref("*필수 입력입니다.");
const isValidAge = ref("*필수 입력입니다.");
const totalValid = ref(false);
const emailAuthMessage = ref("메일이 발송중입니다.");
const authCode = ref("");
const gender = [
  { KO: "남", EN: "MALE" },
  { KO: "여", EN: "FEMALE" },
];
const emailRule = /^([0-9a-zA-Z_.-]+)@([0-9a-zA-Z_-]+)(\.[0-9a-zA-Z_-]+){1,2}$/;
const passwordRule =
  /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,12}$/;
const ageRule = /^([0-9]){1,3}$/;
const emailAuthDialog = ref(false);
function emailAuthSend() {
  emailAuthDialog.value = true;
  emailAuthMessage.value = "메일이 발송중입니다.";
  axios({
    url: "http://j7b308.p.ssafy.io:8081/yeogida/members/checkEmail",
    method: "GET",
    params: { email: signupId.value },
  })
    .then((res) => {
      console.log(res);
      emailAuthDialog.value = true;
      emailAuthMessage.value = "메일이 발송 완료되었습니다!";
    })
    .catch((err) => {
      console.log(err);
      emailAuthMessage.value = "이메일 아이디를 다시 확인해 주세요.";
    });
}
function emailAuthVerify() {
  axios({
    url: "http://j7b308.p.ssafy.io:8081/yeogida/members/cehckEmailCode",
    method: "GET",
    params: {
      email: signupId.value,
      code: authCode.value,
    },
  })
    .then((res) => {
      console.log(res);
      isEmailAuth.value = true;
      emailAuthDialog.value = false;
    })
    .catch((err) => {
      console.log(err);
    });
}
function totalValidCheck() {
  if (
    !isValidId.value &&
    !isValidAge.value &&
    !isValidName.value &&
    !isValidPassword.value &&
    !isValidPassword2.value
  ) {
    totalValid.value = true;
  }
}
function checkId() {
  if (emailRule.test(signupId.value)) {
    isValidId.value = "";
    totalValidCheck();
  } else {
    isValidId.value = "*이메일 형식으로 입력하셔야 합니다.";
  }
}
function checkPassword() {
  if (passwordRule.test(signupPassword.value)) {
    isValidPassword.value = "";
    totalValidCheck();
  } else {
    isValidPassword.value = "*영어, 숫자, 특수문자 조합 8~12자 입니다.";
  }
}
function checkPassword2() {
  if (signupPassword.value == signupPassword2.value) {
    isValidPassword2.value = "";
    totalValidCheck();
  } else {
    isValidPassword2.value = "*비밀번호가 다릅니다.";
  }
}
function checkName() {
  if (signupName.value) {
    isValidName.value = "";
    totalValidCheck();
  } else {
    isValidName.value = "*필수 입력입니다.";
  }
}
function checkAge() {
  if (ageRule.test(signupAge.value)) {
    isValidAge.value = "";
    totalValidCheck();
  } else {
    isValidAge.value = "*숫자를 입력해주세요.";
  }
}
function signupBtn() {
  const user = {
    age: signupAge.value,
    email: signupId.value,
    gender: signupGender.value,
    name: signupName.value,
    pw: signupPassword.value,
  };
  accountStore.signupAccount(user);
}
</script>

<style scoped>
.signup-modal {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.signup-modal-warning {
  width: 310px;
  height: 16px;
  font-family: "MICEGothic";
  font-size: 13px;
  margin-top: 13px;
  color: red;
}
.signup-modal-input {
  width: 310px;
  height: 32px;
  border-radius: 1px;
  border: 1px solid #bcbcbc;
}
.signup-modal-id-btn {
  font-size: 13px;
  font-family: "MICEGothic";
}
.signup-modal-userinfo {
  display: flex;
  justify-content: space-between;
  width: 310px;
  height: 48px;
  margin-top: 13px;
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

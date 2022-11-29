<template>
  <div class="changepassword">
    <div class="changepassword-maintext">비밀번호 변경</div>
    <div class="changepassword-subtext">
      안전한 비밀번호로 내 정보를 변경하세요
    </div>
    <div class="changepassword-subtext">
      이전에 사용한 적이 없는 비밀번호가 안전합니다
    </div>
    <div>
      <input
        type="password"
        placeholder="현재 비밀번호"
        class="changepassword-inputpassword"
      />
    </div>
    <div>
      <input
        v-model="password"
        type="password"
        placeholder="새 비밀번호"
        class="changepassword-inputpassword"
      />
    </div>
    <div>
      <input
        type="password"
        placeholder="새 비밀번호 확인"
        class="changepassword-inputpassword"
      />
    </div>
    <div class="">
      <button class="changepassword-changebtn" @click="changePassword">
        확인
      </button>
    </div>
    <div class="">
      <button
        class="changepassword-cancelbtn"
        @click="$emit('closeChangePassword')"
      >
        취소
      </button>
    </div>
  </div>
</template>

<script setup>
import axios from "axios";
import { ref } from "vue";
//기존에 mypage에서 옮겨옴
//일단 임시로 준 스웨거는  기존 password 검증 단계는 없으니 그건 나중에...

const emit = defineEmits([""]);

const password = ref("");
function changePassword() {
  const token = sessionStorage.getItem("YeogidaToken");
  const pwdata = {
    jwtToken: token,
    pw: password.value,
  };
  axios({
    url: "http://j7b308.p.ssafy.io:8081/yeogida/members",
    method: "PUT",
    data: pwdata,
  })
    .then(() => {
      alert("비밀번호가 변경되었습니다");
      emit("closeChangePassword");
    })
    .catch((err) => {
      console.log(err);
    });
  password.value = "";
}
</script>

<style scoped>
.changepassword {
  width: 450px;
  height: 400px;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  border: solid 1px;
  border-color: #d9d9d9;
  z-index: 3;
}
.changepassword-maintext {
  margin-top: 10px;
  margin-bottom: 10px;
  margin-left: 40px;
  font-family: twayair;
  font-size: 20px;
}
.changepassword-subtext {
  margin-left: 40px;
  font-family: twayair;
  font-size: 12px;
  color: red;
}
.changepassword-inputpassword {
  width: 370px;
  height: 40px;
  margin-top: 15px;
  margin-left: 40px;
  border: solid 1px;
  border-color: #d9d9d9;
}
.changepassword-changebtn {
  width: 370px;
  height: 40px;
  margin-left: 40px;
  margin-top: 30px;
  background-color: #6fc0f7;
  font-size: 16px;
  font-family: twayair;
  color: white;
}
.changepassword-cancelbtn {
  width: 370px;
  height: 40px;
  margin-left: 40px;
  margin-top: 10px;
  background-color: white;
  font-size: 16px;
  font-family: twayair;
  border: solid 1px;
  border-color: #d9d9d9;
}
</style>

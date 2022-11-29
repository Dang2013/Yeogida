<template>
  <div class="select-type-modal">
    <div class="select-type-modal-content">
      <div>
        <v-icon class="reset" @click="reset">mdi-replay </v-icon
        ><span class="reset" @click="reset">초기화</span>
        <span class="select-type-modal-title">태그 고르기</span>
      </div>
      <div class="select-type-modal-subtext">
        <div>여행 유형과 동반 유형을 선택하세요.</div>
        <div>우리들의 서비스는 당신에게 딱 맞는 여행지를 추천합니다.</div>
      </div>
      <div class="select-type-modal-select-text">여행 유형 선택</div>
      <div class="select-type-traveltype-buttons">
        <div>
          <button
            :class="[
              travelType1Selected == true
                ? 'select-type-button-traveltype-selected'
                : 'select-type-button-traveltype',
            ]"
            @click="selectTravelType(travelType1)"
          >
            맛집
          </button>
          <button
            :class="[
              travelType2Selected === true
                ? 'select-type-button-traveltype-selected'
                : 'select-type-button-traveltype',
            ]"
            @click="selectTravelType(travelType2)"
          >
            산
          </button>
          <button
            :class="[
              travelType3Selected === true
                ? 'select-type-button-traveltype-selected'
                : 'select-type-button-traveltype',
            ]"
            @click="selectTravelType(travelType3)"
          >
            바다
          </button>
          <button
            :class="[
              travelType4Selected === true
                ? 'select-type-button-traveltype-selected'
                : 'select-type-button-traveltype',
            ]"
            @click="selectTravelType(travelType4)"
          >
            동굴
          </button>
          <button
            :class="[
              travelType4Selected === true
                ? 'select-type-button-traveltype-selected'
                : 'select-type-button-traveltype',
            ]"
            @click="selectTravelType(travelType8)"
          >
            캠핑
          </button>
        </div>
        <div class="select-type-traveltype-buttons-second">
          <button
            :class="[
              travelType5Selected === true
                ? 'select-type-button-traveltype-selected'
                : 'select-type-button-traveltype',
            ]"
            @click="selectTravelType(travelType5)"
          >
            공원
          </button>
          <button
            :class="[
              travelType6Selected === true
                ? 'select-type-button-traveltype-selected'
                : 'select-type-button-traveltype',
            ]"
            @click="selectTravelType(travelType6)"
          >
            갯벌
          </button>
          <button
            :class="[
              travelType7Selected === true
                ? 'select-type-button-traveltype-selected'
                : 'select-type-button-traveltype',
            ]"
            @click="selectTravelType(travelType7)"
          >
            숲
          </button>
          <button
            :class="[
              travelType7Selected === true
                ? 'select-type-button-traveltype-selected'
                : 'select-type-button-traveltype',
            ]"
            @click="selectTravelType(travelType9)"
          >
            계곡
          </button>
        </div>
      </div>
      <div class="select-type-modal-select-text-second">동반 유형 선택</div>
      <div class="select-type-accompanytype-buttons">
        <div>
          <button
            :class="[
              accompanytype1Selected === true
                ? 'select-type-button-accompanytype-selected'
                : 'select-type-button-accompanytype',
            ]"
            class="select-type-button-accompanytype"
            @click="selectType(accompanytype1)"
          >
            혼자
          </button>
          <button
            :class="[
              accompanytype2Selected === true
                ? 'select-type-button-accompanytype-selected'
                : 'select-type-button-accompanytype',
            ]"
            @click="selectType(accompanytype2)"
          >
            학생
          </button>
          <button
            :class="[
              accompanytype3Selected === true
                ? 'select-type-button-accompanytype-selected'
                : 'select-type-button-accompanytype',
            ]"
            @click="selectType(accompanytype3)"
          >
            친구
          </button>
          <button
            :class="[
              accompanytype4Selected === true
                ? 'select-type-button-accompanytype-selected'
                : 'select-type-button-accompanytype',
            ]"
            @click="selectType(accompanytype4)"
          >
            연인
          </button>
        </div>
        <div class="select-type-accompanytype-buttons-second">
          <button
            :class="[
              accompanytype5Selected === true
                ? 'select-type-button-accompanytype-selected'
                : 'select-type-button-accompanytype',
            ]"
            @click="selectType(accompanytype5)"
          >
            배우자
          </button>
          <button
            :class="[
              accompanytype6Selected === true
                ? 'select-type-button-accompanytype-selected'
                : 'select-type-button-accompanytype',
            ]"
            @click="selectType(accompanytype6)"
          >
            부모님
          </button>
          <button
            :class="[
              accompanytype7Selected === true
                ? 'select-type-button-accompanytype-selected'
                : 'select-type-button-accompanytype',
            ]"
            @click="selectType(accompanytype7)"
          >
            자녀
          </button>
          <button
            :class="[
              accompanytype8Selected === true
                ? 'select-type-button-accompanytype-selected'
                : 'select-type-button-accompanytype',
            ]"
            @click="selectType(accompanytype8, accompanytype8Selected)"
          >
            회사동료
          </button>
        </div>
      </div>
      <div class="select-type-text-selected">선택된 태그</div>
      <div class="select-type-selected">{{ dataStore.types }}</div>
      <div class="select-type-bottomtext">
        선택한 태그를 이용하여 빅데이터가 여행지를 추천합니다.
      </div>
      <button class="select-type-submitbtn" @click="submit">확인</button>
      <button
        class="select-type-cancelbtn"
        @click="modalStore.typeModal = false"
      >
        취소
      </button>
    </div>
  </div>
</template>

<script setup>
import api from "../api/yeogida.js";
import axios from "axios";

import { ref } from "vue";
import { useModalStore, useDataStore, useRecommendStore } from "../store";
import Swal from "sweetalert2";

const modalStore = useModalStore();
const dataStore = useDataStore();
const recommendStore = useRecommendStore();

const travelType1 = "맛집";
const travelType2 = "산";
const travelType3 = "바다";
const travelType4 = "동굴";
const travelType5 = "공원";
const travelType6 = "갯벌";
const travelType7 = "숲";
const travelType8 = "캠핑";
const travelType9 = "계곡";

const travelType1Selected = ref(false);
const travelType2Selected = ref(false);
const travelType3Selected = ref(false);
const travelType4Selected = ref(false);
const travelType5Selected = ref(false);
const travelType6Selected = ref(false);
const travelType7Selected = ref(false);

const accompanytype1 = "혼자";
const accompanytype2 = "학생";
const accompanytype3 = "친구";
const accompanytype4 = "연인";
const accompanytype5 = "배우자";
const accompanytype6 = "부모님";
const accompanytype7 = "자녀";
const accompanytype8 = "회사동료";

const accompanytype1Selected = ref(false);
const accompanytype2Selected = ref(false);
const accompanytype3Selected = ref(false);
const accompanytype4Selected = ref(false);
const accompanytype5Selected = ref(false);
const accompanytype6Selected = ref(false);
const accompanytype7Selected = ref(false);
const accompanytype8Selected = ref(false);
const accompanytype9Selected = ref(false);

function selectType(value) {
  let temp = false;
  for (let i = 0; i < dataStore.type.length; i++) {
    if (dataStore.type[i] === value) {
      temp = true;
      dataStore.type.splice(i, 1);
      i--;
    }
  }

  if (dataStore.type.length == 3) {
    Swal.fire({
      icon: "warning",
      title: "Error!",
      text: "태그는 최대 3개까지만 선택할 수 있습니다.",
      confirmButtonText: "확인",
    });
  } else {
    dataStore.types = "";

    if (temp == false) {
      dataStore.type.push(value);
    }

    for (let i = 0; i < dataStore.type.length; i++) {
      dataStore.types = dataStore.types + "#" + dataStore.type[i] + " ";
    }
  }
}

function selectTravelType(value) {
  let temp = false;
  for (let i = 0; i < dataStore.type.length; i++) {
    if (dataStore.type[i] === value) {
      temp = true;
      dataStore.type.splice(i, 1);
      i--;
    }
  }

  if (dataStore.type.length == 3) {
    Swal.fire({
      icon: "warning",
      title: "Error!",
      text: "태그는 최대 3개까지만 선택할 수 있습니다.",
      confirmButtonText: "확인",
    });
  } else {
    dataStore.types = "";

    if (temp == false) {
      dataStore.type.push(value);
    }

    for (let i = 0; i < dataStore.type.length; i++) {
      dataStore.types = dataStore.types + "#" + dataStore.type[i] + " ";
    }
  }
}

function reset() {
  dataStore.type = [];
  dataStore.types = "";
  accompanytype1Selected.value = false;
  accompanytype2Selected.value = false;
  accompanytype3Selected.value = false;
  accompanytype4Selected.value = false;
  accompanytype5Selected.value = false;
  accompanytype6Selected.value = false;
  accompanytype7Selected.value = false;
  accompanytype8Selected.value = false;
  accompanytype9Selected.value = false;

  travelType1Selected.value = false;
  travelType2Selected.value = false;
  travelType3Selected.value = false;
  travelType4Selected.value = false;
  travelType5Selected.value = false;
  travelType6Selected.value = false;
  travelType7Selected.value = false;
}

function submit() {
  getTourByAccompanyType();
  modalStore.typeModal = false;
}

function getTourByAccompanyType() {
  dataStore.data = [];
  let s = "";
  for (let i = 0; i < dataStore.type.length; i++) {
    s = s + " " + dataStore.type[i];
  }
  axios({
    url: api.tour.keyword(),
    method: "GET",
    params: {
      keywords: s,
    },
  })
    .then((res) => {
      console.log(res);
      dataStore.data = res.data;
      recommendStore.recommendComplete(res.data[0].address, res.data[0].id);
    })
    .catch((err) => {
      console.log(err);
    });
}
</script>

<style scoped>
.select-type-modal {
  width: 660px;
  height: 720px;
  border: solid 1px;
  margin-top: 100px;
  z-index: 1;
  background-color: white;
  position: fixed;
  top: 40vh;
  left: 50vw;
  transform: translate(-50%, -50%);
  font-family: "twayair";
}
.select-type-modal-content {
  margin-top: 20px;
  margin-left: 25px;
}
.reset {
  cursor: pointer;
}
.select-type-modal-title {
  font-size: 24px;
  margin-left: 175px;
}
.select-type-modal-subtext {
  margin-top: 10px;
  font-size: 16px;
}
.select-type-modal-select-text {
  font-size: 20px;
  margin-top: 10px;
}
.select-type-modal-select-text-second {
  font-size: 20px;
  margin-top: 10px;
}
.select-type-traveltype-buttons {
  margin-top: 10px;
  margin-left: 45px;
}
.select-type-traveltype-buttons-second {
  margin-left: 60px;
  margin-top: 5px;
}
.select-type-button-traveltype {
  margin-right: 10px;
  width: 100px;
  height: 40px;
  background-color: #90caf9;
  border-radius: 30px;
  color: white;
}
.select-type-button-traveltype:hover {
  background-color: #0038ff;
}
.select-type-button-traveltype-selected {
  margin-right: 30px;
  width: 100px;
  height: 40px;
  background-color: #0038ff;
  border-radius: 30px;
  color: white;
}
.select-type-accompanytype-buttons {
  margin-left: 45px;
}
.select-type-accompanytype-buttons-second {
  margin-top: 5px;
  margin-left: 50px;
}
.select-type-button-accompanytype {
  margin-right: 10px;
  width: 100px;
  height: 40px;
  background-color: #90caf9;
  border-radius: 30px;
  color: white;
}
.select-type-button-accompanytype:hover {
  background-color: #0038ff;
}
.select-type-button-accompanytype-selected {
  margin-right: 10px;
  width: 100px;
  height: 40px;
  background-color: #0038ff;
  border-radius: 30px;
  color: white;
}
.select-type-text-selected {
  margin-top: 10px;
  font-size: 20px;
}
.select-type-selected {
  margin-top: 10px;
  width: 610px;
  height: 90px;
  border: solid 1px;
  display: flex;
  font-size: 24px;
  justify-content: center;
  align-items: center;
}
.select-type-bottomtext {
  margin-top: 20px;
  font-size: 14px;
}
.select-type-submitbtn {
  margin-top: 10px;
  width: 610px;
  height: 65px;
  border-radius: 90px;
  background-color: #90caf9;
  color: white;
}
.select-type-submitbtn:hover {
  background-color: #0038ff;
}
.select-button-selected {
  background-color: #0038ff;
}
.select-type-cancelbtn {
  margin-top: 10px;
  width: 610px;
  height: 65px;
  border-radius: 90px;
  background-color: #f6f6f6;
  color: black;
}
.select-type-cancelbtn:hover {
  background-color: #0038ff;
}
</style>

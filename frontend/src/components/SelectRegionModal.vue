<template>
  <div class="select-region-modal">
    <div class="select-region-modal-content">
      <div>
        <v-icon class="select-region-modal-reset" @click="reset"
          >mdi-replay </v-icon
        ><span class="select-region-modal-reset" @click="reset">초기화</span>
        <span class="select-region-modal-title">여행지역 선택</span>
      </div>
      <div class="select-region-modal-subtext">
        원하시는 여행 지역을 선택해주세요
      </div>
      <div class="select-region-modal-selectrigion-text">
        <div>
          <span>지역</span
          ><span class="select-region-modal-selectrigion-text-detail"
            >상세지역</span
          >
        </div>
      </div>
      <div>
        <!--
        <v-select
          :items="items"
          label="여행할 도를 선택하세요"
          variant="outlined"
          style="width: 285px; height: 50px"
          @select="selectDo"
        ></v-select>
        -->

        <select
          class="select-region-modal-input"
          required
          @change="selectDo($event)"
        >
          <option value="">여행할 도를 선택하세요</option>
          <option v-for="data in regionsDo" :key="data.id" :value="data.name">
            {{ data.name }}
          </option>
        </select>
        <select
          class="select-region-modal-input-si"
          required
          @change="selectSi($event)"
        >
          <option value="전체">전체</option>
          <option v-for="data in regionSi" :key="data.id" :value="data.name">
            {{ data.name }}
          </option>
        </select>
      </div>

      <div class="select-region-modal-selected-text">선택된 지역</div>
      <div class="select-region-modal-selected-box">{{ region }}</div>
      <div class="select-region-bottomtext">
        선택된 지역을 이용하여 빅데이터가 여행지를 추천합니다
      </div>
      <button class="select-region-submitbtn" @click="submit">확인</button>
      <button
        class="select-region-cancelbtn"
        @click="modalStore.regionModal = false"
      >
        취소
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useModalStore, useDataStore, useRecommendStore } from "../store";
import Swal from "sweetalert2";
import api from "../api/yeogida.js";
import axios from "axios";

//pinia, dataStore는 이 모달에서 도,시를 요청해서 받은 여행지 데이터를 저장하는 스토어
const modalStore = useModalStore();
const dataStore = useDataStore();
const recommendStore = useRecommendStore();

// 도, 시 문자열을 화면에 표시할 변수
const region = ref();
// 도 선택 시 문자열을 저장하는 변수
const doregion = ref();
//api로 받아온 도 데이터 저장하는 변수
const regionsDo = ref();
//api로 받아온 시 데이터 저장하는 변수
const regionSi = ref();
//선택한 도 id를 저장. 전체 클릭 시 해당 변수에 도 id 저장.
const selectedDoId = ref();
//선택한 시id를 저장.
const selectedSiId = ref();
// 전체 선택 시 false, 시 선택 시 true
const doSi = ref();

//도 선택 시 api로 해당 도의 시를 요청해서 regionSi에 저장, 화면에는 region변수에 도 정보를 저장해서 출력
function selectDo(event) {
  for (let i = 0; i < regionsDo.value.length; i++) {
    if (regionsDo.value[i].name == event.target.value) {
      selectedDoId.value = i + 1;
    }
  }
  region.value = "";
  const regiondo = event.target.value;
  region.value = regiondo;
  doregion.value = region.value;

  axios({
    url: api.regions.si(selectedDoId.value),
    method: "GET",
    /*
    headers: {
      Authorization: token,
    },
    */
  })
    .then((res) => {
      regionSi.value = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
}

//시 선택 시
function selectSi(event) {
  if (event.target.value == "전체") {
    doSi.value = false;
  } else {
    doSi.value = true;
    for (let i = 0; i < regionSi.value.length; i++) {
      if (regionSi.value[i].name == event.target.value) {
        selectedSiId.value = regionSi.value[i].id;
      }
    }
    region.value = doregion.value;
    const regionsi = event.target.value;
    region.value = region.value + " " + regionsi;
  }
}

//도 데이터를 받아와서 regionsDo에 저장하는 함수
function getRegionDo() {
  axios({
    url: api.regions.do(),
    method: "GET",
    /*
    headers: {
      Authorization: token,
    },
    */
  })
    .then((res) => {
      regionsDo.value = res.data;
    })
    .catch((err) => {
      console.log(err);
    });
}
getRegionDo(); // 해당 함수 실행

function getRegionBySi() {
  dataStore.data = [];
  axios({
    url: api.tour.si(),
    method: "GET",
    params: {
      jwtToken: sessionStorage.getItem("YeogidaToken"),
      siId: selectedSiId.value,
    },
    /*
    headers: {
      Authorization: token,
    },
    */
  })
    .then((res) => {
      dataStore.data = res.data;
      modalStore.openRegionModal = false;
      recommendStore.recommendComplete(res.data[0].address, res.data[0].id);
    })
    .catch((err) => {
      console.log(err);
    });
}

function getRegionByDo() {
  dataStore.data = [];
  axios({
    url: api.tour.do(),
    method: "GET",
    params: {
      jwtToken: sessionStorage.getItem("YeogidaToken"),
      doId: selectedDoId.value,
    },
    /*
    headers: {
      Authorization: token,
    },
    */
  })
    .then((res) => {
      dataStore.data = res.data;
      recommendStore.recommendComplete(res.data[0].address, res.data[0].id);
      modalStore.openRegionModal = false;
    })
    .catch((err) => {
      console.log(err);
    });
}

function reset() {
  region.value = "";
}

function submit() {
  if (region.value == "") {
    Swal.fire({
      icon: "warning",
      title: "Error!",
      text: "지역을 선택해 주세요",
      confirmButtonText: "확인",
    });
  } else if (doSi.value == true) {
    dataStore.region = region.value;
    modalStore.regionModal = false;
    getRegionBySi();
  } else {
    dataStore.region = region.value;
    modalStore.regionModal = false;
    getRegionByDo();
  }
}
</script>

<style scoped>
.select-region-modal {
  width: 660px;
  height: 640px;
  border: solid 1px;
  z-index: 1;
  background-color: white;
  margin-top: 100px;
  position: fixed;
  top: 40vh;
  left: 50vw;
  transform: translate(-50%, -50%);
  font-family: "twayair";
}
.select-region-modal-content {
  margin-left: 25px;
  margin-top: 20px;
  height: 610px;
}
.select-region-modal-reset {
  cursor: pointer;
}
.select-region-modal-title {
  font-size: 24px;
  margin-left: 155px;
}
.select-region-modal-subtext {
  font-size: 16px;
  margin-top: 10px;
}
.select-region-modal-selectrigion-text {
  margin-top: 10px;
  font-size: 20px;
}
.select-region-modal-selectrigion-text-detail {
  margin-left: 250px;
}
.select-region-modal-input {
  width: 285px;
  height: 50px;
  border: solid 1px;
}
.select-region-modal-input-si {
  width: 285px;
  height: 50px;
  margin-left: 5px;
  border: solid 1px;
}
.select-region-modal-selected-text {
  margin-top: 120px;
  font-size: 20px;
}
.select-region-modal-selected-box {
  margin-top: 10px;
  width: 600px;
  height: 90px;
  border: solid 1px;
  font-size: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.select-region-bottomtext {
  margin-top: 20px;
  font-size: 14px;
}
.select-region-submitbtn {
  margin-top: 10px;
  width: 610px;
  height: 65px;
  border-radius: 90px;
  background-color: #90caf9;
  color: white;
}
.select-region-cancelbtn {
  margin-top: 10px;
  width: 610px;
  height: 65px;
  border-radius: 90px;
  background-color: #f6f6f6;
  color: black;
}
.select-region-submitbtn:hover {
  background-color: #0038ff;
}
.select-region-cancelbtn:hover {
  background-color: #0038ff;
}
</style>

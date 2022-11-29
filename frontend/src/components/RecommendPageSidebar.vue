<template>
  <div class="sidebar">
    <div v-if="sidebar === true" class="recomm-sidebar">
      <div
        :class="[
          isTravelRegion === true
            ? 'recomm-sidebar-box'
            : 'recomm-sidebar-box2',
        ]"
      >
        <div class="recomm-sidebar-top">
          <div class="recomm-sidebar-type">
            <div
              :class="[
                isTravelRegion === true
                  ? 'recomm-sidebar-type-region'
                  : 'recomm-sidebar-type-category-region',
              ]"
            >
              <div class="recomm-sidebar-typename-region">
                <span @click="selectRegion"> 여행지역 선택 </span>
              </div>
            </div>
            <div
              :class="[
                isTravelType === true
                  ? 'recomm-sidebar-type-category-selected'
                  : 'recomm-sidebar-type-category',
              ]"
            >
              <div class="recomm-sidebar-typename-type">
                <span class="recomm-sidebar-type-span" @click="selectType">
                  여행유형 선택
                </span>
              </div>
            </div>
          </div>
          <div class="recomm-sidebar-items">
            <div
              class="recomm-sidebar-items-list"
              @click="recommendStore.aroundInfoModal = true"
            >
              <v-icon style="margin-left: 15px">
                mdi-silverware-fork-knife
              </v-icon>
              <div style="margin-left: 5px">편의시설 정보</div>
            </div>
            <v-btn
              v-if="recommendStore.placeaddress"
              style="
                color: white;
                font-size: 16px;
                font-family: 'MICEGothic Bold';
              "
              color="#6FC0F7"
              width="125px"
              height="40px"
              @click="
                planStore.setPlanner(
                  recommendStore.placeaddress,
                  recommendStore.selectedId
                )
              "
            >
              <span>여행계획 짜기</span>
            </v-btn>
            <v-btn
              v-else
              style="
                color: white;
                font-size: 16px;
                font-family: 'MICEGothic Bold';
              "
              color="#6FC0F7"
              width="125px"
              height="40px"
              disabled
            >
              <span>여행계획 짜기</span>
            </v-btn>
          </div>
          <div
            v-if="isTravelRegion === true"
            class="recomm-sidebar-items-region"
          >
            <div class="recomm-sidebar-selected-region">
              {{ dataStore.region }}
            </div>
            <v-btn
              style="
                color: white;
                font-size: 16px;
                font-family: 'MICEGothic Bold';
              "
              color="#6FC0F7"
              width="125px"
              height="40px"
              @click="openRegionModal"
            >
              <span>여행지역 선택</span>
            </v-btn>
          </div>
          <div v-if="isTravelType === true" class="recomm-sidebar-travletype">
            <div class="recomm-sidebar-items-type">
              <div class="recomm-sidebar-selected-region">내 여행의 태그</div>
              <v-btn
                style="
                  color: white;
                  font-size: 16px;
                  font-family: 'MICEGothic Bold';
                "
                color="#6FC0F7"
                width="125px"
                height="40px"
                @click="openTypeModal"
              >
                <span>태그 고르기</span>
              </v-btn>
            </div>
            <hr class="line" />
            <div
              style="
                display: flex;
                height: 73px;
                align-items: center;
                margin-left: 24px;
              "
            >
              <div v-for="tag in dataStore.type" :key="tag">
                <button class="recomm-sidebar-travletype-buttons">
                  <span class="recomm-sidebar-travletype-buttons-text"
                    >#{{ tag }}</span
                  >
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="recomm-sidebar-contents">
        <div
          v-for="tour in dataStore.data"
          :key="tour.length"
          class="recomm-sidebar-content"
        >
          <div
            class="recomm-sidebar-content-item"
            @click="recommendStore.recommendComplete(tour.address, tour.id)"
          >
            <img
              :src="`https://firebasestorage.googleapis.com/v0/b/yeogida-fb013.appspot.com/o/${tour.id}.jpg?alt=media&token=e3c8a6a5-2786-4f5f-9326-269b0c323590`"
              class="recomm-sidebar-content-img"
            />
            <div>
              <div
                style="
                  font-size: 24px;
                  margin-left: 20px;
                  font-family: 'MICEGothic';
                "
              >
                {{ tour.name }}
              </div>
              <div
                style="
                  font-size: 16px;
                  margin-left: 20px;
                  font-family: 'MICEGothic';
                "
              >
                {{ tour.address }}
              </div>
              <div
                style="
                  font-size: 16px;
                  font-family: 'MICEGothic';
                  margin-top: 20px;
                  margin-left: 20px;
                  color: #5acee0;
                "
              >
                <span
                  v-for="keyword in tour.keywords.slice(0, 3)"
                  :key="keyword"
                  >#{{ keyword }}&nbsp;
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <button
      v-if="sidebar === true"
      class="sidebar-display-btn"
      @click="sidebarClose"
    >
      <div class="left-triangle"></div>
    </button>
    <button
      v-if="sidebar === false"
      class="sidebar-display-btn"
      @click="sidebarOpen"
    >
      <div class="right-triangle"></div>
    </button>
    <select-region-modal
      v-if="modalStore.regionModal === true"
    ></select-region-modal>
    <select-type-modal v-if="modalStore.typeModal === true"></select-type-modal>
  </div>
</template>

<script setup>
import { ref } from "vue";
import {
  useModalStore,
  useDataStore,
  useRecommendStore,
  usePlanStore,
} from "../store";
import SelectRegionModal from "./SelectRegionModal.vue";
import SelectTypeModal from "./SelectTypeModal.vue";
const recommendStore = useRecommendStore();
const modalStore = useModalStore();
const dataStore = useDataStore();
const planStore = usePlanStore();

/*
function imageUrl(value) {
  const imageUrl = new URL(value, import.meta.url).href;
  return imageUrl;
}
*/

const isTravelRegion = ref(modalStore.region);
const isTravelType = ref(modalStore.type);
const sidebar = ref(true);
const regionModal = ref(modalStore.regionModal);
const typeModal = ref(modalStore.typeModal);

function sidebarClose() {
  sidebar.value = false;
}
function sidebarOpen() {
  sidebar.value = true;
}

function selectRegion() {
  isTravelRegion.value = true;
  isTravelType.value = false;
  dataStore.data = [];
  dataStore.types = "";
  dataStore.type = [];
}
function selectType() {
  isTravelRegion.value = false;
  isTravelType.value = true;
  dataStore.data = [];
  dataStore.region = "";
}

function openRegionModal() {
  modalStore.regionModal = true;
  regionModal.value = modalStore.regionModal;
}
function openTypeModal() {
  modalStore.typeModal = true;
  typeModal.value = modalStore.typeModal;
}
</script>
<style scoped>
.sidebar {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 80px;
  z-index: 2;
  height: 100%;
}
.sidebar-display-btn {
  border: solid 1px;
  border-radius: 0 10px 10px 0;
  border-color: #888888;
  background-color: white;
  width: 60px;
  height: 100px;
}
.recomm-sidebar {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 500px;
  width: 500px;
  height: 100%;
  background-color: #ffffff;
}
.recomm-sidebar-box {
  width: 500px;
  height: 290px;
  box-shadow: 0 4px 4px -4px black;
}
.recomm-sidebar-box2 {
  width: 500px;
  height: 355px;
  box-shadow: 0 4px 4px -4px black;
}

.recomm-sidebar-top {
  height: 290px;
}

.recomm-sidebar-type {
  display: flex;
  width: 450px;
  height: 60px;
  margin-top: 40px;
  background-color: #f6f6f6;
  border-radius: 30px;
  margin-left: 25px;
}
.recomm-sidebar-type-region {
  display: flex;
  align-items: center;
  width: 250px;
  border-radius: 30px;
  background-color: #6fc0f7;
  color: white;
}
.recomm-sidebar-type-category {
  display: flex;
  align-items: center;
  width: 200px;
  border-radius: 30px;
  background-color: #f6f6f6;
  justify-content: right;
}
.recomm-sidebar-type-category-region {
  display: flex;
  align-items: center;
  width: 200px;
  border-radius: 30px;
  background-color: #f6f6f6;
}
.recomm-sidebar-type-category-selected {
  display: flex;
  align-items: center;
  width: 250px;
  border-radius: 30px;
  background-color: #6fc0f7;
  color: white;
  justify-content: right;
}
.recomm-sidebar-typename-region {
  font-size: 20px;
  font-family: "MICEGothic";
  margin: 0 25px 0 25px;
  margin-left: 25px;
}
.recomm-sidebar-typename-type {
  font-size: 20px;
  font-family: "MICEGothic";
  margin: 0 25px 0 25px;
  text-align: end;
}
.recomm-sidebar-typename-region:hover {
  cursor: pointer;
  color: #6570fc;
}
.recomm-sidebar-typename-type:hover {
  cursor: pointer;
  color: #6570fc;
}
.recomm-sidebar-items {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  width: 470px;
  height: 42px;
  margin-top: 35px;
}
.recomm-sidebar-items-region {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  width: 470px;
  height: 42px;
  margin-top: 35px;
}
.recomm-sidebar-items-type {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 470px;
  height: 42px;
  margin-top: 35px;
}
.recomm-sidebar-items-list {
  margin: 0px 0px 0px 10px;
  font-size: 20px;
  font-family: "MICEGothic";
  display: flex;
}
.recomm-sidebar-items-list:hover {
  cursor: pointer;
  color: #6570fc;
  transform: scale(0.98);
}
.recomm-sidebar-selected-region {
  margin-left: 25px;
  font-family: "MICEGothic";
  font-size: 24px;
  width: 180px;
  height: 36px;
}
.line {
  width: 445px;
  margin-left: 25px;
  margin-top: 20px;
}
/*.recomm-sidebar-travletype {
  height: 112px;
}
*/
.recomm-sidebar-travletype-buttons {
  font-family: "MICEGothic";
  font-size: 16px;
  text-align: left;
  width: 110px;
  height: 30px;
  background-color: #5acee0;
  color: white;
  margin-bottom: 15px;
  margin-left: 5px;
  margin-right: 20px;
  border-radius: 15px;
}
.recomm-sidebar-travletype-buttons-text {
  margin-left: 7px;
}
.recomm-sidebar-content-item {
  display: flex;
  margin-left: 15px;
  margin-top: 25px;
  cursor: pointer;
  align-items: center;
}
.recomm-sidebar-content-img {
  width: 130px;
  height: 130px;
  border-radius: 100%;
  background-size: cover;
  border: 0px;
}
.recomm-sidebar-content {
  width: 479px;
  height: 160px;
  box-shadow: 0 4px 4px -4px black;
}
.right-triangle {
  width: 0;
  height: 0;
  margin-left: 10px;
  border-bottom: 20px solid transparent;
  border-top: 20px solid transparent;
  border-left: 40px solid black;
  border-right: 40px solid transparent;
}
.left-triangle {
  width: 0;
  height: 0;
  border-bottom: 20px solid transparent;
  border-top: 20px solid transparent;
  border-left: 40px solid black;
  border-right: 40px solid transparent;
  transform: rotate(180deg);
  margin-left: -35px;
}
.recomm-sidebar-contents {
  width: 500px;
  overflow-y: scroll;
  -ms-overflow-style: none; /* IE and Edge */
  scrollbar-width: none; /* Firefox */
}
.recomm-sidebar-contents::-webkit-scrollbar {
  width: 10px; /* 스크롤바의 너비 */
}
.recomm-sidebar-contents::-webkit-scrollbar-thumb {
  height: 10%; /* 스크롤바의 길이 */
  background: #6fc0f7; /* 스크롤바의 색상 */
  border-radius: 10px;
}
</style>

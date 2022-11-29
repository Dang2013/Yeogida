<template>
  <div class="plan">
    <div class="plan-sidebar">
      <div class="plan-sidebar-top">
        <div
          class="plan-sidebar-top-text"
          style="font-size: 16px; margin-top: 15px"
        >
          선택된 여행지
        </div>
        <div
          class="plan-sidebar-top-text"
          style="font-size: 30px; font-family: 'MICEGothic Bold'"
        >
          {{ planStore.planName }}
        </div>
        <div
          class="plan-sidebar-top-text"
          style="font-size: 20px; color: #555555"
        >
          {{ planStore.placeAddress }}
        </div>
        <v-btn
          style="
            color: white;
            font-size: 18px;
            font-family: 'MICEGothic Bold';
            margin-top: 15px;
          "
          color="#6FC0F7"
          width="420px"
          height="35px"
          @click="recommendStore.aroundInfoModal = true"
        >
          주변 지역 검색
        </v-btn>
      </div>
      <div class="plan-sidebar-content">
        <div
          v-for="(item, index) in planStore.items"
          :key="item.title"
          class="plan-items"
        >
          <div class="plan-items-contents">
            <div
              class="plan-items-title"
              @click="planStore.mapFocus(item.position)"
            >
              {{ item.title }}
            </div>
            <div class="plan-items-address">{{ item.address }}</div>
          </div>
          <div class="plan-items-controls">
            <v-icon
              class="plan-items-controls-btn"
              style="font-size: 30px"
              @click="planStore.minusIndex(index)"
            >
              mdi-arrow-up-box
            </v-icon>
            <v-icon
              v-if="item.delete"
              class="plan-items-controls-btn"
              style="font-size: 20px"
              @click="planStore.deleteIndex(index)"
            >
              mdi-delete
            </v-icon>
            <v-icon v-else style="font-size: 20px"> mdi-delete-off </v-icon>
            <v-icon
              class="plan-items-controls-btn"
              style="font-size: 30px"
              @click="planStore.plusIndex(index)"
            >
              mdi-arrow-down-box
            </v-icon>
          </div>
        </div>
      </div>
    </div>
    <PlanPageMap />
  </div>
  <PlaceDetailModal></PlaceDetailModal>
  <AroundInfoModal />
</template>

<script setup>
import { onMounted, onUnmounted } from "vue";
import PlanPageMap from "../components/PlanPageMap.vue";
import PlaceDetailModal from "../components/PlaceDetailModal.vue";
import AroundInfoModal from "../components/AroundInfoModal.vue";
import { usePlaceDetailStore, usePlanStore, useRecommendStore } from "../store";
const placeDetailStore = usePlaceDetailStore();
const planStore = usePlanStore();
const recommendStore = useRecommendStore();
onMounted(() => {
  placeDetailStore.recommMounted();
});
onUnmounted(() => {
  placeDetailStore.recommUnmounted();
});
</script>
<style scoped>
.plan {
  position: relative;
  display: flex;
  height: 1000px;
  top: 80px;
}
.plan-sidebar {
  width: 500px;
  height: 1000px;
  background-color: white;
}
.plan-sidebar-top {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
  height: 200px;
  background-color: white;
  box-shadow: 0 4px 4px -4px black;
}
.plan-sidebar-top-text {
  display: flex;
  width: 440px;
  font-family: "MICEGothic";
}
.plan-items {
  display: flex;
  align-items: center;
  width: 100%;
  height: 100px;
  box-shadow: 0 2px 2px -2px black;
}
.plan-items-contents {
  display: flex;
  flex-direction: column;
  margin-left: 20px;
  width: 410px;
}
.plan-items-controls {
  display: flex;
  flex-direction: column;
  justify-content: space-evenly;
  align-items: center;
  height: 100px;
}
.plan-items-title {
  width: 410px;
  height: 35px;
  font-size: 25px;
  font-family: "MICEGothic Bold";
  cursor: pointer;
}
.plan-items-title:hover {
  color: #6570fc;
}
.plan-items-address {
  width: 410px;
  height: 24px;
  margin-top: 5px;
  font-size: 16px;
  font-family: "MICEGothic";
}
.plan-items-controls-btn {
  cursor: pointer;
}
.plan-items-controls-btn:hover {
  color: #6570fc;
  transform: scale(0.95);
}
</style>

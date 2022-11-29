<template>
  <v-dialog v-model="recommendStore.aroundInfoModal">
    <v-card>
      <div class="around-modal">
        <div class="around-modal-top">
          <p>여행지 주변의 정보를 검색합니다.</p>
        </div>
        <div class="around-modal-ratio" style="margin-top: 20px">
          검색 범위
          <div class="around-modal-slider">
            <v-slider
              v-model="range"
              :min="2"
              :max="6"
              :step="1"
              thumb-label
            ></v-slider>
          </div>
          <div style="margin-left: 20px">
            {{ distance }}
          </div>
        </div>
        <div class="around-modal-ratio" style="margin-top: -15px">
          검색 갯수
          <div class="around-modal-slider">
            <v-slider
              v-model="nums"
              :min="3"
              :max="10"
              :step="1"
              thumb-label
            ></v-slider>
          </div>
          <div style="margin-left: 20px">{{ nums }}개</div>
        </div>
        <div class="around-modal-selection">
          <div class="around-modal-selection-btns">
            <AroundInfoModalBtn
              v-for="category in recommendStore.categories1"
              :key="category.name"
              :category="category"
            />
          </div>
          <div class="around-modal-selection-btns">
            <AroundInfoModalBtn
              v-for="category in recommendStore.categories2"
              :key="category.code"
              :category="category"
            />
          </div>
        </div>
        <div class="around-modal-bottom">
          <v-btn
            color="#6fc0f7"
            style="color: white; font-family: 'MICEGothic'"
            @click="
              ((recommendStore.aroundRange = range),
              (recommendStore.aroundNums = nums),
              (recommendStore.aroundSearchBtn = new Date())),
                (recommendStore.aroundInfoModal = false)
            "
            >검색하기</v-btn
          >
          <v-btn
            style="font-family: 'MICEGothic'"
            @click="recommendStore.aroundInfoModal = false"
            >취소하기</v-btn
          >
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>
<script setup>
import { ref, watch } from "vue";
import { useRecommendStore } from "../store";
import AroundInfoModalBtn from "./AroundInfoModalBtn.vue";
const recommendStore = useRecommendStore();
const range = ref(3);
const nums = ref(5);
const radius = ["200m", "500m", "1km", "2.5km", "5km"];
const distance = ref("500m");
watch(range, (newValue) => {
  distance.value = radius[newValue - 2];
});
</script>
<style scoped>
.around-modal {
  display: flex;
  width: 450px;
  height: 480px;
  flex-direction: column;
  align-items: center;
}
.around-modal-top {
  margin-top: 40px;
  height: 22px;
  font-family: "MICEGothic";
  font-size: 18px;
  color: #6fc0f7;
  text-align: center;
}
.around-modal-ratio {
  display: flex;
  font-family: "MICEGothic";
  align-items: center;
  width: 350px;
}
.around-modal-slider {
  width: 200px;
  margin-top: 20px;
  margin-left: 10px;
}
.around-modal-selection {
  width: 350px;
  height: 140px;
  margin-top: 20px;
  background-color: #eeeeee;
  border-radius: 8px;
}
.around-modal-selection-btns {
  display: flex;
  height: 40px;
  margin-top: 20px;
  margin-left: 12px;
}
.around-modal-bottom {
  display: flex;
  width: 350px;
  justify-content: space-around;
  margin-top: 30px;
  margin-bottom: 15px;
}
</style>

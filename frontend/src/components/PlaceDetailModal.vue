<template>
  <div>
    <v-row justify="center">
      <v-dialog v-model="placeDetailStore.dialog">
        <v-card class="detail-modal">
          <img
            :src="`https://firebasestorage.googleapis.com/v0/b/yeogida-fb013.appspot.com/o/${placeDetailStore.id}.jpg?alt=media&token=e3c8a6a5-2786-4f5f-9326-269b0c323590`"
            class="detail-modal-img"
          />
          <div class="detail-modal-top">
            <div class="detail-modal-title">{{ placeDetailStore.name }}</div>
            <v-btn
              v-if="placeDetailStore.isRecommPage"
              style="
                color: #555555;
                font-size: 14px;
                font-family: 'MICEGothic Bold';
              "
              color="#fae100"
              width="100px"
              height="35px"
              @click="toKakaoMap(placeDetailStore.name)"
            >
              카카오로 보기
            </v-btn>
            <v-btn
              v-else
              style="
                color: white;
                font-size: 16px;
                font-family: 'MICEGothic Bold';
              "
              color="#6570FC"
              width="100px"
              height="35px"
              @click="placeDetailStore.toMap()"
            >
              지도로 보기
            </v-btn>
          </div>
          <div class="detail-modal-middle">
            {{ placeDetailStore.address }}
            <div
              v-if="!isWished"
              class="detail-modal-btn"
              @click="placeDetailStore.addWishList(placeDetailStore.id)"
            >
              <v-icon style="font-size: 25px"> mdi-heart </v-icon>
              <div
                style="display: flex; justify-content: flex-end; width: 60px"
              >
                찜하기
              </div>
            </div>
            <div
              v-else
              class="detail-modal-btn"
              @click="placeDetailStore.subWishList(placeDetailStore.id)"
            >
              <v-icon style="font-size: 25px"> mdi-heart </v-icon>
              <div
                style="display: flex; justify-content: flex-end; width: 60px"
              >
                찜 해제
              </div>
            </div>
          </div>
          <div class="detail-modal-bottom">
            {{ placeDetailStore.type }}
            <div
              v-if="!isVisited"
              class="detail-modal-btn"
              @click="placeDetailStore.reviewDialog = true"
            >
              <v-icon style="font-size: 25px">
                mdi-pencil-box-multiple-outline
              </v-icon>
              <div
                style="display: flex; justify-content: flex-end; width: 60px"
              >
                리뷰
              </div>
            </div>
            <div
              v-else
              class="detail-modal-btn"
              @click="placeDetailStore.subVisitList(placeDetailStore.id)"
            >
              <v-icon style="font-size: 25px">
                mdi-pencil-box-multiple-outline
              </v-icon>
              <div
                style="
                  display: flex;
                  justify-content: flex-end;
                  width: 60px;
                  font-size: 15px;
                "
              >
                리뷰삭제
              </div>
            </div>
          </div>
        </v-card>
      </v-dialog>
    </v-row>
  </div>
  <v-dialog v-model="placeDetailStore.reviewDialog">
    <v-card>
      <v-card-text>
        <div>
          <div class="review-modal-top">
            <p>평점을 매기면 방문 리스트에 등록됩니다.</p>
          </div>
          <div class="text-center">
            <v-rating v-model="rating" hover half-increments></v-rating>
          </div>
          <div
            style="
              display: flex;
              justify-content: space-around;
              margin-top: 20px;
              margin-bottom: 15px;
            "
          >
            <v-btn
              color="error"
              @click="
                placeDetailStore.addVisitList(placeDetailStore.id, rating)
              "
              >평점주기</v-btn
            >
            <v-btn @click="placeDetailStore.reviewDialog = false"
              >취소하기</v-btn
            >
          </div>
        </div>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>
<script setup>
import { storeToRefs } from "pinia";
import { ref, watch } from "vue";
import { useAccountStore, usePlaceDetailStore } from "../store";
const placeDetailStore = usePlaceDetailStore();
const accountStore = useAccountStore();
const { isListChanged } = storeToRefs(accountStore);
const { imageUrl } = storeToRefs(placeDetailStore);
const image = ref("");
const rating = ref(5);
const isWished = ref(false);
const isVisited = ref(false);
function toKakaoMap(name) {
  window.open("https://map.kakao.com/link/search/" + name);
}
watch(imageUrl, (newValue) => {
  image.value = new URL(newValue, import.meta.url).href;
});
watch(isListChanged, () => {
  isWished.value = false;
  isVisited.value = false;
  accountStore.wishedList.forEach((e) => {
    if (placeDetailStore.id == e) {
      isWished.value = true;
    }
  });
  accountStore.visitedList.forEach((e) => {
    if (placeDetailStore.id == e) {
      isVisited.value = true;
    }
  });
});
</script>
<style scoped>
.detail-modal {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 450px;
  height: 480px;
}
.detail-modal-img {
  width: 430px;
  height: 305px;
  border-radius: 15px;
  margin-top: 10px;
}
.detail-modal-top {
  display: flex;
  width: 420px;
  justify-content: space-between;
  align-items: center;
  margin-top: 23px;
}
.detail-modal-title {
  font-family: "MICEGothic Bold";
  font-size: 30px;
}
.detail-modal-middle {
  display: flex;
  margin-top: 12px;
  width: 420px;
  height: 24px;
  justify-content: space-between;
  font-family: "MICEGothic";
  font-size: 20px;
  color: #555555;
}
.detail-modal-btn {
  display: flex;
  color: #6570fc;
}
.detail-modal-btn:hover {
  cursor: pointer;
  color: #6570fc;
  transform: scale(0.95);
}
.detail-modal-bottom {
  display: flex;
  width: 420px;
  height: 24px;
  margin-top: 8px;
  justify-content: space-between;
  font-family: "MICEGothic";
  font-size: 20px;
  color: #555555;
}
.review-modal-top {
  margin-bottom: 20px;
  font-size: 22px;
  font-family: "MICEGothic";
}
</style>

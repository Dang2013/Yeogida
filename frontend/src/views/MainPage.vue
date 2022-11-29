<template>
  <full-page ref="fullpage" :options="options" class="fullpage">
    <div class="mainpage1 page section">
      <div class="mainpage1-content">
        <div style="margin-left: 230px; margin-top: 100px">
          <div class="mainpage1-subtitle">
            <a>지친 당신을 위한 여행지 추천 서비스</a>
          </div>
          <div class="mainpage1-title">
            <a style="margin-top: -40px">여기다</a>
          </div>
          <div v-if="isLogin" class="mainpage1-btns">
            <v-btn
              style="color: white; font-size: 35px; font-family: 'MICEGothic'"
              color="#6570FC"
              width="300px"
              height="90px"
              @click="recommByArea()"
            >
              <a>여행지역 선택</a>
            </v-btn>
            <v-btn
              style="
                margin-left: 70px;
                color: white;
                font-size: 35px;
                font-family: 'MICEGothic';
              "
              color="#6570FC"
              width="300px"
              height="90px"
              @click="recommByType()"
            >
              <a>여행유형 선택</a>
            </v-btn>
          </div>
          <div v-else class="mainpage1-btns" style="margin-top: 50px">
            <div>
              <div class="mainpage1-login">
                로그인하시면 저희 서비스를 이용하실수 있습니다.
              </div>
              <v-btn
                color="#6570FC"
                style="color: white; font-size: 27px; font-family: 'MICEGothic'"
                width="220px"
                height="70px"
                @click="
                  accountStore.openLoginDialog(),
                    (accountStore.loginModal = true)
                "
              >
                <a>로그인 하기</a>
              </v-btn>
            </div>
          </div>
          <div v-if="page1MinHeight" class="mainpage1-introduction">
            <p>몇가지 질문에 답해주시면</p>
            <p style="margin-top: -10px">
              당신에게 맞는 최적의 관광지를 소개해 드립니다.
            </p>
          </div>
        </div>
      </div>
    </div>
    <div class="mainpage2 page section">
      <div class="mainpage2-content">
        <div class="mainpage2-content-comment">
          <div class="mainpage2-content-comment-first">
            <a>당신을 위한 빠른 추천</a>
          </div>
          <div class="mainpage2-content-comment-second">
            <a>여긴 어때요?</a>
          </div>
          <div style="margin-top: 70px" class="mainpage2-content-btn">
            <v-icon class="arrow-btn" @click="plusIndex()">
              mdi-chevron-double-up
            </v-icon>
          </div>
          <div class="mainpage2-content-comment-third">
            <div>키워드 :</div>
            <div style="margin-left: 20px">
              {{ recommendStore.fastKeyword[fastRecommIndex] }}
            </div>
          </div>
          <div style="margin-top: 8px" class="mainpage2-content-btn">
            <v-icon class="arrow-btn" @click="minusIndex()">
              mdi-chevron-double-down
            </v-icon>
          </div>
        </div>
        <div class="mainpage2-content-cards">
          <MainPageCard
            v-for="recomm in recommendStore.fastRecommList"
            :key="recomm.id"
            :recomm="recomm"
          />
        </div>
      </div></div
  ></full-page>
  <PlaceDetailModal></PlaceDetailModal>
</template>

<script setup>
import { storeToRefs } from "pinia";
import { onBeforeUnmount, onMounted, ref } from "vue";
import MainPageCard from "../components/MainPageCard.vue";
import PlaceDetailModal from "../components/PlaceDetailModal.vue";
import router from "../router";
import { useModalStore, useAccountStore, useRecommendStore } from "../store";
// import { useAccountStore } from "../store";
// const accountStore = useAccountStore();
const modalStore = useModalStore();
const accountStore = useAccountStore();
const recommendStore = useRecommendStore();
const height = ref(null);
const width = ref(null);
const page1MinHeight = ref(true);
const fullpage = ref();
const options = ref({
  anchors: ["page1", "page2", "page3"],
});
const { isLogin } = storeToRefs(accountStore);
const fastRecommIndex = ref(0);
recommendStore.fastRecomm();
function resize() {
  height.value = window.innerHeight;
  width.value = window.innerWidth;
  if (height.value < 700) {
    page1MinHeight.value = false;
  } else {
    page1MinHeight.value = true;
  }
}
function plusIndex() {
  if (fastRecommIndex.value != 2) {
    fastRecommIndex.value += 1;
  } else {
    fastRecommIndex.value = 0;
  }
  recommendStore.fastRecommIndex(fastRecommIndex.value);
}
function minusIndex() {
  if (fastRecommIndex.value != 0) {
    fastRecommIndex.value -= 1;
  } else {
    fastRecommIndex.value = 2;
  }
  recommendStore.fastRecommIndex(fastRecommIndex.value);
}
function recommByArea() {
  modalStore.regionModal = true;
  router.push({ name: "Recommend" });
}
function recommByType() {
  modalStore.region = false;
  modalStore.type = true;
  modalStore.typeModal = true;
  router.push({ name: "Recommend" });
}
onMounted(() => {
  resize();
  window.addEventListener("resize", resize);
  setTimeout(() => {
    recommendStore.fastRecommIndex(fastRecommIndex.value);
  }, 500);
});
onBeforeUnmount(() => {
  window.removeEventListener("resize", resize);
});
</script>

<style scoped>
.fullpage {
  width: 100vw;
  height: 100vh;
}
.mainpage1 {
  display: flex;
  width: 100%;
  height: 100vh;
  background-image: url("../assets/images/main1-1.jpg");
  background-size: cover;
  align-items: center;
}
.mainpage1-content {
  display: flex;
  width: 100vw;
  min-width: 1920px;
}
.mainpage1-subtitle {
  height: 60px;
  font-family: "twayair";
  font-size: 48px;
  color: white;
}
.mainpage1-title {
  display: flex;
  width: 600px;
  height: 220px;
  font-family: "twayair";
  font-size: 200px;
  color: white;
}
.mainpage1-btns {
  display: flex;
  margin-top: 80px;
  height: 130px;
}
.mainpage1-login {
  font-size: 22px;
  font-family: "twayair";
  color: white;
  margin-bottom: 10px;
}
.mainpage1-introduction {
  font-size: 40px;
  font-family: "twayair";
  color: white;
}
.mainpage2 {
  display: flex;
  width: 100%;
  height: 100%;
  background-image: url("../assets/images/main2.jpg");
  background-size: cover;
  align-items: center;
}
.mainpage2-content {
  display: flex;
  height: 500px;
  width: 100vw;
  min-width: 1920px;
  background-color: rgba(255, 255, 255, 0.4);
}
.mainpage2-content-comment {
  margin-left: 150px;
  width: 530px;
}
.mainpage2-content-comment-first {
  margin-top: 70px;
  font-size: 48px;
  height: 60px;
  font-family: "twayair";
}
.mainpage2-content-comment-second {
  font-size: 96px;
  height: 110px;
  font-family: "twayair";
}
.mainpage2-content-comment-third {
  display: flex;
  font-size: 40px;
  height: 50px;
  font-family: "twayair";
  color: #9d9d9d;
}
.mainpage2-content-btn {
  height: 28px;
  display: flex;
  width: 530px;
  justify-content: center;
  justify-items: center;
  align-items: center;
}
.arrow-btn:hover {
  cursor: pointer;
  color: #6570fc;
  transform: scale(0.9);
}
.mainpage2-content-cards {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  width: 1240px;
}
</style>
<style>
.fp-watermark {
  display: none !important;
}
</style>

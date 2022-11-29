<template>
  <div class="mypage">
    <div>
      <div class="mypage-img">
        <div class="mypage-text">마이페이지</div>
      </div>
    </div>
    <div class="selectbox">
      <div
        :class="[userProfileOpen == true ? 'backgroundcolor' : 'select']"
        @click="openUserProfile"
      >
        회원정보 확인
      </div>
      <div
        :class="[userWishListOpen == true ? 'backgroundcolor' : 'select']"
        @click="openUserWishList"
      >
        찜 목록
      </div>
      <div
        :class="[userVisitedOpen == true ? 'backgroundcolor' : 'select']"
        @click="openUserVisited"
      >
        나의 여행지
      </div>
    </div>
    <div id="profile" :class="[userProfileOpen == true ? 'display' : 'none']">
      <div class="title">회원정보 확인</div>
      <hr class="topline" />
      <div class="profile-text">
        이메일
        <span class="profile-valueemail">{{ email }}</span>
      </div>
      <hr class="line" />
      <div class="profile-text">
        비밀번호
        <button class="changebtn" @click="openChangePassword">변경</button>
      </div>
      <hr class="line" />
      <div class="profile-text">
        이름 <span class="profile-value">{{ name }}</span>
      </div>
      <hr class="line" />
      <div class="profile-text">
        성별 <span class="profile-value">{{ gender }}</span>
      </div>
      <hr class="line" />
      <div class="profile-text">
        나이 <span class="profile-value">{{ age }}</span>
      </div>
      <hr class="line" />
    </div>

    <div
      :class="[userWishListOpen == true ? 'display' : 'none']"
      class="userwishlist"
    >
      <div class="title">찜 목록</div>
      <hr class="topline" />
      <div class="cards">
        <div v-for="wish in wishPageNatedData()" :key="wish" class="cardlist">
          <v-card width="300px" @click="placeDetailStore.openDialog(wish.id)">
            <v-img
              height="200px"
              width="300px"
              :src="`https://firebasestorage.googleapis.com/v0/b/yeogida-fb013.appspot.com/o/${wish.id}.jpg?alt=media&token=e3c8a6a5-2786-4f5f-9326-269b0c323590`"
              cover
            ></v-img>
            <v-card-item>
              <v-card-title>{{ wish.name }}</v-card-title>
              <v-card-subtitle>
                <span class="mr-1"> {{ wish.address }}</span>
                <v-icon
                  color="error"
                  icon="mdi-fire-circle"
                  size="small"
                ></v-icon>
              </v-card-subtitle>
            </v-card-item>
            <v-card-text>
              <div>
                <span
                  v-for="keyword in wish.keywords.slice(0, 3)"
                  :key="keyword"
                  >#{{ keyword }}&nbsp;
                </span>
              </div>
            </v-card-text>
            <v-divider class="mx-4 mb-1"></v-divider>
          </v-card>
        </div>
      </div>
      <div class="btn-cover">
        <button
          :disabled="wishPageNum === 0"
          class="page-btn"
          @click="wishPrevPage"
        >
          이전
        </button>
        <span class="page-count"
          >{{ wishPageNum + 1 }} / {{ wishPageLast }} 페이지</span
        >
        <button
          :disabled="wishPageNum >= wishPageLast - 1"
          class="page-btn"
          @click="wishNextPage"
        >
          다음
        </button>
      </div>
    </div>

    <div
      :class="[userVisitedOpen == true ? 'display' : 'none']"
      class="userwishlist"
    >
      <div class="title">나의 여행지</div>
      <hr class="topline" />
      <div class="cards">
        <div
          v-for="visited in visitedPageNatedData()"
          :key="visited"
          class="cardlist"
          @click="placeDetailStore.openDialog(visited.tourResponse.id)"
        >
          <v-card width="300px" @click="modalOpen(visited)">
            <v-img
              height="200px"
              width="300px"
              :src="`https://firebasestorage.googleapis.com/v0/b/yeogida-fb013.appspot.com/o/${visited.tourResponse.id}.jpg?alt=media&token=e3c8a6a5-2786-4f5f-9326-269b0c323590`"
              cover
            ></v-img>
            <v-card-item>
              <v-card-title>{{ visited.tourResponse.name }}</v-card-title>
              <v-card-subtitle>
                <span class="mr-1">{{ visited.tourResponse.address }} </span>
                <v-icon
                  color="error"
                  icon="mdi-fire-circle"
                  size="small"
                ></v-icon>
              </v-card-subtitle>
            </v-card-item>
            <v-card-text>
              <div>
                <span
                  v-for="keyword in visited.tourResponse.keywords.slice(0, 3)"
                  :key="keyword"
                  >#{{ keyword }}&nbsp;
                </span>
              </div>
            </v-card-text>
            <v-divider class="mx-4 mb-1"></v-divider>
          </v-card>
        </div>
      </div>
      <div class="btn-cover">
        <button
          :disabled="visitedPageNum === 0"
          class="page-btn"
          @click="visitedPrevPage"
        >
          이전
        </button>
        <span class="page-count"
          >{{ visitedPageNum + 1 }} / {{ visitedPageLast }} 페이지</span
        >
        <button
          :disabled="visitedPageNum >= visitedPageLast - 1"
          class="page-btn"
          @click="visitedNextPage"
        >
          다음
        </button>
      </div>
    </div>
  </div>
  <ChangePassword
    v-if="changePasswordModalOpen === true"
    @closeChangePassword="closeChangePassword"
  ></ChangePassword>
  <PlaceDetailModal></PlaceDetailModal>
</template>

<script setup>
//import CardDetail from "../components/CardDetail.vue";
import axios from "axios";
import { ref } from "vue";
import ChangePassword from "../components/ChangePassword.vue";
import { usePlaceDetailStore, useTravelStore } from "../store";
import PlaceDetailModal from "../components/PlaceDetailModal.vue";

const placeDetailStore = usePlaceDetailStore();

const travelStore = useTravelStore();

const userWishListOpen = ref(false);
const userProfileOpen = ref(true);
const userVisitedOpen = ref(false);
const changePasswordModalOpen = ref(false);
const visitedList = ref([]);
const wishList = ref([]);
const wishId = ref([]);
const visitedId = ref([]);

const pageSize = ref(4);
const wishPageNum = ref(0);
const visitedPageNum = ref(0);
const wishPageLast = ref(0);
const visitedPageLast = ref(0);

//유저 찜 목록 다음 페이지로 이동
function wishNextPage() {
  wishPageNum.value = wishPageNum.value + 1;
}
//유저 찜 목록 이전 페이지로 이동
function wishPrevPage() {
  wishPageNum.value -= 1;
}
//유저 방문 목록 다음 페이지로 이동
function visitedNextPage() {
  visitedPageNum.value += 1;
}
//유저 방문 목록 이전 페이지로 이동
function visitedPrevPage() {
  visitedPageNum.value -= 1;
}

//유저 찜 목록 페이지네이션
function wishPageNatedData() {
  let start = wishPageNum.value * pageSize.value;
  let end = start + pageSize.value;
  return wishList.value.slice(start, end);
}

//유저 방문 리스트 페이지네이션
function visitedPageNatedData() {
  let start = visitedPageNum.value * pageSize.value;
  let end = start + pageSize.value;
  return visitedList.value.slice(start, end);
}

//유저 찜 목록 페이지네이션의 총 페이지 개수 count
function wishPageCount() {
  let listLeng = wishList.value.length;
  let listSize = pageSize.value;
  let page = Math.floor(listLeng / listSize);
  if (listLeng % listSize > 0) page += 1;
  wishPageLast.value = page;
}
wishPageCount();

//유저 방문 목록 페이지네이션의 총 페이지 개수 count
function visitedPageCount() {
  let listLeng = visitedList.value.length;
  let listSize = pageSize.value;
  let page = Math.floor(listLeng / listSize);
  if (listLeng % listSize > 0) page += 1;
  visitedPageLast.value = page;
}
visitedPageCount();

//유저 profile 정보
const name = ref("");
const email = ref("");
const gender = ref("");
const age = ref("");

//회원정보, 찜목록, 방문목록 출력용
function openUserWishList() {
  userWishListOpen.value = true;
  userProfileOpen.value = false;
  userVisitedOpen.value = false;
}
function openUserProfile() {
  userProfileOpen.value = true;
  userWishListOpen.value = false;
  userVisitedOpen.value = false;
}
function openUserVisited() {
  userVisitedOpen.value = true;
  userWishListOpen.value = false;
  userProfileOpen.value = false;
}

//비밀번호 변경 모달 오픈(버튼 클릭)
function openChangePassword() {
  changePasswordModalOpen.value = true;
}
//비밀번호 변경 모달 종료. ChangePassword 모달에서 emit으로 받아서 실행
function closeChangePassword() {
  changePasswordModalOpen.value = false;
}

//유저 찜 목록을 axios로 받아서 저장.
function getUserWishList() {
  const token = sessionStorage.getItem("YeogidaToken");
  axios({
    url: "http://j7b308.p.ssafy.io:8081/yeogida/wish",
    method: "GET",
    params: { jwtToken: token },
    /*
    headers: {
      Authorization: token,
    },
    */
  })
    .then((res) => {
      wishList.value = res.data;
      travelStore.wishList = res.data;
      wishPageCount();
    })
    .catch((err) => {
      console.log(err);
    });
}
//위의 함수 실행
getUserWishList();

//유저 방문 목록을 백에서 axios로 받아서 저장
function getUserVisitedList() {
  const token = sessionStorage.getItem("YeogidaToken");

  axios({
    url: "http://j7b308.p.ssafy.io:8081/yeogida/visit",
    method: "GET",
    params: { jwtToken: token },
    /*
    headers: {
      Authorization: token,
    },
    */
  })
    .then((res) => {
      visitedList.value = res.data;
      travelStore.visitedList = res.data;
      visitedPageCount();
    })
    .catch((err) => {
      console.log(err);
    });
}
//위의 함수 실행
getUserVisitedList();

//유저 정보를 axios로 받아서 저장
function getUserInfo() {
  const token = sessionStorage.getItem("YeogidaToken");
  setTimeout(function () {}, 500);
  axios({
    url: "http://j7b308.p.ssafy.io:8081/yeogida/members",
    method: "GET",
    params: { jwtToken: token },
    //headers: {
    //  Authorization: token,
    //},
  })
    .then((res) => {
      name.value = res.data.name;
      email.value = res.data.email;
      gender.value = res.data.gender;
      age.value = res.data.age;
    })
    .catch((err) => {
      console.log(err);
    });
}
//위의 함수 실행
getUserInfo();
</script>

<style scoped>
.mypage {
  background-color: #90caf9;
  height: 100%;
  width: 100vw;
  z-index: 2;
  position: relative;
  font-family: twayair;
}
.mypage-img {
  margin-top: 80px;
  height: 100px;
  width: 100vw;
  justify-content: center;
  align-items: center;
  background-image: url("../assets/images/사진.png");
  background-size: cover;
}
.mypage-text {
  height: 120px;
  width: 100vw;
  font-family: twayair;
  font-size: 48px;
  display: flex;
  color: white;
  justify-content: center;
  align-items: center;
  text-align: center;
}
.selectbox {
  display: flex;
  margin-left: 60px;
}
.select {
  width: 31.25vw;
  height: 120px;
  font-family: twayair;
  font-size: 48px;
  color: white;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;
  cursor: pointer;
}
#profile {
  justify-content: center;
  margin-left: 60px;
  margin-right: 60px;
  width: 1800px;
  height: 760px;
  background-color: white;
}
.title {
  font-family: twayair;
  font-size: 48px;
  width: 93.7vw;
  height: 9.2vh;
  padding-top: 10px;
  text-align: center;
}
.topline {
  width: 62.5vw;
  margin-left: 300px;
}
.line {
  width: 46.8vw;
  margin-left: 560px;
}
.profile-text {
  font-family: twayair;
  font-size: 48px;
  margin-left: 350px;
  margin-top: 40px;
  display: flex;
}
.changebtn {
  background-color: #90caf9;
  font-family: twayair;
  font-size: 32px;
  margin-left: 700px;
  width: 250px;
  height: 70px;
  border-radius: 100px;
  color: white;
}
.changebtn:hover {
  background-color: #279dfd;
}
.userwishlist {
  margin-left: 60px;
  margin-right: 60px;
  width: 1800px;
  height: 90vh;
  background-color: white;
}
.cards {
  display: flex;
  flex-wrap: wrap;
  margin-top: 70px;
  margin-left: 100px;
  width: 1600px;
  height: 350px;
}
.cardlist {
  margin-left: 50px;
  margin-right: 50px;
  width: 300px;
  height: 400px;
  cursor: pointer;
}
.backgroundcolor {
  background-color: #279dfd;
  width: 31.25vw;
  height: 120px;
  font-family: twayair;
  font-size: 48px;
  display: flex;
  color: white;
  justify-content: center;
  align-items: center;
  text-align: center;
  cursor: pointer;
}
.profile-value {
  margin-left: 170px;
}
.profile-valueemail {
  margin-left: 130px;
}
.cardimg {
  background-image: url("https://firebasestorage.googleapis.com/v0/b/yeogida-fb013.appspot.com/o/1.jpg?alt=media&token=e3c8a6a5-2786-4f5f-9326-269b0c323590");
  background-size: cover;
}
.btn-cover {
  display: flex;
  width: 1800px;
  justify-content: center;
}
.page-count {
  display: flex;
  align-items: center;
  font-size: 20px;
}
.page-btn {
  background-color: #90caf9;
  border-radius: 50px;
  width: 100px;
  height: 50px;
  color: white;
}
.none {
  display: none;
}
</style>

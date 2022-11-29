import { defineStore } from "pinia";
import axios from "axios";
import yeogida from "../api/yeogida";
import Swal from "sweetalert2";
import router from "../router";

export const useCounterStore = defineStore("counter", {
  state: () => ({ count: 0 }),
  getters: {
    double: (state) => state.count * 2,
  },
  actions: {
    increment() {
      this.count++;
    },
  },
});

export const useTravelStore = defineStore("travel", {
  state: () => ({
    id: "",
    doId: 0,
    siId: 0,
    name: "",
    accompanyType: "",
    score: 0,
    travleType: "",
  }),
  getters: {},
  actions: {
    setTravelData(data) {
      this.id = data.id;
      this.doId = data.doId;
      this.siId = data.siId;
      this.name = data.name;
      this.accompanyType = data.accompanyType;
      this.score = data.score;
      this.travleType = data.travleType;
    },
  },
});

export const useUserStore = defineStore("name", {
  state: () => ({
    name: "",
    id: "",
    age: "",
    gender: "",
    wishList: [],
    myList: [],
  }),
  getters: {},
  actions: {
    getUserName() {
      this.name = "서준희";
    },
  },
});

export const useAccountStore = defineStore("account", {
  state: () => ({
    isLogin: sessionStorage.getItem("YeogidaToken") ?? false,
    dialogModal: false,
    loginModal: false,
    age: "",
    email: "",
    gender: "",
    name: "",
    visitedList: [],
    wishedList: [],
    isListChanged: false,
  }),
  getters: {},
  actions: {
    openLoginDialog() {
      this.dialogModal = true;
    },
    switchToLogin() {
      this.loginModal = true;
    },
    switchToSignup() {
      this.loginModal = false;
    },
    loginAccount(user) {
      axios({
        url: yeogida.members.login(),
        method: "POST",
        data: user,
      })
        .then((res) => {
          sessionStorage.setItem("YeogidaToken", res.data);
          this.dialogModal = false;
          this.isLogin = true;
          Swal.fire({
            icon: "success",
            text: "로그인에 성공하였습니다.",
            confirmButtonText: "확인",
          });
        })
        .catch((err) => {
          if (err.response.status == "404") {
            this.dialogModal = false;
            Swal.fire({
              icon: "warning",
              title: "로그인 실패!",
              text: "아이디와 비밀번호를 확인해 주세요.",
              confirmButtonText: "확인",
            });
          } else {
            console.log(err);
            this.dialogModal = false;
            Swal.fire({
              icon: "warning",
              title: "로그인 실패! 😥",
              text: "아무튼 실패함",
              confirmButtonText: "확인",
            });
          }
        });
    },
    logoutAccount() {
      sessionStorage.removeItem("YeogidaToken");
      this.isLogin = false;
      router.push({ name: "MainPage" });
      Swal.fire({
        icon: "success",
        text: "로그아웃 되었습니다.",
        confirmButtonText: "확인",
      });
    },
    signupAccount(user) {
      axios({
        url: yeogida.members.signup(),
        method: "POST",
        data: user,
      })
        .then((res) => {
          console.log(res);
          this.dialogModal = false;
          Swal.fire({
            icon: "success",
            text: "회원가입에 성공하였습니다.",
            confirmButtonText: "확인",
          });
        })
        .catch((err) => {
          console.log(err);
          this.dialogModal = false;
          Swal.fire({
            icon: "warning",
            title: "회원가입 실패",
            text: "아무튼 실패함",
            confirmButtonText: "확인",
          });
        });
    },
    getUserInfo() {
      axios({
        url: yeogida.members.member(),
        method: "GET",
        params: { jwtToken: sessionStorage.getItem("YeogidaToken") },
      })
        .then((res) => {
          console.log(res);
          this.age = res.data.age;
          this.email = res.data.email;
          this.gender = res.data.gender;
          this.name = res.data.name;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getVisitedList() {
      axios({
        url: yeogida.visit(),
        method: "GET",
        params: { jwtToken: sessionStorage.getItem("YeogidaToken") },
      })
        .then((res) => {
          var visitList = [];
          console.log(res);
          res.data.forEach((e) => {
            visitList.push(e.id);
          });
          this.visitedList = visitList;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getWishList() {
      axios({
        url: yeogida.wish(),
        method: "GET",
        params: { jwtToken: sessionStorage.getItem("YeogidaToken") },
      })
        .then((res) => {
          var wishList = [];
          console.log(res);
          res.data.forEach((e) => {
            wishList.push(e.id);
          });
          this.wishedList = wishList;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    listChanged() {
      axios({
        url: yeogida.visit(),
        method: "GET",
        params: { jwtToken: sessionStorage.getItem("YeogidaToken") },
      })
        .then((res) => {
          var visitList = [];
          console.log(res);
          res.data.forEach((e) => {
            visitList.push(e.id);
          });
          this.visitedList = visitList;
        })
        .catch((err) => {
          console.log(err);
        });
      axios({
        url: yeogida.wish(),
        method: "GET",
        params: { jwtToken: sessionStorage.getItem("YeogidaToken") },
      })
        .then((res) => {
          var wishList = [];
          console.log(res);
          res.data.forEach((e) => {
            wishList.push(e.id);
          });
          this.wishedList = wishList;
        })
        .catch((err) => {
          console.log(err);
        });
      this.isListChanged = new Date();
    },
  },
});
export const useRecommendStore = defineStore("Recommend", {
  state: () => ({
    placeName: "",
    placeaddress: "",
    fastKeyword: ["20대 남성", "오늘의 날씨", "현재 위치"],
    fastRecommList: [],
    FTL: [],
    toMapaddress: "",
    selectedId: 0,
    aroundInfoModal: false,
    aroundList: [],
    isAroundListChanged: false,
    aroundRange: 3,
    aroundNums: 3,
    categories1: [
      {
        name: "주차장",
        code: "PK6",
        isListed: true,
      },
      {
        name: "주유소",
        code: "OL7",
        isListed: true,
      },
      {
        name: "관광명소",
        code: "AT4",
        isListed: true,
      },
      {
        name: "숙박시설",
        code: "AD5",
        isListed: true,
      },
    ],
    categories2: [
      {
        name: "음식점",
        code: "FD6",
        isListed: true,
      },
      {
        name: "카페",
        code: "CE7",
        isListed: true,
      },
      {
        name: "지하철",
        code: "SW8",
        isListed: true,
      },
      {
        name: "마트",
        code: "MT1",
        isListed: true,
      },
    ],
    aroundSearchBtn: false,
  }),
  getters: {},
  actions: {
    recommendComplete(placeaddress, selectedPlaceId) {
      this.selectedId = selectedPlaceId;
      this.placeaddress = placeaddress;
    },
    fastRecomm() {
      if (sessionStorage.getItem("YeogidaToken")) {
        axios({
          url: yeogida.tour.gender(),
          method: "GET",
          params: { jwtToken: sessionStorage.getItem("YeogidaToken") },
        })
          .then((res) => {
            console.log(res);
            this.FTL.push(res.data.slice(0, 3));
          })
          .catch((err) => {
            console.log(err);
          });
        this.fastKeyword[0] = "성별+나이";
      } else {
        axios({
          url: yeogida.tour.keyword(),
          params: { keywords: "혼자" },
          method: "GET",
        })
          .then((res) => {
            console.log(res);
            this.FTL.push(res.data.slice(0, 3));
          })
          .catch((err) => {
            console.log(err);
          });
      }
      axios({
        url: yeogida.tour.weather("비"),
        method: "GET",
      })
        .then((res) => {
          console.log(res);
          this.FTL.push(res.data.slice(0, 3));
        })
        .catch((err) => {
          console.log(err);
        });
      axios({
        url: yeogida.tour.doWeather("비", "6"),
        method: "GET",
      })
        .then((res) => {
          console.log(res);
          this.FTL.push(res.data.slice(0, 3));
          this.fastRecommList = this.FTL[0];
        })
        .catch((err) => {
          console.log(err);
        });
    },
    fastRecommIndex(index) {
      this.fastRecommList = this.FTL[index];
    },
    addAroundList(code) {
      this.aroundList.push(code);
      this.categories1.forEach((e) => {
        if (e.code == code) {
          e.isListed = false;
        }
      });
      this.categories2.forEach((e) => {
        if (e.code == code) {
          e.isListed = false;
        }
      });
    },
    subAroundList(code) {
      for (let i = 0; i < this.aroundList.length; i++) {
        if (this.aroundList[i] == code) {
          this.aroundList.splice(i, 1);
          i--;
        }
      }
      this.categories1.forEach((e) => {
        if (e.code == code) {
          e.isListed = true;
        }
      });
      this.categories2.forEach((e) => {
        if (e.code == code) {
          e.isListed = true;
        }
      });
    },
  },
});
export const usePlaceDetailStore = defineStore("PlaceDetail", {
  state: () => ({
    id: 1,
    name: "신사동 가로수길",
    address: "대전 유성구 동서대로 98-39",
    type: "휴양관광",
    imageUrl: "../assets/images/하이브.jpg",
    dialog: false,
    isRecommPage: false,
    reviewDialog: false,
  }),
  getters: {},
  actions: {
    openDialog(placeId) {
      axios({
        url: yeogida.tour.id(placeId),
        method: "GET",
      })
        .then((res) => {
          this.id = res.data.id;
          this.name = res.data.name;
          this.address = res.data.address;
          this.type = res.data.category;
          this.dialog = true;
          const accountStore = useAccountStore();
          accountStore.listChanged();
        })
        .catch((err) => {
          console.log(err);
        });
      this.dialog = true;
    },
    addWishList(placeId) {
      const accountStore = useAccountStore();
      accountStore.wishedList.push(placeId);
      const data = {
        jwtToken: sessionStorage.getItem("YeogidaToken"),
        tourId: placeId,
      };
      axios({
        url: yeogida.wish(),
        method: "POST",
        data: data,
      })
        .then((res) => {
          console.log(res);
          Swal.fire({
            icon: "success",
            text: "위시리스트에 등록하였습니다.",
            confirmButtonText: "확인",
          });
          this.dialog = false;
        })
        .catch((err) => {
          console.log(err);
          Swal.fire({
            icon: "error",
            text: "등록에 실패하였습니다.",
            confirmButtonText: "확인",
          });
          this.dialog = false;
        });
    },
    subWishList(placeId) {
      const accountStore = useAccountStore();
      for (let i = 0; i < accountStore.wishedList.length; i++) {
        if (accountStore.wishedList[i] == placeId) {
          accountStore.wishedList.splice(i, 1);
          console.log("sub");
          i--;
        }
      }
      const data = {
        jwtToken: sessionStorage.getItem("YeogidaToken"),
        id: placeId,
      };
      axios({
        url: yeogida.wish(),
        method: "DELETE",
        params: data,
      })
        .then((res) => {
          console.log(res);
          Swal.fire({
            icon: "success",
            text: "위시리스트에서 뺐습니다.",
            confirmButtonText: "확인",
          });
          this.dialog = false;
        })
        .catch((err) => {
          console.log(err);
          Swal.fire({
            icon: "error",
            text: "등록에 실패하였습니다.",
            confirmButtonText: "확인",
          });
          this.dialog = false;
        });
    },
    addVisitList(placeId, userScore) {
      const accountStore = useAccountStore();
      accountStore.visitedList.push(placeId);
      const data = {
        jwtToken: sessionStorage.getItem("YeogidaToken"),
        tourId: placeId,
        userScore: userScore,
      };
      axios({
        url: yeogida.visit(),
        method: "POST",
        data: data,
      })
        .then((res) => {
          console.log(res);
          Swal.fire({
            icon: "success",
            text: "방문리스트에 등록하였습니다.",
            confirmButtonText: "확인",
          });
          this.reviewDialog = false;
          this.dialog = false;
          const accountStore = useAccountStore();
          accountStore.listChanged();
        })
        .catch((err) => {
          console.log(err);
          Swal.fire({
            icon: "error",
            text: "등록에 실패하였습니다.",
            confirmButtonText: "확인",
          });
          this.reviewDialog = false;
          this.dialog = false;
        });
    },
    subVisitList(placeId) {
      const accountStore = useAccountStore();
      for (let i = 0; i < accountStore.wishedList.length; i++) {
        if (accountStore.visitedList[i] == placeId) {
          accountStore.visitedList.splice(i, 1);
          console.log("sub");
          i--;
        }
      }
      const params = {
        jwtToken: sessionStorage.getItem("YeogidaToken"),
        id: placeId,
      };
      axios({
        url: yeogida.visit(),
        method: "DELETE",
        params: params,
      })
        .then((res) => {
          console.log(res);
          Swal.fire({
            icon: "success",
            text: "방문 리스트에서 뺐습니다.",
            confirmButtonText: "확인",
          });
          this.dialog = false;
        })
        .catch((err) => {
          console.log(err);
          Swal.fire({
            icon: "error",
            text: "등록에 실패하였습니다.",
            confirmButtonText: "확인",
          });
          this.dialog = false;
        });
    },
    toMap() {
      const dataStore = useDataStore();
      const recommendStore = useRecommendStore();
      dataStore.data.push({
        id: this.id,
        name: this.name,
        address: this.address,
        category: this.type,
        imageUrl: "../assets/images/대충 사진.jpg",
        keywords: [],
      });
      this.dialog = false;

      recommendStore.selectedId = this.id;
      recommendStore.placeaddress = this.address;
      recommendStore.toMapaddress = this.address;
      router.push({ name: "Recommend" });
    },
    recommMounted() {
      this.isRecommPage = true;
    },
    recommUnmounted() {
      this.isRecommPage = false;
    },
  },
});

export const useModalStore = defineStore("modal", {
  state: () => ({
    region: true,
    type: false,
    regionModal: false,
    typeModal: false,
  }),
  getters: {},
  actions: {
    openRegionModal() {
      this.regionModal = true;
    },
    openTypeModal() {
      this.typeModal = true;
    },
  },
});

export const useDataStore = defineStore("data", {
  state: () => ({
    data: [],
    type: [],
    types: "",
    travelType1Selected: false,
    travelType2Selected: false,
    travelType3Selected: false,
    travelType4Selected: false,
    travelType5Selected: false,
    travelType6Selected: false,
    travelType7Selected: false,
  }),
  getters: {},
  actions: {},
});

export const usePlanStore = defineStore("plan", {
  state: () => ({
    planName: "",
    selectedId: 0,
    placeAddress: "",
    items: [],
    isListChanged: false,
    mapCenter: "",
  }),
  getters: {},
  actions: {
    setPlanner(address, id) {
      this.placeAddress = address;
      this.selectedId = id;
      axios({
        url: yeogida.tour.id(id),
        method: "GET",
      })
        .then((res) => {
          this.planName = res.data.name;
        })
        .catch((err) => {
          console.log(err);
        });
      router.push({ name: "Plan" });
    },
    additem(item) {
      this.items.push(item);
      this.isListChanged = new Date();
    },
    plusIndex(index) {
      this.items.splice(index + 2, 0, this.items[index]);
      this.items.splice(index, 1);
      this.isListChanged = new Date();
    },
    minusIndex(index) {
      if (index != 0) {
        this.items.splice(index - 1, 0, this.items[index]);
        this.items.splice(index + 1, 1);
        this.isListChanged = new Date();
      }
    },
    deleteIndex(index) {
      this.items.splice(index, 1);
      this.isListChanged = new Date();
    },
    mapFocus(position) {
      this.mapCenter = position;
    },
  },
});

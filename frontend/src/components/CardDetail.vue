<template>
  <div class="carddetail">
    <div class="carddetail-img">
      <button class="carddetail-closebtn" @click="$emit('closeCardDetail')">
        X
      </button>
    </div>
    <div class="carddetail-nametext">{{ travelStore.name }}</div>
    <hr class="carddetail-line" />
    <div class="carddetail-addresstext">
      관광지 주소 : {{ travelStore.doId }}번 도 , {{ travelStore.siId }}번 시
    </div>
    <div class="carddetail-btn">
      <button class="carddetail-mapbtn" @click="mapPopup">지도로 보기</button>
    </div>
    <div class="carddetail-tagtext">
      #{{ travelStore.accompanyType }} #{{ travelStore.travleType }}
    </div>
    <div v-if="isWishList === true">
      <hr class="carddetail-line" />
      <div class="carddetail-visittext">방문하셨나요?</div>
      <button class="carddetail-ratingbtn" @click="Submit">확인</button>
      <!--
      <div class="star-rating space-x-4 mx-auto">
        <input
          id="5-stars"
          v-model="ratings"
          type="radio"
          name="rating"
          value="5"
        />
        <label for="5-stars" class="star pr-4" @click="rate(5)">★</label>
        <input
          id="4-stars"
          v-model="ratings"
          type="radio"
          name="rating"
          value="4"
        />
        <label for="4-stars" class="star" @click="rate(4)">★</label>
        <input
          id="3-stars"
          v-model="ratings"
          type="radio"
          name="rating"
          value="3"
        />
        <label for="3-stars" class="star" @click="rate(3)">★</label>
        <input
          id="2-stars"
          v-model="ratings"
          type="radio"
          name="rating"
          value="2"
        />
        <label for="2-stars" class="star" @click="rate(2)">★</label>
        <input
          id="1-star"
          v-model="ratings"
          type="radio"
          name="rating"
          value="1"
        />
        <label for="1-star" class="star" @click="rate(1)">★</label>
        <span class="scoretext">{{ starRate }}점</span>
      </div>
      -->
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import axios from "axios";
import { useTravelStore } from "../store";
const travelStore = useTravelStore();
const emit = defineEmits([""]);
const props = defineProps({
  wish: Object,
  visited: Object,
});

console.log(props.wish);

//const ratings = ref();
//const starRate = ref();

const tourId = travelStore.id;
const isWishList = ref(true);

/*
function rate(a) {
  starRate.value = a;
  console.log(starRate.value);
}
*/

function Submit() {
  const token = sessionStorage.getItem("YeogidaToken");
  const tourIdData = {
    jwtToken: token,
    tourId: tourId,
  };
  console.log(tourIdData);

  axios({
    url: "http://j7b308.p.ssafy.io:8081/yeogida/visit",
    method: "POST",
    data: tourIdData,
    //headers: {
    //  Authorization: token,
    //},
  })
    .then((res) => {
      console.log(res);
      emit("closeCardDetail");
    })
    .catch((err) => {
      console.log(err);
    });
}
</script>

<style scoped>
.carddetail {
  width: 800px;
  height: 760px;
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: white;
  border: solid 1px;
  border-color: #d9d9d9;
}
.carddetail-img {
  width: 100%;
  height: 50%;
  align-items: center;
  background-image: url("../assets/images/경주 블루원 워터파크.jpg");
  background-size: cover;
}
.carddetail-nametext {
  width: 100%;
  height: 30px;
  font-size: 36px;
  margin-top: 10px;
  align-items: center;
  text-align: center;
}
.carddetail-line {
  margin-top: 30px;
  width: 800px;
}
.carddetail-addresstext {
  width: 100%;
  font-size: 24px;
  margin-top: 15px;
  align-items: center;
  text-align: center;
}
.carddetail-tagtext {
  font-size: 24px;
  margin-top: 25px;
  align-items: center;
  text-align: center;
}
.carddetail-btn {
  margin-top: 10px;
  justify-content: center;
  align-items: center;
  text-align: center;
}
.carddetail-mapbtn {
  font-size: 24px;
  background-color: #d9d9d9;
}
.carddetail-visittext {
  align-items: center;
  text-align: center;
  font-size: 24px;
}

.carddetail-ratingbtn {
  background-color: #ffd700;
  width: 200px;
  height: 50px;
  border-radius: 50px;
  font-family: "twayair";
  float: right;
  font-size: 24px;
  margin-left: 100px;
}

.carddetail-closebtn {
  border-radius: 100px;
  font-size: 20px;
  color: white;
  width: 30px;
  height: 30px;
  float: right;
  padding-right: 20px;
}
.carddetail-closebtn:hover {
  background-color: red;
}

/*
.star-rating {
  display: flex;
  flex-direction: row-reverse;
  font-size: 2.25rem;
  line-height: 2.5rem;
  justify-content: space-around;
  padding: 0 0.2em;
  text-align: center;
  width: 5em;
}
.star-rating input {
  display: none;
}
.star-rating label {
  -webkit-text-fill-color: transparent; /* Will override color (regardless of order) 
  -webkit-text-stroke-width: 2.3px;
  -webkit-text-stroke-color: #2b2a29;
  cursor: pointer;
}
.star-rating :checked ~ label {
  -webkit-text-fill-color: gold;
}
.star-rating label:hover,
.star-rating label:hover ~ label {
  -webkit-text-fill-color: #fff58c;
}
.scoretext {
}
*/
</style>

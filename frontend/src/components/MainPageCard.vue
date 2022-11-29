<template>
  <div class="mainpage-recomm" @click="placeDetailStore.openDialog(recomm.id)">
    <img :src="imageUrl" class="mainpage-recomm-img" />
    <a style="position: absolute; top: 255px; left: 10px">{{ recomm.name }}</a>
  </div>
</template>

<script setup>
import { usePlaceDetailStore } from "../store";
const placeDetailStore = usePlaceDetailStore();
const props = defineProps({
  recomm: Object,
});
// const imageUrl = new URL(props.recomm.imageUrl, import.meta.url).href;
const imageUrl = `https://firebasestorage.googleapis.com/v0/b/yeogida-fb013.appspot.com/o/${props.recomm.id}.jpg?alt=media&token=e3c8a6a5-2786-4f5f-9326-269b0c323590`;
</script>

<style scoped>
.mainpage-recomm {
  width: 330px;
  height: 330px;
  border-radius: 50px;
  font-family: "twayair";
  font-size: 32px;
  color: white;
  position: relative;
}
.mainpage-recomm-img {
  width: 330px;
  height: 330px;
  border-radius: 50px;
}
.mainpage-recomm:hover {
  cursor: pointer;
  transform: scale(0.97);
  opacity: 0.7;
}
</style>

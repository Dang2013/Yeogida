<template>
  <div style="display: flex; height: 1000px">
    <Sidebar />
  </div>
  <RecommendPageMap />
  <AroundInfoModal />
  <PlaceDetailModal></PlaceDetailModal>
</template>

<script setup>
import RecommendPageMap from "../components/RecommendPageMap.vue";
import Sidebar from "../components/RecommendPageSidebar.vue";
import PlaceDetailModal from "../components/PlaceDetailModal.vue";
import AroundInfoModal from "../components/AroundInfoModal.vue";
import { onMounted, onUnmounted } from "vue";
import { usePlaceDetailStore } from "../store";
const placeDetailStore = usePlaceDetailStore();
onMounted(() => {
  placeDetailStore.recommMounted();
});
onUnmounted(() => {
  placeDetailStore.recommUnmounted();
});
</script>
<style scoped></style>

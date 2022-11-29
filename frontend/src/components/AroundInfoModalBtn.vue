<template>
  <v-btn
    v-if="props.category.isListed"
    style="
      color: white;
      font-size: 16px;
      font-family: 'MICEGothic Bold';
      margin-right: 8px;
    "
    color="#999999"
    width="75px"
    height="36px"
    @click="recommendStore.addAroundList(props.category.code)"
  >
    <span>{{ props.category.name }}</span>
  </v-btn>
  <v-btn
    v-else
    style="
      color: white;
      font-size: 16px;
      font-family: 'MICEGothic Bold';
      margin-right: 8px;
    "
    color="#6FC0F7"
    width="75px"
    height="36px"
    @click="recommendStore.subAroundList(props.category.code)"
  >
    <span>{{ props.category.name }}</span>
  </v-btn>
</template>

<script setup>
import { useRecommendStore } from "../store";
const recommendStore = useRecommendStore();
const props = defineProps({
  category: Object,
});
</script>

<style scoped></style>

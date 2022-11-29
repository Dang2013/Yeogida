<template>
  <div id="Fmap" class="plan-map"></div>
</template>

<script setup>
import { storeToRefs } from "pinia";
import Swal from "sweetalert2";
import { onMounted, ref, watch } from "vue";
import { usePlaceDetailStore, usePlanStore, useRecommendStore } from "../store";
const recommendStore = useRecommendStore();
const planStore = usePlanStore();
const placeDetailStore = usePlaceDetailStore();
const { aroundSearchBtn } = storeToRefs(recommendStore);
const { isListChanged } = storeToRefs(planStore);
const { mapCenter } = storeToRefs(planStore);
const map = ref("");
const ps = ref("");
const mainMarker = ref();
const polyline = ref();
const linePath = ref([]);
var aroundMarkers = [];
const coords = ref();
function initMap() {
  var mapContainer = document.getElementById("Fmap"), // 지도를 표시할 div
    mapOption = {
      center: new kakao.maps.LatLng(36.355381964230546, 127.29841504027434), // 지도의 중심좌표
      level: 3, // 지도의 확대 레벨
    };
  // 지도를 생성합니다
  map.value = new kakao.maps.Map(mapContainer, mapOption);
  // 장소 검색 객체를 생성합니다
  ps.value = new kakao.maps.services.Places(map.value);
}
watch(aroundSearchBtn, () => {
  map.value.setCenter(coords.value);
  map.value.setLevel(recommendStore.aroundRange);
  searchAroundInfo();
});
function searchAroundInfo() {
  aroundMarkers.forEach((e) => {
    e.setMap(null);
  });
  aroundMarkers = [];
  recommendStore.aroundList.forEach((e) => {
    ps.value.categorySearch(e, placesSearchCB, { useMapBounds: true });
  });
}
function makeOverListener(map, marker, overlay) {
  return function () {
    overlay.setMap(map, marker);
  };
}
function makeOutListener(overlay) {
  return function () {
    overlay.setMap(null);
  };
}
function makeClickListener(item) {
  return function () {
    planStore.additem(item);
  };
}
function placesSearchCB(data, status, pagination) {
  console.log(pagination);
  if (status === kakao.maps.services.Status.OK) {
    var codeName = data[0].category_group_code;
    var newData = data.slice(0, recommendStore.aroundNums);
    for (var i = 0; i < newData.length; i++) {
      const marker = ref();
      var imageSrc = "https://firebasestorage.googleapis.com/v0/b/yeogida-fb013.appspot.com/o/" + codeName + ".png?alt=media&token=e3c8a6a5-2786-4f5f-9326-269b0c323590", // 마커이미지의 주소입니다
        imageSize = new kakao.maps.Size(30, 30), // 마커이미지의 크기입니다
        imageOption = { offset: new kakao.maps.Point(15, 30) }; // 마커이미지의 옵션입니다. 마커의 좌표와 일치시킬 이미지 안에서의 좌표를 설정합니다.
      var markerImage = new kakao.maps.MarkerImage(
        imageSrc,
        imageSize,
        imageOption
      );
      marker.value = new kakao.maps.Marker({
        map: map.value,
        position: new kakao.maps.LatLng(newData[i].y, newData[i].x),
        image: markerImage,
      });
      var overlayContent =
        '<div class="overlay">' +
        '<div class="overlay-top">' +
        '<span style="margin-left: 5px">' +
        newData[i].place_name +
        "</span>" +
        '</div><div class="overlay-content">' +
        newData[i].address_name +
        '</div><div class="overlay-content">분류 : ' +
        newData[i].category_group_name +
        "</div></div>";
      var overlayPosition = new kakao.maps.LatLng(newData[i].y, newData[i].x);
      var overlayPlaceName = newData[i].place_name;
      var overlayAddress = newData[i].address_name;
      var planItem = {
        title: overlayPlaceName,
        address: overlayAddress,
        position: overlayPosition,
        delete: true,
      };
      const overlay = ref();
      overlay.value = new kakao.maps.CustomOverlay({
        map: map.value,
        position: overlayPosition,
        content: overlayContent,
        yAnchor: 0,
        xAnchor: 1,
      });
      overlay.value.setMap(null);
      // 마커에 클릭이벤트를 등록합니다
      kakao.maps.event.addListener(
        marker.value,
        "mouseover",
        makeOverListener(map.value, marker.value, overlay.value)
      );
      kakao.maps.event.addListener(
        marker.value,
        "mouseout",
        makeOutListener(overlay.value)
      );
      kakao.maps.event.addListener(
        marker.value,
        "click",
        makeClickListener(planItem)
      );
      aroundMarkers.push(marker.value);
    }
  } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
    // 검색결과가 없는경우 해야할 처리가 있다면 이곳에 작성해 주세요
    Swal.fire({
      icon: "error",
      text: "주변에 머시깽이가 없습니다.",
      confirmButtonText: "확인",
    });
  } else if (status === kakao.maps.services.Status.ERROR) {
    // 에러로 인해 검색결과가 나오지 않은 경우 해야할 처리가 있다면 이곳에 작성해 주세요
    Swal.fire({
      icon: "error",
      text: "검색에 실패하였습니다.",
      confirmButtonText: "확인",
    });
  }
}
onMounted(() => {
  if (!window.kakao || !window.kakao.maps) {
    const script = document.createElement("script");
    const SERVICE_KEY = import.meta.env.VITE_KAKAO_MAP_API_KEY;
    script.type = "text/javascript";
    script.src =
      "//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=" +
      decodeURIComponent(SERVICE_KEY) +
      "&libraries=services";
    /* global kakao */
    script.addEventListener("load", () => {
      kakao.maps.load(() => {
        initMap();
      });
    });
    document.head.appendChild(script);
  } else {
    initMap();
  }

  const searchAddress = (place) => {
    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(place, function (result, status) {
      // 정상적으로 검색이 완료됐으면
      if (status === kakao.maps.services.Status.OK) {
        coords.value = new kakao.maps.LatLng(result[0].y, result[0].x);
        if (mainMarker.value) {
          mainMarker.value.setPosition(
            new kakao.maps.LatLng(result[0].y, result[0].x)
          );
        } else {
          // 마커를 생성합니다.
          mainMarker.value = new kakao.maps.Marker({
            map: map.value,
            position: coords.value,
            clickable: true,
          });
          // 마커를 지도에 표시합니다.
          kakao.maps.event.addListener(mainMarker.value, "click", function () {
            placeDetailStore.openDialog(planStore.selectedId);
          });
        }
        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.value.setCenter(coords.value);
        planStore.additem({
          title: planStore.planName,
          address: planStore.placeAddress,
          position: coords.value,
          delete: false,
        });
      } else {
        console.log("에러");
      }
    });
  };
  setTimeout(() => {
    if (planStore.placeAddress) {
      searchAddress(planStore.placeAddress);
    }
  }, 500); //최악의 방법이자 최고의 방법
  watch(isListChanged, () => {
    if (polyline.value) {
      polyline.value.setMap(null);
    }
    linePath.value = [];
    planStore.items.forEach((e) => {
      linePath.value.push(e.position);
    });
    polyline.value = new kakao.maps.Polyline({
      path: linePath.value, // 선을 구성하는 좌표배열 입니다
      strokeWeight: 5, // 선의 두께 입니다
      strokeColor: "#6FC0F7", // 선의 색깔입니다
      strokeOpacity: 0.9, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
      strokeStyle: "solid", // 선의 스타일입니다
    });
    polyline.value.setMap(map.value);
  });
  watch(mapCenter, (newValue) => {
    map.value.setCenter(newValue);
  });
});
</script>
<style>
.plan-map {
  width: 1420px;
  height: 1000px;
  z-index: 1;
}
</style>

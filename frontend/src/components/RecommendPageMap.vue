<template>
  <div id="map" class="map-main"></div>
</template>

<script setup>
import { storeToRefs } from "pinia";
import Swal from "sweetalert2";
import { onMounted, ref, watch } from "vue";
import { useRecommendStore, usePlaceDetailStore } from "../store";
const recommendStore = useRecommendStore();
const placeDetailStore = usePlaceDetailStore();
const map = ref("");
const ps = ref("");
var aroundMarkers = [];
const mainMarker = ref();
const mainMarkerPosition = ref();
const { placeaddress } = storeToRefs(recommendStore);
const { aroundSearchBtn } = storeToRefs(recommendStore);
function initMap() {
  var mapContainer = document.getElementById("map"), // 지도를 표시할 div
    mapOption = {
      center: new kakao.maps.LatLng(36.355381964230546, 127.29841504027434), // 지도의 중심좌표
      level: 3, // 지도의 확대 레벨
    };
  // 지도를 생성합니다
  map.value = new kakao.maps.Map(mapContainer, mapOption);
  // 장소 검색 객체를 생성합니다
  ps.value = new kakao.maps.services.Places(map.value);
}
function addZoom() {
  // 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
  var zoomControl = new kakao.maps.ZoomControl();
  map.value.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
}
watch(aroundSearchBtn, () => {
  map.value.setCenter(mainMarkerPosition.value);
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

// 인포윈도우를 닫는 클로저를 만드는 함수입니다
function makeOutListener(overlay) {
  return function () {
    overlay.setMap(null);
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
        addZoom();
      });
    });
    document.head.appendChild(script);
  } else {
    initMap();
    addZoom();
  }

  const searchAddress = (place) => {
    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(place, function (result, status) {
      // 정상적으로 검색이 완료됐으면
      if (status === kakao.maps.services.Status.OK) {
        mainMarkerPosition.value = new kakao.maps.LatLng(
          result[0].y,
          result[0].x
        );
        if (mainMarker.value) {
          mainMarker.value.setPosition(
            new kakao.maps.LatLng(result[0].y, result[0].x)
          );
        } else {
          // 마커를 생성합니다.
          mainMarker.value = new kakao.maps.Marker({
            map: map.value,
            position: mainMarkerPosition.value,
            clickable: true,
          });
          // 마커를 지도에 표시합니다.
          kakao.maps.event.addListener(mainMarker.value, "click", function () {
            placeDetailStore.openDialog(recommendStore.selectedId);
          });
        }
        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.value.setCenter(mainMarkerPosition.value);
      } else {
        Swal.fire({
          icon: "warning",
          title: "검색 실패!",
          text: "여행지 주소를 확인해주세요!",
          confirmButtonText: "확인",
        });
      }
    });
  };
  watch(placeaddress, (newValue) => {
    searchAddress(newValue);
  });
  setTimeout(() => {
    if (recommendStore.toMapaddress) {
      searchAddress(recommendStore.toMapaddress);
      recommendStore.toMapaddress = "";
    }
  }, 500); //최악의 방법이자 최고의 방법
});
</script>
<style>
.map-main {
  position: absolute;
  width: 1920px;
  height: 1000px;
  top: 80px;
  z-index: 1;
}
.overlay {
  width: 250px;
  height: 80px;
  border-radius: 10px;
  background-color: white;
}
.overlay-top {
  height: 25px;
  background-color: #5a87e0;
  font-family: "MICEGothic";
  font-size: 16px;
  color: white;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
}
.overlay-content {
  margin-left: 5px;
  margin-top: 4px;
  height: 20px;
  font-family: "MICEGothic";
  font-size: 12px;
}
</style>

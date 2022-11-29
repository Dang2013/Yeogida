import { API_HOST_URL } from "../config";
const HOST = API_HOST_URL;
const MEMBERS = "members/";
const REGIONS = "regions/";
const TOUR = "tour/";
// const VISIT = "visit/";
// const WISH = "wish/";

export default {
  members: {
    signup: () => HOST + MEMBERS + "join", // 회원가입
    login: () => HOST + MEMBERS + "login", // 로그인
    member: () => HOST + MEMBERS, // GET 회원정보조회, PUT 비밀번호변경 DELETE 회원탈퇴
  },
  regions: {
    do: () => HOST + REGIONS + "do", // 광역시,도 데이터 조회
    si: (doId) => HOST + REGIONS + "si/" + `${doId}`, // 시,군,구 데이터 조회
  },
  tour: {
    id: (tourId) => HOST + TOUR + `${tourId}`, // 관광지 단건 조회
    keyword: () => HOST + TOUR + "keywords/", // 동반 유형별 추천 조회
    gender: () => HOST + TOUR + "gender/", // 성별, 연령별 관광지 추천 조회
    do: () => HOST + TOUR + "region/do", //
    si: () => HOST + TOUR + "region/si", //
    type: (type) => HOST + TOUR + "travle/" + `${type}`, // 여행 유형별 관광지 조회
    weather: (weather) => HOST + TOUR + "weather/" + `${weather}`, // 날씨별 관광지 추천 저회
    doWeather: (weather, doId) =>
      HOST + TOUR + "weather/" + `${weather}/` + `${doId}`, // 날씨별 도/광역시 별 추천 조회
  },
  visit: () => HOST + "visit", // 관광지 방문리스트 crud
  wish: () => HOST + "wish", // 찜리스트 crud
};

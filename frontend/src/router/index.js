import { createRouter, createWebHistory } from "vue-router";
import MainPage from "../views/MainPage.vue";
import MyPage from "../views/MyPage.vue";
import RecommendPage from "../views/RecommendPage.vue";
import Plan from "../views/PlanPage.vue";
import Swal from "sweetalert2";
import { useAccountStore } from "../store";

const routes = [
  {
    path: "/",
    name: "MainPage",
    component: MainPage,
  },
  {
    path: "/MyPage",
    name: "MyPage",
    component: MyPage,
  },
  {
    path: "/Recommend",
    name: "Recommend",
    component: RecommendPage,
  },
  {
    path: "/Plan",
    name: "Plan",
    component: Plan,
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});
router.beforeEach((to, from, next) => {
  const accountStore = useAccountStore();
  const token = sessionStorage.getItem("YeogidaToken");

  //authPages에 로그인이 필요한 RouterView를 등록하면 됨
  const authPages = ["Recommend", "MyPage", "Plan"];
  const isAuthRequired = authPages.includes(to.name);

  if (isAuthRequired && !token) {
    if (from.name == undefined) {
      router.push({ name: "MainPage" });
      Swal.fire({
        icon: "warning",
        title: "Error!",
        text: "로그인이 필요한 서비스입니다.",
        confirmButtonText: "확인",
      });
    } else {
      accountStore.loginModal = true;
      accountStore.openLoginDialog();
    }
  } else {
    next();
  }
});

export default router;

from django.urls import path
from . import views

urlpatterns = [
    # 동반, 여행 유형 키워드 기반 추천
    path('recombykeywords/<str:keywords>', views.find_sim_tourlists),

    # 행정구역 내에서 이전 관광지 이력으로 추천 - do_id만 입력되는 경우
    path('recombylogdo/<int:do_id>/<int:tour_id>',
         views.find_sim_tourlists_in_do),
    # 행정구역 내에서 이전 관광지 이력으로 추천 - si_id까지 입력되는 경우
    path('recombylogsi/<int:si_id>/<int:tour_id>',
         views.find_sim_tourlists_in_si),
    # 행정구역 내에서 유형 키워드 기반으로 추천 - do_id만 입력되는 경우
    path('recombykeyworddo/<int:do_id>/<str:keywords>',
         views.find_sim_tourlists_in_do_keywords),
    # 행정구역 내에서 유형 키워드 기반으로 추천 - si_id까지 입력되는 경우
    path('recombykeywordsi/<int:si_id>/<str:keywords>',
         views.find_sim_tourlists_in_si_keywords),
]

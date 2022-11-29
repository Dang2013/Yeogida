from django.shortcuts import render
from rest_framework.decorators import api_view
from rest_framework.response import Response
from rest_framework import status
import pandas as pd
from sqlalchemy import create_engine
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity

# 동반, 여행 유형 기반 추천


@api_view(['GET'])
def find_sim_tourlists(request, keywords):
    engine = create_engine(
        'mysql+pymysql://b308:durlek308@j7b308.p.ssafy.io:3306/b308', convert_unicode=True)
    conn = engine.connect()

    tourlists = pd.read_sql_table('tour', conn)

    # 입력받은 동반유형 키워드를 데이터프레임에 추가
    new_data = {
        'name': ['선택 동반유형 키워드'],
        'keywords': [keywords],
    }

    new_df = pd.DataFrame(new_data)
    tourlists_df = pd.concat([tourlists, new_df], ignore_index=True)

    # 데이터프레임 코사인 유사도 계산
    count_vect = CountVectorizer(min_df=0, ngram_range=(1, 2))
    keywords_mat = count_vect.fit_transform(tourlists_df['keywords'])

    keywords_sim = cosine_similarity(keywords_mat, keywords_mat)

    # 유사도 높은 순으로 정렬
    sorted_ind = keywords_sim.argsort()[:, ::-1]
    base_index = tourlists_df.shape[0] - 1
    similar_indexes = sorted_ind[base_index, 1:11]

    similar_indexes = similar_indexes.reshape(-1)

    return Response(similar_indexes, status=status.HTTP_200_OK)

# 행정구역 내에서 이전 관광지 이력으로 추천 - do_id만 입력되는 경우


@api_view(['GET'])
def find_sim_tourlists_in_do(request, do_id, tour_id):
    engine = create_engine(
        'mysql+pymysql://b308:durlek308@j7b308.p.ssafy.io:3306/b308', convert_unicode=True)
    conn = engine.connect()

    tourlists = pd.read_sql_table('tour', conn)

    # 입력받은 행정구역 내의 데이터만 필터링
    is_in_region = tourlists['do_id'] == do_id
    df = tourlists[is_in_region]

    base_tourlist = tourlists[tourlists['id'] == tour_id]

    # 데이터프레임 코사인 유사도 계산
    count_vect = CountVectorizer(min_df=0, ngram_range=(1, 2))
    keywords_mat = count_vect.fit_transform(tourlists['keywords'])
    keywords_sim = cosine_similarity(keywords_mat, keywords_mat)

    # 유사도 높은 순으로 정렬
    sorted_ind = keywords_sim.argsort()[:, ::-1]
    # 행정구역 내 관광지 + 입력받은 관광지 데이터 프레임 중 입력받은 관광지 기반으로 계산
    base_index = base_tourlist.index.values
    similar_indexes = sorted_ind[base_index]
    result = pd.DataFrame(similar_indexes)
    new = result.transpose()
    new.columns = ['인덱스']
    new_result = new[new['인덱스'].isin(df.index.to_list())].head(10)

    return Response(new_result.values, status=status.HTTP_200_OK)

# 행정구역 내에서 이전 관광지 이력으로 추천 - si_id까지 입력되는 경우


@api_view(['GET'])
def find_sim_tourlists_in_si(request, si_id, tour_id):
    engine = create_engine(
        'mysql+pymysql://b308:durlek308@j7b308.p.ssafy.io:3306/b308', convert_unicode=True)
    conn = engine.connect()

    tourlists = pd.read_sql_table('tour', conn)

    # 입력받은 행정구역 내의 데이터만 필터링
    is_in_region = tourlists['si_id'] == si_id
    df = tourlists[is_in_region]

    base_tourlist = tourlists[tourlists['id'] == tour_id]

    # 데이터프레임 코사인 유사도 계산
    count_vect = CountVectorizer(min_df=0, ngram_range=(1, 2))
    keywords_mat = count_vect.fit_transform(tourlists['keywords'])
    keywords_sim = cosine_similarity(keywords_mat, keywords_mat)

    # 유사도 높은 순으로 정렬
    sorted_ind = keywords_sim.argsort()[:, ::-1]
    # 행정구역 내 관광지 + 입력받은 관광지 데이터 프레임 중 입력받은 관광지 기반으로 계산
    base_index = base_tourlist.index.values
    similar_indexes = sorted_ind[base_index]
    result = pd.DataFrame(similar_indexes)
    new = result.transpose()
    new.columns = ['인덱스']
    new_result = new[new['인덱스'].isin(df.index.to_list())].head(10)

    return Response(new_result.values, status=status.HTTP_200_OK)

# 행정구역 내에서 유형 키워드 기반으로 추천 - do_id만 입력되는 경우


@api_view(['GET'])
def find_sim_tourlists_in_do_keywords(request, do_id, keywords):
    engine = create_engine(
        'mysql+pymysql://b308:durlek308@j7b308.p.ssafy.io:3306/b308', convert_unicode=True)
    conn = engine.connect()

    tourlists = pd.read_sql_table('tour', conn)

    # 입력받은 행정구역 내의 데이터만 필터링
    is_in_region = tourlists['do_id'] == do_id
    df = tourlists[is_in_region]

    # 입력받은 유형 키워드 데이터프레임에 추가
    new_data = {
        'name': ['선택 동반유형 키워드'],
        'keywords': [keywords],
    }
    keywords_df = pd.DataFrame(new_data)
    new_df = pd.concat([tourlists, keywords_df], ignore_index=True)

    # 만든 데이터프레임으로 코사인 유사도 계산
    count_vect = CountVectorizer(min_df=0, ngram_range=(1, 2))
    keywords_mat = count_vect.fit_transform(new_df['keywords'])
    keywords_sim = cosine_similarity(keywords_mat, keywords_mat)

    # 유사도 높은 순으로 정렬
    sorted_ind = keywords_sim.argsort()[:, ::-1]
    # 행정구역 내 관광지 + 입력받은 관광지 데이터 프레임 중 입력받은 관광지 기반으로 계산
    base_index = new_df.shape[0] - 1
    similar_indexes = sorted_ind[base_index]
    result = pd.DataFrame(similar_indexes)
    result.columns = ['인덱스']
    new_result = result[result['인덱스'].isin(df.index.to_list())].head(10)

    return Response(new_result.values, status=status.HTTP_200_OK)

# 행정구역 내에서 유형 키워드 기반으로 추천 - si_id까지 입력되는 경우


@api_view(['GET'])
def find_sim_tourlists_in_si_keywords(request, si_id, keywords):
    engine = create_engine(
        'mysql+pymysql://b308:durlek308@j7b308.p.ssafy.io:3306/b308', convert_unicode=True)
    conn = engine.connect()

    tourlists = pd.read_sql_table('tour', conn)

    # 입력받은 행정구역 내의 데이터만 필터링
    is_in_region = tourlists['si_id'] == si_id
    df = tourlists[is_in_region]

    # 입력받은 유형 키워드 데이터프레임에 추가
    new_data = {
        'name': ['선택 동반유형 키워드'],
        'keywords': [keywords],
    }
    keywords_df = pd.DataFrame(new_data)
    new_df = pd.concat([tourlists, keywords_df], ignore_index=True)

    # 만든 데이터프레임으로 코사인 유사도 계산
    count_vect = CountVectorizer(min_df=0, ngram_range=(1, 2))
    keywords_mat = count_vect.fit_transform(new_df['keywords'])
    keywords_sim = cosine_similarity(keywords_mat, keywords_mat)

    # 유사도 높은 순으로 정렬
    sorted_ind = keywords_sim.argsort()[:, ::-1]
    # 행정구역 내 관광지 + 입력받은 관광지 데이터 프레임 중 입력받은 관광지 기반으로 계산
    base_index = new_df.shape[0] - 1
    similar_indexes = sorted_ind[base_index]
    result = pd.DataFrame(similar_indexes)
    result.columns = ['인덱스']
    new_result = result[result['인덱스'].isin(df.index.to_list())].head(10)

    return Response(new_result.values, status=status.HTTP_200_OK)

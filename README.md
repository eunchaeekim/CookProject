#  👩🏻‍🍳 요리 레시피 등록 및 식비 관리 서비스
타겟은 자취생 및 요리를 하는 사람들이다. 자신이 만든 레시피를 등록 할 수 있으며, 커뮤니티를 통해 다른 사람이 올린 레시피를 참고 할 수도 있다. 
또한, 해당 요리를 위해 구매한 식재료 금액을 직접 등록함으로써, 일주일 및 한달 간 소비한 비용을 확인하여 식비를 절약 할 수 있다.

## 프로젝트 기능 및 설계
- 회원가입 기능
  - 사용자는 회원가입을 할 수 있다.
  - 네이버, 카카오, 구글 계정으로 가입하거나 자체 회원가입 (이메일, 패스워드)이 가능하다. 이메일은 unique 해야한다. 

- 로그인 기능
  - 사용자는 로그인을 할 수 있다. 로그인시 회원가입때 사용한 이메일과 패스워드가 일치해야한다.
  - 로그인 성공시 token이 송신 된다. (Refresh token, Access token)
 
- 회원 탈퇴
   - 바로 delete하는 것이 아닌 Spring Batch를 이용해 일주일 뒤 일괄 삭제한다.

- 요리 레시피 게시글 등록/수정/삭제 기능 
  - 사용자는 등록하고자 하는 요리에 대한 내용을 작성할 수 있다.
    (게시글 제목(cook_title), 썸네일 사진(cook_thumbnail_url), 요리 메뉴명(cook_name), 카테고리(category), 몇인분 기준(cook_amount), 조리 시간(cook_time), 요리 방법 설명 및 사진 (cook_method_description, cook_method_photo_url), 들어가는 식재료 이름 및 필요한 양(cook_ingredient, cook_ingredient_amount), 식재료 구매하기(쿠팡연동,크롤링))
  - 사진 : amazon s3 같은 storage 서비스 이용
  - 커뮤니티 공개 여부를 지정할 수 있다. (공개/ 비공개)

- 요리 레시피 조회 기능
  - 게시글 전체를 조회할 수 있다.
  - 검색하여 레시피를 조회할 수 있다. (검색 키워드 : 요리 메뉴명 및 식재료)
  - 지정한 카테고리 별로 요리 레시피를 조회할 수 있다. (카테고리 : 한식/양식/중식/...)
  - 설정한 조리 시간 구간으로 만들 수 있는 요리 레시피를 조회할 수 있다.

- 커뮤니티 기능
  - 공개로 등록된 레시피에 대해 좋아요 및 댓글을 달 수 있다.
  - 공개로 등록된 레시피에 대해 북마크를 할 수 있다.
  - 다른 유저에 대해서 팔로우을 걸 수 있고, 팔로우를 건 유저의 새로운 글이 게시될 때 알림을 받을 수 있다.
 
- 랭킹 기능 (캐싱)
  - 좋아요 개수를 기반으로 게시글에 대해서, 주간 및 월간 게시글 랭킹을 보여준다. 

- 식비 조회 기능
  - 입력한 식재료 구매 날짜 및 금액을 바탕으로, 일주일 및 한달과 같이 기간을 설정하여 해당 기간 동안 사용한 식비를 조회할 수 있다.

## ERD 
![image](https://github.com/withbeluga/CookProject/assets/128959426/ead0ad91-53a8-41fa-a0ae-de2aee121a29)


## Trouble Shooting


### Tech Stack
<div align=center> 
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> 
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> 
  <img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white">
</div>

 <div align="center">
 
# 대여 서비스 플랫폼 SharePod!
 물건을 사기엔 부담되고, 잠시 대여하고 싶을 때 사용할 수 있는 물건 대여 서비스 플랫폼입니다.
 
 <img src="https://user-images.githubusercontent.com/59475849/160775149-0be495f9-68a2-4d89-973c-e6cb1a50e5eb.png">
 
 </div>
 
 <br>
 
 <div align="center">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=MySQL&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/>&nbsp;
    <img src="https://img.shields.io/badge/Notion-000000?style=flat&logo=Notion&logoColor=white"/>
    <img src="https://img.shields.io/badge/Github-181717?style=flat&logo=Github&logoColor=white"/>&nbsp;
  <img src="https://img.shields.io/badge/Slack-4A154B?style=flat&logo=slack&logoColor=white"/>
 <img src="https://img.shields.io/badge/Amazon AWS-232F3E?style=flat&logo=Amazon AWS&logoColor=#232F3E"/>
 <img src="https://img.shields.io/badge/Amazon S3-white?style=flat&logo=Amazon S3&logoColor=#white"/>
 <img src="https://img.shields.io/badge/Redis-white?style=flat&logo=Redis&logoColor=#DC382D"/>
 <img src="https://img.shields.io/badge/Socket.io-black?style=flat&logo=Socket.io&logoColor=#DC382D"/>
</div>

<br>

## 팀 소개
 <div align="center">
 
|송민혁|이승수|김도엽|
|:--------:|:--------:|:--------:|
|<img src="https://cdn-icons-png.flaticon.com/512/528/528256.png" width=500>|<img src="https://user-images.githubusercontent.com/84774696/160975814-550bf8b0-532a-4ddb-a88d-0eeca38c585b.png" width=500>|<img src="https://user-images.githubusercontent.com/97426034/161458870-ab00508e-c057-45e7-9a93-6bd58b07174d.png" width=500>|
|https://github.com/thdals83|https://github.com/leeseungsoo0701|https://github.com/kkamangdol|

</div>


## 팀 소개
 <div align="center">
 
|이름|깃허브|
|:--------:|:--------:|
|송민혁|https://github.com/thdals83|
|이승수|https://github.com/leeseungsoo0701|
|김도엽|https://github.com/kkamangdol|
 
</div>

<br>

## ⚒️ 기능 소개
=> 썸네일, 영상 변경
[![Video Label](.jpg)](https://youtu.be/)

<br>


## 프로젝트 구성
### 백엔드 아키텍처
 <div align="center">
 <img src="https://user-images.githubusercontent.com/59475849/160796220-c55b19f4-7f08-4095-8686-1a5ea2725eb8.png" width="900" height="600">
</div>

<br>

### ⚙️ 개발 환경
- **Server** : AWS EC2(Ubuntu 18.82 LTS)  

- **Framework** : Springboot(2.6.4)

- **Database** : Mysql (AWS RDS,8.0.27)  

- **ETC** : AWS S3, Redis

## 
### 📝 공통 문서
- **ERD(Entity Relationship Diagram)** - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/ERD" >상세보기 - WIKI 이동</a>
  
- **API(Application Programming Interface)** - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/API" >상세보기 - WIKI 이동</a>

## 
### 📌 주요 기능 - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5" >상세보기 - WIKI 이동</a>
- #### 검색 - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/v1/service/BoardService.java" >상세 코드 (354 line)</a>

- #### 로그인 - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/v1/service/UserService.java" >상세 코드 (64 line)</a>

- #### 찜하기 - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/v1/service/NoticeService.java" >상세 코드 (27 ~ line)</a>

- #### 알림 기능 - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/v1/service/NoticeService.java" >상세 코드 (26 ~ line)</a>

- #### 품질 인증 - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/v1/service/AuthService.java" >상세 코드 (34 ~ line)</a>

- #### 영상 릴스(Shorts) - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/v1/service/BoardService.java" >상세 코드 (47 line)</a>

- #### 1:1 채팅 - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/blob/main/src/main/java/com/spring/sharepod/v1/controller/ChatController.java" >상세 코드 (32 ~ line)</a>

## 
###  💡 문제 해결과정
- #### 이미지 동영상 어디다가 올릴꺼인데? AWS S3,IAM JWT - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/WIKI,--%EC%9D%B4%EB%AF%B8%EC%A7%80-%EB%8F%99%EC%98%81%EC%83%81-%EC%96%B4%EB%94%94%EB%8B%A4%EA%B0%80-%EC%98%AC%EB%A6%B4%EA%BA%BC%EC%9D%B8%EB%8D%B0%3F-AWS-S3,IAM" >WIKI</a>
- #### DB에서 꺼내오자! Entity → DTO - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/WIKI,-DB%EC%97%90%EC%84%9C-%EA%BA%BC%EB%82%B4%EC%98%A4%EC%9E%90!-Entity-%E2%86%92-DTO" >WIKI</a>
- #### where문에 null이...? 동적 쿼리 사용 - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/WIKI,-where%EB%AC%B8%EC%97%90-null%EC%9D%B4...%3F-%EB%8F%99%EC%A0%81-%EC%BF%BC%EB%A6%AC-%EC%82%AC%EC%9A%A9" >WIKI</a>
- #### 메시지데이터 자동 삭제(CORN) - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/WIKI,-%EB%A9%94%EC%8B%9C%EC%A7%80%EB%8D%B0%EC%9D%B4%ED%84%B0-%EC%9E%90%EB%8F%99-%EC%82%AD%EC%A0%9C(CORN)" >WIKI</a>
-  #### 로그인 너란 친구.... JWT - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/WIKI,-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EB%84%88%EB%9E%80-%EC%B9%9C%EA%B5%AC....-JWT" >WIKI</a>
-  #### 에러가 나긴 했는데 무슨 에러라고?? ErrorCodeException - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/WIKI,-%EC%97%90%EB%9F%AC%EA%B0%80-%EB%82%98%EA%B8%B4-%ED%96%88%EB%8A%94%EB%8D%B0-%EB%AC%B4%EC%8A%A8-%EC%97%90%EB%9F%AC%EB%9D%BC%EA%B3%A0%3F%3F-ErrorCodeException" >WIKI</a>
- #### 채팅을 하는데 우편이 이용된다?! Pub Sub 을 이용한 웹소켓 채팅 기능 - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/WIKI,-%EC%B1%84%ED%8C%85%EC%9D%84-%ED%95%98%EB%8A%94%EB%8D%B0-%EC%9A%B0%ED%8E%B8%EC%9D%B4-%EC%9D%B4%EC%9A%A9%EB%90%9C%EB%8B%A4%3F!-Pub-Sub-%EC%9D%84-%EC%9D%B4%EC%9A%A9%ED%95%9C-%EC%9B%B9%EC%86%8C%EC%BC%93-%EC%B1%84%ED%8C%85-%EA%B8%B0%EB%8A%A5" >WIKI</a>

## 대여 서비스 플랫폼 SharePod!
 물건을 사기엔 부담되고, 잠시 대여하고 싶을 때 사용할 수 있는 물건 대여 서비스 플랫폼입니다.
 
 <img src="https://user-images.githubusercontent.com/59475849/160775149-0be495f9-68a2-4d89-973c-e6cb1a50e5eb.png">
 
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
 <img src="https://user-images.githubusercontent.com/59475849/160796220-c55b19f4-7f08-4095-8686-1a5ea2725eb8.png" height="400">
</div>

<br>

### ⚙️ 개발 환경
- **Server** : AWS EC2(Ubuntu 18.82 LTS)  
- **Framework** : Springboot
- **Database** : Mysql (AWS RDS)  
- **ETC** : AWS S3, Redis

<br>

### 📝 공통 문서
- **ERD(Entity Relationship Diagram)** - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/ERD" >상세보기 - WIKI 이동</a>  
- **API(Application Programming Interface)** - <a href="https://github.com/HangHae99ProjectTeam10/SharePod-Server/wiki/API" >상세보기 - WIKI 이동</a>

<br>

### 📌 주요 기능
#### Security
- SSL
- CORS 
#### 로그인
- Spring Security + JWT 기반 일반 로그인
- OAuth2 Kakao 소셜 로그인
- 비밀번호 찾기 Mail 발송
#### Member
- Profile 정보 변경
- Trilog 작성 유도 Mail
- Grade Up
#### Trils (영상 릴스)
- 게시물, 좋아요, 해쉬태그 CRUD
- 스트리밍 서비스 제공
#### Trilog (블로그) 
- 게시물, 댓글, 대댓글, 좋아요 CRUD
- Toast UI Editor (WSYSWYG) 에디터 활용
- 페이징 및 검색 기능

### 📌 에러 핸들링

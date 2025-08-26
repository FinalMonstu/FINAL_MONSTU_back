# FINAL_MONSTU_back
MONSTU project backend code

---

### ⚡ Live Site: [MonStu_Site_Link](https://monstu.win)  접근코드 24601

### 📦 GitHub Front Repository: [github.com](https://github.com/FinalMonstu/FINAL_MONSTU_front)

### 💿 Demo Video: [youtube.com](https://www.youtube.com/watch?v=54qtrOOisXw)

### 📖 Developer Profile: [notion.com](https://www.notion.so/PORTFOLIO-19e303eae1f280828d69f4b34a9654a7?pvs=4)

### 🚩 Develop Log Blog: [tistory.com](https://code-is-code.tistory.com/)
---

### 📖 소개

MonStu는 웹 상의 텍스트를 활용해 다양한 언어 학습을 지원하는 서비스입니다. 

사용자가 문장이나 단어를 하이라이트하면 AI 번역이 즉시 제공되며, 번역 내역을 관리할 수 있습니다.

---

### 🚀 주요 기능

- 실시간 단어 / 문장 번역
- 회원 관리, 게시글 관리

---

### ⭐ 성과

- RESTful API 설계 및 JPA 기반 CRUD 기능 구현
- QueryDSL을 활용한 동적 필터링 검색 기능 개발
- 캐싱 적용으로 AI 번역 속도 개선 (평균 140ms → 129ms)
- CompletableFuture와 I/O pool을 활용한 비동기 방식으로 외부 번역 API 기능 속도 개선 (380ms → 180ms)

---

### ✅ 성능·신뢰성 개선

- 3단계 조회 파이프라인: 캐시 → DB → 외부 API, 동시 실행 후 ‘첫 성공 결과’ 반환(캐시/DB 경쟁 실행으로 지연 최소화)

- 회복력 패턴: completeOnTimeout, orTimeout, exceptionally로 타임아웃·예외 내성 강화

- 결과 캐싱 & 영속화: 외부 API 결과를 캐시 저장 + DB 저장으로 재사용·비용 절감

---

### 🛠️ 사용 기술 스택

- **Frontend**: React, Material-UI (MUI)
- **Backend**: Spring Boot 3.4.4
- **DB**: MySQL 8.3
- **AI API**: Google Translation API
- **Auth & Security**: JWT, Spring Security
- **ORM & Query**: JPA, QueryDSL

### ⚙️ 배포 환경

- OS: Ubuntu Linux
- Domain : Cloudflare
- Hardware: Laptop

---

### 🙆‍♂️ 개발자 이력서 [notion.com](https://www.notion.so/248303eae1f280949123e25ee9ad7d04)

---

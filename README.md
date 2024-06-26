# Schedule App Server

### 1️⃣ 소개
이 프로젝트는 JWT 토큰을 이용한 회원가입, 로그인, JWT 시큐리티 재발급 기능을 포함한 인증 시스템을 구현한 스케줄 관리 API입니다.  
스케줄에 대한 CRUD 기능과 댓글 작성, 파일 업로드 기능이 있습니다.  

✔️로그 파일은 브랜치를 확인해주세요(작업중에는 파일을 일일이 확인하는 불편함으로 내렸습니다)

### 2️⃣ 기능

- 인증 및 인가
  - **회원가입**: 사용자 가입
  - **로그인**: 로그인하고 JWT 토큰발급받기
  - **토큰 재발급**: 만료된 토큰을 재발급받기

- 스케줄 관리
  - **스케줄 생성**: 새로운 스케줄 추가
  - **스케줄 조회**: 모든 스케줄 목록 조회
  - **스케줄 상세 조회**: 특정 스케줄의 상세 정보 조회
  - **스케줄 수정**: 기존 스케줄의 내용 수정
  - **스케줄 삭제**: 특정 스케줄 삭제

- 댓글 관리
  - **댓글 작성**: 특정 스케줄 댓글 작성
  - **댓글 수정**: 작성된 댓글 수정
  - **댓글 삭제**: 작성된 댓글 삭제

- 파일 관리
  - **파일 업로드**: 스케줄에 파일을 업로드
  - **파일 조회**: 업로드된 파일을 조회
  - **파일 삭제**: 업로드된 파일을 삭제


##  프로젝트 세부 사항

### 3️⃣ 구현 과정
  - 1 단계: JDBC를 사용해서 MySQL 데이터베이스에 스케줄을 저장하도록 전환했습니다.
  - 2 단계: JPA를 도입해서 데이터베이스 상호작용을 더욱 효율적으로 구현했습니다.
  
- 테스트 리팩토링:
  - 초기에는 JUnit만을 사용해서 테스트를 작성했고,
  - 이후 Mockito를 도입해서 Mock 객체를 사용한 유닛 테스트로 리팩토링했습니다.

## 4️⃣ 사용된 기술
  - Spring Boot: 애플리케이션의 기반 프레임워크.
- Spring Web: RESTful API 구현.
- Spring Validation: 데이터 유효성 검사.
- Spring Data JPA: 데이터베이스 접근 계층.
- MySQL
- JDBC: 초기 단계에서는 JDBC를 통해 MySQL 데이터베이스와의 상호작용을 구현했습니다.
- JPA: JPA로 전환해서 데이터베이스 작업을 더욱 효율적으로 처리했습니다.
- Mockito
- Intellij CheckStyle 적용
- Lombok: 보일러플레이트 코드를 줄이기 위해 사용.
- Mockito: 단위 테스트를 위한 목킹 프레임워크.
- JUnit 5: 단위 테스트 프레임워크.
- Swagger/OpenAPI: API 문서화.
- Redocly: API 문서화(swagger로 최종 수정)
- Jakarta Validation: 데이터 유효성 검사를 위한 표준 API.
- SLF4J with Logback: 로깅 프레임워크.
---

### 5️⃣ 💻API DOC [링크](https://dami0806.github.io/schedule-management/)

---
### 6️⃣ 프로젝트 설명 및 이미지 

**프로젝트 스크린샷**
| 작업 내용 | 사진 |
|-----------|------|
| **Use Case Diagram** | <img width="700" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/4dab83c8-99f3-456d-be1a-b90ab3806191">|
|Erd| <img width="600" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/157a0997-236a-43a3-afaf-1f2d89cf4533">|
|회원가입 |<img width="984" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/f961fd00-242f-47ec-877c-bc9c3085759b">|
|로그인 |<img width="900" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/84869d9c-2f1f-41ef-931e-bb61883cb5db">|
|토큰값 갱신|<img width="1000" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/1a30c63b-2435-4879-88f6-aa217777ceac">|
|스케줄 작성|<img width="900" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/738babdb-ab3f-42bc-bb68-a40b262998f4">|
|스케줄 목록조회|<img width="900" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/b7aa9a51-a1be-4911-926b-6a0c29586fa4">|
|스케줄 수정|<img width="900" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/2a09107b-d857-401e-abb3-4d377926d0a5">|
|스케줄 상세조회|<img width="900" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/0044fa12-3e00-4c63-8ec8-878f0502d39a">|
|댓글 작성|<img width="900" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/a3a3cf5b-4915-4ea4-b8ee-8d8ccf3af888">|
|댓글 수정|<img width="600" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/acdd9117-2fcd-4b6e-a651-f2fcbdeed3f2">  <img width="600" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/d0876d8c-c9f2-4f71-a1f8-4ff94f5dbc1c">|
|댓글 삭제|<img width="600" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/7c93fa68-cdff-4966-ada0-d214eb8b367b">  <img width="600" alt="image" src="https://github.com/dami0806/schedule-management/assets/85047035/1dcac384-234c-4a0f-a085-ed9b92e3c0cf">|

### + 초반 작업
| 작업 내용 | 사진 |
|-----------|------|
| **Use Case Diagram** | <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/4845f900-75c3-47e4-89c1-0e6e36636b9a">|
| **ERD 다이어그램** | <img width="300" alt="ERD 다이어그램" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/d84e206b-ce73-42fe-9f28-fdaad2564cfd"> |
|  **초기 프로젝트 스크린샷**  |   |
| 할일추가 <br> - 전체일정 <br> - 세부사항 <br> - 수정 & 삭제 시 <br>  비밀번호 유효성검사 | <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/f2dbf2ca-df7e-48a1-a8d1-38f5ce490bdf" > <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/feae617c-4fd8-423b-b955-870506637133">  <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/9e473814-f11c-4c18-9924-b14f5b9e2d50">  <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/07781081-dab7-428c-bde8-82becd37c624">||
| Jacoco  | <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/4b6847a2-91f7-473f-9c44-91b61c38aaa9">|

---
### ⚡️7️⃣ 사용된 기술 개념
- [리펙토링](https://github.com/dami0806/ScheduleManagement/wiki/%EC%A2%8B%EC%9D%80-%EC%BD%94%EB%93%9C%EB%A5%BC-%EC%9C%84%ED%95%9C-%EA%B3%A0%EB%AF%BC#1%EB%B6%88%EB%B3%80%EC%84%B1-%EC%9C%A0%EC%A7%80-%EA%B3%A0%EB%AF%BC)
- [Test코드](https://github.com/dami0806/ScheduleManagement/wiki/TestCode#mockito%EB%9E%80-%EB%AC%B4%EC%97%87%EC%9D%B8%EA%B0%80)
-  [Exception처리](https://github.com/dami0806/ScheduleManagement/wiki/Exception%EC%B2%98%EB%A6%AC#%EC%9A%94%EC%95%BD)
- [Swagger](https://github.com/dami0806/ScheduleManagement/wiki/Swagger#%EC%9C%A0%ED%9A%A8%EC%84%B1-%EA%B2%80%EC%82%AC%EB%A5%BC-%EC%84%A4%EC%A0%95%ED%95%98%EA%B3%A0-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95)
- [DTO와 엔터티 ResponseEntity](https://github.com/dami0806/ScheduleManagement/wiki/DTO%EC%99%80-%EC%97%94%ED%84%B0%ED%8B%B0----ResponseEntity#1-dto%EC%99%80-%EC%97%94%ED%84%B0%ED%8B%B0%EC%9D%98-%EC%B0%A8%EC%9D%B4%EC%A0%90)
- [ISSUE](https://github.com/dami0806/ScheduleManagement/wiki/%08Issue#issue)

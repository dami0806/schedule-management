# SchedulingAppServer

## 1. 프로젝트 개요
이 프로젝트는 스케줄 관리 시스템으로, 사용자들이 일정을 효율적으로 관리할 수 있도록 돕는 애플리케이션입니다.  
사용자는 새로운 일정을 추가하고, 기존 일정을 수정 및 삭제할 수 있으며, 모든 일정을 조회할 수 있습니다.  
또한 파일 업로드 및 다운로드 기능을 제공해서, 일정과 관련된 파일을 관리할 수 있습니다.

##  프로젝트 세부 사항

### 2. 기술 스택
- Spring Boot
- MySQL
- JDBC: 초기 단계에서는 JDBC를 통해 MySQL 데이터베이스와의 상호작용을 구현했습니다.
- JPA: 향후 JPA로 전환하여 데이터베이스 작업을 더욱 효율적으로 처리했습니다.
- Mockito
- Intellij CheckStyle 적용


### 3. 기능
- 스케줄 관리: 사용자는 스케줄을 추가, 수정, 삭제, 조회할 수 있습니다.
- 파일 관리: 파일 업로드 및 다운로드 기능을 제공합니다.
- 비밀번호 검증: 특정 스케줄의 비밀번호를 검증할 수 있습니다.

### 4. 구현 과정
- 초기 단계: 애플리케이션은 내부에서 List를 사용해서 스케줄을 관리했습니다.
- 중간 단계: JDBC를 사용해서 MySQL 데이터베이스에 스케줄을 저장하도록 전환했습니다.
- 향후 계획: JPA를 도입해서 데이터베이스 상호작용을 더욱 효율적으로 구현할 예정입니다.
  
- 테스트 리팩토링:
  - 초기에는 JUnit만을 사용해서 테스트를 작성했고,
  - 이후 Mockito를 도입해서 Mock 객체를 사용한 유닛 테스트로 리팩토링했습니다.

## 5. 사용된 기술
- Java 11: 
- Spring Boot: 애플리케이션의 기반 프레임워크.
- Spring Web: RESTful API 구현.
- Spring Validation: 데이터 유효성 검사.
- Spring Data JPA: 데이터베이스 접근 계층.
- H2 Database: 인메모리 데이터베이스로 개발 및 테스트 환경에서 사용.
- Lombok: 보일러플레이트 코드를 줄이기 위해 사용.
- Mockito: 단위 테스트를 위한 목킹 프레임워크.
- JUnit 5: 단위 테스트 프레임워크.
- Swagger/OpenAPI: API 문서화.
- Jakarta Validation: 데이터 유효성 검사를 위한 표준 API.
- SLF4J with Logback: 로깅 프레임워크.
---

## 6. 엔티티 설명
- Schedule
  - id: Long, primary key
  - title: String, 제목
  - description: String, 설명
  - assignee: String, 담당자 이메일
  - date: String, 날짜
  - password: String, 비밀번호
  - isDeleted: boolean, 삭제 여부
- File
  - fileName: String, 파일 이름
  - fileType: String, 파일 형식
  - filePath: String, 파일 경로
  - uploadDate: Date, 업로드 날짜
  - fileSize: Long, 파일 크기
---

## 7. 프로젝트 설명 및 이미지 

| 작업 내용 | 사진 |
|-----------|------|
| **Use Case Diagram** | <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/4845f900-75c3-47e4-89c1-0e6e36636b9a">|
| **API**  <br> 1. 일정관리 <br> 2 <br> 파일 업로드, <br> 다운로드| <img width="500" alt="스케줄 API" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/bc4f79a6-1aa6-4833-8e0d-74ce6532e685"> <img width="500" alt="파일 업로드, 다운로드" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/a87acb44-2938-439c-802c-eed12b5a659b"> |
| **ERD 다이어그램** | <img width="300" alt="ERD 다이어그램" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/d84e206b-ce73-42fe-9f28-fdaad2564cfd"> |
|  **프로젝트 스크린샷**  |   |
| 할일추가 <br> - 전체일정 <br> - 세부사항 <br> - 수정 & 삭제 시 <br>  비밀번호 유효성검사 | <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/f2dbf2ca-df7e-48a1-a8d1-38f5ce490bdf" > <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/feae617c-4fd8-423b-b955-870506637133">  <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/9e473814-f11c-4c18-9924-b14f5b9e2d50">  <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/07781081-dab7-428c-bde8-82becd37c624">||
| **Swagger** | <img width="500" alt="프로젝트 스크린샷" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/f7afa26a-b058-4653-bb4e-5b9bffe87145"> |
| Jacoco  | <img width="500" alt="image" src="https://github.com/dami0806/ScheduleManagement/assets/85047035/37584e7d-241a-464a-84ff-bbdec5b546d8"> <br> - 정상처리만 테스트해서 코드 커버리지낮음|

# ⚡️8. 사용된 기술 개념

###  [Exception처리](https://github.com/dami0806/ScheduleManagement/wiki/Exception%EC%B2%98%EB%A6%AC#%EC%9A%94%EC%95%BD)
### [Swagger](https://github.com/dami0806/ScheduleManagement/wiki/Swagger#%EC%9C%A0%ED%9A%A8%EC%84%B1-%EA%B2%80%EC%82%AC%EB%A5%BC-%EC%84%A4%EC%A0%95%ED%95%98%EA%B3%A0-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94-%EB%B0%A9%EB%B2%95)
### [DTO와 엔터티 ResponseEntity](https://github.com/dami0806/ScheduleManagement/wiki/DTO%EC%99%80-%EC%97%94%ED%84%B0%ED%8B%B0----ResponseEntity#1-dto%EC%99%80-%EC%97%94%ED%84%B0%ED%8B%B0%EC%9D%98-%EC%B0%A8%EC%9D%B4%EC%A0%90)

### [ISSUE](https://github.com/dami0806/ScheduleManagement/wiki/%08Issue#issue)

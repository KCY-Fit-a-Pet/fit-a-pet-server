# Fit a Pet
김최양 사이드 프로젝트 FitaPet 백엔드 Repository 입니다.

- 기획&디자인: [김유빈](https://github.com/youvebeen09)
- [프론트엔드](https://github.com/KCY-Fit-a-Pet/fit-a-pet-client): [최희진](https://github.com/heejinnn)
- 백엔드: [양재서](https://github.com/psychology50)

## [개발 기록]
- [Side Project: Kakao Chat CI](https://jaeseo0519.tistory.com/323)
- [NCP Cloud Function & Object Storage presigned URL 발급](https://jaeseo0519.tistory.com/331)
- [Service Layer 분리에 대하여](https://jaeseo0519.tistory.com/314)
- [API 세분화에 대하여](https://jaeseo0519.tistory.com/341)
- [OIDC OAuth2 인증](https://jaeseo0519.tistory.com/349)
- [Nginx 구성과 HTTPS 설정](https://jaeseo0519.tistory.com/354)
- [GitHub Action & NCP CD pipeline 구축](https://jaeseo0519.tistory.com/354)
- [멀티 모듈화](https://jaeseo0519.tistory.com/359)

## [ Contents ]
- [Project Summary](#project-summary)
- [Version Control](#version-control)
- [Dev Environment](#dev-environment)
- [Tech Stack](#tech-stack)
    - [Framework & Library](#framework--library)
    - [Build tool](#build-tool)
    - [Database](#database)
    - [Infra](#infra)
- [Project Check List](#project-check-list)
- [System Architecture](#system-architecture)
- [WAS Architecture](#was-architecture)
- [ERD](#erd)
- [Branch Convention](#branch-convention)

## Version Control
| Version # | Revision Date | Description        | Author |
|:---------:|:-------------:|:-------------------|:------:|
|  v0.0.1   |   2023.10.1   | 프로젝트 기본 기능 구현 및 배포 | 양재서 |
|  v0.0.2   |  2024.02.17   | 멀티 모듈화 아키텍처 추가     | 양재서 |

## Dev Environment
- IntelliJ 2023.1.2
- Postman 10.18.9
- GitHub 
- Windows 11
- Notion

## Tech Stack
### Framework & Library
- JDK 17
- SpringBoot 3.1.0
- SpringBoot Security
- Spring Data JPA
- Spring Doc Open API  
- Lombok 
- JUnit5
- jjwt 0.11.5
- httpclient 4.5.14 & httpclient5 5.1.4 

### Build tool
- Gradle

### Database
- MySQL8
- Redis

### Infra
- AWS EC2 (for Build Server)
- Docker & Docker-compose
- Jenkins
- GitHub Todo bot
- GitHub Action
- Kakao Talk
- Naver Cloud Platform Server (for WAS)
- Naver Cloud Platform Cloud DB for Redis
- Naver Cloud Platform Object Storage
- Naver Cloud Platform Simple & Easy Notification Service
- Naver Cloud Platform API Gateway & Cloud Functions
- Goorm IDE (for DB Server)

## Project Check List
- [ ] 실제 서비스를 공개적으로 배포하고 운영하는 경험을 해보았다.
- [ ] 유저의 피드백에 따라 성능/사용성을 개선하고 신규 기능을 추가해보았다.
- [ ] 발견되는 버그와 개선사항들을 정리하고 쌓인 이슈들을 체계적으로 관리해보았다.
- [X] 코드를 지속적으로 리팩토링하고 디자인 패턴을 적용해보았다.
- [X] 위의 시도에서 더 좋은 설계와 더 빠른 개발 사이의 트레이드 오프를 고민해본 적이 있다.
- [X] 반복되는 수정과 배포에 수반되는 작업들을 자동화 해보았다.
- [ ] 언어나 프레임워크만으로 구현할 수 없는 것들을 직접 구현해보았다.
- [ ] 내가 사용한 라이브러리나 프레임 워크의 한계를 느끼고 개선해보았다.
- [ ] 코드나 제품의 퀄리티를 유지하기 위한 분석툴이나 테스트 툴을 도입해보았다.
- [X] 타인과의 협업을 효율적으로 하기 위한 고민을 해보았다.

## System Architecture
<div align="center"><img src="https://github.com/KCY-Fit-a-Pet/fit-a-pet-client/assets/96044622/08a48299-460b-46c9-ac83-ac7aec15d73c"></img></div>

## WAS Architecture
<div align="center"><img src="https://github.com/KCY-Fit-a-Pet/fit-a-pet-client/assets/96044622/81ec4e4b-8a15-4a26-a8ff-96022dfde03f"></img></div>

- WAS Server 내부에 Nginx를 통해 Reverse Proxy를 구현했습니다.

## Multi Module Architecture

<div align="center"><img src="https://github.com/KCY-Fit-a-Pet/fit-a-pet-server/assets/96044622/df673786-71d5-4a30-b0ba-2c9e9f4f2603"></img></div>

- Multi Module Architecture를 적용하여 각 모듈별로 분리하여 개발하고, 빌드 및 배포를 진행하고 있습니다.
- 각 모듈별로 담당하는 역할을 분리하여 개발하고 있습니다.
- 각 모듈에 대한 Convention은 다음과 같습니다.
  - [fitapet-common](https://github.com/KCY-Fit-a-Pet/fit-a-pet-server/tree/develop/fitapet-common/Readme.md) : 독립적인 오픈 소스로 배포 가능한 수준의 공통 모듈. 모든 모듈이 의존한다.
  - [fitapet-infra](https://github.com/KCY-Fit-a-Pet/fit-a-pet-server/tree/develop/fitapet-infra/Readme.md) : 데이터베이스, 인프라스트럭처, 클라우드 서비스와 연동하는 모듈. common 모듈에 의존한다.
  - [fitapet-domain](https://github.com/KCY-Fit-a-Pet/fit-a-pet-server/tree/develop/fitapet-domain/Readme.md) : 도메인 로직을 담당하며, repository를 보호하는 모듈. common 모듈에 의존한다.
  - [fitapet-app-external-api](https://github.com/KCY-Fit-a-Pet/fit-a-pet-server/tree/develop/fitapet-app-external-api/Readme.md) : Client와 연동하는 외부 API를 담당하는 모듈. common, infra, domain 모든 모듈에 의존한다.

## ERD
<div align="center"><img src="https://github.com/KCY-Fit-a-Pet/fit-a-pet-server/assets/96044622/9b75726d-1695-4459-8c3f-72f664d6d036"></img></div>

- 현재 많은 부분이 수정되었고, 앞으로도 계속 수정될 예정입니다.

## Branch Convention

```
main ── develop ── feature
└── hotfix
```

| Brach name | description                                                                                                                               |
| --- |-------------------------------------------------------------------------------------------------------------------------------------------|
| main | 배포 중인 서비스 브랜치 <br/> • 실제 서비스가 이루어지는 브랜치입니다. <br/> • 해당 브랜치를 기준으로 develop 브랜치가 분기됩니다.<br/> • 긴급 수정 안건에 대해서는 hotfix 브랜치에서 처리합니다.            |
| develop | 작업 브랜치 <br/> • 개발, 테스트, 릴리즈 등 배포 전 단계의 기준이 되는 브랜치입니다. <br/> • 프로젝트의 default 브랜치입니다. <br/> • 해당 브랜치에서 feature 브랜치가 분기됩니다.                  |
| feature | 기능 단위 구현 <br/> • 개별 개발자가 맡은 작업을 개발하는 브랜치입니다. <br/> • feature/(feature-name)처럼 머릿말-꼬릿말(개발하는 기능)으로 명명합니다. <br/> • kebab-case 네이밍 규칙을 준수합니다. |
| hotfix | 서비스 중 긴급 수정 사항 처리 <br/> • main에서 분기합니다. |

## Commit Convention

| emoji | message | description |
| --- | --- | --- |
| :sparkles: | feat | 새로운 기능 추가, 기존 기능을 요구 사항에 맞추어 수정 |
| :bug: | fix | 기능에 대한 버그 수정 |
| :green_heart: | build | 빌드 관련 수정 |
| :pushpin: | chore | 패키지 매니저 수정, 그 외 기타 수정 ex) .gitignore |
| :construction_worker: | ci | CI 관련 설정 수정 |
| :closed_book: | docs | 문서(주석) 수정 |
| :art: | style | 코드 스타일, 포맷팅에 대한 수정 |
| :recycle: | refactor | 기능 변화가 아닌 코드 리팩터링 |
| :white_check_mark: | test | 테스트 코드 추가/수정 |
| :bookmark: | release | 버전 릴리즈 |
| :ambulance: | hotfix | 긴급 수정 |
| :twisted_rightwards_arrows: | branch | 브랜치 추가/병합 |

<details>
<summary>토글 접기/펼치기</summary>
<div markdown="1">
    - [ ]  `**feat : 회원가입 API 구현**`과 같이 `**머릿말: 내용**` 형식으로 작성합니다.
    - [ ]  refactoring의 경우 기능 변화 없이 구조 개선을 하는 경우 사용합니다.
    - [ ]  여러 작업을 동시 실행한 경우 한 줄에 한 내용씩 입력합니다. 순서는 메인이 된 작업을 우선으로 둡니다.
        
        ```
        - ❌ 잘못된 예시_1
        feat: 버튼 컴포넌트 구현, API 중복 요청 현상 해결
        
        - ❌ 잘못된 예시_2
        feat: 버튼 컴포넌트 구현 || fix: API 중복 요청 현상 해결
        
        - ⭕ 올바른 예시
        feat: 버튼 컴포넌트 구현
        fix: API 중복 요청 현상 해결
        ```
</div>
</details>

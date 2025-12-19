# Spring WebFlux 실습 - 리액티브 웹 애플리케이션

**Spring Boot WebFlux를 활용한 고성능 비동기 웹 애플리케이션 학습 프로젝트**입니다. 리액티브 프로그래밍, 논블로킹 I/O, R2DBC를 통한 데이터베이스 연동 등을 실제 코드로 체험할 수 있습니다.

---

## 🎯 프로젝트 목표

| 목표 | 설명 |
|------|------|
| **리액티브 패러다임 이해** | Project Reactor 기반 Mono/Flux 학습 |
| **논블로킹 I/O 실습** | 동시성 처리 및 성능 최적화 경험 |
| **R2DBC 데이터 접근** | 비동기 데이터베이스 연동 |
| **WebFlux 라우팅** | 함수형 엔드포인트 구성 |
| **Redis 캐싱** | 리액티브 캐시 처리 |
| **실전 예제 코드** | 실무 패턴 학습 |

---

## 🛠 기술 스택

| 분야 | 기술 |
|------|------|
| **프레임워크** | Spring Boot 3.5.4 WebFlux |
| **런타임** | Project Reactor |
| **데이터베이스** | MySQL 8.0+ (R2DBC) |
| **캐시** | Redis |
| **Java 버전** | 17 LTS |
| **빌드 도구** | Gradle |
| **테스트** | JUnit 5, Reactor Test |
| **API 패턴** | RESTful + 함수형 라우팅 |

---

## 📦 프로젝트 구조

```
spring-webflux-practice/                    # 루트 프로젝트
│
├── build.gradle                             # Gradle 빌드 설정
├── settings.gradle                          # Gradle 설정
├── gradlew / gradlew.bat                   # Gradle Wrapper
│
└── src/
    │
    ├── main/
    │   │
    │   ├── java/com/laze/springwebfluxpractice/
    │   │   │
    │   │   ├── SpringWebfluxPracticeApplication.java  # 진입점
    │   │   │
    │   │   ├── RouteConfig.java              # 함수형 라우팅 설정
    │   │   ├── SampleHandler.java            # 함수형 핸들러
    │   │   │
    │   │   ├── controller/                   # 어노테이션 기반 컨트롤러
    │   │   │   ├── SampleController.java    # 기본 샘플
    │   │   │   ├── UserController.java      # 사용자 CRUD
    │   │   │   ├── PostController.java      # 게시글 Redis 캐시
    │   │   │   └── PostR2dbcController.java # R2DBC 데이터 접근
    │   │   │
    │   │   ├── service/                     # 비즈니스 로직
    │   │   │   ├── UserService.java         # 사용자 서비스
    │   │   │   ├── PostService.java         # 게시글 서비스
    │   │   │   └── PostServiceR2dbc.java   # R2DBC 게시글 서비스
    │   │   │
    │   │   ├── repository/                  # 데이터 접근 계층
    │   │   │   ├── UserRepository.java      # 사용자 R2DBC 저장소
    │   │   │   └── PostRepository.java      # 게시글 R2DBC 저장소
    │   │   │
    │   │   ├── dto/                         # 데이터 전송 객체
    │   │   │   ├── UserDto.java
    │   │   │   ├── PostDto.java
    │   │   │   └── ResponseDto.java
    │   │   │
    │   │   └── config/                      # 설정 클래스
    │   │       ├── WebClientConfig.java    # WebClient 설정
    │   │       ├── RedisConfig.java         # Redis 반응형 설정
    │   │       └── R2dbcConfig.java         # R2DBC 설정
    │   │
    │   └── resources/
    │       ├── application.yml              # 기본 설정
    │       ├── application-dev.yml          # 개발 프로필
    │       ├── application-prod.yml         # 프로덕션 프로필
    │       └── logback-spring.xml           # 로깅 설정
    │
    └── test/
        └── java/com/laze/springwebfluxpractice/
            ├── UserServiceTest.java         # 서비스 테스트
            ├── PostControllerTest.java      # 컨트롤러 테스트
            ├── RouteConfigTest.java         # 라우트 테스트
            └── WebClientTest.java           # WebClient 테스트
```

---

## 🚀 빠른 시작

### 필수 요구사항

```bash
# Java 17+ 설치 확인
java --version

# MySQL 실행 (포트 3308)
docker run -d \
  --name mysql \
  -p 3308:3306 \
  -e MYSQL_ROOT_PASSWORD=password \
  -e MYSQL_DATABASE=r2dbcpractice \
  mysql:8.0

# Redis 실행 (포트 6379)
docker run -d \
  --name redis \
  -p 6379:6379 \
  redis:latest
```

### 데이터베이스 스키마 생성

```sql
CREATE DATABASE IF NOT EXISTS r2dbcpractice;

USE r2dbcpractice;

-- 사용자 테이블
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    age INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 게시글 테이블
CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    content LONGTEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- 인덱스 추가
CREATE INDEX idx_posts_user_id ON posts(user_id);
CREATE INDEX idx_users_email ON users(email);
```

### 프로젝트 설정 및 실행

**1단계: 클론 및 빌드**
```bash
git clone https://github.com/L-a-z-e/spring-webflux-practice.git
cd spring-webflux-practice
./gradlew build
```

**2단계: 개발 환경 실행**
```bash
# 기본 프로필로 실행
./gradlew bootRun

# 특정 프로필로 실행
./gradlew bootRun --args='--spring.profiles.active=dev'
```

**3단계: 접속 및 테스트**
```bash
# 헬스 체크
curl http://localhost:8080/actuator/health

# API 테스트
curl http://localhost:8080/api/users
```

---

## 📚 핵심 개념

### 1. Mono와 Flux

**Mono** - 0개 또는 1개의 요소 발행

```java
@GetMapping("/user/{id}")
public Mono<UserDto> getUser(@PathVariable Long id) {
    return userService.findById(id);
}
```

**Flux** - 0개 이상의 여러 요소 발행

```java
@GetMapping("/users")
public Flux<UserDto> getAllUsers() {
    return userService.findAll();
}
```

### 2. 논블로킹 I/O

**블로킹 코드 (❌ 피해야 할 패턴)**
```java
// 스레드 블로킹 - 1000명 동시 요청 시 1000개 스레드 필요
User user = userRepository.findById(id); // DB I/O 대기
return user;
```

**논블로킹 코드 (✅ WebFlux 패턴)**
```java
// 스레드 사용 최소화 - 적은 스레드로 많은 요청 처리
public Mono<User> findById(Long id) {
    return userRepository.findById(id); // I/O 동안 다른 작업 진행
}
```

### 3. 반응형 스트림 연산

**map** - 요소 변환
```java
userService.findAll()
    .map(user -> new UserDto(user));
```

**flatMap** - 비동기 작업 체이닝
```java
postService.findAll()
    .flatMap(post -> userService.findById(post.getUserId())
        .map(user -> new PostWithUserDto(post, user))
    );
```

**filter** - 요소 필터링
```java
userService.findAll()
    .filter(user -> user.getAge() >= 18);
```

**reduce** - 집계
```java
userService.findAll()
    .reduce(0, (count, user) -> count + 1);
```

---

## 🏗 아키텍처 패턴

### 요청 처리 흐름

```
Client 요청
   ↓
RouteConfig (함수형 라우팅)
   또는
@RestController (어노테이션)
   ↓
Handler / @RequestMapping
   ↓
Service (Mono/Flux 반환)
   ↓
Repository (R2DBC 비동기 쿼리)
   ↓
MySQL / Redis (데이터 저장소)
   ↓
반응형 스트림 처리
   ↓
클라이언트 응답
```

### 컴포넌트별 역할

| 컴포넌트 | 역할 | 예제 |
|---------|------|------|
| **RouteConfig** | 함수형 엔드포인트 정의 | `/api/sample` 라우트 |
| **Controller** | HTTP 요청/응답 처리 | `@PostMapping`, `@GetMapping` |
| **Service** | 비즈니스 로직 | Mono/Flux 변환, 데이터 처리 |
| **Repository** | 데이터 접근 | R2DBC CRUD 작업 |
| **Config** | 설정 클래스 | Bean 등록, 프로퍼티 설정 |

---

## 💡 실전 예제 코드

### 1. 함수형 라우팅

**RouteConfig.java**
```java
@Configuration
public class RouteConfig {
    
    @Bean
    public RouterFunction<ServerResponse> sampleRoutes(SampleHandler handler) {
        return route()
            // GET /sample
            .GET("/sample", handler::handle)
            // POST /sample with body
            .POST("/sample/create", handler::create)
            // GET /sample/{id}
            .GET("/sample/{id}", handler::detail)
            .build();
    }
}
```

**SampleHandler.java**
```java
@Component
public class SampleHandler {
    
    public Mono<ServerResponse> handle(ServerRequest request) {
        return ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(Map.of("message", "Hello WebFlux!"))
            .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .bodyValue(Map.of("error", e.getMessage()))
            );
    }
    
    public Mono<ServerResponse> create(ServerRequest request) {
        return request.bodyToMono(String.class)
            .flatMap(body -> ServerResponse.ok()
                .bodyValue(Map.of("received", body))
            );
    }
}
```

### 2. R2DBC 데이터 접근

**UserRepository.java**
```java
@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    
    // 메서드명으로 쿼리 생성
    Mono<User> findByEmail(String email);
    
    // 이름으로 모두 검색
    Flux<User> findByNameContaining(String name);
    
    // 나이 범위로 검색
    Flux<User> findByAgeBetween(int minAge, int maxAge);
}
```

**UserService.java**
```java
@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    // 전체 사용자 조회
    public Flux<UserDto> findAll() {
        return userRepository.findAll()
            .map(this::toDto)
            .delayElement(Duration.ofMillis(100)); // 시뮬레이션 지연
    }
    
    // ID로 조회
    public Mono<UserDto> findById(Long id) {
        return userRepository.findById(id)
            .map(this::toDto)
            .switchIfEmpty(
                Mono.error(new RuntimeException("User not found"))
            );
    }
    
    // 이메일로 조회
    public Mono<UserDto> findByEmail(String email) {
        return userRepository.findByEmail(email)
            .map(this::toDto);
    }
    
    // 사용자 생성
    public Mono<UserDto> create(UserDto dto) {
        return userRepository.save(toEntity(dto))
            .map(this::toDto);
    }
    
    // 사용자 수정
    public Mono<UserDto> update(Long id, UserDto dto) {
        return userRepository.findById(id)
            .flatMap(user -> {
                user.setName(dto.getName());
                user.setEmail(dto.getEmail());
                user.setAge(dto.getAge());
                return userRepository.save(user);
            })
            .map(this::toDto);
    }
    
    // 사용자 삭제
    public Mono<Void> delete(Long id) {
        return userRepository.deleteById(id);
    }
    
    // DTO 변환
    private UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }
    
    private User toEntity(UserDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setAge(dto.getAge());
        return user;
    }
}
```

### 3. 어노테이션 기반 컨트롤러

**UserController.java**
```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    // 전체 사용자 조회
    @GetMapping
    public Flux<UserDto> getAllUsers() {
        return userService.findAll();
    }
    
    // 단일 사용자 조회
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> 
                Mono.just(ResponseEntity.notFound().build())
            );
    }
    
    // 사용자 생성
    @PostMapping
    public Mono<ResponseEntity<UserDto>> createUser(@RequestBody UserDto dto) {
        return userService.create(dto)
            .map(saved -> ResponseEntity.status(HttpStatus.CREATED).body(saved));
    }
    
    // 사용자 수정
    @PutMapping("/{id}")
    public Mono<ResponseEntity<UserDto>> updateUser(
        @PathVariable Long id,
        @RequestBody UserDto dto) {
        return userService.update(id, dto)
            .map(ResponseEntity::ok)
            .onErrorResume(e -> 
                Mono.just(ResponseEntity.notFound().build())
            );
    }
    
    // 사용자 삭제
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteUser(@PathVariable Long id) {
        return userService.delete(id)
            .then(Mono.just(ResponseEntity.noContent().<Void>build()))
            .onErrorResume(e -> 
                Mono.just(ResponseEntity.notFound().build())
            );
    }
}
```

### 4. Redis 캐싱

**PostController.java**
```java
@RestController
@RequestMapping("/api/posts")
public class PostController {
    
    private final PostService postService;
    private final ReactiveRedisTemplate<String, Post> redisTemplate;
    
    private static final String CACHE_PREFIX = "post:";
    
    @GetMapping("/{id}")
    public Mono<Post> getPost(@PathVariable Long id) {
        String cacheKey = CACHE_PREFIX + id;
        
        // Redis에서 조회 시도
        return redisTemplate.opsForValue()
            .get(cacheKey)
            .switchIfEmpty(
                // 캐시 미스 시 DB에서 조회
                postService.findById(id)
                    .flatMap(post -> 
                        // 캐시에 저장
                        redisTemplate.opsForValue()
                            .set(cacheKey, post, Duration.ofMinutes(10))
                            .then(Mono.just(post))
                    )
            );
    }
    
    @PostMapping
    public Mono<Post> createPost(@RequestBody Post post) {
        return postService.save(post)
            .flatMap(saved -> {
                // 새 게시글을 캐시에 저장
                String cacheKey = CACHE_PREFIX + saved.getId();
                return redisTemplate.opsForValue()
                    .set(cacheKey, saved, Duration.ofMinutes(10))
                    .then(Mono.just(saved));
            });
    }
}
```

### 5. 에러 핸들링

```java
@RestController
public class ExampleController {
    
    private final UserService userService;
    
    @GetMapping("/user/{id}")
    public Mono<ResponseEntity<UserDto>> getUser(@PathVariable Long id) {
        return userService.findById(id)
            .map(ResponseEntity::ok)
            .onErrorResume(IllegalArgumentException.class, e -> 
                Mono.just(ResponseEntity.badRequest().build())
            )
            .onErrorResume(e -> 
                Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build())
            );
    }
}
```

---

## 🔧 설정 파일

### application.yml

```yaml
server:
  port: 8080
  servlet:
    context-path: /

spring:
  application:
    name: spring-webflux-practice
  
  # R2DBC MySQL 설정
  r2dbc:
    url: r2dbc:mysql://localhost:3308/r2dbcpractice
    username: root
    password: password
    pool:
      max-acquire-time: 2000ms
      max-idle-time: 30m
      max-lifetime: 1h
      max-size: 20
      min-idle: 5
  
  # Redis 설정
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      timeout: 2000ms
      lettuce:
        pool:
          max-active: 8
          max-idle: 4
          min-idle: 2
  
  # JPA/R2DBC 설정
  jpa:
    database-platform: org.hibernate.dialect.MySQL8Dialect
    show-sql: false
  
  # WebFlux 설정
  webflux:
    base-path: /api
    max-in-memory-buffer-size: 512KB

# 로깅 설정
logging:
  level:
    root: INFO
    com.laze.springwebfluxpractice: DEBUG
    io.r2dbc: DEBUG
    io.lettuce.core: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"

# 액추에이터 설정
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
  endpoint:
    health:
      show-details: when-authorized
```

---

## 🧪 테스트

### StepVerifier 활용

```java
@Test
public void testFindAll() {
    Flux<User> result = userService.findAll();
    
    StepVerifier.create(result)
        .expectNextCount(3)
        .verifyComplete();
}

@Test
public void testFindById() {
    Mono<User> result = userService.findById(1L);
    
    StepVerifier.create(result)
        .assertNext(user -> {
            assertEquals(1L, user.getId());
            assertEquals("John", user.getName());
        })
        .verifyComplete();
}
```

### WebTestClient 활용

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
    
    @Autowired
    private WebTestClient webTestClient;
    
    @Test
    public void testGetAllUsers() {
        webTestClient.get()
            .uri("/api/users")
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UserDto.class)
            .hasSize(3);
    }
}
```

---

## 📊 성능 비교

### 동시성 처리 능력

| 패턴 | 동시 요청 | 스레드 수 | 메모리 |
|------|----------|---------|-------|
| **서블릿 (MVC)** | 1,000 | 1,000+ | 높음 |
| **WebFlux** | 10,000+ | 50-100 | 낮음 |

### 응답 시간 개선

```
요청 처리 시간: 100ms (DB I/O)

서블릿: 1,000개 요청 × 100ms = 100,000ms (100초)
WebFlux: 1,000개 요청 ÷ 10 (동시성) × 100ms = 10,000ms (10초)

→ 약 10배 빠른 처리 ⚡
```

---

## 📖 API 엔드포인트

### 사용자 API

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| **GET** | `/api/users` | 전체 사용자 조회 |
| **GET** | `/api/users/{id}` | 단일 사용자 조회 |
| **POST** | `/api/users` | 사용자 생성 |
| **PUT** | `/api/users/{id}` | 사용자 수정 |
| **DELETE** | `/api/users/{id}` | 사용자 삭제 |

### 게시글 API

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| **GET** | `/api/posts` | 전체 게시글 조회 |
| **GET** | `/api/posts/{id}` | 단일 게시글 조회 (캐시) |
| **POST** | `/api/posts` | 게시글 생성 |
| **PUT** | `/api/posts/{id}` | 게시글 수정 |
| **DELETE** | `/api/posts/{id}` | 게시글 삭제 |

### 함수형 라우트API

| 메서드 | 엔드포인트 | 설명 |
|--------|-----------|------|
| **GET** | `/sample` | 기본 샘플 |
| **POST** | `/sample/create` | 데이터 생성 |
| **GET** | `/sample/{id}` | 상세 조회 |

---

## 🔍 디버깅

### 리액티브 스트림 추적

```java
userService.findAll()
    .doOnNext(user -> System.out.println("User: " + user.getName()))
    .doOnError(error -> System.err.println("Error: " + error.getMessage()))
    .doFinally(signal -> System.out.println("Finished: " + signal));
```

### 로그 활성화

```yaml
logging:
  level:
    io.r2dbc.mysql: DEBUG
    org.springframework.data.r2dbc: DEBUG
    reactor.netty: DEBUG
```

---

## 📚 학습 경로

### 1단계: 기본 개념 (1주)
- [ ] Mono/Flux 이해
- [ ] 함수형 라우팅 작성
- [ ] 기본 CRUD 구현

### 2단계: 데이터 접근 (2주)
- [ ] R2DBC 저장소 작성
- [ ] 쿼리 메서드 사용
- [ ] 트랜잭션 관리

### 3단계: 고급 주제 (2주)
- [ ] Redis 캐싱 통합
- [ ] WebClient 사용
- [ ] 에러 핸들링

### 4단계: 최적화 (1주)
- [ ] 성능 튜닝
- [ ] 모니터링
- [ ] 프로덕션 배포

---

## 🚀 배포

### Docker 배포

```dockerfile
FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/spring-webflux-practice-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker Compose

```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
    environment:
      SPRING_R2DBC_URL: r2dbc:mysql://mysql:3306/r2dbcpractice
      SPRING_REDIS_HOST: redis

  mysql:
    image: mysql:8.0
    ports:
      - "3308:3306"
    environment:
      MYSQL_ROOT_PASSWORD: password
      MYSQL_DATABASE: r2dbcpractice
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:latest
    ports:
      - "6379:6379"

volumes:
  mysql_data:
```

---

## 📝 의존성

### 핵심 라이브러리

```gradle
// Spring Boot WebFlux (리액티브 웹)
implementation 'org.springframework.boot:spring-boot-starter-webflux'

// R2DBC (리액티브 데이터 접근)
implementation 'org.springframework.boot:spring-boot-starter-data-r2dbc'
implementation 'io.asyncer:r2dbc-mysql'

// Redis (반응형)
implementation 'org.springframework.boot:spring-boot-starter-data-redis-reactive'

// Validation (입력 검증)
implementation 'org.springframework.boot:spring-boot-starter-validation'

// Actuator (모니터링)
implementation 'org.springframework.boot:spring-boot-starter-actuator'

// Lombok (보일러플레이트 제거)
compileOnly 'org.projectlombok:lombok'
annotationProcessor 'org.projectlombok:lombok'

// 테스트
testImplementation 'org.springframework.boot:spring-boot-starter-test'
testImplementation 'io.projectreactor:reactor-test'
```

---

## 🎓 추가 학습 자료

### 공식 문서
- [Spring WebFlux](https://spring.io/projects/spring-webflux)
- [Project Reactor](https://projectreactor.io/)
- [R2DBC](https://r2dbc.io/)

### 실습 권장 항목
1. **Backpressure 처리** - 버퍼 오버플로우 관리
2. **Hot vs Cold Publisher** - 구독 시점에 따른 동작
3. **Scheduling** - 스레드 풀 최적화
4. **Circuit Breaker** - 장애 전파 방지

---

## 🐛 일반적인 문제 해결

### 1. R2DBC 연결 실패

```bash
# MySQL 포트 확인
netstat -an | grep 3308

# MySQL 컨테이너 로그 확인
docker logs mysql
```

### 2. 메모리 누수

```java
// ❌ 잘못된 구독 방식
flux.subscribe(
    item -> System.out.println(item),
    error -> System.err.println(error)
);

// ✅ 올바른 구독 방식
Disposable disposable = flux.subscribe(...);
disposable.dispose(); // 수동 정리
```

### 3. 블로킹 감지

```java
// ❌ 블로킹 작업
List<User> users = userRepository.findAll().collectList().block();

// ✅ 논블로킹
userRepository.findAll().collectList()
    .subscribe(users -> System.out.println(users));
```

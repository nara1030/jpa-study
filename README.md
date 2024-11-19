### 요구사항
```text
부서원 목록 조회 API 만들어보고자 한다.
단, 부서장 권한을 갖고 있는 사용자에 한해서 호출이 가능하다.

----
몇 가지 간단한 테스트를 해보고자 작성한 코드인데,
먼저 JPA만 이용해 조회해본 후 시큐리티도 같이 연동해서 조회 테스트해볼 생각이다.
그리고 이후 현재 스크립트로 들어가 있는 데이터 삽입, 수정 테스트 예정이다.

1O. String 타입으로 넘어오는 데이터를 DTO에서 여러 타입으로 변환해서 잘 받는지(반대의 경우도) 여부 확인
   - 예를 들어 호출 시 넘어오는 데이터는 {"userId": "user1", "password": "1234", "role": "LEADER", "requestTime": "20241116 11:34:32"}와 같이 단순 문자열이지만,
     서버에서는 Role 타입(Enum), LocalDate 타입 등으로 잘 받아져야 한다.
     (Role은 DB로 관리하나, DTO 테스트 위해 Enum 임시 작성한다.)
   - 이후 이를 엔티티로 변경하여 DB 로그에 기록한다.
2△. JPA M:N 관계를 두 가지 방법으로 조회 API 테스트
   - 두 방법으로 설정
     1O) 자바에서도 중간 엔티티를 하나 생성해서 풀어보기
     2X) 자바에서는 M:N 관계로, RDB에서는 M:1:N 관계로 풀어보기
3X. 조회 API에 시큐리티를 붙여본다.
   - API 엔드포인트에 LEADER ROLE 권한을 부여해서, 부서원 조회 메소드를 단순화한다.
     즉, 해당 메소드에 포함되어 있는 사용자의 ROLE를 검증하는 로직을 제거할 수 있다.
   - 엔드 포인트가 아니라 메소드 레벨에서 권한을 부여해줄 수도 있다.
     @PreAuthorize, @Secured 등을 사용할 수 있다.
   - 조금 번거로운 면이 있다. 시큐리티가 사용자 정보를 인식하도록
     UserDetails, UserDetailsService를 만들어줘야 해서 나중으로..
4△. 생성/수정/삭제 테스트
   - RDB 테이블 생성은 스크립트로, 이후 DML만 JPA로 API 테스트 해본다.
   - RESTfUl API 사용
     즉 URI를 통해 리소스 명시하고, HTTP Method 통해 해당 자원에 대한 CRUD OPERATION 적용
   - SQL LOGGER 설정
5X. 응답 형식 공통화(예외 포함)

```

### 정리
```text
1. String 타입으로 넘겨주는 데이터가 Enum과 연동되려면 Enum 상수와 일치해야 한다.
   즉, 이 경우 LEADER와 같이 대문자여야 한다.
   만약 클라이언트로부터 소문자 문자열(ex. leader)이 들어온다면, 커스텀 JsonDeserializer를 사용해 매핑해주어야 한다[A][B].
2. 1과 비슷하게 String 타입으로 넘어오는 날짜을 LocalDateTime 타입으로 받으려면,
   Jackson 라이브러리나 다른 JSON 라이브러리를 사용해야 한다.
   일반적으로 DTO에 @JsonFormat을 사용한다.
3. com.fasterxml.jackson.databind.exc.InvalidDefinitionException:
   No serializer found for class org.hibernate.proxy.pojo.bytebuddy.ByteBuddyInterceptor and no properties discovered to create BeanSerializer 
   2번 API 테스트로 UserResponse를 응답으로 반환하는 와중에 위 에러가 발생했다.
   결론적으로 아직 해결은 못한 상황인데, 원인으로 두 가지를 추측했다.
   첫째, 서비스단에서 엔티티를 반환하고 컨트롤러에서 DTO로 변경 후 반환했다.
     즉 트랜잭션 범위를 벗어나서 영속성 컨텍스트를 사용했기에 조회 불가 필드가 있을 것이다.
     따라서 엔티티를 DTO로 반환하는 로직을 서비스 레이어로 이동시켰다.
     참고로 이와 관련된 JPA 설정으로 OPEN-IN-VIEW라는 게 있는 거 같긴 하다.
   둘째, 필드에 데이터는 세팅이 되었으나, 직렬화하는 과정에서 문제를 일으킨다.
     따라서 UserResponse의 deptMembers 필드의 Getter 메소드 리턴 타입을
     List<User>에서 List<String>으로 변경해보았는데 동작했다.
     이를 List<User>로 반환할 수 있도록 고민해봐야겠다..
4. 생성/수정의 경우 변경사항 하나와 엔티티 연관관계 생성시 고려사항 하나가 있었다.
   4-1. UserRquest DTO를 변경해주었다.
        사용자 부서코드 및 권한을 받아야 했다.
        부서 코드는 문자열로 받은 후 DTO에서 GETTER 호출시 ENUM 타입을 반환하도록 했다.
        권한의 경우 배열(Role[])로 받도록 변경해주었다.
   4-2. 회원가입의 경우, User 엔티티에 부서(Dept) 정보 및 권한(UserRole) 정보를 설정해주어야 한다.
        기존 코드가 다음과 같았다[C].
        부서 테이블(Dept)이나 권한 테이블(Role)이 코드성 테이블이라 삽입이 필요가 없긴 하지만,
        User 엔티티에서 참조시 DB에 존재하는지 여부를 확인할 필요가 있다고 생각했다.
        따라서 현재 코드처럼 수정하였다[D].

----
A. Deserializer는 직렬화된 데이터, 즉 JSON과 같은 걸 객체로 변환하는 역할(언마셜링)을 한다.
   내 경우 Jackson 라이브러리를 포함해주지 않았는데 임포트가 되어 좀 의아했는데,
   스프링 부트나 스프링 MVC에는 Jackson 라이브러리가 포함되어 있다고 한다.
B. A에서 언급한대로 스프링 MVC에서 @RestController 혹은 @Controller와 @ResponseBody를 사용할 때,
   별도의 설정 없이 리턴 객체를 JSON으로 마셜링해준다.
   즉 컨트롤러가 리턴하는 객체는 Spring MVC 내부 설정된 여러 개의 HttpMessageConverter 구현체 중,
   지정된 MediaType에 따라 선택되어 마셜링된다.
   흔히 접한 MappingJackson2HttpMessageConverter가 이 역할을 한다.
   (cf. MappingJackson2JsonView)
C. 최초 작성했던 회원 가입 로직이다.
   @Transactional
   public void create(UserRequest userRequest) {
       // 1. User 엔티티 생성(Role 정보는 연관 테이블인 UserRole에서 관리)
       User user = new User();
       user.setId(userRequest.getUserId());
       user.setPassword(userRequest.getPassword());
       Dept deptEntity = new Dept(userRequest.getDept().getCode(), userRequest.getDept().getName());
       user.setDept(deptEntity);
       userRepository.save(user);

       // 2. UserRole 엔티티 생성
       for (org.among.jpatest.common.Role role : userRequest.getRoles()) {
           UserRole userRole = new UserRole();
           userRole.setId(UUID.randomUUID().toString());  // 임시
           userRole.setUser(user);
           Role roleEntity = new Role();
           roleEntity.setId(role.getCode());
           roleEntity.setName(role.getName());
           userRole.setRole(roleEntity);

           userRoleRepository.save(userRole);
       }
   }
D. 수정한 회원 가입 로직이다.
   @Transactional
   public void create(UserRequest userRequest) {
       // 1. User 엔티티 생성(Role 정보는 연관 테이블인 UserRole에서 관리)
       User user = new User();
       user.setId(userRequest.getUserId());
       user.setName(userRequest.getName());
       user.setPassword(userRequest.getPassword());
       // 부서 정보 조회
       Dept dept = deptRepository.findById(userRequest.getDept().getCode())
               .orElseThrow(() -> new RuntimeException("Dept not found with code: " + userRequest.getDept().getCode()));
       user.setDept(dept);
       userRepository.save(user);

       // 2. UserRole 엔티티 생성
       for (org.among.jpatest.common.Role role : userRequest.getRoles()) {
           UserRole userRole = new UserRole();
           userRole.setId(UUID.randomUUID().toString());  // 임시
           userRole.setUser(user);
           Role roleEntity = roleRepository.findById(role.getCode())
                   .orElseThrow(() -> new RuntimeException("Role not found with code: " + role.getCode()));
           userRole.setRole(roleEntity);
           userRoleRepository.save(userRole);
       }
   }
```



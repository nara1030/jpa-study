package org.among.jpatest.api;

import org.among.jpatest.common.Dept;
import org.among.jpatest.common.Role;
import org.among.jpatest.dto.UserRequest;
import org.among.jpatest.dto.UserResponse;
import org.among.jpatest.entity.User;
import org.among.jpatest.entity.UserRole;
import org.among.jpatest.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {
    @Autowired
    private UserService userService;

    @PostMapping("/dept/members")
    public ResponseEntity<UserResponse> getDeptMembers(@RequestBody UserRequest userRequest) {
        // LOG: (ID: ) 접근 요청이 있습니다.
        System.out.printf("(ID: %s) 접근 요청이 있습니다.%n", userRequest.getUserId());

        // 1. 조회 로직
        // 객체 그래프 탐색 테스트: User -> Dept, User -> Role
        User user = userService.authentication(userRequest.getUserId(), userRequest.getPassword());
//        System.out.println("부서 ID: " + user.getDept().getId());
//        System.out.println("부서 상수: " + Dept.getDeptById(user.getDept().getId()).name());
//        System.out.println("부서명: " + Dept.getDeptById(user.getDept().getId()).getName());

        // LOG: ** 부서 **님으로 확인되었습니다.
        System.out.printf("%s 부서 %s 님으로 확인되었습니다.%n", Dept.getDeptById(user.getDept().getId()).getName(), userRequest.getUserId());

        // 유저 권한 확인
//        user.getUserRoles()
//                .stream()
//                .map(UserRole::getRole)
//                .forEach(role -> System.out.println(Role.getRoleById(role.getId())));

        // 해당 롤 아니거나, 롤 없는 경우 예외 처리

        // 2. DB 로그 기록


        // 3. 권한 확인 후 부서원 조회
        // 3-1. JPA 쿼리 조회(M:1:N)
        UserResponse userResponse = userService.findByDeptId(user);

        // LOG: ** 부서 부서원 목록은 다음과 같습니다.
        System.out.printf("%s 부서 부서원 목록은 다음과 같습니다.%n", Dept.getDeptById(user.getDept().getId()).getName());
//        userResponse.getDeptMembers()
//                .forEach(member -> System.out.print(member.getId() + " : "));

        // 3-2. JPA 쿼리 조회(M:N)


        // 3-3. 시큐리티 연동

        // 4. 리턴
        return ResponseEntity.ok(userResponse);
    }

    // 회원 가입 로직

    // 회원정보 수정 로직

    // 회원 탈퇴 로직
}
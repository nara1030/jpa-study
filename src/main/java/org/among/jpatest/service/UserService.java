package org.among.jpatest.service;

import org.among.jpatest.dto.UserRequest;
import org.among.jpatest.dto.UserResponse;
import org.among.jpatest.entity.Dept;
import org.among.jpatest.entity.Role;
import org.among.jpatest.entity.User;
import org.among.jpatest.entity.UserRole;
import org.among.jpatest.repository.DeptRepository;
import org.among.jpatest.repository.RoleRepository;
import org.among.jpatest.repository.UserRepository;
import org.among.jpatest.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DeptRepository deptRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 시큐리티 기능 사용않은 기본 자격증명 검증 메소드
    public User authentication(String userId, String password) {
        // 사용자 존재 여부 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        // 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password for user: " + userId);
        }

        return user;
    }

    // 팀장의 경우, 부서원 목록 조회
    public UserResponse findByDeptId(User user) {
        List<User> members = findByDeptId(user.getDept().getId(), user.getUserRoles());
        return new UserResponse(user, members);
    }

    // UserEntity로 받는 것보다 파라미터를 구체적으로 명시하는 게 의도가 좋을 거 같은데,
    // Entity를 받는 건 조금 고민해봐야 할 거 같다.
    private List<User> findByDeptId(String deptId, Set<UserRole> userRoles) {
        boolean isLeader = userRoles.stream()
                .anyMatch(userRole -> isLeader(userRole.getRole())); // 하나라도 존재하면 true 반환

        if (!isLeader) {
            throw new RuntimeException("Not a leader in the deptId: " + deptId);
        }

        return userRepository.findByDeptId(deptId);
    }

    private boolean isLeader(Role role) {
        return "02".equals(role.getId()); // 02(LEADER)
    }

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

    @Transactional
    public void update(UserRequest userRequest) {
        // 전제조건
        // A. 회원 정보 존재(로그인 전제)
        // B. userRequest 모든 필드 들어온다고 가정(변경 필드만 들어온다고 가정할 수도 있음)

        User user = userRepository.findById(userRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userRequest.getUserId()));

        // 1. 비밀번호 수정
        if (!user.getPassword().equals(userRequest.getPassword())) {
            user.setPassword(userRequest.getPassword());
        }

        // 2. 부서 수정
        if (!user.getDept().getId().equals(userRequest.getDept().getCode())) {
            Dept dept = new Dept(userRequest.getDept().getCode(), userRequest.getDept().getName());
            user.setDept(dept);
        }

        // 3. 역할(권한) 수정(비교가 어려워서 비어있지 않으면 갱신)
        if (userRequest.getRoles() != null && userRequest.getRoles().length > 0) {
            // 기존 역할 삭제
            userRoleRepository.deleteByUser(user);

            // 새로운 역할 추가
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

        userRepository.save(user); // 꼭 필요한지 모르겠지만..
    }

    @Transactional
    public void delete(UserRequest userRequest) {
        // 전제조건
        // A. 회원 정보 존재(로그인 전제)

        User user = userRepository.findById(userRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userRequest.getUserId()));

        // 1. 해당 User의 UserRole 삭제
        userRoleRepository.deleteByUser(user);

        // 2. User 삭제
        userRepository.delete(user);
    }

}

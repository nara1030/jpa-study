package org.among.jpatest.service;

import org.among.jpatest.dto.UserResponse;
import org.among.jpatest.entity.Role;
import org.among.jpatest.entity.User;
import org.among.jpatest.entity.UserRole;
import org.among.jpatest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

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
}

package org.among.jpatest.dto;

import org.among.jpatest.common.Dept;
import org.among.jpatest.common.Role;
import org.among.jpatest.entity.User;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserResponse {
    private String userId;

    private Set<Role> roles;

    private Dept dept;

    private List<User> deptMembers;

    // 변경 가능성이 많은 쪽(UserResponse)에서 없는 쪽(UserEntity)으로 의존성 방향 설정
    public UserResponse(User user, List<User> deptMembers) {
        this.userId = user.getId();
        this.roles = user.getUserRoles()
                .stream()
                .map(userRole -> Role.getRoleById(userRole.getRole().getId()))
                .collect(Collectors.toSet());
        this.dept = Dept.getDeptById(user.getDept().getId());
        this.deptMembers = deptMembers;
    }

    public String getUserId() {
        return userId;
    }

    public Dept getDept() {
        return dept;
    }

    public Set<Role> getRoles() {
        return roles;
    }

//    public List<User> getDeptMembers() {
//        return deptMembers;
//    }
    public List<String> getDeptMembers() {
        return deptMembers.stream()
                .map(User::getId)
                .toList();
    }
}

package org.among.jpatest.repository;

import org.among.jpatest.entity.User;
import org.among.jpatest.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
    // User와 연관된 UserRole 삭제하는 메소드
    void deleteByUser(User user);
}

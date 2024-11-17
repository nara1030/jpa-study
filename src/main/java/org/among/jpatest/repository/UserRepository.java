package org.among.jpatest.repository;

import org.among.jpatest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    // 사용자ID로 사용자 조회

    // 사용자가 팀장이라면(서비스 로직 처리), 해당 부서 팀원 목록 조회
    List<User> findByDeptId(String deptId);
}

package org.among.jpatest.repository;

import org.among.jpatest.entity.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRepository extends JpaRepository<Dept, String> {
}

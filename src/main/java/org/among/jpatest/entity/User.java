package org.among.jpatest.entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
public class User {

    @Id
    private String id;

    private String name;

    private String password;

    // User에서 Role 조회하려고 양방향 지정
    // mappedBy: UserRole 엔티티에서 User 관계 나타내는 필드명
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UserRole> roles;

    // name: USER 테이블에 존재하는 외래키 칼럼명
    // referencedColumnName: DEPT 테이블에서 외래키가 참조하는 칼럼명
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_code", referencedColumnName = "id")
    private Dept dept;

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public Set<UserRole> getUserRoles() {
        return roles;
    }

    public Dept getDept() {
        return dept;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDept(Dept dept) {
        this.dept = dept;
    }

    public void setUserRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public void setName(String name) {
        this.name = name;
    }
}

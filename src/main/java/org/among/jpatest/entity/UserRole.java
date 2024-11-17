package org.among.jpatest.entity;

import jakarta.persistence.*;

@Entity
public class UserRole {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", role=" + role +
                '}';
    }
}

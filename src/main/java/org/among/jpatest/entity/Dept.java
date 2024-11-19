package org.among.jpatest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Dept {
    @Id
    private String id;

    private String name;

    public Dept(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Dept() {
    }

    public String getId() {
        return id;
    }
}

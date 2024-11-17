package org.among.jpatest.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Dept {
    @Id
    private String id;

    private String name;

    public String getId() {
        return id;
    }
}

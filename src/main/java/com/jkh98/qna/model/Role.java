package com.jkh98.qna.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ERole name;

    /*
     * Manually insert:
     *
     * INSERT INTO roles(name) VALUES('ROLE_USER');
     * INSERT INTO roles(name) VALUES('ROLE_MODERATOR');
     * INSERT INTO roles(name) VALUES('ROLE_ADMIN');
     *
     */
    public enum ERole {
        ROLE_USER,
        ROLE_MODERATOR,
        ROLE_ADMIN
    }
}
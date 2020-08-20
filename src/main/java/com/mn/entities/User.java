package com.mn.entities;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity(name = "user")
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "username", length = 30, nullable = false, unique = true)
    private String username;

    @Column(name = "password", length = 100, nullable = false)
    private String password;

    @Column(name = "role", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "salt", length = 16, nullable = false)
    private byte[] salt;
}

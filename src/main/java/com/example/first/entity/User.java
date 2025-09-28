package com.example.first.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String passwordResetToken;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime passwordResetTokenExpiryDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_users_access_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "access_group_id")
    )
    private Set<AccessGroup> accessGroups = new HashSet<>();
}

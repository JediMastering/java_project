package com.example.first.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_access_groups")
@Getter
@Setter
public class AccessGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_group_id")
    private Long accessGroupId;

    private String name;

    @OneToMany(mappedBy = "accessGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<AccessGroupPermission> permissions = new HashSet<>();
}
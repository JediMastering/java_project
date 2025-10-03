package com.application.erp.accessgroup.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import com.application.erp.shared.polymorphic.PolymorphicEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_access_groups")
@Getter
@Setter
public class AccessGroup implements PolymorphicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_group_id")
    private Long accessGroupId;

    @Override
    public Long getId() {
        return this.accessGroupId;
    }

    private String name;

    @OneToMany(mappedBy = "accessGroup", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<AccessGroupPermission> permissions = new HashSet<>();
}
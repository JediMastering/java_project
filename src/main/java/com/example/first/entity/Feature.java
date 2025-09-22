package com.example.first.entity;

import com.example.first.polymorphic.PolymorphicEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tb_features")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Feature implements PolymorphicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Feature parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Feature> children = new HashSet<>();

    // Flags para indicar quais ações são aplicáveis a esta feature
    private boolean hasView = false;
    private boolean hasCreate = false;
    private boolean hasEdit = false;
    private boolean hasDelete = false;

    public Feature(String name, Feature parent, boolean hasView, boolean hasCreate, boolean hasEdit, boolean hasDelete) {
        this.name = name;
        this.parent = parent;
        this.hasView = hasView;
        this.hasCreate = hasCreate;
        this.hasEdit = hasEdit;
        this.hasDelete = hasDelete;
    }
}

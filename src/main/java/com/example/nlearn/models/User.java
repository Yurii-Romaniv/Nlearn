package com.example.nlearn.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.AccessType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @AccessType(AccessType.Type.PROPERTY)
    private int id;

    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private TestSessionInfo testSessionInfo;

    @JsonIgnore
    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Group> groups = new HashSet<>();


    public void saveGroups() {
        groups.forEach(group -> group.addUser(this));
    }

    public void removeGroups(List<Group> groups) {
        groups.forEach(g -> {
            g.removeUser(this);
            this.groups.remove(g);
        });
    }

    @PreRemove
    private void removeGroupAssociations() {
        this.groups.forEach(group -> group.getUsers().remove(this));

    }
}

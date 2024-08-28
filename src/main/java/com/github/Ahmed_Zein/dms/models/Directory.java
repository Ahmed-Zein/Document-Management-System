package com.github.Ahmed_Zein.dms.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "directory")
public class Directory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "private_dir", nullable = false)
    private Boolean privateDir = false;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "work_space_id")
    private WorkSpace workSpace;

    @JsonIgnore
    @OneToMany(mappedBy = "directory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

}
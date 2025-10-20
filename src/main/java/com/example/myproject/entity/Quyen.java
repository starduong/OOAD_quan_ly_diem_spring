package com.example.myproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "Quyen")
public class Quyen {
    @Id
    @Column(name = "MaQuyen")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer maQuyen;

    @Column(name = "TenQuyen", nullable = false, length = 50)
    private String tenQuyen;

    @Column(name = "MoTaQuyen", length = 50)
    private String moTaQuyen;

}
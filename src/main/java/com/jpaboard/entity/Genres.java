package com.jpaboard.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "GENRES")
@Data
public class Genres {

    @Column(name = "GENRENAME")
    private String genrename;

    @Id
    @Column(name = "GENRECODE")
    private String genrecode;
}

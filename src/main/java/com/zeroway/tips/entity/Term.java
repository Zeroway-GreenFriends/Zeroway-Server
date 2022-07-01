package com.zeroway.tips.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Term {

    @Id @GeneratedValue
    @Column(name = "term_id")
    private Long id;

    private String name;

    @Column(length = 1000)
    private String description;



}

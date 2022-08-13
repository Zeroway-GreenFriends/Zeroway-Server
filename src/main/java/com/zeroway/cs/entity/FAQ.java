package com.zeroway.cs.entity;


import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class FAQ {

    @Id @GeneratedValue
    @Column(name = "faq_id")
    private Long id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false, length = 1000)
    private String answer;
}

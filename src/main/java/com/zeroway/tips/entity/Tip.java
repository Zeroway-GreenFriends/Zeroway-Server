package com.zeroway.tips.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@ToString
@Getter
public class Tip {

    @Id @GeneratedValue
    @Column(name = "tip_id")
    private Long id;

    private String title;

    @Column(length = 1000)
    private String content;

}

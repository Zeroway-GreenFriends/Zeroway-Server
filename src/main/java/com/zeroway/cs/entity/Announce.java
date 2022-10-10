package com.zeroway.cs.entity;

import com.zeroway.common.BaseEntity;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class Announce extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "announce_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    public Announce(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

package com.zeroway.community.entity;

import javax.persistence.*;

@Entity
public class PostImage {

    @Id @GeneratedValue
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String url;
}

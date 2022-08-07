package com.zeroway.community.entity;

import com.zeroway.common.BaseEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class PostImage extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String url;

    @Builder
    public PostImage(Post post, String url) {
        this.post = post;
        this.url = url;
    }
}

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

    private Long postId;
    @Column(nullable = false)
    private String url;

    @Builder
    public PostImage(Long postId, String url) {
        this.postId = postId;
        this.url = url;
    }
}

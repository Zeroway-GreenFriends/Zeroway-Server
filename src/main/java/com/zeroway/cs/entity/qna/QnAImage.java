package com.zeroway.cs.entity.qna;

import com.zeroway.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class QnAImage extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "qna_image_id")
    private Long id;

    private Long qna_id;

    @Column(nullable = false)
    private String url;

    @Builder
    public QnAImage(Long qna_id, String url) {
        this.qna_id = qna_id;
        this.url = url;
    }
}

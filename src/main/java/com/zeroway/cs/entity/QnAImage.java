package com.zeroway.cs.entity;

import com.zeroway.common.BaseEntity;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class QnAImage extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "qna_image_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "qna_id")
    private QnA qna;

    @Column(nullable = false)
    private String url;

}

package com.zeroway.cs.entity;

import com.zeroway.common.BaseEntity;
import com.zeroway.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class QnA extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "qna_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type", length=50)
    private TypeOfQuestion typeOfQuestion;

    @Column(nullable = false, length = 1000)
    private String question;

    @Column(length = 1000)
    private String answer;

    private Long userId;

    @Builder
    public QnA(TypeOfQuestion typeOfQuestion, String question, Long userId) {
        this.typeOfQuestion = typeOfQuestion;
        this.question = question;
        this.userId = userId;
    }
}

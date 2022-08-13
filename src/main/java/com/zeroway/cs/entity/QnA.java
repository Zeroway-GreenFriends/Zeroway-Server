package com.zeroway.cs.entity;

import com.zeroway.common.BaseEntity;
import com.zeroway.user.entity.User;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}

package com.zeroway.cs.entity.report;

import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Report {

    @Id @GeneratedValue
    @Column(name = "report_id")
    private long id;

    //신고한 사람
    @Column(nullable = false)
    private long userId;

    //신고 카테고리
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoryOfReport categoryOfReport;

    //신고할 글 ID
    @Column(nullable = false)
    private long targetId;

    //신고 유형
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeOfReport typeOfReport;

    //신고 확인 여부
    @ColumnDefault("false")
    private boolean check;

    @Builder
    public Report(long userId, CategoryOfReport categoryOfReport, long targetId, TypeOfReport typeOfReport) {
        this.userId = userId;
        this.categoryOfReport = categoryOfReport;
        this.targetId = targetId;
        this.typeOfReport = typeOfReport;
    }
}

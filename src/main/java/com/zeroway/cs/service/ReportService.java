package com.zeroway.cs.service;

import com.zeroway.common.BaseException;
import com.zeroway.community.dto.ReportReq;
import com.zeroway.cs.entity.report.CategoryOfReport;
import com.zeroway.cs.entity.report.Report;
import com.zeroway.cs.entity.report.TypeOfReport;
import com.zeroway.cs.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;
import static com.zeroway.common.BaseResponseStatus.INVALID_REPORT_TYPE;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    // 신고하기
    public void reportTarget(Long userId, CategoryOfReport category, ReportReq reportReq) throws BaseException{
        try {
            TypeOfReport type = TypeOfReport.enumOf(reportReq.getType());
            if(type == null) {throw new BaseException(INVALID_REPORT_TYPE);}
            reportRepository.save(Report.builder()
                    .userId(userId)
                    .category(category)
                    .targetId(reportReq.getTargetId())
                    .type(type).build());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
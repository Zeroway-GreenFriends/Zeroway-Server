package com.zeroway.information.service;

import com.zeroway.common.BaseException;
import com.zeroway.information.dto.InfoRes;
import com.zeroway.information.repository.TermsOfUseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zeroway.common.BaseResponseStatus.REQUEST_ERROR;

@Service
@RequiredArgsConstructor
public class TermsOfUseService {

    private final TermsOfUseRepository repository;

    // 가장 최신 서비스 이용약관 조회
    public InfoRes getLatestTermsOfUse(boolean url) {
        InfoRes infoRes = new InfoRes(repository.findFirstByOrderByPublishedAtDesc());
        if (url) {
            infoRes.setContent(null);
        } else {
            infoRes.setUrl(null);
        }
        return infoRes;
    }

    // 특정 버전의 서비스 이용약관 조회
    public InfoRes getTermsOfUse(Integer id) throws BaseException {
        return new InfoRes(repository.findById(id)
                .orElseThrow(() -> new BaseException(REQUEST_ERROR)));
    }

}

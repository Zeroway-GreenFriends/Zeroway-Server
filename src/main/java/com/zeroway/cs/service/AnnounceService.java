package com.zeroway.cs.service;

import com.zeroway.common.BaseException;
import com.zeroway.cs.dto.AnnounceListRes;
import com.zeroway.cs.dto.AnnounceRes;
import com.zeroway.cs.entity.Announce;
import com.zeroway.cs.repository.AnnounceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;
import static com.zeroway.common.BaseResponseStatus.INVALID_ANNOUNCE_ID;

@Service
@RequiredArgsConstructor
public class AnnounceService {

    private final AnnounceRepository announceRepository;

    // 공지사항 전체 조회
    public List<AnnounceListRes> getAnnounceList() throws BaseException {
        try {
            List<Announce> announceList = announceRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
            return announceList.stream()
                    .map(announce -> new AnnounceListRes(announce.getId(), announce.getTitle(), announce.getCreatedAt()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 공지사항 상세 조회
    public AnnounceRes getAnnounce(Long announceId) throws BaseException{
        try {
            Optional<Announce> findAnnounce = announceRepository.findById(announceId);
            if(findAnnounce.isEmpty()) {
                throw new BaseException(INVALID_ANNOUNCE_ID);
            }
            else return new AnnounceRes(findAnnounce.get().getTitle(), findAnnounce.get().getContent(), findAnnounce.get().getCreatedAt());
        } catch (BaseException e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

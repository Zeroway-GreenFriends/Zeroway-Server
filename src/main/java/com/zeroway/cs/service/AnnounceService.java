package com.zeroway.cs.service;

import com.zeroway.common.BaseException;
import com.zeroway.cs.dto.AnnounceListRes;
import com.zeroway.cs.entity.Announce;
import com.zeroway.cs.repository.AnnounceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.zeroway.common.BaseResponseStatus.DATABASE_ERROR;

@Service
@RequiredArgsConstructor
public class AnnounceService {

    private final AnnounceRepository announceRepository;

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
}

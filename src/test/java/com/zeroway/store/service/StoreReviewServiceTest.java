package com.zeroway.store.service;

import com.zeroway.challenge.repository.LevelRepository;
import com.zeroway.store.dto.CreateReviewReq;
import com.zeroway.store.entity.Store;
import com.zeroway.store.entity.StoreReview;
import com.zeroway.store.repository.StoreRepository;
import com.zeroway.store.repository.StoreReviewRepository;
import com.zeroway.user.entity.ProviderType;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class StoreReviewServiceTest {
    @Autowired
    private StoreReviewService storeReviewService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private StoreReviewRepository storeReviewRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Test
    void 리뷰_작성() throws Exception {

        // given
        // 회원
        User user = userRepository.save(new User(1L, "a@test.com", "이름", ProviderType.GOOGLE, null, null, levelRepository.findById(1).get(), null));

        // 리뷰
        CreateReviewReq req = new CreateReviewReq();
        req.setStoreId(1L);
        req.setScore(4.5);
        req.setContent("정말 친절하고 좋아요!");

        Double oldScore = storeRepository.findById(1L).get().getScoreAvg();

        // when
        Long reviewId = storeReviewService.createReview(req, user.getId());

        // then
        StoreReview storeReview = storeReviewRepository.findById(reviewId).get();
        assertThat(storeReview.getStoreId()).isEqualTo(1L);
        assertThat(storeReview.getScore()).isEqualTo(4.5);
        assertThat(storeReview.getUserId()).isEqualTo(user.getId());
        assertThat(storeReview.getContent()).isEqualTo("정말 친절하고 좋아요!");

    }

}
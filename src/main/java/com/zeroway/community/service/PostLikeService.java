package com.zeroway.community.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.LikeListRes;
import com.zeroway.community.dto.UserInfo;
import com.zeroway.community.entity.PostLike;
import com.zeroway.community.repository.post.PostLikeRepository;
import com.zeroway.community.repository.post.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;

    /**
     * 좋아요 기능
     */
    @Transactional
    @Modifying(clearAutomatically = true)
    public void like(Long userId, Long postId, boolean like) throws BaseException {
        try {
            Optional<PostLike> optional = postLikeRepository.findByUserIdAndPostId(userId, postId);
            if (optional.isPresent()) {
                PostLike postLike = optional.get();
                if (like) {// 좋아요
                    postLike.setStatus(StatusType.ACTIVE);
                } else { // 좋아요 취소
                    postLike.setStatus(StatusType.INACTIVE);
                }
            }
            // PostLike 없는 경우, 좋아요만 수행 (좋아요 취소는 수행하지 않음)
            else if (like) postLikeRepository.save(PostLike.builder().userId(userId).postId(postId).build());
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 좋아요 목록 조회
    public LikeListRes getLikeList(Long postId) throws BaseException {
        try {
            List<UserInfo> postLikeList = postRepository.getPostLikeList(postId);
            return new LikeListRes(postLikeList);
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}

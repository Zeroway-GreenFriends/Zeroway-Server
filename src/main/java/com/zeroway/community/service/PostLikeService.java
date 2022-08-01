package com.zeroway.community.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.LikeListRes;
import com.zeroway.community.dto.UserInfo;
import com.zeroway.community.entity.Post;
import com.zeroway.community.entity.PostLike;
import com.zeroway.community.repository.post.PostLikeRepository;
import com.zeroway.community.repository.post.PostRepository;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final UserRepository userRepository;

    /**
     * 좋아요 기능
     * @return true 좋아요, false 좋아요 취소
     */
    @Transactional
    public boolean like(Long userId, Long postId) throws BaseException {
        try {
            Optional<PostLike> optional = postLikeRepository.findByUserIdAndPostId(userId, postId);
            if (optional.isPresent()) {
                PostLike like = optional.get();
                if(like.getStatus().equals(StatusType.ACTIVE)) {// 좋아요 취소
                    optional.get().setStatus(StatusType.INACTIVE);
                    return false;
                } else {
                    optional.get().setStatus(StatusType.ACTIVE); // 좋아요
                    return true;
                }
            }
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_JWT));
            Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(INVALID_POST_ID));
            postLikeRepository.save(PostLike.builder().user(user).post(post).build());
            return true; // 좋아요
        } catch (BaseException e) {
            throw e;
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

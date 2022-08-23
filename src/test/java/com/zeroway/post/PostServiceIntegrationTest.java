package com.zeroway.post;

import com.zeroway.challenge.repository.LevelRepository;
import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.GetPostByUserRes;
import com.zeroway.community.entity.Bookmark;
import com.zeroway.community.entity.Comment;
import com.zeroway.community.entity.Post;
import com.zeroway.community.entity.PostLike;
import com.zeroway.community.repository.comment.CommentRepository;
import com.zeroway.community.repository.post.BookmarkRepository;
import com.zeroway.community.repository.post.PostLikeRepository;
import com.zeroway.community.repository.post.PostRepository;
import com.zeroway.community.service.PostService;
import com.zeroway.user.entity.ProviderType;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import com.zeroway.utils.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class PostServiceIntegrationTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    PostLikeRepository postLikeRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    BookmarkRepository bookmarkRepository;
    @Autowired
    LevelRepository levelRepository;
    @Autowired
    JwtService jwtService;
    @Mock
    private MockHttpServletRequest request;

    private Long userId;

    private Long createRequestJWT() {
        Long userId = userRepository.findByEmail("testYeji@test.com").get().getId();
        String accessToken = jwtService.createAccessToken(userId);

        request = new MockHttpServletRequest();
        request.addHeader("Bearer", accessToken);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        return userId;
    }

    /**
     * 테스트용 유저 생성 및 게시물, 댓글 세팅
     * 글 4개 : 내가 작성 3개 (1, 2(삭제), 3), 다른 사람 작성 1개 (4)
     * 댓글 2개 : 1번 게시물 -> 2개 (유지 1, 삭제 1)
     * 스크랩 1개 : 3번 게시물 1개
     * 글 좋아요 4개 : 1, 2, 3, 4 (모든 게시물 1개씩)
     * 게시물 이미지 1개 : 3번 게시물 1개
     */
    @BeforeEach
    void setPostRepository() {
        this.userId = createRequestJWT();

        // 내가 쓴 글 1
        Post saveMe = postRepository.save(Post.builder()
                .userId(userId)
                .content("1번 게시물")
                .challenge(false)
                .build());

        commentRepository.save(Comment.builder()
                .postId(saveMe.getId())
                .userId(3L)
                .content("댓글~")
                .build());

        Comment deleteComment = commentRepository.save(Comment.builder()
                .postId(saveMe.getId())
                .userId(3L)
                .content("삭제된 댓글~")
                .build());
        deleteComment.setStatus(StatusType.INACTIVE);
        postLikeRepository.save(PostLike.builder()
                .userId(4L)
                .postId(saveMe.getId())
                .build());

        // 내가 쓴 글 2 : 삭제됨
        Post saveMe2 = postRepository.save(Post.builder()
                .userId(userId)
                .content("2번 게시물")
                .challenge(false)
                .build());
        saveMe2.setStatus(StatusType.INACTIVE);
        postLikeRepository.save(PostLike.builder()
                .userId(4L)
                .postId(saveMe2.getId())
                .build());

        // 내가 쓴 글 3
        Post saveMe3 = postRepository.save(Post.builder()
                .userId(this.userId)
                .content("3번 게시물")
                .challenge(false)
                .build());
        // 스크랩함
        bookmarkRepository.save(Bookmark.builder()
                .userId(this.userId)
                .postId(saveMe3.getId())
                .build());
        postLikeRepository.save(PostLike.builder()
                .userId(4L)
                .postId(saveMe3.getId())
                .build());
        postLikeRepository.save(PostLike.builder()
                .userId(4L)
                .postId(saveMe3.getId())
                .build());

        // 다른 사람이 쓴 글
        Post notMe = postRepository.save(Post.builder()
                .userId(2L)
                .content("4번 게시물")
                .challenge(false)
                .build());
        postLikeRepository.save(PostLike.builder()
                .userId(4L)
                .postId(notMe.getId())
                .build());

    }

    @DisplayName("내가 쓴 글 조회")
    @Test
    void post() throws BaseException {
        // given : userId
        User user = userRepository.findById(this.userId).get();

        List<GetPostByUserRes> getPostByUserRes = postService.getPostListByUser();

        assertThat(getPostByUserRes.get(0).getNickname()).isEqualTo(user.getNickname());
        assertThat(getPostByUserRes.get(0).getProfileImgUrl()).isEqualTo(user.getProfileImgUrl());
        // 1번 게시물
        assertThat(getPostByUserRes.get(0).getContent()).isEqualTo("1번 게시물");
        assertThat(getPostByUserRes.get(0).getLikeCount()).isEqualTo(1);
        assertThat(getPostByUserRes.get(0).getCommentCount()).isEqualTo(1);
        assertThat(getPostByUserRes.get(0).getImgCount()).isEqualTo(0);
        assertThat(getPostByUserRes.get(0).isScraped()).isFalse();

        // 2번 게시물
        assertThat(getPostByUserRes.get(1).getContent()).isEqualTo("2번 게시물");
        assertThat(getPostByUserRes.get(1).getLikeCount()).isEqualTo(1);
        assertThat(getPostByUserRes.get(1).getCommentCount()).isEqualTo(0);
        assertThat(getPostByUserRes.get(1).getImgCount()).isEqualTo(0);
        assertThat(getPostByUserRes.get(0).isScraped()).isFalse();

        // 3번 게시물
        assertThat(getPostByUserRes.get(2).getContent()).isEqualTo("3번 게시물");
        assertThat(getPostByUserRes.get(2).getLikeCount()).isEqualTo(1);
        assertThat(getPostByUserRes.get(2).getCommentCount()).isEqualTo(0);
        assertThat(getPostByUserRes.get(2).getImgCount()).isEqualTo(1);
        assertThat(getPostByUserRes.get(0).isScraped()).isTrue();

        // 4번 게시물
        assertThat(getPostByUserRes.get(3).getContent()).isEqualTo("4번 게시물");
        assertThat(getPostByUserRes.get(3).getLikeCount()).isEqualTo(1);
        assertThat(getPostByUserRes.get(3).getCommentCount()).isEqualTo(0);
        assertThat(getPostByUserRes.get(3).getImgCount()).isEqualTo(0);
        assertThat(getPostByUserRes.get(0).isScraped()).isFalse();
    }
}

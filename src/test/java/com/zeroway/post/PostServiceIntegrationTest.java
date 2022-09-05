package com.zeroway.post;

import com.zeroway.challenge.repository.LevelRepository;
import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.GetPostListByMypageRes;
import com.zeroway.community.entity.*;
import com.zeroway.community.repository.comment.CommentRepository;
import com.zeroway.community.repository.post.BookmarkRepository;
import com.zeroway.community.repository.post.PostImageRepository;
import com.zeroway.community.repository.post.PostLikeRepository;
import com.zeroway.community.repository.post.PostRepository;
import com.zeroway.community.service.PostService;
import com.zeroway.user.entity.ProviderType;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import com.zeroway.utils.JwtService;
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
    PostImageRepository postImageRepository;
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

    private User user;
    private Long userId;

    // test용 유저
    Optional<User> createUser() {
        return Optional.ofNullable(User.builder()
                .id(1L)
                .email("test")
                .nickname("예지테스트한다")
                .provider(ProviderType.valueOf("KAKAO"))
                .level(levelRepository.findById(1).get())
                .profileImgUrl("https://zeroway.s3.ap-northeast-2.amazonaws.com/userProfile/5b31ec29-854f-4744-85af-384797423fc3_IMG_20220810_162950.jpg")
                .build());
    }

    private Long createRequestJWT() {
        this.user = userRepository.save(createUser().get());
        String accessToken = jwtService.createAccessToken(user.getId());

        request = new MockHttpServletRequest();
        request.addHeader("Bearer", accessToken);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        return user.getId();
    }

    /**
     * 테스트용 유저 생성 및 게시물, 댓글 세팅
     * 글 4개 : 내가 작성 3개 (1, 2(삭제), 3), 다른 사람 작성 1개 (4)
     * 댓글 2개 : 1번 게시물 -> 2개 (유지 1, 삭제 1)
     * 스크랩 1개 : 3번 게시물 1개
     * 글 좋아요 4개 : 1, 2, 3, 4 (모든 게시물 1개씩)
     * 게시물 이미지 1개 : 3번 게시물 1개
     */
    void setPostData() {
        this.userId = createRequestJWT();

        User u2 = userRepository.save(User.builder()
                .id(2L)
                .email("2")
                .nickname("2")
                .provider(ProviderType.KAKAO)
                .level(levelRepository.findById(1).get())
                .build());

        User u3 = userRepository.save(User.builder()
                .id(3L)
                .email("3")
                .nickname("3")
                .provider(ProviderType.KAKAO)
                .level(levelRepository.findById(1).get())
                .build());

        User u4 = userRepository.save(User.builder()
                .id(4L)
                .email("4")
                .nickname("4")
                .provider(ProviderType.KAKAO)
                .level(levelRepository.findById(1).get())
                .build());

        // 내가 쓴 글 1
        Post saveMe = postRepository.save(Post.builder()
                .userId(userId)
                .content("1번 게시물")
                .challenge(false)
                .build());

        commentRepository.save(Comment.builder()
                .postId(saveMe.getId())
                .userId(u3.getId())
                .content("댓글~")
                .build());

        Comment deleteComment = commentRepository.save(Comment.builder()
                .postId(saveMe.getId())
                .userId(u3.getId())
                .content("삭제된 댓글~")
                .build());
        deleteComment.setStatus(StatusType.INACTIVE);
        postLikeRepository.save(PostLike.builder()
                .userId(u4.getId())
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
                .userId(u4.getId())
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
                .userId(u4.getId())
                .postId(saveMe3.getId())
                .build());
        postImageRepository.save(PostImage.builder()
                .postId(saveMe3.getId())
                .url("http")
                .build());

        // 다른 사람이 쓴 글
        Post notMe = postRepository.save(Post.builder()
                .userId(u2.getId())
                .content("4번 게시물")
                .challenge(false)
                .build());
        postLikeRepository.save(PostLike.builder()
                .userId(u4.getId())
                .postId(notMe.getId())
                .build());

    }

    @DisplayName("내가 쓴 글 repo test")
    @Test
    void postRepo() throws BaseException {
        List<GetPostListByMypageRes> getPostByUserRes = postRepository.getPostListByUser(this.userId, 1L, 30L);

        for (GetPostListByMypageRes p : getPostByUserRes) {
            System.out.println(p.getContent());
        }
    }

    @DisplayName("내가 쓴 글 조회")
    @Test
    void post() throws BaseException {
        setPostData();

        // given : userId
        Long page = 1L;
        Long size = 30L;

        List<GetPostListByMypageRes> getPostByUserRes = postService.getPostListByUser(page, size);

        assertThat(getPostByUserRes.get(0).getNickname()).isEqualTo(this.user.getNickname());
        assertThat(getPostByUserRes.get(0).getProfileImgUrl()).isEqualTo(this.user.getProfileImgUrl());
        assertThat(getPostByUserRes.size()).isEqualTo(2);

        // 1번 게시물
        assertThat(getPostByUserRes.get(0).getContent()).isEqualTo("1번 게시물");
        assertThat(getPostByUserRes.get(0).getLikeCount()).isEqualTo(1);
        assertThat(getPostByUserRes.get(0).getCommentCount()).isEqualTo(1);
        assertThat(getPostByUserRes.get(0).getImgCount()).isEqualTo(0);
        assertThat(getPostByUserRes.get(0).isScraped()).isFalse();

        // 2번 게시물은 삭제됐기 때문에 조회 안됨

        // 3번 게시물
        assertThat(getPostByUserRes.get(1).getContent()).isEqualTo("3번 게시물");
        assertThat(getPostByUserRes.get(1).getLikeCount()).isEqualTo(1);
        assertThat(getPostByUserRes.get(1).getCommentCount()).isEqualTo(0);
        assertThat(getPostByUserRes.get(1).getImgCount()).isEqualTo(1);
        assertThat(getPostByUserRes.get(1).isScraped()).isTrue();

        // 4번 게시물은 다른 유저가 작성한거라서 조회 안됨
    }

    @DisplayName("내가 쓴 글 조회 : 작성한 글이 없는 경우")
    @Test
    void post1() throws BaseException {
        Long userId = createRequestJWT();
        Long page = 1L;
        Long size = 30L;

        List<GetPostListByMypageRes> postListByUser = postService.getPostListByUser(page, size);

        assertThat(postListByUser.size()).isEqualTo(0);
    }

    @DisplayName("내가 댓글 단 글 조회")
    @Test
    void comment() throws BaseException {
        User other = userRepository.save(User.builder()
                .id(2L)
                .email("2")
                .nickname("2")
                .provider(ProviderType.KAKAO)
                .level(levelRepository.findById(1).get())
                .build());

        Long userId = createRequestJWT();
        Long page = 1L;
        Long size = 30L;

        // 내가 댓글 단 게시물 갯수
        int cnt = commentRepository.findByUserIdAndStatus(userId, StatusType.ACTIVE).size();

        // 댓글 2개 달았음 (+1)
        Post post1 = postRepository.save(Post.builder()
                .userId(userId)
                .content("글1")
                .challenge(false)
                .build());
        commentRepository.save(Comment.builder()
                .postId(post1.getId())
                .content("댓글")
                .userId(userId)
                .build());
        commentRepository.save(Comment.builder()
                .postId(post1.getId())
                .content("또댓글")
                .userId(userId)
                .build());

        List<Post> all = postRepository.findAll();
        Post post2 = all.get(0);
        // 댓글 1개 달았음 (+1)
        commentRepository.save(Comment.builder()
                .postId(post2.getId())
                .content("댓글")
                .userId(userId)
                .build());
        // 다른 사람이 단 댓글 (카운트 X)
        commentRepository.save(Comment.builder()
                .postId(post2.getId())
                .content("댓글!")
                .userId(other.getId())
                .build());

        List<GetPostListByMypageRes> postListByComment = postService.getPostListBycomment(page, size);
        for (GetPostListByMypageRes g : postListByComment) {
            System.out.println(g.getContent());
        }
        assertThat(postListByComment.size()).isEqualTo(cnt + 2);
    }

    @DisplayName("내가 좋아요 누른 글 조회")
    @Test
    void like() throws BaseException {
        User other = userRepository.save(User.builder()
                .id(2L)
                .email("2")
                .nickname("2")
                .provider(ProviderType.KAKAO)
                .level(levelRepository.findById(1).get())
                .build());

        Long userId = createRequestJWT();
        Long page = 1L;
        Long size = 30L;

        // 내가 좋아한 게시물 갯수
        int cnt = postLikeRepository.findByUserIdAndStatus(userId, StatusType.ACTIVE).size();
        List<Post> all = postRepository.findAll();

        // 좋아요 +1
        Post post1 = all.get(0);
        postLikeRepository.save(PostLike.builder()
                .postId(post1.getId())
                .userId(userId)
                .build());

        // 좋아요 && 취소
        Post post2 = all.get(1);
        PostLike save = postLikeRepository.save(PostLike.builder()
                .postId(post2.getId())
                .userId(userId)
                .build());
        save.setStatus(StatusType.INACTIVE);

        List<GetPostListByMypageRes> result = postService.getPostListByLike(page, size);

        assertThat(result.size()).isEqualTo(cnt + 1);
    }
}

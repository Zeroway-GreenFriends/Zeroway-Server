package com.zeroway.community.service;

import com.zeroway.common.BaseException;
import com.zeroway.common.StatusType;
import com.zeroway.community.dto.CreatePostReq;
import com.zeroway.community.entity.Bookmark;
import com.zeroway.community.entity.Post;
import com.zeroway.community.entity.PostImage;
import com.zeroway.community.repository.comment.CommentRepository;
import com.zeroway.community.repository.post.BookmarkRepository;
import com.zeroway.community.repository.post.PostImageRepository;
import com.zeroway.community.repository.post.PostRepository;
import com.zeroway.community.dto.PostListRes;
import com.zeroway.community.dto.PostRes;
import com.zeroway.s3.S3Uploader;
import com.zeroway.user.entity.User;
import com.zeroway.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zeroway.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BookmarkRepository bookmarkRepository;
    private final S3Uploader s3Uploader;


    // 전체 글 조회
    public List<PostListRes> getPostList(Long userId, String sort) throws BaseException {
        List<PostListRes> result = new ArrayList<>();
        try {
            for (PostListRes post : postRepository.getPostList(userId, sort)) {
                Long postId = post.getPostId();
                // 게시글 이미지 조회
                post.getImageList().addAll(postImageRepository.findUrlByPostId(postId));
                result.add(post);
            }
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 글 상세 조회
    public PostRes getPost(Long postId, Long userId) throws BaseException {
        PostRes post;
        try {
            post = postRepository.getPost(postId, userId);

            //유효하지 않은 게시글 id
            if(post == null)
                throw new BaseException(INVALID_POST_ID);

            // 게시글 이미지 조회
            post.getImageList().addAll(postImageRepository.findUrlByPostId(postId));

            // 댓글 조회
            post.getCommentList().addAll(commentRepository.getCommentList(postId, userId));

            return post;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            log.info(e.getMessage());
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 글 저장
    @Transactional
    public void createPost(CreatePostReq req, List<MultipartFile> images, Long userId) throws BaseException {
        try {
            // post 저장
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_JWT));
            Post savedPost = postRepository.save(Post.builder().user(user).content(req.getContent()).challenge(req.isChallenge()).build());

            // 이미지 파일 업로드 및 postImage 저장
            for (String url : s3Uploader.uploadFiles(images, "postImages")) {
                postImageRepository.save(PostImage.builder().post(savedPost).url(url).build());
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new BaseException(FILE_UPLOAD_ERROR);
        }
    }

    /**
     * 북마크 및 북마크 취소 기능
     * @return true 북마크, false 북마크 취소
     */
    @Transactional
    public boolean bookmark(Long userId, Long postId) throws BaseException {
        try {
            Optional<Bookmark> optional = bookmarkRepository.findByUserIdAndPostId(userId, postId);
            if (optional.isPresent()) {
                Bookmark bookmark = optional.get();
                if (bookmark.getStatus().equals(StatusType.ACTIVE)) {
                    optional.get().setStatus(StatusType.INACTIVE);
                    return false;
                } else {
                    optional.get().setStatus(StatusType.ACTIVE);
                    return true;
                }
            }
            User user = userRepository.findById(userId).orElseThrow(() -> new BaseException(INVALID_JWT));
            Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(INVALID_POST_ID));
            bookmarkRepository.save(Bookmark.builder().user(user).post(post).build());
            return true;
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    // 글 삭제
    @Transactional
    public void deletePost(Long postId, Long userId) throws BaseException {
        try {
            Post post = postRepository.findById(postId).orElseThrow(() -> new BaseException(INVALID_POST_ID));

            // 작성자가 아닌 회원이 요청한 경우
            if(!post.getUser().getId().equals(userId)) throw new BaseException(UNAUTHORIZED_REQUEST);

            // 상태를 INACTIVE로 수정
            post.setStatus(StatusType.INACTIVE);

        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }

    }

}

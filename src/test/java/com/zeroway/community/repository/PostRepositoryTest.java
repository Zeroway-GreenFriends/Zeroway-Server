package com.zeroway.community.repository;

import com.zeroway.challenge.entity.Level;
import com.zeroway.community.dto.PostListRes;
import com.zeroway.community.entity.Post;
import com.zeroway.user.entity.ProviderType;
import com.zeroway.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PostRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    PostRepository postRepository;

    @Test
    public void getPostList() {

        User user = User.builder().email("test@gmail.com").nickname("user1").provider(ProviderType.GOOGLE).build();
        em.persist(user);

        for(int i=0; i<10; i++) {
            Post post = Post.builder()
                    .user(user)
                    .content("내용" + i)
                    .build();
            em.persist(post);

        }

//        List<PostListRes> list = postRepository.getPostList(1L, "createdAt");
//        System.out.println("list = " + list);
        for (PostListRes post : postRepository.getPostList(1L, "createdAt")) {
            System.out.println("post = " + post);
        }

    }
}
package com.zeroway.community.repository;

import com.zeroway.community.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostImageRepository
        extends JpaRepository<PostImage, Long>, PostImageRepositoryCustom {
}

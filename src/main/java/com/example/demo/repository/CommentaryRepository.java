package com.example.demo.repository;

import com.example.demo.entity.Commentary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface CommentaryRepository extends JpaRepository<Commentary,Integer> {
   List<Commentary> findAllByPostId(int postId);
   List<Commentary> findByPostIdOrderByCreationDate(int postId);
   @Query("SELECT c.author.id FROM Commentary c where c.id=:commId")
   Optional<Integer> findAuthorIdByCommentaryId(@Param("commId") int id);
    @Query("SELECT c.post.author.id FROM Commentary c where c.id=:commId")
    Optional<Integer>findPostAuthorIdByCommentaryId(@Param("commId") int id);
}

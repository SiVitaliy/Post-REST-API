package com.example.demo.repository;

import com.example.demo.entity.Post;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Integer> {

   Optional<Post> findById(int id);
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.images WHERE p.author = :user")
    List<Post> findAllByAuthor(User user);
    @Query("SELECT p.author.id FROM Post p where p.id=:postId")
    Optional<Integer> findAuthorIdByPostId(@Param("postId") int id);
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.images WHERE p.id = :id")
    Optional<Post> findByIdWithImages(@Param("id") int id);


    @Query("""
       select p
       from Post p
       left join fetch p.author     
   """)//left чтобы при удалении автора не удалялись посты
    Page<Post> findAllWithAuthor(Pageable pageable);

}

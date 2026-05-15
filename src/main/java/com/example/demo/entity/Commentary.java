package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name="commentaries")
public class Commentary {
    @Column(name="id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="author_id",referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_commentary_author", foreignKeyDefinition = "FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL"))
    private User author;
    @Column(name="creation_date", nullable = false)
    private LocalDateTime creationDate;
//    @Column(name="number_of_likes", nullable = false)
//    private int numberOfLikes;
//    @Column(name="number_of_dislikes", nullable = false)
//    private int numberOfDislikes;
    @Column(name = "text", columnDefinition = "TEXT")
    private String text;
    @ManyToOne
    @JoinColumn(name="post_id",referencedColumnName = "id")
    private Post post;

    public Commentary(int id, User author, LocalDateTime creationDate, int numberOfLikes, int numberOfDislikes, String text) {
        this.id = id;
        this.author = author;
        this.creationDate = creationDate;
//        this.numberOfLikes = numberOfLikes;
//        this.numberOfDislikes = numberOfDislikes;
        this.text = text;
    }
    public Commentary(User author, LocalDateTime creationDate, int numberOfLikes, int numberOfDislikes, String text) {

        this.author = author;
        this.creationDate = creationDate;
//        this.numberOfLikes = numberOfLikes;
//        this.numberOfDislikes = numberOfDislikes;
        this.text = text;
    }

    public Commentary() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

//    public int getNumberOfLikes() {
//        return numberOfLikes;
//    }
//
//    public void setNumberOfLikes(int numberOfLikes) {
//        this.numberOfLikes = numberOfLikes;
//    }
//
//    public int getNumberOfDislikes() {
//        return numberOfDislikes;
//    }
//
//    public void setNumberOfDislikes(int numberOfDislikes) {
//        this.numberOfDislikes = numberOfDislikes;
//    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}

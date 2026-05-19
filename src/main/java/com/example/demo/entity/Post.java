package com.example.demo.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name= "posts")
public class Post {
    @Column(name="id")
    @Id
    @GeneratedValue(strategy =GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="author_id",referencedColumnName = "id" ,foreignKey = @ForeignKey(name = "fk_post_author", foreignKeyDefinition = "FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE SET NULL"))
    private User author;
    @Column(name="creation_date", nullable = false)
    private LocalDateTime creationDate;
    @Column(name="number_of_likes", nullable = false)
    private int numberOfLikes;
    @Column(name="number_of_dislikes", nullable = false)
    private int numberOfDislikes;
    @Column(name="title", nullable = false, length = 100)
    private String title;

    @Column(name="text", columnDefinition = "TEXT")
    private String text;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Commentary> commentaries;
    @OneToMany(mappedBy = "post",cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    @BatchSize(size = 50)
    private List<PostImage> images;


    public Post() {
    }

    public Post(int id, User author, LocalDateTime creationDate, int numberOfLikes, int numberOfDislikes, String title, String text, List<Commentary> commentaries, List<PostImage> images) {
        this.id = id;
        this.author = author;
        this.creationDate = creationDate;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
        this.title = title;
        this.text = text;
        this.commentaries = commentaries;
        this.images = images;
    }

    public Post(int id, User author, LocalDateTime creationDate, int numberOfLikes, int numberOfDislikes, String title, String text, List<Commentary> commentaries) {
        this.id = id;
        this.author = author;
        this.creationDate = creationDate;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
        this.title = title;
        this.text = text;
        this.commentaries = commentaries;
    }

    public Post(User author, LocalDateTime creationDate, int numberOfLikes, int numberOfDislikes, String title, String text, List<Commentary> commentaries) {
        this.author = author;
        this.creationDate = creationDate;
        this.numberOfLikes = numberOfLikes;
        this.numberOfDislikes = numberOfDislikes;
        this.title = title;
        this.text = text;
        this.commentaries = commentaries;
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

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public int getNumberOfDislikes() {
        return numberOfDislikes;
    }

    public void setNumberOfDislikes(int numberOfDislikes) {
        this.numberOfDislikes = numberOfDislikes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Commentary> getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(List<Commentary> commentaries) {
        this.commentaries = commentaries;
    }

    public List<PostImage> getImages() {
        return images;
    }

    public void setImages(List<PostImage> images) {
        this.images = images;
    }
}

package com.example.demo.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="post_images")
public class PostImage {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "imageUrl", length = 100)
    private String imageUrl;
    @Column(name="sort_order")
    private int sortOrder;

    @Column(name="created_at")
    private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name="post_id",referencedColumnName = "id")
    private Post post;
    public PostImage(){}

    public PostImage(int id, String imageUrl, int sortOrder, LocalDateTime createdAt, Post post) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.createdAt = createdAt;
        this.post = post;
    }

    public PostImage(int id, String imageUrl, int sortOrder, Post post) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.post = post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

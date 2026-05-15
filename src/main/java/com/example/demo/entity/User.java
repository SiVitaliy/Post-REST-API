package com.example.demo.entity;

import com.example.demo.dto.request.UserRequest.RegisterUserRequest;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
@Entity
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="full_name",nullable = false, length =100)
    private String fullName;
    @Column(name="email", nullable = false, length =150, unique = true)
    private String email;

    @Column(name="password",length = 200)
    private String password;

    @Column(name="year_of_birth")
    private LocalDate yearOfBirth;
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;
    @Column(name="country_code", length = 2)
    private String countryCode;
    @Column(name="bio", length = 30000)
    private String bio;

    @Column(nullable = false)
    private String role = "USER";
    @Column(name="profile_picture", length = 100)
    private String profilePictureUrl;

    @OneToMany(mappedBy = "author")
    private List<Post> posts;

    @OneToMany(mappedBy = "author")
    private List<Commentary> commentaries;


    public User(int id, String fullName, String email, String password, LocalDate yearOfBirth, LocalDateTime creationDate, String countryCode, String bio, String role, String profilePictureUrl, List<Post> posts, List<Commentary> commentaries) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.yearOfBirth = yearOfBirth;
        this.creationDate = creationDate;
        this.countryCode = countryCode;
        this.bio = bio;
        this.role = role;
        this.profilePictureUrl = profilePictureUrl;
        this.posts = posts;
        this.commentaries = commentaries;
    }

    public User(RegisterUserRequest registerUserRequest ){

        PasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();
        this.fullName = registerUserRequest.fullName();
        this.email = registerUserRequest.email();
        this.creationDate = LocalDateTime.now();
        this.password = passwordEncoder.encode(registerUserRequest.password());
    }

    public User() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(LocalDate yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Commentary> getCommentaries() {
        return commentaries;
    }

    public void setCommentaries(List<Commentary> commentaries) {
        this.commentaries = commentaries;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePicture) {
        this.profilePictureUrl = profilePicture;
    }
}

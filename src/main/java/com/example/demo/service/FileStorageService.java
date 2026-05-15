package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.entity.PostImage;
import com.example.demo.repository.PostImageRepository;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.io.File;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
@Service
public class FileStorageService {
    private final Path profilePictureRoot = Paths.get("uploads/profilePictures");
    private final Path imagesRoot = Paths.get("uploads/posts");
    private final PostImageRepository postImageRepository ;

    public FileStorageService(PostImageRepository postImageRepository) {
        this.postImageRepository = postImageRepository;
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(profilePictureRoot);
            Files.createDirectories(imagesRoot);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать папку для загрузки файлов", e);
        }
    }

    public String saveProfilePicture(int userId, MultipartFile file) {
        try {
            // Проверка на пустой файл
            if (file == null || file.isEmpty()) {
                throw new RuntimeException("Файл не передан");
            }

            // Проверка типа файла
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Можно загружать только изображения");
            }

            // Получаем расширение файла
            String originalFilename = file.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // Генерируем уникальное имя файла
            String filename = userId + "_" + System.currentTimeMillis() + extension;

            // Сохраняем файл
            Path target = profilePictureRoot.resolve(filename);
            file.transferTo(target);

            // Возвращаем URL для доступа к файлу
            return "/uploads/profilePictures/" + filename;

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении файла", e);
        }
    }

    public Resource loadProfilePicture(String filename) {
        try {
            Path file = profilePictureRoot.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            }

            throw new RuntimeException("Файл не найден: " + filename);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }

    public void deleteProfilePicture(String filename) {
        try {
            Path file = profilePictureRoot.resolve(filename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при удалении файла", e);
        }
    }

    public List<PostImage> saveImages(List<MultipartFile> images, Post post) {
        if (images == null || images.isEmpty()) {
            throw new RuntimeException("Файлы не переданы");
        }

        List<PostImage> savedImages = new ArrayList<>();

        // Создаём папку для поста
        Path postDir = imagesRoot.resolve(String.valueOf(post.getId()));
        try {
            Files.createDirectories(postDir);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать папку для поста " + post.getId(), e);
        }

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);

            // Проверка на пустой файл
            if (image == null || image.isEmpty()) {
                throw new RuntimeException("Файл не передан");
            }

            // Проверка типа файла
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new RuntimeException("Можно загружать только изображения");
            }

            // Получаем расширение файла
            String originalFilename = image.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            // Генерируем уникальное имя файла
            String filename = post.getId() + "_" + System.currentTimeMillis() + "_" + i + extension;

            try {
                // Сохраняем файл
                Path target = postDir.resolve(filename);
                image.transferTo(target);

                // URL для доступа к файлу
                String imageUrl = "/uploads/posts/" + post.getId() + "/" + filename;

                // Создаём сущность PostImage
                PostImage postImage = new PostImage();
                postImage.setPost(post);
                postImage.setImageUrl(imageUrl);
                postImage.setSortOrder(i);
                postImage.setCreatedAt(LocalDateTime.now());
                postImageRepository.save(postImage);
                savedImages.add(postImage);

            } catch (IOException e) {
                throw new RuntimeException("Ошибка при сохранении файла: " + filename, e);
            }
        }

        return savedImages;
    }

    public Resource loadImage(int postId, String filename) {
        try {
            Path postDir = imagesRoot.resolve(String.valueOf(postId));
            Path file = postDir.resolve(filename);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() && resource.isReadable()) {
                return resource;
            }

            throw new RuntimeException("Файл не найден: " + filename);

        } catch (MalformedURLException e) {
            throw new RuntimeException("Ошибка при загрузке файла", e);
        }
    }
    public void deletePostImages(int postId) {
        Path postDir = imagesRoot.resolve(String.valueOf(postId));
        try {
            if (Files.exists(postDir)) {
                Files.walk(postDir)
                        .sorted(Comparator.reverseOrder())  // сначала файлы, потом папка
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при удалении папки поста " + postId, e);
        }
    }
}

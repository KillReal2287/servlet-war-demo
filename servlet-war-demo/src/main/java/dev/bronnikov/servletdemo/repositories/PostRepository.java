package dev.bronnikov.servletdemo.repositories;

import dev.bronnikov.servletdemo.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {

    List<Post> findAll(String tag, int page, int size);

    Optional<Post> findById(Long id);

    List<String> findAllTags();

    int count(String tag);

    Post save(Post post);

    void incrementLikes(Long postId);
}

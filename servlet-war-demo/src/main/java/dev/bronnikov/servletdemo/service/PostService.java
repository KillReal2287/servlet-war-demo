package dev.bronnikov.servletdemo.service;

import dev.bronnikov.servletdemo.Post;
import dev.bronnikov.servletdemo.repositories.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll(String tag, int page, int size) {
        return postRepository.findAll(tag, page, size);
    }

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Post not found: " + id));
    }

    public List<String> findAllTags() {
        return postRepository.findAllTags();
    }

    public int count(String tag) {
        return postRepository.count(tag);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void like(Long postId) {
        postRepository.incrementLikes(postId);
    }
}

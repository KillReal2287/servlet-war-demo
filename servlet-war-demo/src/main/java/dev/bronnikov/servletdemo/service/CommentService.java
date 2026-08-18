package dev.bronnikov.servletdemo.service;

import dev.bronnikov.servletdemo.Comment;
import dev.bronnikov.servletdemo.repositories.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment save(Long postId, Comment comment) {
        comment.setPostId(postId);
        return commentRepository.save(comment);
    }
}

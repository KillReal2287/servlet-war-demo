package dev.bronnikov.servletdemo.repositories;

import dev.bronnikov.servletdemo.Comment;

public interface CommentRepository {

    Comment save(Comment comment);
}

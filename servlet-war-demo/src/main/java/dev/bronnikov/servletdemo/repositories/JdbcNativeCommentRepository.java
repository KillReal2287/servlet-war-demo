package dev.bronnikov.servletdemo.repositories;

import dev.bronnikov.servletdemo.Comment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Comment save(Comment comment) {
        return jdbcTemplate.queryForObject(
                """
                        insert into post_comments(post_id, text)
                        values(?, ?)
                        returning id, post_id, text
                        """,
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getLong("post_id"),
                        rs.getString("text")
                ),
                comment.getPostId(), comment.getText()
        );
    }
}

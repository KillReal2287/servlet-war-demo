package dev.bronnikov.servletdemo.repositories;

import dev.bronnikov.servletdemo.Comment;
import dev.bronnikov.servletdemo.Post;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll(String tag, int page, int size) {
        int offset = page * size;
        List<Post> posts;

        if (isBlank(tag)) {
            posts = jdbcTemplate.query(
                    """
                            select id, title, image_url, likes_count
                            from posts
                            order by id desc
                            limit ? offset ?
                            """,
                    (rs, rowNum) -> new Post(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("image_url"),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            rs.getInt("likes_count"),
                            new ArrayList<>()
                    ),
                    size, offset
            );
        } else {
            posts = jdbcTemplate.query(
                    """
                            select p.id, p.title, p.image_url, p.likes_count
                            from posts p
                            join post_tags pt on pt.post_id = p.id
                            join tags t on t.id = pt.tag_id
                            where t.name = ?
                            order by p.id desc
                            limit ? offset ?
                            """,
                    (rs, rowNum) -> new Post(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("image_url"),
                            new ArrayList<>(),
                            new ArrayList<>(),
                            rs.getInt("likes_count"),
                            new ArrayList<>()
                    ),
                    tag, size, offset
            );
        }

        posts.forEach(this::loadDetails);
        return posts;
    }

    @Override
    public Optional<Post> findById(Long id) {
        List<Post> posts = jdbcTemplate.query(
                """
                        select id, title, image_url, likes_count
                        from posts
                        where id = ?
                        """,
                (rs, rowNum) -> new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("image_url"),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        rs.getInt("likes_count"),
                        new ArrayList<>()
                ),
                id
        );

        posts.forEach(this::loadDetails);
        return posts.stream().findFirst();
    }

    @Override
    public List<String> findAllTags() {
        return jdbcTemplate.query(
                "select name from tags order by name",
                (rs, rowNum) -> rs.getString("name")
        );
    }

    @Override
    public int count(String tag) {
        if (isBlank(tag)) {
            Integer count = jdbcTemplate.queryForObject("select count(*) from posts", Integer.class);
            return count == null ? 0 : count;
        }

        Integer count = jdbcTemplate.queryForObject(
                """
                        select count(*)
                        from posts p
                        join post_tags pt on pt.post_id = p.id
                        join tags t on t.id = pt.tag_id
                        where t.name = ?
                        """,
                Integer.class,
                tag
        );
        return count == null ? 0 : count;
    }

    @Override
    public Post save(Post post) {
        Long postId = jdbcTemplate.queryForObject(
                """
                        insert into posts(title, image_url, likes_count)
                        values(?, ?, ?)
                        returning id
                        """,
                Long.class,
                post.getTitle(), post.getImageUrl(), post.getLikesCount()
        );

        post.setId(postId);
        saveParagraphs(post);
        saveTags(post);
        saveComments(post);

        return post;
    }

    @Override
    public void incrementLikes(Long postId) {
        jdbcTemplate.update(
                "update posts set likes_count = likes_count + 1 where id = ?",
                postId
        );
    }

    private void loadDetails(Post post) {
        post.setParagraphs(findParagraphs(post.getId()));
        post.setTags(findTags(post.getId()));
        post.setComments(findComments(post.getId()));
    }

    private List<String> findParagraphs(Long postId) {
        return jdbcTemplate.query(
                """
                        select text
                        from post_paragraphs
                        where post_id = ?
                        order by paragraph_order
                        """,
                (rs, rowNum) -> rs.getString("text"),
                postId
        );
    }

    private List<String> findTags(Long postId) {
        return jdbcTemplate.query(
                """
                        select t.name
                        from tags t
                        join post_tags pt on pt.tag_id = t.id
                        where pt.post_id = ?
                        order by t.name
                        """,
                (rs, rowNum) -> rs.getString("name"),
                postId
        );
    }

    private List<Comment> findComments(Long postId) {
        return jdbcTemplate.query(
                """
                        select id, post_id, text
                        from post_comments
                        where post_id = ?
                        order by id
                        """,
                (rs, rowNum) -> new Comment(
                        rs.getLong("id"),
                        rs.getLong("post_id"),
                        rs.getString("text")
                ),
                postId
        );
    }

    private void saveParagraphs(Post post) {
        List<String> paragraphs = nullToEmpty(post.getParagraphs());
        for (int i = 0; i < paragraphs.size(); i++) {
            String text = paragraphs.get(i);
            if (isBlank(text)) {
                continue;
            }

            jdbcTemplate.update(
                    "insert into post_paragraphs(post_id, paragraph_order, text) values(?, ?, ?)",
                    post.getId(), i + 1, text
            );
        }
    }

    private void saveTags(Post post) {
        for (String tag : nullToEmpty(post.getTags())) {
            if (isBlank(tag)) {
                continue;
            }

            Long tagId = jdbcTemplate.queryForObject(
                    """
                            insert into tags(name)
                            values(?)
                            on conflict (name) do update set name = excluded.name
                            returning id
                            """,
                    Long.class,
                    tag.trim()
            );

            jdbcTemplate.update(
                    "insert into post_tags(post_id, tag_id) values(?, ?) on conflict do nothing",
                    post.getId(), tagId
            );
        }
    }

    private void saveComments(Post post) {
        for (Comment comment : nullToEmpty(post.getComments())) {
            if (comment == null || isBlank(comment.getText())) {
                continue;
            }

            jdbcTemplate.update(
                    "insert into post_comments(post_id, text) values(?, ?)",
                    post.getId(), comment.getText()
            );
        }
    }

    private static <T> List<T> nullToEmpty(List<T> values) {
        return Objects.requireNonNullElse(values, Collections.emptyList());
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

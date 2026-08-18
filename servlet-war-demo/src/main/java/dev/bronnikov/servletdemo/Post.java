package dev.bronnikov.servletdemo;

import java.util.ArrayList;
import java.util.List;

public class Post {

    private Long id;
    private String title;
    private String imageUrl;
    private List<String> paragraphs = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    private int likesCount;
    private List<Comment> comments = new ArrayList<>();

    public Post() {
    }

    public Post(Long id, String title, String imageUrl, List<String> paragraphs,
                List<String> tags, int likesCount, List<Comment> comments) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.paragraphs = paragraphs;
        this.tags = tags;
        this.likesCount = likesCount;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getParagraphs() {
        return paragraphs;
    }

    public void setParagraphs(List<String> paragraphs) {
        this.paragraphs = paragraphs;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

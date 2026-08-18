package dev.bronnikov.servletdemo.controller;

import dev.bronnikov.servletdemo.Comment;
import dev.bronnikov.servletdemo.Post;
import dev.bronnikov.servletdemo.service.CommentService;
import dev.bronnikov.servletdemo.service.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping
    public String posts(
            @RequestParam(name = "tag", required = false) String tag,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model
    ) {
        int normalizedSize = normalizeSize(size);
        int normalizedPage = Math.max(page, 0);
        int totalPosts = postService.count(tag);
        int totalPages = (int) Math.ceil((double) totalPosts / normalizedSize);
        List<Post> posts = postService.findAll(tag, normalizedPage, normalizedSize);

        model.addAttribute("posts", posts);
        model.addAttribute("post", new Post());
        model.addAttribute("comment", new Comment());
        model.addAttribute("tags", postService.findAllTags());
        model.addAttribute("selectedTag", tag);
        model.addAttribute("page", normalizedPage);
        model.addAttribute("size", normalizedSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("pageSizes", List.of(10, 20, 50));

        return "posts";
    }

    @GetMapping("/{postId}")
    public String post(@PathVariable("postId") Long postId, Model model) {
        model.addAttribute("post", postService.findById(postId));
        model.addAttribute("comment", new Comment());
        return "post";
    }

    @PostMapping
    public String save(@ModelAttribute Post post) {
        postService.save(post);
        return "redirect:/mvc/posts";
    }

    @PostMapping("/{postId}/comments")
    public String saveComment(@PathVariable("postId") Long postId, @ModelAttribute Comment comment) {
        commentService.save(postId, comment);
        return "redirect:/mvc/posts";
    }

    @PostMapping("/{postId}/likes")
    public String like(@PathVariable("postId") Long postId) {
        postService.like(postId);
        return "redirect:/mvc/posts";
    }

    private int normalizeSize(int size) {
        if (size == 20 || size == 50) {
            return size;
        }
        return 10;
    }
}

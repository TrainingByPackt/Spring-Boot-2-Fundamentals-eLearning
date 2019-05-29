package com.packt.springboot.blogmania.blogpost;

import com.packt.springboot.blogmania.category.CategoryController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/blogposts")
@RequiredArgsConstructor
public class BlogPostController {
    private final BlogPostService blogPostService;
    private final CategoryController categoryController;

    @GetMapping("/new")
    public String prepareNewBlogPost(Model model) {
        model.addAttribute("blogPost", blogPostService.prepareNewBlogPost());
        model.addAttribute("categories", categoryController.retrieveAllCategories());

        return "blogposts/form";
    }

    @GetMapping("/{slug}")
    public ModelAndView displayBlogPostBySlug(@PathVariable String slug) throws BlogPostNotFoundException {
        BlogPost blogPost = blogPostService.findBlogPostBySlug(slug)
                .orElseThrow(() -> new BlogPostNotFoundException("Blog post with slug " + slug + " could not be found"));

        return new ModelAndView("blogposts/details", "blogPost", blogPost);
    }

    @GetMapping("/sample-post")
    public ModelAndView displaySampleBlogPost() {
        BlogPost blogPost = new BlogPost();
        blogPost.setTitle("A sample blog post");
        blogPost.setPostDate(LocalDateTime.now());

        blogPost.setContent("Writing blog posts is fun!");

        return new ModelAndView("blogposts/details", "blogPost", blogPost);
    }

    @PostMapping
    public String saveBlogPost(BlogPost blogPost) {
        blogPost.setPostDate(LocalDateTime.now());

        blogPostService.save(blogPost);

        return "redirect:/";
    }

    @GetMapping("/random")
    public RedirectView displayRandomBlogPost(RedirectAttributes attributes) {
        attributes.addFlashAttribute("flashMessage", "Enjoy this post");

        BlogPost blogPost = blogPostService.randomBlogPost();

        return new RedirectView("/blogposts/" + blogPost.getSlug());
    }

    @ModelAttribute
    public void addDefaultAttributes(Model model) {
        int allPostsCount = blogPostService.numberOfBlogPosts();
        model.addAttribute("allPostsCount", allPostsCount);
    }

}

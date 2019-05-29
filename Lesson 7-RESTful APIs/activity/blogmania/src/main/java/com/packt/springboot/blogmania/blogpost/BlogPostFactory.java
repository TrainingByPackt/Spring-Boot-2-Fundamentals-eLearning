package com.packt.springboot.blogmania.blogpost;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BlogPostFactory {

    public BlogPost createBlogPostWithDefaults() {
        BlogPost preparedBlogPost = new BlogPost();
        preparedBlogPost.setPostDate(LocalDateTime.now());

        return preparedBlogPost;
    }

}

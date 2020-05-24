package com.computingfacts.di.service;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * @author joseph
 */
@Service("publicationService")
class PublicationServiceImpl implements PublicationService {

    private final BlogService blogService;
    // the PublicationService has a dependency on the CommentService
    private CommentService commentService;

    public PublicationServiceImpl(BlogService blogService) {
        Assert.notNull(blogService, "blogService should not be empty");
        this.blogService = blogService;
    }

    // a setter method so that the Spring container can inject a CommentService
    @Required
    @Override
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }

    @Override
    public List<Map.Entry<String, String>> findPublishedArticles() {
        return blogService.findPublishedArticles();
    }

    @Override
    public List<Map.Entry<String, Map<String, String>>> findCommentsByUsername(String username) {

        return commentService.commentsByUserId(username);
    }

}

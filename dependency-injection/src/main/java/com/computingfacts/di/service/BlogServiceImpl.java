package com.computingfacts.di.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author joseph
 */
@Service
class BlogServiceImpl implements BlogService {

    private final CommentService commentService;
    private final Stream<Map.Entry<String, String>> blogRepository;

    @Autowired
    public BlogServiceImpl(CommentService commentService, Stream<Map.Entry<String, String>> blogRepository) {
        this.commentService = commentService;
        this.blogRepository = blogRepository;

    }

    @Override
    public List<Map.Entry<String, String>> findPublishedArticles() {

        return blogRepository
                .collect(Collectors.toList());
    }

    @Override
    public List<Map.Entry<String, Map<String, String>>> findCommentsByUsername(String username) {
        return commentService.commentsByUserId(username);
    }

}

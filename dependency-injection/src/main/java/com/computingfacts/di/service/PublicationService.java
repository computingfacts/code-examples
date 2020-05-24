package com.computingfacts.di.service;

import java.util.List;
import java.util.Map;

/**
 * @author joseph
 */
public interface PublicationService {

    List<Map.Entry<String, String>> findPublishedArticles();

    List<Map.Entry<String, Map<String, String>>> findCommentsByUsername(String username);

    void setCommentService(CommentService commentService);
}

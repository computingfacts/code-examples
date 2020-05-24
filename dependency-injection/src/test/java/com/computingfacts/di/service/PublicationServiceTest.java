package com.computingfacts.di.service;

import com.computingfacts.di.DependencyInjectionApplication;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;

/**
 *
 * @author joseph
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DependencyInjectionApplication.class)
public class PublicationServiceTest {

    @Autowired
    private BlogService blogService;
    @Autowired
    private PublicationService publicationService;
    @Autowired
    private CommentService commentService;

    @Test
    void injectedComponentsAreNotNull() {

        assertThat(commentService).isNotNull();
        assertThat(blogService).isNotNull();
        assertThat(publicationService).isNotNull();

    }

    /**
     * Test of findPublishedArticles method, of class PublicationService.
     */
    @Test
    public void testFindPublishedArticles() {
        log.debug("testFindPublishedArticles");
        List<Map.Entry<String, String>> result = publicationService.findPublishedArticles();
        assertThat(result).isNotEmpty();
        assertThat(result, hasSize(greaterThanOrEqualTo(3)));

    }

    /**
     * Test of findCommentsByUsername method, of class PublicationService.
     */
    @Test
    public void testFindCommentsByUsername() {
        log.debug("testFindCommentsByUsername");
        publicationService.setCommentService(commentService);
        List<Map.Entry<String, Map<String, String>>> result = publicationService.findCommentsByUsername("Fred Brooks");

        assertThat(result).isNotEmpty();
        assertThat(result, hasSize(greaterThanOrEqualTo(2)));

        List<String> data = result
                .stream()
                .map(Map.Entry::getValue)
                .map(Map::entrySet)
                .flatMap(Collection::stream)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        assertThat(data).containsAnyOf("Fred Brooks");

    }

}

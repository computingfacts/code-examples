package com.computingfacts.api.controller;

import com.computingfacts.api.model.Autocomplete;
import com.computingfacts.api.model.Suggestion;
import com.computingfacts.api.service.IndexService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author joseph
 */
@RestController
public class SearchController {

    private final IndexService indexService;

    public SearchController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping(value = "/")
    ResponseEntity<String> root() {
        return ResponseEntity.ok(londonZoneDateTime());
    }

    @PostMapping("/service/webclient/auto/search")
    public Mono<Autocomplete> autocompleteSearch(@RequestParam(value = "query", required = true) String query) {
        if (query != null && query.length() >= 3) {
            return webclientSearch(query.toLowerCase());

        }
        return Mono.empty();
    }

    @GetMapping("/service/webclient/nonblocking/search")
    public Flux<Suggestion> queryServiceSearch(@RequestParam(value = "query", required = true) String query) {
        return suggestions(query.toLowerCase());
    }

    @GetMapping("/service/okhttp/search")
    public Optional<Autocomplete> okHttpSearch(@RequestParam(value = "query", required = true) String query) {
        return okHttp(query.toLowerCase());
    }

    @GetMapping("/service/blocking/search")
    public List<Suggestion> blockingSearch(@RequestParam(value = "query", required = true) String query) {
        return indexService.getSuggestions(query.trim());
    }

    @GetMapping("/service/webclient/search")
    public Mono<Autocomplete> queryService(@RequestParam(value = "query", required = true) String query) {
        return webclientSearch(query.toLowerCase());
    }

    @GetMapping("/service/okhttp/auto/search")
    public Optional<Autocomplete> okHttpAutoCompleteSearch(@RequestParam(value = "query", required = true) String query) {

        if (query != null && query.length() >= 3) {
            return okHttp(query.toLowerCase());

        }

        return Optional.empty();
    }

    private Mono<Autocomplete> webclientSearch(String query) {

        return indexService.webclientAutocompleteSearch(query.trim());
    }

    private Flux<Suggestion> suggestions(String query) {

        return indexService.webclientSuggestions(query.trim());
    }

    private Optional<Autocomplete> okHttp(String query) {

        return indexService.okhttpAutocompleteSearch(query.trim());
    }

    private String londonZoneDateTime() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneId.of("Europe/London"));

        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG).format(zonedDateTime);
    }

}

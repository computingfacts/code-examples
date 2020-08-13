package com.computingfacts.api.service;

import com.computingfacts.api.model.Autocomplete;
import com.computingfacts.api.model.Suggestion;
import java.util.List;
import java.util.Optional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author joseph
 */
public interface IndexService {

    Optional<Autocomplete> okhttpAutocompleteSearch(String query);

    Mono<Autocomplete> webclientAutocompleteSearch(String searchTerm);

    Flux<Suggestion> webclientSuggestions(String searchTerm);

    List<Suggestion> getSuggestions(String searchTerm);

}

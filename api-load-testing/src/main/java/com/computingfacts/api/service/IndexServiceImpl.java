package com.computingfacts.api.service;

import com.computingfacts.api.config.IndexProperties;
import com.computingfacts.api.exceptions.IndexServiceException;
import com.computingfacts.api.model.Autocomplete;
import com.computingfacts.api.model.Suggestion;
import com.squareup.moshi.JsonAdapter;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.CacheControl;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author joseph
 */
@Slf4j
@Service
class IndexServiceImpl implements IndexService {

    private static final int CACHE_DURATION = 30_000;
    private static final String QUERY_PARAM = "term";
    private static final String FORMAT = "&format=json";
    private static final String ENDPOINT = "/autocomplete";
    private final IndexProperties indexProperties;
    private final WebClient webClient;
    private final OkHttpClient okHttpClient;
    private final JsonAdapter<Autocomplete> autoCompleteJsonAdapter;

    @Autowired
    public IndexServiceImpl(JsonAdapter<Autocomplete> jsonAdapter, IndexProperties indexProperties, WebClient webClient, OkHttpClient okHttpClien) {
        this.autoCompleteJsonAdapter = jsonAdapter;
        this.indexProperties = indexProperties;
        this.webClient = webClient;
        this.okHttpClient = okHttpClien;
    }

    private UriComponentsBuilder uriComponentsBuilder() {

        return UriComponentsBuilder.fromHttpUrl(indexProperties.getBaseUrl() + indexProperties.getEnzymeCentricUrl() + ENDPOINT);
    }

    private URI buildURI(@NonNull String searchTerm) {

        return uriComponentsBuilder()
                .queryParam(QUERY_PARAM, "{searchTerm}")
                .query(FORMAT)
                .build(searchTerm.toLowerCase());

    }

    private CacheControl cacheControl() {
        return new CacheControl.Builder()
                .onlyIfCached()
                .build();
    }

    @Override
    public Optional<Autocomplete> okhttpAutocompleteSearch(String query) {

        HttpUrl httpUrl = HttpUrl.get(buildURI(query));
        Request request = new Request.Builder()
                .cacheControl(cacheControl())
                //.cacheControl(CacheControl.FORCE_CACHE)
                .url(httpUrl)
                .build();
        try {

            Autocomplete result = null;
            try (Response response = okHttpClient.newCall(request).execute()) {

                if (response.code() != 504) {
                    log.info("Cache Response : " + response.cacheResponse());
                    log.info("Network Response " + response.cacheResponse());
                    result = autoCompleteJsonAdapter.fromJson(response.body().source());
                }
            }
            if (result == null) {
                Request networkRequest = new Request.Builder()
                        .url(httpUrl)
                        .build();
                try (Response networkResponse = okHttpClient.newCall(networkRequest).execute()) {
                    result = autoCompleteJsonAdapter.fromJson(networkResponse.body().source());
                    log.info("Cache Response " + networkResponse.cacheResponse());
                    log.info("Network Response " + networkResponse.cacheResponse());
                }
            }

            return Optional.ofNullable(result);
        } catch (IOException ex) {
            log.error(ex.getLocalizedMessage());
        }
        return Optional.empty();

    }

    @Override
    public Mono<Autocomplete> webclientAutocompleteSearch(String searchTerm) {

        return webClient.get()
                .uri(buildURI(searchTerm))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> response.bodyToMono(Autocomplete.class))
                .cache(Duration.ofSeconds(CACHE_DURATION))
                .switchIfEmpty(Mono.empty());
    }

    @Override
    public Flux<Suggestion> webclientSuggestions(String searchTerm) {

        return webClient.get()
                .uri(buildURI(searchTerm))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(Autocomplete.class))
                .flatMapIterable(Autocomplete::getSuggestions)
                .onBackpressureBuffer()
                .cache(Duration.ofSeconds(CACHE_DURATION))
                .switchIfEmpty(Flux.empty());
    }

    @Override
    public List<Suggestion> getSuggestions(String searchTerm) {

        return webClient.mutate().build().get()
                .uri(buildURI(searchTerm))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> Mono.just(new IndexServiceException("client error with status : " + response.rawStatusCode())))
                .onStatus(HttpStatus::is5xxServerError, response -> response.bodyToMono(IndexServiceException.class))
                .bodyToMono(Autocomplete.class)
                .map(suggestion -> suggestion.getSuggestions())
                .cache(Duration.ofSeconds(60))
                .switchIfEmpty(Mono.empty())
                .block(Duration.ofSeconds(30));
    }

}

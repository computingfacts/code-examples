package com.computingfacts.api.config;

import com.computingfacts.api.model.Autocomplete;
import com.computingfacts.api.model.Reactome;
import com.computingfacts.api.okhttp.CacheInterceptor;
import com.computingfacts.api.okhttp.LoggingInterceptor;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.io.File;
import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

/**
 *
 * @author joseph
 */
@Configuration
public class ApiConfig {

    private static final int MAX_IN_MEMORY = 16 * 1024 * 1024;//16MB
    private static final int TIMEOUT = 120_000;
    @Autowired
    private IndexProperties indexProperties;

    @Bean
    public JsonAdapter<Autocomplete> autoCompleteJsonAdapter() {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(Autocomplete.class);
    }

    private Cache cache() {
        int cacheSize = 10 * 1024 * 1024; // 10 MB
        File cacheDirectory = new File(indexProperties.getCacheDir());
        return new Cache(cacheDirectory, cacheSize);
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .dispatcher(dispatcher())
                .addNetworkInterceptor(new CacheInterceptor())
                .addInterceptor(new LoggingInterceptor())
                .cache(cache())
                .connectionPool(connectionPool())
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }

    @Bean
    public JsonAdapter<Reactome> reactomeJsonAdapter() {
        Moshi moshi = new Moshi.Builder().build();
        return moshi.adapter(Reactome.class);
    }

    private ConnectionPool connectionPool() {
        return new ConnectionPool();
    }

    private Dispatcher dispatcher() {
        Dispatcher dispatcher = new Dispatcher(Executors.newFixedThreadPool(20));
        dispatcher.setMaxRequests(2000);
        dispatcher.setMaxRequestsPerHost(1000);
        return dispatcher;
    }

    @Bean
    public WebClient webClient() {

        return WebClient
                .builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient()))
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer
                        .defaultCodecs()
                        .maxInMemorySize(MAX_IN_MEMORY))
                        .build())
                .build();

    }

    private HttpClient httpClient() {

        ConnectionProvider provider = ConnectionProvider.builder("api_connection_pool")
                .maxConnections(Integer.MAX_VALUE)
                .pendingAcquireMaxCount(-1)
                .pendingAcquireTimeout(Duration.ofSeconds(60L))
                .maxIdleTime(Duration.ofMillis(10000L))
                .maxLifeTime(Duration.ofMillis(10000L))
                .lifo()
                //.metrics(true)//To enable metrics, you must add the dependency `io.micrometer:micrometer-core`
                .build();

        return HttpClient.create(provider)
                .compress(true)
                .wiretap(false)
                .keepAlive(true)
                .secure(spec -> spec.sslContext(SslContextBuilder.forClient()))
                .protocol(HttpProtocol.HTTP11, HttpProtocol.H2)
                .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(60, TimeUnit.SECONDS))
                .addHandlerLast(new WriteTimeoutHandler(60, TimeUnit.SECONDS))));

    }

    @Bean
    public ReactorResourceFactory reactorResourceFactory() {
        return new ReactorResourceFactory();
    }

}

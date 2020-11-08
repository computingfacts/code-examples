package uk.gov.dwp.dwpservice.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import java.util.concurrent.TimeUnit;
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

/**
 *
 * @author joseph
 */
@Configuration
public class DwpConfig {

    private static final int MAX_IN_MEMORY = 16 * 1024 * 1024;
    private static final int TIMEOUT = 120_000;


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

        return HttpClient.create()
                .compress(true)
                .wiretap(false)
                .keepAlive(true)
                .secure(spec -> spec.sslContext(SslContextBuilder.forClient()))
                .protocol(HttpProtocol.HTTP11, HttpProtocol.H2)
                .tcpConfiguration(client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))
                .addHandlerLast(new WriteTimeoutHandler(TIMEOUT, TimeUnit.MILLISECONDS))));

    }

    @Bean
    public ReactorResourceFactory reactorResourceFactory() {
        return new ReactorResourceFactory();
    }
}

package com.computingfacts.api.okhttp;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.util.StopWatch;

/**
 *
 * @author joseph
 */
@Slf4j
public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Request request = chain.request();
        log.info(String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()));
        Response response = chain.proceed(request);
        stopWatch.stop();
        log.info(String.format("Received response for %s in %s",
                request.url(), stopWatch.getTotalTimeSeconds(), response.headers()));
        return response;
    }
}

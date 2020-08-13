package com.computingfacts.api.okhttp;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 *
 * @author joseph
 */
public class CacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        int maxAge = 60_000;
        Response res = response.newBuilder()
                .removeHeader("X-Cache-Info")
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "public, max-age=" + maxAge)
                .build();
        return res;
    }

}

package com.theappsolutions.boilerplate.data.data_sources.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.theappsolutions.boilerplate.data.PreferencesManager;
import com.theappsolutions.boilerplate.injection.ApplicationContext;
import com.theappsolutions.boilerplate.other.api.MockingInterceptor;
import com.theappsolutions.boilerplate.other.api.RequestsHandler;
import com.theappsolutions.boilerplate.util.other_utils.NetworkUtils;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.theappsolutions.boilerplate.util.other_utils.NetworkUtils.isNetworkConnected;

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
public class RetrofitBuilder {

    public final static String AUTHORIZATION_KEY = "Authorization";

    @NonNull
    public static Retrofit buildRetrofitInstance(@ApplicationContext Context context,
                                                 RequestsHandler requestsHandler,
                                                 Gson gson,
                                                 Map<String, String> mapHeaders,
                                                 String url,
                                                 PreferencesManager preferencesManager) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new ConnectionCheckInterceptor(context))
                .addInterceptor(new MaiHeaderInterceptor(preferencesManager))
                .addInterceptor(MockingInterceptor.create(requestsHandler))
                .addInterceptor(logging)
                .addNetworkInterceptor(chain -> {
                    Request.Builder request = chain.request().newBuilder();
                    for (Map.Entry<String, String> currentMap :
                            mapHeaders.entrySet()) {
                        request.addHeader(
                                currentMap.getKey(), currentMap.getValue());
                    }
                    return chain.proceed(request.build());
                });

        OkHttpClient httpClient = builder.build();
        return new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private static class MaiHeaderInterceptor implements Interceptor {

        private PreferencesManager preferencesManager;

        public MaiHeaderInterceptor(PreferencesManager preferencesManager) {
            this.preferencesManager = preferencesManager;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            return chain.proceed(
                    preferencesManager.getToken().map(token -> request.newBuilder()
                            .header(AUTHORIZATION_KEY, token)
                            .method(request.method(), request.body())
                            .build()).orElse(request)
            );
        }
    }

    private static class ConnectionCheckInterceptor implements Interceptor {

        private Context appContext;

        public ConnectionCheckInterceptor(Context appContext) {
            this.appContext = appContext;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            if (!isNetworkConnected(appContext)) {
                throw NetworkUtils.noConnectionException(appContext);
            }
            return chain.proceed(chain.request());
        }
    }
}

package com.andor.navigate.logit.core;

import android.content.Context;
import androidx.annotation.Nullable;
import com.andor.navigate.logit.auth.Authorization;
import com.andor.navigate.logit.core.api.LoginAPIService;
import com.andor.navigate.logit.core.api.UserAPIService;
import com.andor.navigate.logit.welcome.UserModel;
import com.google.gson.Gson;
import okhttp3.Authenticator;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Route;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class NetworkRequest {
    private static final String BASE_URL = "https://demo6030150.mockable.io/v1/";

    private final Retrofit.Builder retrofitBuilder;
    private final AuthHelper authHelper;


    public NetworkRequest(Context context) {
        retrofitBuilder = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()));
        authHelper = AuthHelper.getInstance(context);

    }

    public void postLoginRequest(String email, String password, final NetworkCallback<Authorization> callback) {
        String credentials = Utils.Companion.getAuthorizationHeader(email, password);
        Retrofit retrofit = retrofitBuilder.client(provideClient()).build();
        retrofit.create(LoginAPIService.class).loginAccount(credentials).enqueue(new Callback<Authorization>() {
            @Override
            public void onResponse(@NotNull Call<Authorization> call, @NotNull Response<Authorization> response) {
                if (response.body() != null) {
                    callback.onResponse(response.body());
                } else {
                    callback.onError("No response body found");
                }
            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {
                callback.onError(t.getMessage());

            }
        });
    }

    public void getUserdetail(final NetworkCallback<UserModel> callback) {
        Retrofit retrofit = retrofitBuilder.client(provideClientWithAuthenticator()).build();
        retrofit.create(UserAPIService.class).getUser(Objects.requireNonNull(authHelper.getIdToken())).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(@NotNull Call<UserModel> call, @NotNull Response<UserModel> response) {
                if (response.body() != null) {
                    callback.onResponse(response.body());
                } else {
                    callback.onError("No response body found");
                }
            }

            @Override
            public void onFailure(@NotNull Call<UserModel> call, @NotNull Throwable t) {
                callback.onError(t.getMessage());
            }
        });

    }

    private OkHttpClient provideClientWithAuthenticator() {
        OkHttpClient.Builder mClient = new OkHttpClient.Builder();
        mClient.connectTimeout(30, TimeUnit.SECONDS);
        mClient.readTimeout(30, TimeUnit.SECONDS);
        mClient.writeTimeout(30, TimeUnit.SECONDS);
        mClient.authenticator(new Authenticator() {
            @Nullable
            @Override
            public Request authenticate(@Nullable Route route, okhttp3.Response response) throws IOException {
                if (authHelper.isLoggedIn() && (response.code() == 401 || response.code() == 403)) {
                    String credentials = Utils.Companion.getAuthorizationHeader(authHelper.getEmail(), authHelper.getPassword());
                    Retrofit retrofit = retrofitBuilder.client(provideClient()).build();
                    Response<Authorization> executeResponse = retrofit.create(LoginAPIService.class).loginAccount(credentials).execute();
                    if (executeResponse.isSuccessful() && executeResponse.body() != null) {
                        return response.request().newBuilder()
                                .header("Auth-Token", Objects.requireNonNull(executeResponse.body().getToken()))
                                .build();
                    }
                }
                return null;
            }
        });
        return mClient.build();
    }

    private OkHttpClient provideClient() {
        OkHttpClient.Builder mClient = new OkHttpClient.Builder();
        mClient.connectTimeout(30, TimeUnit.SECONDS);
        mClient.readTimeout(30, TimeUnit.SECONDS);
        mClient.writeTimeout(30, TimeUnit.SECONDS);
        return mClient.build();
    }
}

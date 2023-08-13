package com.akmal.androidtasklessons.di

import android.content.Context
import com.akmal.androidtasklessons.R
import com.akmal.androidtasklessons.data.remote.apiService.ApiService
import com.akmal.androidtasklessons.utils.Constants
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {

    @Provides
    @Singleton
    fun provideInterceptorMobile(
        @ApplicationContext context: Context
    ): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
            val requestBuilder = request.newBuilder()
            requestBuilder.addHeader("Connection", "close")
            try {
                chain.proceed(requestBuilder.build())
            } catch (e: Exception) {
                Response.Builder()
                    .request(requestBuilder.build())
                    .protocol(Protocol.HTTP_2)
                    .code(500)
                    .message(context.getString(R.string.sww))
                    .body("{${e}}".toResponseBody(null)).build()
            }
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        interceptor: Interceptor,
        @ApplicationContext context: Context
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(ChuckerInterceptor.Builder(context).build())
            .addInterceptor(interceptor)
            .retryOnConnectionFailure(false)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        gson: Gson,
        client: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}
package com.humbjorch.myapplication.sis.di

import com.humbjorch.myapplication.data.datSource.api.remote.WebService
import com.humbjorch.myapplication.sis.utils.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

        @Provides
        @Singleton
        fun provideLoggingInterceptor(): HttpLoggingInterceptor {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            return loggingInterceptor
        }

        @Provides
        @Singleton
        fun provideHttpBuilder(logging: HttpLoggingInterceptor): OkHttpClient {
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(logging)
            builder.connectTimeout(10, TimeUnit.SECONDS)
            builder.writeTimeout(10, TimeUnit.SECONDS)
            builder.readTimeout(10, TimeUnit.SECONDS)
            return builder.build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        }

        @Provides
        @Singleton
        fun provideWebService(retrofit: Retrofit): WebService {
            return retrofit.create(WebService::class.java)
        }


}
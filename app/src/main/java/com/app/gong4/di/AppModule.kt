package com.app.gong4.di

import com.app.gong4.MainApplication
import com.app.gong4.api.QnaService
import com.app.gong4.api.StudyGroupService
import com.app.gong4.api.UserCategoryService
import com.app.gong4.api.UserService
import com.app.gong4.utils.Constants.BASE_URL
import com.app.gong4.utils.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesNetworkInterceptor() = Interceptor { chain ->
        val token = MainApplication.tokenManager.getAccessToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        val response = chain.proceed(newRequest)

        response.newBuilder().build()
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient
            .Builder()
            .addInterceptor(providesNetworkInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideStudyGroupService(retrofit: Retrofit): StudyGroupService = retrofit.create(StudyGroupService::class.java)

    @Provides
    @Singleton
    fun provideCategoryService(retrofit: Retrofit): UserCategoryService = retrofit.create(UserCategoryService::class.java)

    @Provides
    @Singleton
    fun provideQnaService(retrofit: Retrofit): QnaService = retrofit.create(QnaService::class.java)

}
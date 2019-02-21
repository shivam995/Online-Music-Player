package com.mplayer.soundcast.restclient

import com.google.gson.JsonObject
import com.mplayer.soundcast.BuildConfig
import com.mplayer.soundcast.music_library.model.MusicLibraryDao
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import java.io.IOException
import java.util.concurrent.TimeUnit


/**
 * Created by Shivam Jaiswal on 22/03/18.
 */
interface RestClient {

    companion object {

        private const val BASE_URL = "https://soundcast.back4app.io/"


        //Create a new Interceptor
        private var headerAuthorizationInterceptor: Interceptor = object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                var request = chain.request()
                val headers = request.headers().newBuilder()
                    .add("X-Parse-Application-Id",BuildConfig.ParseApplicationKey)
                    .add("X-Parse-REST-API-Key", BuildConfig.ParseRestApiKey).build()
                request = request.newBuilder().headers(headers).build()
                return chain.proceed(request)
            }
        }

        private val okHttpClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectionPool(ConnectionPool(3, 60, TimeUnit.SECONDS))
            .retryOnConnectionFailure(true)
            .addInterceptor(headerAuthorizationInterceptor)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .build()

        private var retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()



        fun getClient(): RestClient{
            return retrofit.create(RestClient::class.java)
        }
    }

    @GET("classes/songs_library")
    fun getMusicLibrary(): Call<MusicLibraryDao>


//    @POST("auction/create_auction")
//    fun createAuction(@Body data: JsonObject): Call<ResponseBody>


}

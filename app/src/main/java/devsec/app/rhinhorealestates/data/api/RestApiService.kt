
package devsec.app.rhinhorealestates.api

import devsec.app.rhinhorealestates.data.api.ChangeRequest
import devsec.app.rhinhorealestates.data.api.CodeRequest
import devsec.app.rhinhorealestates.data.api.EmailRequest
import devsec.app.rhinhorealestates.data.models.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import devsec.app.rhinhorealestates.data.api.UserRequest
import okhttp3.RequestBody

interface RestApiService {


    //*********************** Sign up/in ***********************//
    @Headers("Content-Type:application/json")
    @POST("user/Signin")
    fun loginUser(@Body info: UserRequest): Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("user/compte")
    fun registerUser(
        @Body info: User
    ): Call<ResponseBody>

    //*********************** User ***********************//
    @GET("user")
    fun getUsers(): Call<List<User>>

    @POST("user/resetpassword")
    fun verifcode(@Body code: CodeRequest): Call<Unit>
    @POST("user/changepwd")
    fun changePass(@Body code: ChangeRequest): Call<Unit>

    @POST("user/resetpwd")
    fun generatecode(@Body email: EmailRequest): Call<Unit>

    @GET("user/{id}")
    fun getUser(@Path("id") id: String): Call<User>

    @GET("/user/getNews")
    fun getNews(): Call<List<New>>
    @POST("/user/getnew")
    fun getNewsbySearch(@Body search: String): Call<List<New>>

    @Headers("Content-Type:application/json")
    @PATCH("user/{id}")
    fun updateUser(@Path("id") id: String, @Body user: User): Call<User>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: String): Call<ResponseBody>





    @Multipart
    @Headers("Content-Type:application/json")
    @POST("user/updateUser/{id}")
    fun updatewithimage(
        @Path("id") id: String,
        @Part("first_name") firstname: RequestBody,
        @Part("last_name") last_name: RequestBody,
        @Part("email") email: RequestBody,
        @Part image: MultipartBody.Part,
        ): Call<ResponseBody>




    @Multipart
    @POST("uploadfile")
    fun uploadImage(
        @Part myFile: MultipartBody.Part
    ): Call<ResponseBody>




}

class RetrofitInstance {
    companion object {

        const val BASE_URL: String = "http://192.168.1.168:5000/"
     // const val BASE_URL: String = "http://192.168.0.11:9090/api/"


        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client: OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()
        fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}



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

    @GET("user/profil/{id}")
    fun getUser(@Path("id") id: String): Call<User>

    @GET("/user/getNews")
    fun getNews(): Call<List<New>>


    @GET("/categorie/")
    fun getCategories(): Call<List<Categories>>


    @GET("/user/allAvocat")
    fun getAvocat(): Call<List<UserTest>>

    @GET("/user/getAllusers")
    fun getClient(): Call<List<UserTest>>

    @GET("user/getbycategorie/{categorie}")
    fun getAvocatByCategorie(@Path("categorie") categorie:String): Call<List<UserTest>>

    @POST("/user/getnew")
    fun getNewsbySearch(@Body search: String): Call<List<New>>

    @Headers("Content-Type:application/json")
    @PATCH("user/{id}")
    fun updateUser(@Path("id") id: String, @Body user: User): Call<User>

    @DELETE("user/{id}")
    fun deleteUser(@Path("id") id: String): Call<ResponseBody>




    @Multipart
    @PUT("user/updatepic/{id}")
    suspend fun updatePic(
        @Path("id") id: String,
        @Part image:RequestBody
    ): Call<ResponseBody>




    @Headers("Content-Type:application/json")
    @POST("user/updateUser/{id}")
    fun update(
        @Path("id") id: String,
        @Body info: User
        ): Call<ResponseBody>

    @Headers("Content-Type:application/json")
    @POST("user/UpdateAvocat/{id}")
    fun updateUserForUpdateAvocat(
        @Path("id") id: String,
        @Body info: User
    ): Call<ResponseBody>


    @Multipart
    @POST("user/uploadfile/{id}")
    fun uploadImageProfile(
        @Path("id") id: String,
        @Part myFile: MultipartBody.Part
    ): Call<ResponseBody>

    @Multipart
    @POST("uploadfile")
    fun uploadImage(
        @Part myFile: MultipartBody.Part
    ): Call<ResponseBody>
    ///Case/////
    @GET("Case/getCaseByAvocat/{id}")
    fun getCasesbyAvocat(@Path("id") id: String): Call<List<Case>>

    @Headers("Content-Type:application/json")
    @POST("Case/add")
    fun addCase(
        @Body info: Case
    ): Call<ResponseBody>
    ///Packs////

    @Headers("Content-Type:application/json")
    @POST("Pack/add")
    fun addPack(
        @Body info: Pack
    ): Call<ResponseBody>

    @GET("user/getpack/{id}")
    fun getPackbyAvocat(@Path("id") id: String): Call<List<Pack>>

//Appointement//
    @Headers("Content-Type:application/json")
    @POST("RendezVous/getbyAvocat/{idAvocat}")
    fun getdatesbyAvocat(@Path("idAvocat") id: String): Call<List<Appointement>>

    @Headers("Content-Type:application/json")
    @POST("RendezVous/getbyClient/{idUser}")
    fun getdatesbyClient(@Path("idUser") id: String): Call<List<Appointement>>

    @Headers("Content-Type:application/json")
    @POST("RendezVous/add")
    fun addAppointement(@Body info: Appointement): Call<ResponseBody>

}

class RetrofitInstance {
    companion object {


        const val BASE_URL: String = "http://10.0.2.2:5000/"
        const val BASE_IMG: String = "http://10.0.2.2:5000/img/"



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


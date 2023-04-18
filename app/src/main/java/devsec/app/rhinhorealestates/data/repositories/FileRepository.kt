package devsec.app.rhinhorealestates.data.repositories

import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import okhttp3.MultipartBody

class FileRepository {

    suspend fun uploadFile(file: MultipartBody.Part) = RetrofitInstance.getRetrofitInstance()
        .create(RestApiService::class.java).uploadImage(file)

}
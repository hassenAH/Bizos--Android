package devsec.app.rhinhorealestates.data.api

import retrofit2.http.Body
import retrofit2.http.POST

interface MessageService {

    @POST("/msg")
    suspend fun getMessages(
        @Body message:MessageRequest
    ): List<MessageResponse>
}
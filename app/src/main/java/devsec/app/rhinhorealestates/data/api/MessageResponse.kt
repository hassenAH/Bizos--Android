package devsec.app.rhinhorealestates.data.api


data class MessageResponse(
    val id: String,
    val from: String,
    val to: String,
    val text: String
)
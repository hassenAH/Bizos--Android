package devsec.app.rhinhorealestates.data.models

data  class User (
    val  _id :  String ,
    val  first_name :  String ,
    val lastname: String,
    val  email :  String ,
    val  password :  String,
    val specialiste :String? = null,
    val experience: Int? = null,
    val image: String? = null
) {




}

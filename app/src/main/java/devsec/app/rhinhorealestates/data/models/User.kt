package devsec.app.rhinhorealestates.data.models

data  class User (
    val  id :  String ,
    val  username :  String ,
    val  email :  String ,
    val  password :  String
) {

    constructor(id: String) : this(id,"","","")
    constructor(username: String, password: String, email: String) : this("", username, email, password)
   // constructor(id: String) : this(id,"","","","","")

}
//    val  photo :  String
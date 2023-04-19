package devsec.app.rhinhorealestates.data.models

data  class User (
    val  id :  String ,
    val  firstname :  String ,
    val lastname: String,
    val  email :  String ,
    val  password :  String
) {

    constructor(id: String) : this(id,"","","","")


}
//    val  photo :  String
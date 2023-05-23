package devsec.app.rhinhorealestates.data.models

import java.util.Date


data  class Appointement (



    val  idUser :  String ,
    val Username: String,
    val categorie:  String,
    val  Date:  Date,
    val  idAvocat:   String,
    var isExpandable: Boolean = false

)
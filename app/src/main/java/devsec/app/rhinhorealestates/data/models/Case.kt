package devsec.app.rhinhorealestates.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data  class Case (
    val  title:  String ,
    val  traite :  Boolean ,
    val description: String,
    val  nameUser :  String ,
    val  LastnameUser :  String,
    val  Prix:   Number,
    val  idAvocat:   String
) : java.io.Serializable, Parcelable
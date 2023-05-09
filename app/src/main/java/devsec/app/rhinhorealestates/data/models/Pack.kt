package devsec.app.rhinhorealestates.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data  class Pack (
    val  title:  String ,
    val id:String,
    val description: String,
    val  name :  String ,
    val  prix:   Number,

) : java.io.Serializable, Parcelable
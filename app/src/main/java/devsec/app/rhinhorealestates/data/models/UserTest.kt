package devsec.app.rhinhorealestates.data.models

import android.os.Parcel
import android.os.Parcelable

data  class UserTest (
    val  _id :  String ,
    val  first_name :  String ,
    val last_name: String,
    val  email :  String ,
    val  password :  String,
    val specialite :String? = null,
    val experience: Int? = null,
    val image: String? = null,
    val location:String?=null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(first_name)
        parcel.writeString(last_name)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(specialite)
        parcel.writeValue(experience)
        parcel.writeString(image)
        parcel.writeString(location)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserTest> {
        override fun createFromParcel(parcel: Parcel): UserTest {
            return UserTest(parcel)
        }

        override fun newArray(size: Int): Array<UserTest?> {
            return arrayOfNulls(size)
        }
    }
}






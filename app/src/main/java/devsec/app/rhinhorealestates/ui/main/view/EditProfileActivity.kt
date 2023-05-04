package devsec.app.rhinhorealestates.ui.main.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.models.User
import devsec.app.rhinhorealestates.utils.services.RealPathUtil
import devsec.app.rhinhorealestates.utils.session.SessionPref
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    lateinit var sessionPref: SessionPref
    lateinit var id : String
    lateinit var image : String
    lateinit var file : File

    lateinit var user : HashMap<String, String>
    companion object {
        const val REQUEST_IMAGE_PICK = 1
    }
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        sessionPref = SessionPref(this)
        user = sessionPref.getUserPref()
        image = user.get(SessionPref.USER_IMAGE).toString()
        val firstname = findViewById<TextView>(R.id.editProfilefirstname)
        val email = findViewById<TextView>(R.id.editProfileEmail)

        val lastname = findViewById<TextView>(R.id.editProfilelastname)

        val updateButton = findViewById<Button>(R.id.updateProfileButton)
        val imageView: ShapeableImageView = findViewById(R.id.editProfileImage)
        val url = "http://172.16.8.136:5000/img/"+image
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.logo)
            .into(imageView)

        id = user.get(SessionPref.USER_ID).toString()
        firstname.text = user.get(SessionPref.USER_firstname)
        email.text = user.get(SessionPref.USER_EMAIL)
        lastname.text = user.get(SessionPref.USER_lastname)


        imageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        val toolbar = findViewById<Toolbar>(R.id.editProfileToolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        updateButton.setOnClickListener {
            val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)

            val requestBody = RequestBody.create(MediaType.parse("image"), file)
            val imagePart = MultipartBody.Part.createFormData("image", file.name, requestBody)
            val namePart = RequestBody.create(MediaType.parse("text/plain"), firstname.text.toString())
            val emailPart = RequestBody.create(MediaType.parse("text/plain"),email.text.toString(),)
            val lastnamepart = RequestBody.create(MediaType.parse("text/plain"), lastname.text.toString())
            Log.d("RequestBody", requestBody.toString())
            Toast.makeText( applicationContext, requestBody.toString(), Toast.LENGTH_SHORT).show()
            val call = retIn.updatewithimage(id,namePart,lastnamepart,emailPart,imagePart)
            call.enqueue(object : Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {

                        Toast.makeText( applicationContext, "Account deleted", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    Toast.makeText(applicationContext, "Error deleting account", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }
    fun getRealPathFromURI(uri: Uri?): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = uri?.let { contentResolver.query(it, projection, null, null, null) }
        val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val filePath = cursor?.getString(columnIndex ?: 0)
        cursor?.close()
        return filePath ?: ""
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data

            // Get the file path of the selected image using the RealPathUtil class
            val filePath = imageUri?.let { RealPathUtil.getRealPathFromURI_API19(this, it) }

            // Create a RequestBody object from the selected image file
            val imageFile = File(filePath)
            val requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)

            // Create a MultipartBody.Part object from the RequestBody object
            val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)

            val imageView: ShapeableImageView = findViewById(R.id.editProfileImage)
            imageView.setImageURI(imageUri)

            // Load the image into an ImageView or do something else with it
        }
    }


}
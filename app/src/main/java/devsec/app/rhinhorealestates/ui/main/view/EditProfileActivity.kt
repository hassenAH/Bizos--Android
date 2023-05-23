package devsec.app.rhinhorealestates.ui.main.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.ContentValues.TAG
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle

import android.provider.MediaStore
import android.util.Log

import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.github.dhaval2404.imagepicker.ImagePicker

import com.permissionx.guolindev.PermissionX
import com.squareup.picasso.Picasso
import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.models.User

import devsec.app.rhinhorealestates.utils.session.SessionPref
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.*



class EditProfileActivity : AppCompatActivity() {
    lateinit var sessionPref: SessionPref
    lateinit var id : String
    lateinit var image : String
    lateinit var file : File
    lateinit var imageButton : ImageButton
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

        val url = "http://172.16.4.178:5000/img/"+image


        id = user.get(SessionPref.USER_ID).toString()
        firstname.text = user.get(SessionPref.USER_firstname)
        email.text = user.get(SessionPref.USER_EMAIL)
        lastname.text = user.get(SessionPref.USER_lastname)




        val toolbar = findViewById<Toolbar>(R.id.editProfileToolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        imageButton = findViewById(R.id.editProfileImage)
        val imageUrl = RetrofitInstance.BASE_IMG+user[SessionPref.USER_IMAGE] // Replace with the URL of the server image
        Picasso.get().load(imageUrl).into(imageButton)
        val startForImageResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == Activity.RESULT_OK) {
                    val uri: Uri = data?.data!!
                    val file = uri.path?.let { File(it) }
                    Log.d(TAG, "testinggg testinggg: ${file!!.name}")
                    multipartImageUpload(file)
                    imageButton.setImageURI(uri)
                } else if (resultCode == ImagePicker.RESULT_ERROR) {
                    Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT)
                        .show()
                }
            }

        imageButton.setOnClickListener {

            PermissionX.init(this).permissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ).request { allGranted, _, _ ->
                if (allGranted) {
                    ImagePicker.with(this).compress(1024).crop().createIntent {
                        startForImageResult.launch(it)
                    }
                }
            }


        }

        updateButton.setOnClickListener {
            val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)



            val user = User("",firstname.text.toString() ,lastname.text.toString(),email.text.toString(),"","",0,"")
            val call = retIn.update(id,user)
            call.enqueue(object : Callback<ResponseBody> {

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {

                        Toast.makeText( applicationContext, "Account updated", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    Toast.makeText(applicationContext, "Error deleting account", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }




    var picUri: Uri? = null
    private val IMAGE_RESULT = 200
    var textView: TextView? = null








    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_RESULT -> {
                    //val imageUri = getImageFilePath(data)
                    // Get the bitmap from the URI
                    val uri = data?.data!!
                    uri.path?.let {
                        var file = File (it)
                        multipartImageUpload(file)
                        Log.d("testing gg", "onActivityResult: ${file.name}")
                        imageButton.setImageURI(uri)
                    }

                    //mBitmap = BitmapFactory.decodeFile(imageUri)
                    //if (mBitmap != null) multipartImageUpload() else {
                    // Toast.makeText(applicationContext, "Bitmap is null. Try again", Toast.LENGTH_SHORT).show()
                    //  }
                }
            }
        }
    }

    private fun getImageFromFilePath(data: Intent?): String? {
        val isCamera = data == null || data.data == null
        return getPathFromURI(data!!.data)
    }

    fun getImageFilePath(data: Intent?): String? {
        return getImageFromFilePath(data)
    }

    private fun getPathFromURI(contentUri: Uri?): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = contentUri?.let { contentResolver.query(it, proj, null, null, null) }
        val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        if (column_index == null || cursor.count == 0) {
            cursor?.close()
            return null
        }
        cursor.moveToFirst()
        val path = cursor.getString(column_index)
        cursor.close()
        return path
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("pic_uri", picUri)
    }

    private fun multipartImageUpload(file: File?) {
        file?.name?.let { Log.e("Upload", it) }

        val mediaType = getMediaType(file?.extension)
        val requestBody = file?.asRequestBody(mediaType)
        val body: MultipartBody.Part? = requestBody?.let {
            MultipartBody.Part.createFormData("image", file?.name, it)
        }

        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        val call = body?.let { retIn.uploadImageProfile(id, it) }
        if (call != null) {
            call.enqueue(object : Callback<ResponseBody> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    Log.e("Upload", response.body().toString())
                    if (response.code() == 200) {
                        textView?.text = "Uploaded Successfully!"
                    }
                    Log.d("Testin image test", response.body().toString())
                    Toast.makeText(
                        applicationContext,
                        response.code().toString() + " ",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, "Request failed", Toast.LENGTH_SHORT).show()
                    t.printStackTrace()
                }
            })
        }
    }

    private fun getMediaType(extension: String?): MediaType? {
        return when (extension?.toLowerCase()) {
            "png" -> "image/png".toMediaTypeOrNull()
            "jpg", "jpeg" -> "image/jpeg".toMediaTypeOrNull()
            "gif" -> "image/gif".toMediaTypeOrNull()
            else -> null
        }
    }
}



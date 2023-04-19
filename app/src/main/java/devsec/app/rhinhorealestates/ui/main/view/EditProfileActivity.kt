package devsec.app.rhinhorealestates.ui.main.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.models.User
import devsec.app.rhinhorealestates.utils.session.SessionPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditProfileActivity : AppCompatActivity() {
    lateinit var sessionPref: SessionPref
    lateinit var id : String
    lateinit var user : HashMap<String, String>

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        sessionPref = SessionPref(this)

        val username = findViewById<TextView>(R.id.editProfilelastname)
        val email = findViewById<TextView>(R.id.editProfileEmail)

        val password = findViewById<TextView>(R.id.editProfilefirstname)

        val updateButton = findViewById<Button>(R.id.updateProfileButton)

        user = sessionPref.getUserPref()
        id = user.get(SessionPref.USER_ID).toString()
        username.text = user.get(SessionPref.USER_firstname)
        email.text = user.get(SessionPref.USER_EMAIL)
        password.text = user.get(SessionPref.USER_lastname)



        val toolbar = findViewById<Toolbar>(R.id.editProfileToolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }

        updateButton.setOnClickListener {
            val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
            val userPref =
                User(id,username.text.toString(),password.text.toString(),email.text.toString(),"")
            val call = retIn.updateUser(
                user.get(SessionPref.USER_ID).toString(),
               userPref
            )
            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@EditProfileActivity, "Profile Updated", Toast.LENGTH_SHORT).show()
                        sessionPref.setUserPref(username.text.toString(), email.text.toString(), password.text.toString() )
                        val intent = Intent(this@EditProfileActivity, MainMenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@EditProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@EditProfileActivity, "Failed to update profile", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }

}
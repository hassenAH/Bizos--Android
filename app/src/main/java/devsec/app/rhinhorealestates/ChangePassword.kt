package devsec.app.RhinhoRealEstates

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import devsec.app.RhinhoRealEstates.databinding.ActivityChangePassBinding
import devsec.app.RhinhoRealEstates.databinding.ActivityChangePasswordBinding
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.api.ChangeRequest
import devsec.app.rhinhorealestates.ui.main.view.LoginActivity
import devsec.app.rhinhorealestates.utils.services.LoadingDialog
import devsec.app.rhinhorealestates.utils.session.SessionPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePassword : AppCompatActivity() {
    lateinit var changebtn : Button
    lateinit var sessionPref: SessionPref
    lateinit var ePass: EditText
    lateinit var eConfirm: EditText

    lateinit var loadingDialog: LoadingDialog
    lateinit var user : HashMap<String, String>
    private lateinit var binding: ActivityChangePasswordBinding
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        sessionPref = SessionPref(this)
        user = sessionPref.getUserPref()
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)
        sessionPref = SessionPref(this)

        val email = user.get(SessionPref.USER_EMAIL)
        ePass = findViewById(R.id.passwordchangeInputEditText)
        eConfirm = findViewById(R.id.passwordchangeInputEditText2)

        changebtn = findViewById(R.id.btn_Confirm)
        val toolbar = findViewById<Toolbar>(R.id.changepassToolbar)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        changebtn.setOnClickListener {
            if (validateCode()) {
                if (email != null) {
                    validate(email)
                }
            }
        }
    }


private fun validateCode(): Boolean {
    val passwordText = ePass.text.toString().trim()

    if (passwordText.isEmpty()) {
        ePass.error = "Code is required"
        ePass.requestFocus()
        return false
    }
    val confirmText = eConfirm.text.toString().trim()

    if (confirmText.isEmpty()) {
        eConfirm.error = "Code is required"
        eConfirm.requestFocus()
        return false
    }
    if(confirmText != passwordText )
    {
        eConfirm.error = "Code is required"
        eConfirm.requestFocus()
        ePass.requestFocus()
        return false
    }

    return true
}
private fun validate(email:String) {
    loadingDialog.startLoadingDialog()
    val ePass = binding.passwordchangeInputEditText.text.toString()

    val codeRequest = ChangeRequest(email,ePass)
    Log.d("ChangepassActivity", "Code: $ePass")
    val apiService = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
    apiService.changePass(codeRequest).enqueue(object : Callback<Unit> {
        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            loadingDialog.dismissDialog()

            if (response.isSuccessful) {
                Toast.makeText(this@ChangePassword, "change succes", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@ChangePassword, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                Log.e("CodeActivity", "Error occurred: $errorMessage")

                Toast.makeText(this@ChangePassword, "Problem", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Unit>, t: Throwable) {
            loadingDialog.dismissDialog()
            Log.e("changeActivity", "Network error occurred", t)

            Toast.makeText(this@ChangePassword, "Verify Code", Toast.LENGTH_SHORT).show()
        }
    })
}
}
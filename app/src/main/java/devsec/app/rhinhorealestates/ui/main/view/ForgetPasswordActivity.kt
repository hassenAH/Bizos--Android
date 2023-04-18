package devsec.app.rhinhorealestates.ui.main.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import devsec.app.RhinhoRealEstates.R
import devsec.app.RhinhoRealEstates.databinding.ActivityForgetPasswordBinding
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.utils.services.LoadingDialog
import devsec.app.rhinhorealestates.utils.session.SessionPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import devsec.app.rhinhorealestates.data.api.EmailRequest

class ForgetPasswordActivity : AppCompatActivity() {

    lateinit var sessionPref: SessionPref
    lateinit var loadingDialog: LoadingDialog
    lateinit var sendcodebtn : Button
    lateinit var etEmail: EditText
    private lateinit var binding: ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)
        sessionPref = SessionPref(this)

        etEmail = findViewById(R.id.EmailEditText2)
        sendcodebtn = findViewById(R.id.ForgetBtn)

        sendcodebtn.setOnClickListener {
            if (validateForget()) {
                forget()
            }
        }
    }

    private fun validateForget(): Boolean {
        val emailText = etEmail.text.toString().trim()
        if (emailText.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return false
        }
        return true
    }

    private fun forget() {
        loadingDialog.startLoadingDialog()
        val email = binding.EmailEditText2.text.toString()
        val emailRequest = EmailRequest(email)
        Log.d("ForgetPasswordActivity", "Email: $email")
        val apiService = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        apiService.generatecode(emailRequest).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                loadingDialog.dismissDialog()

                if (response.isSuccessful) {
                    Toast.makeText(this@ForgetPasswordActivity, "Code sent to your email", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ForgetPasswordActivity, CodeActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                    Log.e("ForgetPasswordActivity", "Error occurred: $errorMessage")

                    Toast.makeText(this@ForgetPasswordActivity, "Send Code Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                loadingDialog.dismissDialog()
                Log.e("ForgetPasswordActivity", "Network error occurred", t)

                Toast.makeText(this@ForgetPasswordActivity, "Send Code Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

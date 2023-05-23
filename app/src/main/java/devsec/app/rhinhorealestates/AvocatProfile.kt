package devsec.app.RhinhoRealEstates

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import devsec.app.RhinhoRealEstates.databinding.ActivityAvocatProfileBinding
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance

import devsec.app.rhinhorealestates.data.Adapter.PackAdapters

import devsec.app.rhinhorealestates.data.models.Pack
import devsec.app.rhinhorealestates.data.models.UserTest
import devsec.app.rhinhorealestates.ui.main.view.ChatActivity

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AvocatProfile : AppCompatActivity() {
    private lateinit var user: UserTest
    private lateinit var rvPack: RecyclerView
    private lateinit var packAdapter: PackAdapters
    private lateinit var mainView: ActivityAvocatProfileBinding
    lateinit var SearchEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mainView = ActivityAvocatProfileBinding.inflate(layoutInflater)
        setContentView(mainView.root)
val btn = findViewById<Button>(R.id.add_Appointement)
val btnmsg = findViewById<Button>(R.id.messagebtn)

        // Récupérer les données de l'intent
        user = intent.getParcelableExtra("user")!!

        // Utiliser les données pour afficher les détails de l'avocat
        mainView.profileLayout.tvFullname.text=user.first_name+"  "+user.last_name
        mainView.profileLayout.tvSpeciality.text=user.specialite
        mainView.profileLayout.tvAddress.text=user.location
        mainView.profileLayout.experienseProfile.text="Experience : "+user.experience.toString()
        Glide.with(this)
            .load(RetrofitInstance.BASE_IMG+user.image) // Remplacez imageUrl par l'URL réelle de l'image
            .into(mainView.profileLayout.ivPic)




        rvPack = findViewById(R.id.recyclerViewPacks)

        val id: String? = intent.getStringExtra("idAvocat")


        btn.setOnClickListener {

            val profileFragment = addAppointement.newInstance(id!!)
            supportFragmentManager.beginTransaction()
                .replace(R.id.swipe_refresh_layout, profileFragment)
                .addToBackStack(null)
                .commit()

        }
        btnmsg.setOnClickListener {
            val intent = Intent(this@AvocatProfile, ChatActivity::class.java)
            intent.putExtra("user", id)

            startActivity(intent)
        }
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvPack.layoutManager = layoutManager
        packAdapter = PackAdapters(listOf())
        rvPack.adapter = packAdapter



        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn.getPackbyAvocat(id!!).enqueue(object : Callback<List<Pack>> {
            override fun onResponse(call: Call<List<Pack>>, response: Response<List<Pack>>) {
                if (response.isSuccessful) {
                    val packs = response.body();
                    if (packs != null) {
                        packAdapter.Packs = packs // update the adapter with the retrieved cars
                        packAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(TAG, "Failed to get Cases: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Pack>>, t: Throwable) {
                Log.e(TAG, "Failed to get Cases", t)
            }
        })



    }
}
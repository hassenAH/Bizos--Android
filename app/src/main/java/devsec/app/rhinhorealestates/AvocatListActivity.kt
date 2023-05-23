package devsec.app.RhinhoRealEstates

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.Adapter.UsersAdapter
import devsec.app.rhinhorealestates.data.models.UserTest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class AvocatListActivity : AppCompatActivity() {

    private lateinit var rvUsers: RecyclerView
    private lateinit var usersAdapter: UsersAdapter
    lateinit var SearchEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_avocat_by_categorie)

        SearchEditText = findViewById(R.id.SearchinputA)

        rvUsers = findViewById(R.id.Avocatsitem_recycler_view)

        val catName: String? = intent.getStringExtra("catname")
        Log.d(TAG, "onCreate: hellooo $catName")

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvUsers.layoutManager = layoutManager
        usersAdapter = UsersAdapter(listOf())
        rvUsers.adapter = usersAdapter



        val retIn1 = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn1.getAvocatByCategorie(catName!!).enqueue(object : Callback<List<UserTest>> {
            override fun onResponse(call: Call<List<UserTest>>, response: Response<List<UserTest>>) {
                if (response.isSuccessful) {
                    val users = response.body();
                    if (users != null) {
                        usersAdapter.users = users// update the adapter with the retrieved cars
                        usersAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(TAG, "Failed to get avocats: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<UserTest>>, t: Throwable) {
                Log.e(TAG, "Failed to get avocats", t)
            }
        })

        // Add setOnItemClickListener to the RecyclerView

    }

    companion object {
        private const val TAG = "AvocatListActivity"
    }
}

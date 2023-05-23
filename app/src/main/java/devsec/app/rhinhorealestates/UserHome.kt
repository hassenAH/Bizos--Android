package devsec.app.RhinhoRealEstates

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import devsec.app.RhinhoRealEstates.databinding.ActivityMainMenuBinding
import devsec.app.RhinhoRealEstates.databinding.FragmentHomeBinding
import devsec.app.RhinhoRealEstates.databinding.FragmentUserHomeBinding
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.Adapter.CategoriesAdapter
import devsec.app.rhinhorealestates.data.Adapter.UsersAdapter

import devsec.app.rhinhorealestates.data.models.Categories
import devsec.app.rhinhorealestates.data.models.New
import devsec.app.rhinhorealestates.data.models.User
import devsec.app.rhinhorealestates.data.models.UserTest
import devsec.app.rhinhorealestates.ui.main.view.LoginActivity
import devsec.app.rhinhorealestates.ui.main.view.SplashScreenActivity
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class user_home : Fragment() {

    private lateinit var devenirAvocat: TextView
    private lateinit var rvCategories: RecyclerView
    private lateinit var rvUsers: RecyclerView
    private lateinit var categorieAdapter: CategoriesAdapter
    private lateinit var userAdapter: UsersAdapter
    lateinit var SearchEditText: EditText
    lateinit var card: CardView
    private lateinit var binding:FragmentUserHomeBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_user_home, container, false)

        SearchEditText = view.findViewById(R.id.Searchinput1)
        rvCategories = view.findViewById(R.id.recyclerViewCategorie)
        rvUsers=view.findViewById(R.id.recyclerViewUser)
        devenirAvocat = view.findViewById(R.id.devenirAvocat)

        // rvnews.layoutManager = LinearLayoutManager(requireContext())
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvCategories.layoutManager = layoutManager
        categorieAdapter = CategoriesAdapter(listOf())
     // create an empty adapter
        rvCategories.adapter = categorieAdapter

        val layoutManager1 = LinearLayoutManager(requireContext())
        layoutManager1.orientation = LinearLayoutManager.HORIZONTAL
        rvUsers.layoutManager = layoutManager1

        userAdapter = UsersAdapter(listOf())
        rvUsers.adapter = userAdapter

        devenirAvocat.setOnClickListener {
            var intent = Intent(requireContext(), DevenirAvocat::class.java)
            startActivity(intent)
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn.getCategories().enqueue(object : Callback<List<Categories>> {

            override fun onResponse(
                call: Call<List<Categories>>,
                response: Response<List<Categories>>
            ) {
                if (response.isSuccessful) {
                    val categorie = response.body();
                    if (categorie != null) {
                        categorieAdapter.categories = categorie// update the adapter with the retrieved cars
                        categorieAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(TAG, "Failed to get cars: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Categories>>, t: Throwable) {
                Log.e(TAG, "Failed to get cars", t)
            }

        }

        )
        val retIn1 = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn1.getAvocat().enqueue(object : Callback<List<UserTest>> {
            override fun onResponse(call: Call<List<UserTest>>, response: Response<List<UserTest>>) {


                if (response.isSuccessful) {
                    val users = response.body();
                    if (users != null) {
                        userAdapter.users = users// update the adapter with the retrieved cars
                        userAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(TAG, "Failed to get avocats: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<UserTest>>, t: Throwable) {
                Log.e(TAG, "Failed to get avocats", t)
            }


        }

        )

    }


    companion object {
        private const val TAG = "newsFragment"
    }
}

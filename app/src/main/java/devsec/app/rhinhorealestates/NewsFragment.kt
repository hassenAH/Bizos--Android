package devsec.app.RhinhoRealEstates

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.Adapter.NewAdapter
import devsec.app.rhinhorealestates.data.models.New
import okhttp3.ResponseBody

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class NewsFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private lateinit var rvnews: RecyclerView
    private lateinit var NewAdapter: NewAdapter
    lateinit var SearchEditText: TextInputLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        SearchEditText = view.findViewById(R.id.Searchinput)
        rvnews = view.findViewById(R.id.Newsitem_recycler_view)
        rvnews.layoutManager = LinearLayoutManager(requireContext())
        NewAdapter = NewAdapter(listOf()) // create an empty adapter
        rvnews.adapter = NewAdapter
        SearchEditText.editText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_SEARCH) {
                // User has finished typing, perform action
                val searchText = SearchEditText.editText?.text.toString()
                // do something with searchText
                val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
                retIn.getNewsbySearch(searchText).enqueue(object : Callback<List<New>> {
                    override fun onResponse(call: Call<List<New>>, response: Response<List<New>>) {
                        if (response.isSuccessful) {
                            val news = response.body();
                            if (news != null) {
                                NewAdapter.news = news // update the adapter with the retrieved cars
                                NewAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                            }
                        } else {
                            Log.e(TAG, "Failed to get cars: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<List<New>>, t: Throwable) {
                        Log.e(TAG, "Failed to get cars", t)
                    }
                })
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn.getNews().enqueue(object : Callback<List<New>> {
            override fun onResponse(call: Call<List<New>>, response: Response<List<New>>) {
                if (response.isSuccessful) {
                  val news = response.body();
                    if (news != null) {
                        NewAdapter.news = news // update the adapter with the retrieved cars
                        NewAdapter.notifyDataSetChanged() // notify the adapter that the data has changed
                    }
                } else {
                    Log.e(TAG, "Failed to get cars: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<New>>, t: Throwable) {
                Log.e(TAG, "Failed to get cars", t)
            }
        })
    }

    companion object {
        private const val TAG = "newsFragment"
    }
}

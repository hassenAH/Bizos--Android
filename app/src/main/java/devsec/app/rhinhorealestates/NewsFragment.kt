package devsec.app.RhinhoRealEstates

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        rvnews = view.findViewById(R.id.Newsitem_recycler_view)
        rvnews.layoutManager = LinearLayoutManager(requireContext())
        NewAdapter = NewAdapter(listOf()) // create an empty adapter
        rvnews.adapter = NewAdapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
        retIn.getNews().enqueue(object : Callback<List<New>> {
            override fun onResponse(call: Call<List<New>>, response: Response<List<New>>) {
                if (response.isSuccessful) {
                  val news = response.body()
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
        private const val TAG = "CarsFragment"
    }
}

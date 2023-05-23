package devsec.app.rhinhorealestates.ui.main.view

import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.api.RestApiService
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.api.MessageResponse
import devsec.app.rhinhorealestates.data.models.Appointement
import devsec.app.rhinhorealestates.data.models.User
import devsec.app.rhinhorealestates.utils.session.SessionPref
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageAdapter(context: Context, messages: MutableList<MessageResponse>, private val username: String) :
    ArrayAdapter<MessageResponse>(context, R.layout.message_item, messages) {
    private class ViewHolder {
        lateinit var messageContainer: LinearLayout
        lateinit var fromTextView: TextView
        lateinit var textTextView: TextView
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var rowView = convertView
        val viewHolder: ViewHolder
        if (rowView == null) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.message_item, parent, false)
            viewHolder = ViewHolder()
            viewHolder.fromTextView = rowView.findViewById(R.id.from_text_view)
            viewHolder.textTextView = rowView.findViewById(R.id.text_text_view)
            viewHolder.messageContainer = rowView.findViewById(R.id.message_container)
            rowView.tag = viewHolder
        } else {
            viewHolder = rowView.tag as ViewHolder
        }

        val message = getItem(position)
        if (message != null) {
            val retIn = RetrofitInstance.getRetrofitInstance().create(RestApiService::class.java)
            retIn.getUser( message.from).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.isSuccessful) {
                        val dates = response.body();
                        if (dates != null) {
                            viewHolder.fromTextView.text = dates.first_name + " " +dates.last_name
                        }
                    } else {

                    }
                }

                override fun onFailure(call:Call<User> , t: Throwable) {

                }
            })
            viewHolder.textTextView.text =
                message.text // Update textTextView instead of messageTextView

            if (message.from == username) {
                viewHolder.messageContainer.gravity = Gravity.END
                viewHolder.textTextView.gravity = Gravity.END
                viewHolder.textTextView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.black,
                    )
                )
                // Update textTextView instead of messageTextView
            } else {
                viewHolder.messageContainer.gravity = Gravity.START
                viewHolder.textTextView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                ) // Update textTextView instead of messageTextView
            }
        }
        return rowView!!
    }
}





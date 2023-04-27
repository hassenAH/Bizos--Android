package devsec.app.rhinhorealestates.data.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.squareup.picasso.Picasso
import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.data.models.New

class NewAdapter (var news: List<New>): RecyclerView.Adapter<NewAdapter.NewViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_new_item, parent, false)
        return NewViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewViewHolder, position: Int) {
        val new= news[position]
        holder.bind(new)
    }

    override fun getItemCount() = news.size

    inner class NewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val date: TextView = itemView.findViewById(R.id.date)
        private val snippet: TextView = itemView.findViewById(R.id.snippet)


        val imageView: ImageView = itemView.findViewById(R.id.thumbnail)




        fun bind(new: New) {
            title.text = new.title
            date.text = new.date
            snippet.text = new.snippet
            val url = new.thumbnail
            Picasso.get().load(url).into(imageView)


        }
    }
}

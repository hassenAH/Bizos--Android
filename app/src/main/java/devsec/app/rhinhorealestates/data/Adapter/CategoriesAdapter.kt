package devsec.app.rhinhorealestates.data.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

import com.squareup.picasso.Picasso
import devsec.app.RhinhoRealEstates.AvocatListActivity
import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.api.RetrofitInstance
import devsec.app.rhinhorealestates.data.models.Categories

class CategoriesAdapter (var categories: List<Categories>): RecyclerView.Adapter<CategoriesAdapter.CategorieViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategorieViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.CategorieViewHolder, position: Int) {
        val cat= categories[position]
        holder.bind(cat)
        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, AvocatListActivity::class.java)
            intent.putExtra("catname",cat.name)
            holder.itemView.context.startActivity(intent)
             }
    }

    override fun getItemCount() = categories.size

    inner class CategorieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        val imageView: ImageView = itemView.findViewById(R.id.imageCat)




        fun bind(cat: Categories) {
            name.text = cat.name
            val url = RetrofitInstance.BASE_IMG+cat.image
            Picasso.get().load(url).into(imageView)


        }
    }
}

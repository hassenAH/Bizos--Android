package devsec.app.rhinhorealestates.data.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import devsec.app.RhinhoRealEstates.PackDetailsFragment
import devsec.app.RhinhoRealEstates.R
import devsec.app.RhinhoRealEstates.databinding.FragmentPackItemBinding
import devsec.app.rhinhorealestates.data.models.Pack


class PackAdapter(var Packs: List<Pack>, val fragmentManager: FragmentManager): RecyclerView.Adapter<PackAdapter.PackViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackViewHolder {
        val view = // LayoutInflater.from(parent.context)
         FragmentPackItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PackViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackViewHolder, position: Int) {
        val pack= Packs[position]
        val tvTitle = holder.itemView.findViewById<TextView>(R.id.title)
        val tvPrix = holder.itemView.findViewById<TextView>(R.id.Prix)
        val tvDescription = holder.itemView.findViewById<TextView>(R.id.description)
        val tvName = holder.itemView.findViewById<TextView>(R.id.name)
        tvTitle.text = pack.title
        tvDescription.text = pack.description
        tvName.text = pack.name
        tvPrix.text = pack.prix.toString() + "$"


        // Picasso.get().load(RetrofitClient.URL+"img/"+car.image).into(imageView)
        holder.itemView.setOnClickListener{
            navigateToDetails(pack, fragmentManager)
        }
    }

    override fun getItemCount() = Packs.size

    inner class PackViewHolder(itemView: FragmentPackItemBinding) : RecyclerView.ViewHolder(itemView.root) {
      /*  private val title: TextView = itemView.findViewById(R.id.title)
        private val description: TextView = itemView.findViewById(R.id.description)
        private val prix: TextView = itemView.findViewById(R.id.Prix)

        private val name: TextView = itemView.findViewById(R.id.name)







        fun bind(pack: Pack) {

                title.text = pack.title
                description.text = pack.description
                name.text = pack.name

                prix.text = pack.prix.toString() + "$"




        }*/
    }

    private fun navigateToDetails(pack: Pack, fragmentManager: FragmentManager) {
        val bundle = Bundle().apply {
            putParcelable("pack", pack)
        }
        val packDetailsFragment = PackDetailsFragment()
        packDetailsFragment.arguments = bundle
        fragmentManager.beginTransaction().apply {
            replace(R.id.fragments_container, packDetailsFragment)
            addToBackStack(null)
            commit()
        }
    }
}

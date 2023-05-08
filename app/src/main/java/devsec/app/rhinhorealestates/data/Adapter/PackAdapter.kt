package devsec.app.rhinhorealestates.data.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import devsec.app.RhinhoRealEstates.R
import devsec.app.rhinhorealestates.data.models.Case
import devsec.app.rhinhorealestates.data.models.Pack


class PackAdapter(var Packs: List<Pack>): RecyclerView.Adapter<PackAdapter.PackViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_pack_item, parent, false)
        return PackViewHolder(view)
    }

    override fun onBindViewHolder(holder: PackViewHolder, position: Int) {
        val pack= Packs[position]
        holder.bind(pack)
    }

    override fun getItemCount() = Packs.size

    inner class PackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val description: TextView = itemView.findViewById(R.id.description)
        private val prix: TextView = itemView.findViewById(R.id.Prix)

        private val name: TextView = itemView.findViewById(R.id.name)







        fun bind(pack: Pack) {

                title.text = pack.title
                description.text = pack.description
                name.text = pack.name

                prix.text = pack.prix.toString() + "$"




        }
    }
}

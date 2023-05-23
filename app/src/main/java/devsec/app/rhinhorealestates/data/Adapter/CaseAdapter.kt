package devsec.app.rhinhorealestates.data.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import devsec.app.RhinhoRealEstates.PackDetailsFragment

import devsec.app.RhinhoRealEstates.R
import devsec.app.RhinhoRealEstates.case_details
import devsec.app.RhinhoRealEstates.databinding.FragmentCaseItemBinding
import devsec.app.rhinhorealestates.data.models.Case


class CaseAdapter(var Cases: List<Case>,val fragmentManager: FragmentManager): RecyclerView.Adapter<CaseAdapter.CaseViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CaseViewHolder {
        val view =   FragmentCaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CaseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CaseViewHolder, position: Int) {
        val case= Cases[position]
        val title: TextView = holder.itemView.findViewById(R.id.title)
         val description: TextView = holder.itemView.findViewById(R.id.description)
         val Prix: TextView = holder.itemView.findViewById(R.id.Prix)

         val nameUser: TextView = holder.itemView.findViewById(R.id.nameUser)

         val LastnameUser: TextView = holder.itemView.findViewById(R.id.LastnameUser)
        title.text = case.title
        description.text = case.description
        nameUser.text = case.nameUser
        LastnameUser.text = case.LastnameUser
        Prix.text = case.Prix.toString() + "$"
        holder.itemView.setOnClickListener{
            navigateToDetails(case, fragmentManager)
        }
    }

    override fun getItemCount() = Cases.size

    inner class CaseViewHolder(itemView: FragmentCaseItemBinding) : RecyclerView.ViewHolder(itemView.root) {
        /*private val title: TextView = itemView.findViewById(R.id.title)
        private val description: TextView = itemView.findViewById(R.id.description)
        private val Prix: TextView = itemView.findViewById(R.id.Prix)

        private val nameUser: TextView = itemView.findViewById(R.id.nameUser)

        private val LastnameUser: TextView = itemView.findViewById(R.id.LastnameUser)





        fun bind(case: Case) {
            if(!case.traite) {
                title.text = case.title
                description.text = case.description
                nameUser.text = case.nameUser
                LastnameUser.text = case.LastnameUser
                Prix.text = case.Prix.toString() + "$"
            }



        }*/
    }
    private fun navigateToDetails(case: Case, fragmentManager: FragmentManager) {
        val bundle = Bundle().apply {
            putParcelable("case", case)
        }
        val packDetailsFragment = case_details()
        packDetailsFragment.arguments = bundle
        fragmentManager.beginTransaction().apply {
            replace(R.id.fragments_container, packDetailsFragment)
            addToBackStack(null)
            commit()
        }
    }
}

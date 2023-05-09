package devsec.app.RhinhoRealEstates

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import devsec.app.rhinhorealestates.data.models.Pack


class PackDetailsFragment : Fragment() {

    private lateinit var pack: Pack
    private lateinit var packtitle: TextView
    private lateinit var packname: TextView
    private lateinit var packprix: TextView
    private lateinit var packtdescription: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pack = arguments?.getParcelable<Pack>("pack")!!

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pack_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Access the TextViews

        packtitle = view.findViewById(R.id.packTitle)
        packname = view.findViewById(R.id.packName)
        packprix = view.findViewById(R.id.packPrix)
        packtdescription = view.findViewById(R.id.packDescription)



        packtitle.text = pack.title
        packname.text = pack.name
        packprix.text = pack.prix.toString()
        packtdescription.text = pack.description


    }
}
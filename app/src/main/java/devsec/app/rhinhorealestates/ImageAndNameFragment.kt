package devsec.app.RhinhoRealEstates

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import devsec.app.rhinhorealestates.utils.session.SessionPref


class ImageAndNameFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var sessionPref: SessionPref
    lateinit var id : String
    lateinit var image : String

    lateinit var user : HashMap<String, String>
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_image_and_name, container, false)
        textView = view.findViewById(R.id.Nametxt)
        sessionPref = SessionPref(requireContext())
        user = sessionPref.getUserPref()
        image = user.get(SessionPref.USER_IMAGE).toString()
        Log.e(ContentValues.TAG, image)
        val imageView: ShapeableImageView = view.findViewById(R.id.ProfileImage)
        val url = "http://192.168.1.168:5000/img/"+image
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.logo)
            .into(imageView)

        id = user.get(SessionPref.USER_ID).toString()
        textView.text = user.get(SessionPref.USER_firstname) + " " +user.get(SessionPref.USER_lastname)


        return view
        // Inflate the layout for this fragment

    }


}
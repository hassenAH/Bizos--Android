package devsec.app.RhinhoRealEstates

import android.icu.text.Transliterator.Position
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class DevenirAvocat : AppCompatActivity() {
    lateinit var sExperience:Spinner
    lateinit var sSpecialite:Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devenir_avocat)
        sExperience=findViewById(R.id.experienceSnipper)
        sSpecialite=findViewById(R.id.Specialite)
        val expSpinner = listOf(1, 2, 3, 4, 5,7,8,9,10)
        val specialite= arrayOf("Social Right","Commercial And Business Law","Criminal law & Criminal procedure","droit civil","Civil Right")

        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,specialite)
        val adapterr = ArrayAdapter(this,android.R.layout.simple_list_item_1,expSpinner)
    sSpecialite.adapter = adapter
    sExperience.adapter=adapterr
        sSpecialite.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = specialite[position]
                Toast.makeText(this@DevenirAvocat,"selectedItem : $selectedItem" ,Toast.LENGTH_LONG).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        })
        sExperience.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = expSpinner[position]
                Toast.makeText(this@DevenirAvocat,"selectedItem : $selectedItem" ,Toast.LENGTH_LONG).show()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        })

    }
}
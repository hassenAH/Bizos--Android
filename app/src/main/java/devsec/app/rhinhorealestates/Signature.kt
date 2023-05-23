package devsec.app.RhinhoRealEstates

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport

import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener.Builder.withContext
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener.Builder.withContext
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener.Builder.withContext
import com.kyanogen.signatureview.SignatureView
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class SignatureFragment : Fragment() {
    private lateinit var bitmap: Bitmap
    private lateinit var clear: Button
    private lateinit var save: Button
    private lateinit var signatureView: SignatureView
    private lateinit var path: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_signature, container, false)
        signatureView = rootView.findViewById(R.id.signature_view)
        clear = rootView.findViewById(R.id.clear)
        save = rootView.findViewById(R.id.save)

        clear.setOnClickListener { signatureView.clearCanvas() }

        save.setOnClickListener {
            bitmap = signatureView.signatureBitmap
            path = saveImage(bitmap)
        }

       // requestMultiplePermissions()

        return rootView
    }

    private fun saveImage(myBitmap: Bitmap): String {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val wallpaperDirectory = File(
            Environment.getExternalStorageDirectory().toString() + IMAGE_DIRECTORY
        )

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }

        try {
            val f = File(
                wallpaperDirectory,
                Calendar.getInstance().timeInMillis.toString() + ".jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(
                requireContext(),
                arrayOf(f.path),
                arrayOf("image/jpeg"),
                null
            )
            fo.close()
            Toast.makeText(context, "Signature Saved !!!", Toast.LENGTH_SHORT).show()
            signatureView.clearCanvas()
            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }
        return ""
    }

   /* private fun requestMultiplePermissions() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        Toast.makeText(
                            requireContext(),
                            "All permissions are granted by the user!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // Show alert dialog navigating to Settings
                        // openSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .withErrorListener {
                Toast.makeText(requireContext(), "Some Error!", Toast.LENGTH_SHORT).show()
            }
            .onSameThread()
            .check()
    }*/
    companion object {
        private const val IMAGE_DIRECTORY = "/signdemo"
    }
}

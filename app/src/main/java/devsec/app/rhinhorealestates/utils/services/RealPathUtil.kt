package devsec.app.rhinhorealestates.utils.services

import android.content.Context
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore

class RealPathUtil {
    companion object {
        fun getRealPathFromURI_API19(context: Context, uri: Uri): String? {
            var filePath = ""
            val wholeID = DocumentsContract.getDocumentId(uri)
            // Split at colon, use second item in the array
            val id = wholeID.split(":".toRegex()).toTypedArray()[1]
            val column = arrayOf(MediaStore.Images.Media.DATA)
            // where id is equal to
            val sel = MediaStore.Images.Media._ID + "=?"
            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, sel, arrayOf(id), null
            )
            val columnIndex = cursor!!.getColumnIndex(column[0])
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
            return filePath
        }
    }
}
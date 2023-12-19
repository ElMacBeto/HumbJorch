package com.humbjorch.myapplication.sis.utils.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import androidx.core.content.FileProvider
import com.humbjorch.myapplication.App
import com.humbjorch.myapplication.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


fun share(context: Context, activity: Activity, bitmapScreen: Bitmap) {
    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val stream = FileOutputStream("$cachePath/s_shot.jpg")
        bitmapScreen.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()

        val imagePath = File(context.cacheDir, "images")
        val newFile = File(imagePath, "/s_shot.jpg")
        val contentUri =
            FileProvider.getUriForFile(
                context,
                "${App.instance.packageName}.provider",
                newFile
            )

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.type = "image/jpg"
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf<String>())
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            shareIntent.setDataAndType(
                contentUri,
                context.contentResolver?.getType(contentUri)
            )
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            activity.startActivity(
                Intent.createChooser(
                    shareIntent,
                    context.getString(R.string.label_share)
                )
            )
        }

    } catch (e: IOException) {
        Log.w("w", e)
    }
}
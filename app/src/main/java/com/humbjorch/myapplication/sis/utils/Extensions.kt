package com.humbjorch.myapplication.sis.utils

import android.app.Activity
import android.content.Context
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.humbjorch.myapplication.sis.utils.timer.CheckConnection
import de.hdodenhof.circleimageview.CircleImageView
import com.humbjorch.myapplication.sis.utils.util.Constants.PHOTO_AUTHENTICATION_DEFAULT
import java.util.Timer

fun CircleImageView.loadImageUrl(resource: String?) {
    if (resource != null) {
        val options: RequestOptions = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()

        Glide.with(this.context)
            .load(
                resource.ifEmpty { PHOTO_AUTHENTICATION_DEFAULT }
            )
            .apply(options)
            .into(this)
    }
}


fun Activity.launchTimer(){
    val timer = Timer()
    timer.schedule(CheckConnection(this),0,4000)
}
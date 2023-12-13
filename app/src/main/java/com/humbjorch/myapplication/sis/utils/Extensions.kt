package com.humbjorch.myapplication.sis.utils

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import de.hdodenhof.circleimageview.CircleImageView
import com.humbjorch.myapplication.sis.utils.util.Constants.PHOTO_AUTHENTICATION_DEFAULT

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
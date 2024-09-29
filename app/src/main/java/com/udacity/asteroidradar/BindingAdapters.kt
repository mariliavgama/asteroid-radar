package com.udacity.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getString
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.udacity.asteroidradar.Constants.IMAGE_MEDIA_TYPE
import com.udacity.asteroidradar.domain.PictureOfDay
import com.udacity.asteroidradar.main.ApiStatus

/**
 * Binding adapter used to hide the spinner once data is available
 */
@BindingAdapter("loadingStatus")
fun loadingStatus(view: View, status: ApiStatus?) {
    view.isVisible = status == ApiStatus.LOADING
}

/**
 * Binding adapter used to display the Picture of the Day from URL using Glide
 */
@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    val context = imageView.context
    imageView.contentDescription = getString(context, R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)

    pictureOfDay?.let {
        if (it.mediaType == IMAGE_MEDIA_TYPE) {
            Glide.with(imageView.context)
                .load(it.url)
                .error(R.drawable.placeholder_picture_of_day)
                .transition(DrawableTransitionOptions.withCrossFade(200))
                .apply(RequestOptions.centerCropTransform())
                .into(imageView)

            imageView.contentDescription = String.format(
                context.getString(R.string.nasa_picture_of_day_content_description_format),
                it.title
            )
        }
    }
}

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.apply {
            setImageResource(R.drawable.asteroid_hazardous)
            contentDescription = getString(context, R.string.potentially_hazardous_asteroid_image)
        }
    } else {
        imageView.apply {
            setImageResource(R.drawable.asteroid_safe)
            contentDescription = getString(context, R.string.not_hazardous_asteroid_image)
        }
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

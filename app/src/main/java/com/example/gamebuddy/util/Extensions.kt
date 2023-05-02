package com.example.gamebuddy.util

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.example.gamebuddy.R
import kotlinx.coroutines.flow.MutableStateFlow
import retrofit2.HttpException
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


// Retrofit Extensions
fun <T> handleUseCaseException(exception: Throwable): DataState<T> {
    exception.printStackTrace()
    when (exception) {
        is HttpException -> {
            return DataState.error(
                response = Response(
                    message = exception.message(),
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Error()
                )
            )
        }
        else -> {
            return DataState.error(
                response = Response(
                    message = exception.message ?: "Unknown error",
                    uiComponentType = UIComponentType.Dialog(),
                    messageType = MessageType.Error()
                )
            )
        }
    }
}


// Queue Extensions
fun StateMessage.isMessageExistInQueue(
    queue: Queue<StateMessage>,
): Boolean {

    queue.items.forEach { stateMessage ->
        if (stateMessage.response.message == response.message) {
            return true
        }
        if (stateMessage.response.messageType == response.messageType) {
            return true
        }
        if (stateMessage.response.uiComponentType == response.uiComponentType) {
            return true
        }
    }

    return false
}


// Glide Extensions
fun ImageView.loadImageFromUrl(
    string: String?,
    fitCenter: Boolean = true,
    centerCrop: Boolean = true,
) {
    if (string == null) {
        loadImageFromDrawable(R.drawable.ic_launcher_foreground)
    } else {
        val url = string
        val uri = Uri.parse(url)
        val multiTransform = getMultiTransform(fitCenter, centerCrop)
        val options = RequestOptions()
            .transform(multiTransform)
            .error(R.drawable.ic_launcher_background)//error imageID has to be here
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(context)
            .load(uri)
            .apply(options)
            .into(this)
    }
}

fun ImageView.loadImageFromDrawable(
    id: Int,
) {
    val multiTransformation = MultiTransformation(CenterCrop())
    val options = RequestOptions()
        .transform(multiTransformation)
        .error(R.drawable.ic_launcher_background)//error imageID has to be here
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    Glide.with(context)
        .load(id)
        .apply(options)
        .into(this)
}

// String Extensions
@RequiresApi(Build.VERSION_CODES.O)
fun String.formatDateTime(): String {
    val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
    val dateTime = LocalDateTime.parse(this, dateTimeFormatter)
    val now = LocalDateTime.now(ZoneId.of("UTC"))
    val duration = ChronoUnit.HOURS.between(dateTime, now)

    return when {
        duration < 1 -> "${dateTime.hour}:${String.format("%02d", dateTime.minute)}"
        duration < 24 -> "${dateTime.hour}:${String.format("%02d", dateTime.minute)}"
        else -> "${duration / 24}d"
    }
}

private fun getMultiTransform(
    fitCenter: Boolean,
    centerCrop: Boolean,
): MultiTransformation<Bitmap> {
    return when {
        fitCenter && !centerCrop -> MultiTransformation(FitCenter())
        !fitCenter && centerCrop -> MultiTransformation(CenterCrop())
        fitCenter && centerCrop -> MultiTransformation(FitCenter(), CenterCrop())
        else -> MultiTransformation()
    }
}
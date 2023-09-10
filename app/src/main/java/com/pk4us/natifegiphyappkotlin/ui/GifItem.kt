package com.pk4us.natifegiphyappkotlin.ui

import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GifItem(
    imageUrl: String,
    onGifClick: (String) -> Unit,
    requestManager: RequestManager,
    requestOptions: RequestOptions
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        var expanded by remember { mutableStateOf(false) }
        val haptic = LocalHapticFeedback.current
        Card(
            modifier = Modifier
                .combinedClickable(
                    onClick = { onGifClick(imageUrl) },
                    onLongClick = {
                        expanded = true
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    },
                )
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            val requestManager = remember { requestManager }
            val requestOptions = remember { requestOptions }

            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            600
                        )
                    }
                },
                update = { imageView ->
                    requestManager
                        .load(imageUrl)
                        .apply(requestOptions)
                        .into(imageView)
                }
            )
        }

        if (expanded) {
            CardMenu(
                imageUrl = imageUrl,
                onGifClick = onGifClick,
                onDismiss = { expanded = false })
        }
    }
}

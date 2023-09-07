package com.pk4us.natifegiphyappkotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder

class SecondActivity : ComponentActivity() {
    companion object {
        const val GIF_URL = "extra_gif_url"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gifUrl = intent.getStringExtra(GIF_URL)

        setContent {
            FullScreenGifScreen(gifUrl)
        }
    }
}

@Composable
fun FullScreenGifScreen(gifUrl: String?) {
    gifUrl?.let {
        val imageLoader =
            ImageLoader.Builder(LocalContext.current).components { add(GifDecoder.Factory()) }
                .build()
        Image(
            painter = rememberAsyncImagePainter(model = gifUrl, imageLoader),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        )
    }
}
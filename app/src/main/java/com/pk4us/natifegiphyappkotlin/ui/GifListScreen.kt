package com.pk4us.natifegiphyappkotlin.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions

@Composable
fun GifListScreen(images: List<String>, onGifClick: (String) -> Unit) {
    val requestManager = Glide.with(LocalView.current)
    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(images) { imageUrl ->
            GifItem(
                imageUrl = imageUrl,
                onGifClick = onGifClick,
                requestManager = requestManager,
                requestOptions = requestOptions
            )
        }
    }
}
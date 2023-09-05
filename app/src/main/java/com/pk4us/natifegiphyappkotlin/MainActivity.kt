package com.pk4us.natifegiphyappkotlin

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.pk4us.natifegiphyappkotlin.theme.NatifeGiphyAppKotlinTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.giphy.com/v1/"
const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var gifs by remember { mutableStateOf<List<String>>(emptyList()) }

            // retrofit
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

            // service
            val retroService = retrofit.create(DataService::class.java)
            retroService.getGifs().enqueue(object : Callback<DataResult?> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<DataResult?>, response: Response<DataResult?>
                ) {
                    val body = response.body()
                    if (body == null) {
                        Log.d(TAG, "onResponse: No response...")
                    }

                    if (response.isSuccessful) {
                        val gifUrls =
                            response.body()?.res?.map { it.images.ogImage.url } ?: emptyList()
                        gifs = gifUrls
                    } else {
                        Log.d(TAG, "onResponse: No response...")
                    }
                }

                override fun onFailure(call: Call<DataResult?>, t: Throwable) {
                }
            })

            NatifeGiphyAppKotlinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    GifListScreen(images = gifs) { selectedGifUrl ->
                        val intent = Intent(this@MainActivity, SecondActivity::class.java)
                        intent.putExtra(SecondActivity.GIF_URL, selectedGifUrl)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}

@Composable
fun GifListScreen(images: List<String>, onGifClick: (String) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(images) { imageUrl ->
            GifItem(imageUrl = imageUrl, onGifClick = onGifClick)
        }
    }
}


@Composable
fun GifItem(imageUrl: String, onGifClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable { onGifClick(imageUrl) }
    ) {
        val imageLoader = ImageLoader.Builder(LocalContext.current)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
        Image(
            painter = rememberAsyncImagePainter(imageUrl,imageLoader),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}
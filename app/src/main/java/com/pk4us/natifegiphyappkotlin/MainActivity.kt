package com.pk4us.natifegiphyappkotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.GifDecoder
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
            NatifeGiphyAppKotlinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ScreenMain(context = this)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    @Composable
    fun ScreenMain(context: Context) {
        val trendingGifsState = remember { mutableStateOf<List<String>>(emptyList()) }
        val searchGifsState = remember { mutableStateOf<List<String>>(emptyList()) }
        var searchText by remember { mutableStateOf("") }

        fun searchGifs(query: String) {
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

            val retroService = retrofit.create(DataService::class.java)
            retroService.searchGifs(query).enqueue(object : Callback<DataResult?> {
                override fun onResponse(
                    call: Call<DataResult?>, response: Response<DataResult?>
                ) {
                    val body = response.body()
                    if (body == null) {
                        Log.d(TAG, "onResponse: No response...")
                        return
                    }

                    if (response.isSuccessful) {
                        val gifUrls =
                            response.body()?.res?.map { it.images.ogImage.url } ?: emptyList()
                        searchGifsState.value = gifUrls
                    } else {
                        Log.d(TAG, "onResponse: No response...")
                    }
                }

                override fun onFailure(call: Call<DataResult?>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}", t)
                }
            })
        }

        fun getTrendingGifs() {
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build()

            val retroService = retrofit.create(DataService::class.java)
            retroService.getGifs().enqueue(object : Callback<DataResult?> {
                override fun onResponse(
                    call: Call<DataResult?>, response: Response<DataResult?>
                ) {
                    val body = response.body()
                    if (body == null) {
                        Log.d(TAG, "onResponse: No response...")
                        return
                    }

                    if (response.isSuccessful) {
                        val gifUrls =
                            response.body()?.res?.map { it.images.ogImage.url } ?: emptyList()
                        trendingGifsState.value = gifUrls
                    } else {
                        Log.d(TAG, "onResponse: No response...")
                    }
                }

                override fun onFailure(call: Call<DataResult?>, t: Throwable) {
                    Log.e(TAG, "onFailure: ${t.message}", t)
                }
            })
        }

        // Загрузка трендовых GIF при запуске приложения
        LaunchedEffect(Unit) {
            getTrendingGifs()
        }

        Column {
            // Search input
            val keyboardController = LocalSoftwareKeyboardController.current
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                singleLine = true,
                label = { Text("Search GIFs") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                keyboardActions = KeyboardActions(
                    onDone = {
                        searchGifs(searchText)
                        keyboardController?.hide()
                    }
                ),
                trailingIcon = {
                    IconButton(onClick = {
                        searchGifs(searchText)
                        keyboardController?.hide()
                    }) {
                        Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                    }
                }
            )

            GifListScreen(images = if (searchText.isEmpty()) trendingGifsState.value else searchGifsState.value) { selectedGifUrl ->
                val intent = Intent(context, SecondActivity::class.java)
                intent.putExtra(SecondActivity.GIF_URL, selectedGifUrl)
                context.startActivity(intent)
            }
        }
    }

    @Composable
    fun GifListScreen(images: List<String>, onGifClick: (String) -> Unit) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(images) { imageUrl ->
                GifItem(imageUrl = imageUrl, onGifClick = onGifClick)
            }
        }
    }


    @Composable
    fun GifItem(imageUrl: String, onGifClick: (String) -> Unit) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .clickable { onGifClick(imageUrl) }) {
            val imageLoader =
                ImageLoader.Builder(LocalContext.current).components { add(GifDecoder.Factory()) }
                    .build()
            Image(
                painter = rememberAsyncImagePainter(imageUrl, imageLoader),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }
    }
}
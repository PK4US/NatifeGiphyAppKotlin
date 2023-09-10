package com.pk4us.natifegiphyappkotlin.activity

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pk4us.natifegiphyappkotlin.navigation.Screen
import com.pk4us.natifegiphyappkotlin.ui.GifListScreen
import com.pk4us.natifegiphyappkotlin.utils.getTrendingGifs
import com.pk4us.natifegiphyappkotlin.utils.searchGifs

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    val trendingGifsState = remember { mutableStateOf<List<String>>(emptyList()) }
    val searchGifsState = remember { mutableStateOf<List<String>>(emptyList()) }
    var searchText by remember { mutableStateOf("") }

    getTrendingGifs(trendingGifsState)

    Column {
        val keyboardController = LocalSoftwareKeyboardController.current
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
                searchGifs(it, searchGifsState)
            },
            singleLine = true,
            label = { Text("Search GIFs") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            keyboardActions = KeyboardActions(
                onDone = {
                    searchGifs(searchText, searchGifsState)
                    keyboardController?.hide()
                }
            ),
            trailingIcon = {
                IconButton(onClick = {
                    searchGifs(searchText, searchGifsState)
                    keyboardController?.hide()
                }) {
                    Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
                }
            }
        )

        GifListScreen(
            images = if (searchText.isEmpty()) trendingGifsState.value
            else searchGifsState.value
        ) { selectedGifUrl ->
            val encodedUrl = Uri.encode(selectedGifUrl)
            navController.navigate(route = Screen.Detail.withArgs(encodedUrl))
        }
    }
}
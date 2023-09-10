package com.pk4us.natifegiphyappkotlin.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun CardMenu(imageUrl: String, onGifClick: (String) -> Unit, onDismiss: () -> Unit) {
    DropdownMenu(expanded = true, onDismissRequest = { onDismiss() }) {
        DropdownMenuItem(text = { Text("Open") },
            onClick = { onGifClick(imageUrl) },
            leadingIcon = {
                Icon(
                    Icons.Outlined.OpenInNew, contentDescription = null
                )
            })
        DropdownMenuItem(text = { Text("Download") },
            onClick = {  },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Save, contentDescription = null
                )
            })
        Divider()
        DropdownMenuItem(text = { Text("Share") },
            onClick = {  },
            leadingIcon = {
                Icon(
                    Icons.Outlined.Share, contentDescription = null
                )
            })
    }
}
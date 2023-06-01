package com.npsdk.jetpack_sdk.base.view

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberImagePainter

@Composable
fun ImageFromUrl(url: String, modifier: Modifier, contentScale: ContentScale? = ContentScale.FillWidth) {
	val painter = rememberImagePainter(url)

	Image(
		painter = painter,
		contentDescription = "Image from URL",
		modifier = modifier,
		contentScale = contentScale!!
	)
}
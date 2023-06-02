package com.npsdk.jetpack_sdk.base.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import com.npsdk.R

@Preview
@Composable
fun ShimmerLoading() {
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .padding(24.dp)
    ) {
        Line()
        Spacer(modifier = Modifier.height(6.dp))
        Line()
        Spacer(modifier = Modifier.height(6.dp))
        Line()
    }
}

@Composable
private fun Line() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(244.dp)
            .background(colorResource(id = R.color.background))
            .shimmer(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color.LightGray)
        )
    }
}
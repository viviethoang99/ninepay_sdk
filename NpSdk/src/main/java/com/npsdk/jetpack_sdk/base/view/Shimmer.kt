package com.npsdk.jetpack_sdk.base.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        Line(150)
        Spacer(modifier = Modifier.height(24.dp))
        Line(40)
        Spacer(modifier = Modifier.height(40.dp))
        Line(40)
        Spacer(modifier = Modifier.height(6.dp))
        Line(40)
        Spacer(modifier = Modifier.height(6.dp))
        Line(40)
    }
}

@Composable
private fun Line(height: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(6.dp))
            .height(height.dp)
            .shimmer(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(6.dp))
                .height(height.dp)
                .background(colorResource(id = R.color.grey)),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}
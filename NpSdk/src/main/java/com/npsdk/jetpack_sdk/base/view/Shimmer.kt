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
import com.npsdk.R
import com.valentinilk.shimmer.shimmer

@Preview
@Composable
fun ShimmerLoading() {
    Column(
        modifier = Modifier
            .background(color = Color.White)
            .padding(24.dp)
    ) {
        Line(150, isHeader = true)
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
private fun Line(height: Int, isHeader: Boolean? = false) {
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
            if (isHeader!!) Column(modifier = Modifier.padding(horizontal = 60.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(6.dp))
                        .height(10.dp)
                        .background(Color.Black),
                )
                Spacer(modifier = Modifier.height(10.dp))

                Box(modifier = Modifier.padding(horizontal = 30.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(shape = RoundedCornerShape(6.dp))
                            .height(10.dp)
                            .background(Color.Black),
                    )
                }

                Spacer(modifier = Modifier.height(60.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(6.dp))
                        .height(10.dp)
                        .background(Color.Black),
                )
            }
        }
    }
}
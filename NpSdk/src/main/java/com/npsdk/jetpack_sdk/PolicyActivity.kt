package com.npsdk.jetpack_sdk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.view.TopAppBarApp
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.rizzi.bouquet.ResourceType
import com.rizzi.bouquet.VerticalPDFReader
import com.rizzi.bouquet.rememberVerticalPdfReaderState

class PolicyActivity : ComponentActivity() {

    var url: String? = null

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = intent.extras
        if (bundle != null && bundle.containsKey("url")) {
            url = bundle.getString("url")
        }
        setContent {
            PaymentNinepayTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Scaffold(topBar = {
                        TopAppBarApp("Điều khoản & chính sách")
                    }, content = { paddingValues ->
                        Body(paddingValues = paddingValues, url)
                    })
                }
            }
        }
    }

    @Composable
    private fun Body(paddingValues: PaddingValues?, url: String? = "") {
        if (url.isNullOrBlank()) {
            Box {}
            return
        }
        val pdfState = rememberVerticalPdfReaderState(
            resource = ResourceType.Remote(url),
            isZoomEnable = true,
            isAccessibleEnable = true,
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(top = paddingValues!!.calculateTopPadding()).fillMaxSize()
                .background(colorResource(id = R.color.white))
        ) {

            VerticalPDFReader(
                state = pdfState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.White)
            )
            if (!pdfState.isLoaded) CircularProgressIndicator(color = colorResource(R.color.green))
        }
    }

}
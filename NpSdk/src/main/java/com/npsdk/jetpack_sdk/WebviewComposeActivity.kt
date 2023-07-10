package com.npsdk.jetpack_sdk

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.viewinterop.AndroidView
import com.npsdk.R
import com.npsdk.jetpack_sdk.base.listener.CloseListener
import com.npsdk.jetpack_sdk.base.view.ShowConfirmDialog
import com.npsdk.jetpack_sdk.base.view.TopAppBarApp
import com.npsdk.jetpack_sdk.theme.PaymentNinepayTheme
import com.npsdk.module.NPayLibrary
import com.npsdk.module.utils.Constants
import com.npsdk.module.utils.NameCallback
import java.net.URLDecoder
import java.util.regex.Pattern


class WebviewComposeActivity : ComponentActivity() {

    private var urlLoad = ""

    //    private val WEBVIEW_STATUS_CANCEL = -2
//    private val WEBVIEW_STATUS_FAIL = -1
//    private val WEBVIEW_STATUS_PENDING = 0
    private val WEBVIEW_STATUS_PROCESSED = 1
    var showDialog by mutableStateOf(false)


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showDialog = true
            }
        }

        OnBackPressedDispatcher().addCallback(this, callback)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {
                showDialog = true
            }
        }


        val intent = intent.extras

        if (intent != null) {
            if (intent.getString("url") != null) {
                urlLoad = intent.getString("url")!!
            }
        }

        setContent {

            PaymentNinepayTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Scaffold(
                        topBar = {
                            TopAppBarApp(
                                isCloseButton = true,
                                onBack = {
                                    showDialog = true
                                }
                            )
                        }, content = { paddingValues ->
                            Body(paddingValues = paddingValues)
                        })
                }
            }
        }

        CloseListener().listener(this)
    }

    override fun onDestroy() {
        CloseListener().cancelListener(this)
        super.onDestroy()
    }

    @Composable
    private fun Body(paddingValues: PaddingValues?) {

        Box(
            modifier = Modifier.padding(top = paddingValues!!.calculateTopPadding()).fillMaxSize()
                .background(colorResource(id = R.color.background)),
            contentAlignment = Alignment.Center
        ) {
            AndroidView(factory = {
                WebView(it).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                            println("my url $url")
                            if (url!!.contains("payment_result?status")) {
                                val regex = "status=(-?\\d+)&message=([^&]+)"
                                val pattern = Pattern.compile(regex)
                                val matcher = pattern.matcher(url)
                                if (matcher.find()) {
                                    val status = matcher.group(1)
                                    val message = matcher.group(2)
                                    Handler().postDelayed({ finish() }, 1000)
                                    if (status.toInt() == WEBVIEW_STATUS_PROCESSED) {
                                        if (NPayLibrary.getInstance().listener != null) {
                                            if (DataOrder.isShowResultScreen) {
                                                // Move to result screen
                                                val intent = Intent(context, ResultPayment::class.java)
                                                intent.putExtra("status", Constants.SUCCESS)
                                                context.startActivity(intent)
                                            } else {
                                                NPayLibrary.getInstance().callBackToMerchant(
                                                    NameCallback.SDK_PAYMENT, false, null
                                                )
                                            }
                                        }
                                    } else {
                                        // Move to error page
                                        moveToErrorPage(view!!.context, message)
                                    }
                                }
                                return false
                            }

                            if (url.contains("error/payment")) {
                                finish()
                                moveToErrorPage(view!!.context, "Payment failed")
                                return false
                            }
                            return super.shouldOverrideUrlLoading(view, url)
                        }
                    }
                    this.setLayerType(View.LAYER_TYPE_HARDWARE, null)
                    this.settings.javaScriptEnabled = true
                    this.settings.allowFileAccess = true
                    this.settings.allowContentAccess = true
                    this.settings.databaseEnabled = true
                    this.settings.loadsImagesAutomatically = true
                    this.settings.domStorageEnabled = true
                    this.settings.setSupportZoom(true)
                    this.settings.displayZoomControls = false
                    this.settings.builtInZoomControls = true
                    this.settings.cacheMode = WebSettings.LOAD_DEFAULT
                    this.settings.mediaPlaybackRequiresUserGesture = false
                    this.settings.pluginState = WebSettings.PluginState.ON
                    this.webChromeClient = object : WebChromeClient() {
                        override fun onPermissionRequest(request: PermissionRequest?) {
                            request?.grant(request.resources)
                        }
                    }
                    this.loadUrl(urlLoad)
                }
            })

            if (showDialog) ShowConfirmDialog(onLeft = {
                showDialog = false
                NPayLibrary.getInstance().callbackBackToAppfrom(NameCallback.PAYMENT_SCREEN)
                finish()
            }, onRight = {
                showDialog = false
            }, onDismiss = {
                showDialog = false
            }
            )
        }

    }

    private fun moveToErrorPage(context: Context, error: String) {
        if (DataOrder.isShowResultScreen) {
            // Move to error page
            val intent = Intent(context, ResultPayment::class.java)
            intent.putExtra("status", Constants.FAIL)
            intent.putExtra("message", decodeMessage(error))
            startActivity(intent)
        } else {
            NPayLibrary.getInstance().callBackToMerchant(
                NameCallback.SDK_PAYMENT, false, null
            )
        }
    }

    private fun decodeMessage(encodedMessage: String): String? {
        return URLDecoder.decode(encodedMessage)
    }

    override fun onBackPressed() {
        showDialog = true
    }
}
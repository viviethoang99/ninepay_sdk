@file:OptIn(ExperimentalMaterial3Api::class)

package com.npsdk.jetpack_sdk.base.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.npsdk.R
import com.npsdk.jetpack_sdk.theme.fontAppDefault
import com.npsdk.jetpack_sdk.theme.initColor

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MyEdittext(
    label: String,
    keyboardType: KeyboardType? = KeyboardType.Text,
    maxLength: Int? = 50,
    errText: String? = null,
    onTextChanged: (String) -> Unit = {},
    enabled: Boolean? = true,
    initText: String? = null,
    visualTransformation: VisualTransformation? = VisualTransformation.None,
    onTap: () -> Unit = {},
    onFocusOut: (String) -> Unit = {},
    tooltipsText: String? = ""
) {

    var isFocused by remember {
        mutableStateOf(false)
    }

    var textInput by remember {
        mutableStateOf("")
    }

    var isFirstFocus by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column {
        Box(modifier = Modifier.height(56.dp).clip(shape = RoundedCornerShape(8.dp)).clickable {
            onTap()
        }

        ) {

            TextField(value = initText ?: textInput,
                onValueChange = { value ->
                    var tempText = ""
                    if (value.length <= maxLength!!) {
                        tempText = if (keyboardType == KeyboardType.Number) {
                            value.filter { it.isDigit() }
                        } else {
                            val filteredText = value.replace(Regex("[^a-zA-Z ]"), "")
                            filteredText.uppercase()
                        }
                        textInput = tempText
                        onTextChanged(tempText)
                    }


                },
                textStyle = TextStyle(
                    color = colorResource(id = R.color.black),
                    fontFamily = fontAppDefault, fontSize = 14.sp, fontWeight = FontWeight.W400
                ),
                visualTransformation = visualTransformation!!,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = colorResource(id = R.color.black),
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black,
                    containerColor = Color.White,
                    selectionColors = TextSelectionColors(
                        handleColor = Color.Black,
                        backgroundColor = initColor().copy(0.6f)
                    )

                ),
                singleLine = true,
                keyboardActions = KeyboardActions(),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    autoCorrect = false,
                    imeAction = ImeAction.Done,
                    keyboardType = keyboardType!!
                ),
                modifier = Modifier.fillMaxWidth().background(Color.White).clickable {
                    onTap()
                }.onFocusChanged { state ->
                    isFocused = state.isFocused
                    // Focus của Jetpack bị ngu, chưa focus nhưng đã vào hàm này.
                    if (state.isFocused) {
                        isFirstFocus = true
                    } else {
                        if (isFirstFocus) {
                            onFocusOut(textInput)
//                            onTextChanged(textInput)
                        }
                        isFirstFocus = false
                    }
                },
                label = {
                    if (isFocused || (initText ?: textInput).isNotBlank()) Text(
                        label, style = TextStyle(
                            color = colorResource(id = R.color.grey), fontSize = 11.sp
                        )
                    )
                },
                trailingIcon = {
                    if (isFocused && (initText ?: textInput).isNotBlank()) IconButton(onClick = {
                        textInput = ""
                        onTextChanged(textInput)
                    }, content = {
                        Image(
                            modifier = Modifier.size(17.dp),
                            contentDescription = null,
                            painter = painterResource(R.drawable.icon_close_toolbar)
                        )
                    })

                    if (!isFocused && enabled!! && tooltipsText!!.isNotBlank()) TooltipView(label, tooltipsText)
                }

            )

            if (!isFocused && (initText ?: textInput).isBlank()) Text(
                label, modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp), style = TextStyle(
                    fontSize = 14.sp, color = colorResource(
                        id = R.color.grey
                    )
                )
            )

            /// Jetpack chưa hỗ trợ đổi text nếu set thuộc tính enabled
            /// Đo đó vẫn để enabled là true, nhưng chèn 1 layout không màu lên.
            // Mục đích là để có thể click được và gọi hàm onTap().
            enabled?.let {
                if (!enabled) {
                    Box(contentAlignment = Alignment.CenterEnd) {
                        Box(modifier = Modifier.height(56.dp).background(Color.Transparent).fillMaxWidth()
                            .clickable {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                                onTap()
                            })
                        if (tooltipsText!!.isNotBlank()) Row(horizontalArrangement = Arrangement.End) {
                            if (!isFocused) TooltipView(label, tooltipsText)
                            Spacer(modifier = Modifier.padding(end = 12.dp))
                        }
                    }
                }
            }

            if (isFocused) Box(
                modifier = Modifier.align(Alignment.BottomStart).height(2.dp).padding(horizontal = 12.dp).fillMaxWidth()
                    .background(initColor())
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        errText?.let {
            if (errText.isNotBlank()) Text(
                text = errText, style = TextStyle(
                    fontSize = 11.sp,
                    fontFamily = fontAppDefault,
                    fontWeight = FontWeight.W400,
                    color = Color(0xFFEB5757)
                ), modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}
	
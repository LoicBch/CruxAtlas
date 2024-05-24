package com.example.camperpro.android.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.R
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions


@Composable
fun SearchField(
    modifier: Modifier,
    @StringRes placeHolder: Int,
    onUserSearch: ((TextFieldValue) -> Unit) = {},
    onBackPress: (() -> Unit) = {}
) {

    val textState = remember { mutableStateOf(TextFieldValue()) }
    val boxIsFocused = remember {
        mutableStateOf(false)
    }

    TextField(modifier = modifier
        .border(
            if (boxIsFocused.value) BorderStroke(1.dp, AppColor.BlueCamperPro)
            else BorderStroke(0.dp, Color.Transparent), shape = RoundedCornerShape(
                4.dp
            )
        )
        .onFocusChanged {
            boxIsFocused.value = it.isFocused
        }
        .fillMaxWidth(),
              maxLines = 1,
              leadingIcon = {
                  Icon(modifier = Modifier.clickable { if (boxIsFocused.value) onBackPress() },
                      imageVector = if (boxIsFocused.value) Icons.Filled.ArrowBack else Icons.Filled.Search,
                      contentDescription = "",
                      tint = if (boxIsFocused.value) AppColor.BlueCamperPro else Color.Unspecified
                  )
              },
              trailingIcon = {
                  if (textState.value.text.isNotEmpty()) {
                      Icon(
                          modifier = Modifier.clickable { textState.value = TextFieldValue("") },
                          imageVector = Icons.Outlined.Close,
                          contentDescription = "",
                          tint = AppColor.Secondary
                      )
                  }
              },
              placeholder = { Text(text = stringResource(placeHolder)) },
              value = textState.value,
              onValueChange = { textFieldValue ->
                  textState.value = textFieldValue
                  onUserSearch(textFieldValue)
              },
              colors = TextFieldDefaults.textFieldColors(
                  backgroundColor = if (boxIsFocused.value) Color.White else AppColor.ClearBlue,
                  focusedIndicatorColor = Color.Transparent,
                  unfocusedIndicatorColor = Color.Transparent,
                  disabledIndicatorColor = Color.Transparent,
                  placeholderColor = Color.Black

              )
    )

}

@Composable
fun FilterSearchField(
    modifier: Modifier,
    @StringRes placeHolder: Int,
    onUserSearch: ((TextFieldValue) -> Unit) = {}
) {

    val textState = remember { mutableStateOf(TextFieldValue()) }
    val boxIsFocused = remember {
        mutableStateOf(false)
    }

    TextField(modifier = modifier
        .border(
            if (boxIsFocused.value) BorderStroke(1.dp, AppColor.BlueCamperPro)
            else BorderStroke(0.dp, Color.Transparent), shape = RoundedCornerShape(
                4.dp
            )
        )
        .onFocusChanged {
            boxIsFocused.value = it.isFocused
        }
        .fillMaxWidth(),
              maxLines = 1,
              leadingIcon = {
                  Icon(
                      imageVector = if (boxIsFocused.value) Icons.Filled.ArrowBack else Icons.Filled.Search,
                      contentDescription = "",
                      tint = if (boxIsFocused.value) AppColor.BlueCamperPro else Color.Unspecified
                  )
              },
              trailingIcon = {
                  if (textState.value.text.isNotEmpty()) {
                      Icon(
                          modifier = Modifier.clickable { textState.value = TextFieldValue("") },
                          imageVector = Icons.Outlined.Close,
                          contentDescription = "",
                          tint = AppColor.Secondary
                      )
                  }
              },
              placeholder = {
                  Text(
                      text = stringResource(placeHolder),
                      fontWeight = FontWeight.W500, fontFamily = FontFamily(Font(R.font.circularstdmedium))
                  )
              },
              value = textState.value,
              onValueChange = { textFieldValue ->
                  textState.value = textFieldValue
                  onUserSearch(textFieldValue)
              },
              colors = TextFieldDefaults.textFieldColors(
                  backgroundColor = if (boxIsFocused.value) Color.White else AppColor.ClearBlue,
                  focusedIndicatorColor = Color.Transparent,
                  unfocusedIndicatorColor = Color.Transparent,
                  disabledIndicatorColor = Color.Transparent,
                  placeholderColor = Color.Black

              )
    )

}
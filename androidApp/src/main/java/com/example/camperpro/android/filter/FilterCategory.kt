package com.example.camperpro.android.filter

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.camperpro.android.LocalDependencyContainer
import com.example.camperpro.android.R
import com.example.camperpro.android.composables.AppButton
import com.example.camperpro.android.composables.HistoricSearchList
import com.example.camperpro.android.ui.theme.AppColor
import com.example.camperpro.android.ui.theme.Dimensions
import kotlinx.coroutines.launch

@Composable
fun FilterCategorySelection(
    categorySelected: FilterCategory,
    onSelectButtonClick: (FilterCategory) -> Unit,
    onCategorySelected: (FilterCategory) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp)) {

        FilterStepOne(
            categorySelected,
            onFilterCategorySelected = { onCategorySelected(it) })

        if (categorySelected != FilterCategory.UNSELECTED) {
            FilterStepTwo(onSelectButtonClick, categorySelected)
        }

        Spacer(modifier = Modifier.weight(1f))

        AppButton(
            isActive = categorySelected.optionSelected != null,
            onClick = { /*TODO*/ },
            modifier = Modifier,
            textRes = R.string.apply_filters
        )
    }
}


@Composable
fun FilterStepTwo(
    onSelectButtonClick: (FilterCategory) -> Unit,
    categorySelected: FilterCategory
) {

    val scrollState = rememberScrollState()
    var buttonLabel by remember {
        mutableStateOf("")
    }

    buttonLabel = if (categorySelected.optionSelected != null) {
        categorySelected.optionSelected!!
    } else {
        when (categorySelected) {
            FilterCategory.GARAGE -> stringResource(id = R.string.choose_services_type)
            FilterCategory.DEALERS -> stringResource(id = R.string.choose_motorhome_brand)
            FilterCategory.ACCESSORIES -> stringResource(id = R.string.choose_accessories_brand)
            else -> {
                ""
            }
        }
    }

    Column(modifier = Modifier.scrollable(scrollState, Orientation.Vertical)) {
        Text(
            modifier = Modifier.padding(top = 60.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            color = AppColor.outlineText,
            text = "${
                stringResource(
                    id = R.string.step
                ).uppercase()
            } 2"
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.W700,
            text = stringResource(
                id = R.string.filter_step2_title
            )
        )

        Divider(modifier = Modifier.padding(vertical = 12.dp))

        if (categorySelected.optionSelected != null) {
            MaterialTextField(
                onSelectButtonClick = onSelectButtonClick,
                categorySelected = categorySelected
            )
        } else {
            FilterOptionsButton(
                onSelectButtonClick = onSelectButtonClick,
                categorySelected = categorySelected,
                buttonLabel = buttonLabel
            )
        }

        HistoricSearchList(categorySelected, onSelectFilter = {
            categorySelected.optionSelected = it
            buttonLabel = it
        })
    }
}

@Composable
fun FilterOptionsButton(
    onSelectButtonClick: (FilterCategory) -> Unit,
    categorySelected: FilterCategory,
    buttonLabel: String
) {

    Button(
        modifier = Modifier.height(Dimensions.buttonHeight),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        shape = RoundedCornerShape(Dimensions.radiusTextField),
        border = BorderStroke(2.dp, AppColor.Tertiary),
        onClick = { onSelectButtonClick(categorySelected) }) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = buttonLabel,
                color = AppColor.Tertiary,
                fontWeight = FontWeight.W500,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier.padding(end = 12.dp),
                painter = painterResource(id = R.drawable.unfold),
                contentDescription = ""
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MaterialTextField(
    onSelectButtonClick: (FilterCategory) -> Unit,
    categorySelected: FilterCategory
) {

    BasicTextField(
        value = categorySelected.optionSelected!!,
        modifier = Modifier
            .height(Dimensions.buttonHeight)
            .fillMaxWidth()
            .clickable { onSelectButtonClick(categorySelected) },
        onValueChange = { },
        enabled = false,
        readOnly = true,
        textStyle = TextStyle(
            color = AppColor.Primary,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp
        ),
        singleLine = true,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.OutlinedTextFieldDecorationBox(
                value = categorySelected.optionSelected!!,
                visualTransformation = VisualTransformation.None,
                innerTextField = innerTextField,
                placeholder = null,
                interactionSource = remember { MutableInteractionSource() },
                label = {
                    Text(
                        text = categorySelected.name.lowercase(),
                        fontWeight = FontWeight.W500,
                        fontSize = 11.sp,
                        color = AppColor.Primary
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.unfold),
                        contentDescription = "", tint = AppColor.Primary
                    )
                },
                singleLine = true,
                enabled = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    textColor = AppColor.Primary,
                    focusedBorderColor = AppColor.Primary,
                    unfocusedBorderColor = AppColor.Primary
                ),
                border = {
                    TextFieldDefaults.BorderBox(
                        false,
                        isError = false,
                        interactionSource = remember { MutableInteractionSource() },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = AppColor.Primary,
                            focusedBorderColor = AppColor.Primary,
                            unfocusedBorderColor = AppColor.Primary
                        ),
                        focusedBorderThickness = 2.dp,
                        unfocusedBorderThickness = 2.dp
                    )
                }
            )
        }
    )
}


@Composable
fun LastSearchItem(search: String, onSearchDelete: () -> Unit, onSelectSearch: (String) -> Unit) {
    Row(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.historic),
            contentDescription = "", tint = AppColor.Primary
        )

        Text(
            modifier = Modifier
                .padding(start = 15.dp)
                .clickable { onSelectSearch(search) },
            text = search,
            fontSize = 14.sp,
            fontWeight = FontWeight(450),
            color = AppColor.Tertiary
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            modifier = Modifier.clickable { onSearchDelete() },
            imageVector = Icons.Outlined.Close,
            contentDescription = "",
            tint = AppColor.Secondary
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun FilterStepOne(
    selectedItem: FilterCategory,
    onFilterCategorySelected: (FilterCategory) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        val sheetState = LocalDependencyContainer.current.appViewModel.bottomSheetIsShowing
        val coroutine = rememberCoroutineScope()

        IconButton(onClick = { coroutine.launch { sheetState.hide() } }) {
            Icon(imageVector = Icons.Sharp.Close, contentDescription = "", tint = AppColor.Tertiary)
        }
        Spacer(modifier = Modifier.weight(0.5f))

        Text(
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            text = stringResource(
                id = R.string.filters_title
            )
        )
        Spacer(modifier = Modifier.weight(0.5f))

        if (selectedItem != FilterCategory.UNSELECTED) {
            IconButton(onClick = { onFilterCategorySelected(FilterCategory.UNSELECTED) }) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "",
                    tint = AppColor.Secondary
                )
            }
        }
    }

    Text(
        modifier = Modifier.padding(top = 60.dp),
        fontSize = 12.sp,
        fontWeight = FontWeight.W500,
        color = AppColor.outlineText,
        text = "${
            stringResource(
                id = R.string.step
            ).uppercase()
        } 1"
    )

    Text(
        modifier = Modifier.padding(top = 12.dp),
        fontSize = 22.sp,
        fontWeight = FontWeight.W700,
        text = stringResource(
            id = R.string.filter_step1_title
        )
    )

    Divider(modifier = Modifier.padding(top = 13.dp))

    Column(
        modifier = Modifier
            .selectableGroup()
            .padding(top = 20.dp)
    ) {

        FilterCategory.values().dropLast(2).forEach { option ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
                    .selectable(
                        selected = (selectedItem == option),
                        onClick = { onFilterCategorySelected(option) },
                        role = Role.RadioButton
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioGroupItem(
                    selectedItem = selectedItem,
                    filterCategory = option
                )
            }
        }
    }
}

@Composable
fun RowScope.RadioGroupItem(
    selectedItem: FilterCategory,
    filterCategory: FilterCategory
) {

    Icon(
        painter = painterResource(id = filterCategory.icon),
        contentDescription = "",
        tint = if (selectedItem == filterCategory) AppColor.Primary else AppColor.Tertiary
    )
    Text(
        modifier = Modifier.padding(start = 10.dp),
        text = stringResource(filterCategory.title),
        fontSize = 14.sp,
        fontWeight = FontWeight.W500,
        color = if (selectedItem == filterCategory) AppColor.Primary else AppColor.Tertiary
    )
    Spacer(modifier = Modifier.weight(0.5f))
    RadioButton(
        modifier = Modifier.padding(end = 16.dp),
        selected = (selectedItem == filterCategory),
        onClick = null,
        colors = RadioButtonDefaults.colors(selectedColor = if (selectedItem == filterCategory) AppColor.Primary else AppColor.Tertiary)
    )
}
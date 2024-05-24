package com.example.camperpro.android.filter

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
import com.example.camperpro.domain.model.composition.Filter
import com.example.camperpro.domain.model.composition.filterName
import com.example.camperpro.utils.FilterType
import kotlinx.coroutines.launch

@Composable
fun FilterCategorySelection(
    filterSelected: Filter,
    onSelectButtonClick: (FilterType) -> Unit,
    onCategorySelected: (FilterType) -> Unit,
    onApplyFilter: () -> Unit,
    onHistoricFilterSelection: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 12.dp, horizontal = 16.dp)
    ) {

        FilterStepOne(selectedRadioButton = filterSelected.category,
                      onRadioButtonSelected = { onCategorySelected(it) })
        Spacer(modifier = Modifier.weight(1f))
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            if (filterSelected.category != FilterType.UNSELECTED_DEALER) {
                FilterStepTwo(onSelectButtonClick, filterSelected, onHistoricFilterSelection)
            }
            Spacer(modifier = Modifier.weight(1f))

            AppButton(
                isActive = true,
                onClick = {
                    onApplyFilter()
                },
                modifier = Modifier,
                textRes = R.string.apply_filters
            )
        }
    }
}


@Composable
fun FilterStepTwo(
    onSelectButtonClick: (FilterType) -> Unit,
    filterSelected: Filter,
    onHistoricFilterSelection: (String) -> Unit
) {

    val scrollState = rememberScrollState()
    var buttonLabel by remember {
        mutableStateOf("")
    }

    buttonLabel = when (filterSelected.category) {
        FilterType.SERVICE -> stringResource(id = R.string.choose_services_type)
        FilterType.BRAND -> stringResource(id = R.string.choose_motorhome_brand)
        else -> {
            ""
        }
    }

    Column() {
        Text(
            modifier = Modifier.padding(top = 60.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.W500,
            color = AppColor.outlineText, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            text = "${
                stringResource(
                    id = R.string.step
                ).uppercase()
            } 2"
        )

        Text(
            modifier = Modifier.padding(top = 12.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.W700, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            text = stringResource(
                id = R.string.filter_step2_title
            )
        )

        Divider(modifier = Modifier.padding(vertical = 12.dp))

        if (filterSelected.filterId == "") {
            FilterOptionsButton(
                onSelectButtonClick = onSelectButtonClick,
                categorySelected = filterSelected.category,
                buttonLabel = buttonLabel
            )
        } else {
            MaterialTextField(
                onSelectButtonClick = onSelectButtonClick, filterSelected = filterSelected
            )
        }

        Text(
            modifier = Modifier.padding(top = 20.dp, bottom = 22.dp),
            text = stringResource(id = R.string.last_searched),
            fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            color = AppColor.outlineText,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500
        )

        HistoricSearchList(filterSelected.category, onSelectFilter = {
            onHistoricFilterSelection(it)
        })
    }
}

@Composable
fun FilterOptionsButton(
    onSelectButtonClick: (FilterType) -> Unit, categorySelected: FilterType, buttonLabel: String
) {

    Button(modifier = Modifier.height(Dimensions.buttonHeight),
           colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
           shape = RoundedCornerShape(4.dp),
           border = BorderStroke(1.dp, AppColor.Tertiary),
           onClick = { onSelectButtonClick(categorySelected) }) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = buttonLabel,
                color = AppColor.Tertiary, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
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
    onSelectButtonClick: (FilterType) -> Unit, filterSelected: Filter
) {

    BasicTextField(value = filterSelected.filterName,
                   modifier = Modifier
                       .height(Dimensions.buttonHeight)
                       .fillMaxWidth()
                       .clickable { onSelectButtonClick(filterSelected.category) },
                   onValueChange = { },
                   enabled = false,
                   readOnly = true,
                   textStyle = TextStyle(
                       color = AppColor.Primary, fontWeight = FontWeight.W500, fontSize = 16.sp
                   ),
                   singleLine = true,
                   decorationBox = @Composable { innerTextField ->
                       TextFieldDefaults.OutlinedTextFieldDecorationBox(value = filterSelected.category.name,
                                                                        visualTransformation = VisualTransformation.None,
                                                                        innerTextField = innerTextField,
                                                                        placeholder = null,
                                                                        interactionSource = remember { MutableInteractionSource() },
                                                                        label = {
                                                                            Text(
                                                                                text = stringResource(
                                                                                    id = when (filterSelected.category) {
                                                                                        FilterType.BRAND -> R.string.motorhome_brands
                                                                                        FilterType.SERVICE -> R.string.motorhome_services
                                                                                        FilterType.COUNTRIES -> R.string.filter_step1_option1
                                                                                        FilterType.UNSELECTED_DEALER -> R.string.filter_step1_option1
                                                                                        FilterType.UNSELECTED_EVENT -> TODO()
                                                                                    }
                                                                                ),
                                                                                fontFamily = FontFamily(
                                                                                    Font(R.font.circularstdmedium)
                                                                                ),
                                                                                fontWeight = FontWeight.W500,
                                                                                fontSize = 11.sp,
                                                                                color = AppColor.Primary
                                                                            )
                                                                        },
                                                                        trailingIcon = {
                                                                            Icon(
                                                                                painter = painterResource(
                                                                                    id = R.drawable.unfold
                                                                                ),
                                                                                contentDescription = "",
                                                                                tint = AppColor.Primary
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
                                                                        })
                   })
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
            contentDescription = "",
            tint = AppColor.Primary
        )

        Text(
            modifier = Modifier
                .padding(start = 15.dp)
                .clickable { onSelectSearch(search) },
            text = search, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            fontSize = 14.sp,
            fontWeight = FontWeight(450),
            color = AppColor.Primary30
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
    selectedRadioButton: FilterType, onRadioButtonSelected: (FilterType) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
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
            fontFamily = FontFamily(Font(R.font.circularstdmedium)),
            text = stringResource(
                id = R.string.filters_title
            )
        )
        Spacer(modifier = Modifier.weight(0.5f))

        IconButton(modifier = Modifier.alpha(
            if (selectedRadioButton != FilterType.UNSELECTED_DEALER) {
                100f
            } else {
                0f
            }
        ), onClick = { onRadioButtonSelected(FilterType.UNSELECTED_DEALER) }) {
            Icon(
                modifier = Modifier.graphicsLayer {
                    rotationY = 180f
                },
                imageVector = Icons.Filled.Refresh,
                contentDescription = "",
                tint = AppColor.Secondary
            )
        }
    }

    Text(
        modifier = Modifier.padding(top = 60.dp),
        fontSize = 12.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
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
        fontSize = 22.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
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

        RadioButtonsFilter.values().dropLast(1).forEach { option ->
            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth()
                    .selectable(
                        selected = (selectedRadioButton == option.filterType),
                        onClick = { onRadioButtonSelected(option.filterType) },
                        role = Role.RadioButton
                    ), verticalAlignment = Alignment.CenterVertically
            ) {
                RadioGroupItem(
                    selectedItem = selectedRadioButton, radioButton = option
                )
            }
        }
    }
}

fun ellipsize(texte: String, tailleMax: Int): String {
    if (texte.length <= tailleMax) {
        return texte
    } else {
        val texteTronque = texte.substring(0, tailleMax)
        return "$texteTronque..."
    }
}


@Composable
fun RowScope.RadioGroupItem(
    selectedItem: FilterType, radioButton: RadioButtonsFilter
) {

    val title = ellipsize(stringResource(radioButton.title), 30)

    Icon(
        painter = painterResource(id = radioButton.icon),
        contentDescription = "",
        tint = if (selectedItem == radioButton.filterType) AppColor.Primary else AppColor.Tertiary
    )
    Text(
        modifier = Modifier.padding(start = 10.dp),
        text = title,
        fontSize = 14.sp, fontFamily = FontFamily(Font(R.font.circularstdmedium)),
        fontWeight = FontWeight.W500,
        color = if (selectedItem == radioButton.filterType) AppColor.Primary else AppColor.Tertiary
    )
    Spacer(modifier = Modifier.weight(1f))
    RadioButton(
        selected = (selectedItem == radioButton.filterType),
        onClick = null,
        colors = RadioButtonDefaults.colors(selectedColor = if (selectedItem == radioButton.filterType) AppColor.Primary else AppColor.Tertiary)
    )
}
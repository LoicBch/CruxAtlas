package com.horionDev.climbingapp.android.profile

import RouteWithLogDto
import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.horionDev.climbingapp.android.R
import com.horionDev.climbingapp.android.composables.AppButton
import com.horionDev.climbingapp.android.login.TextFieldAnimate
import com.horionDev.climbingapp.android.cragsDetails.customTabIndicator
import com.horionDev.climbingapp.android.ui.theme.AppColor
import com.horionDev.climbingapp.data.model.dto.BoulderLogDto
import com.horionDev.climbingapp.data.model.dto.UserDto
import com.horionDev.climbingapp.domain.model.entities.Boulder
import com.horionDev.climbingapp.domain.model.entities.User
import com.horionDev.climbingapp.utils.Country
import com.horionDev.climbingapp.utils.GENDER
import com.horionDev.climbingapp.utils.SessionManager
import com.horionDev.climbingapp.utils.SessionManager.user
import com.horionDev.climbingapp.utils.ageList
import com.horionDev.climbingapp.utils.heightList
import com.horionDev.climbingapp.utils.weightList
import com.horionDev.climbingapp.utils.yearList
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.io.File
import java.io.FileOutputStream

@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator, viewModel: ProfileViewModel = getViewModel()
) {

    val popupState by viewModel.galleryPopup.collectAsState()
    val routeLogs by viewModel.routeLogs.collectAsState()
//    val boulderLogs by viewModel.boulderLogs.collectAsState()
    val boulderLogs = emptyList<Pair<BoulderLogDto, Boulder>>()
    val user by viewModel.user.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp)
    ) {
        ProfileBlock(user) {
            viewModel.showUpdatePopup()
        }
        Tabs(routeLogs, boulderLogs) {
            viewModel.showGalleryPopup(it)
        }
    }

    if (popupState == ProfileViewModel.ProfilePopup.GALLERY) {
        PopupGallery(onClose = {
            viewModel.hidePopup()
        }, url = popupState.url)
    } else if (popupState == ProfileViewModel.ProfilePopup.UPDATE) {
        PopupUpdateProfile(onClose = {
            viewModel.hidePopup()
        }, onUpdate = {
            viewModel.hidePopup()
            viewModel.updateUser(it)
        }, onUpdatePhoto = {
            val inputStream = context.contentResolver.openInputStream(it)
            val fileBytes = inputStream?.readBytes()
            viewModel.updatePhoto(fileBytes!!)
        })
    }

}

@Composable
fun ProfileBlock(user: User, onOpenUpdatePopup: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(200.dp)
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(25),
            )
            .background(
                color = Color.White, shape = RoundedCornerShape(25)
            )
            .padding(16.dp)
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GlideImage(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                imageModel = { SessionManager.user.imageUrl ?: R.drawable.default_profile },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.FillBounds, alignment = Alignment.Center
                )
            )
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = SessionManager.user.username,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                fontFamily = FontFamily(Font(R.font.oppinsedium))
            )
        }

        Column(
            Modifier
                .fillMaxHeight()
                .padding(start = 16.dp)
                .weight(2f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Row(Modifier) {
                Text(
                    text = "Ascents : 155",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )
            }

            Row(Modifier.padding(vertical = 8.dp)) {
                Text(
                    text = "Red point : 8A",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )
            }

            Row(Modifier) {
                Text(
                    text = "Flashed : 7b+",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )
            }
        }
        Image(
            modifier = Modifier
                .size(30.dp)
                .padding(top = 8.dp, end = 8.dp)
                .clickable {
                    onOpenUpdatePopup()
                }, painter = painterResource(id = R.drawable.edit), contentDescription = ""
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(
    routeLogs: List<RouteWithLogDto>,
    boulderLogs: List<Pair<BoulderLogDto, Boulder>>,
    onImageClick: (String) -> Unit
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    val tabScreensTitle = listOf(
        stringResource(id = R.string.about),
        stringResource(id = R.string.routes),
        stringResource(id = R.string.boulders)
    )

    val density = LocalDensity.current

    val tabWidths = remember {
        val tabWidthStateList = mutableStateListOf<Dp>()
        repeat(tabScreensTitle.size) {
            tabWidthStateList.add(0.dp)
        }
        tabWidthStateList
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        TabRow(selectedTabIndex = pagerState.currentPage,
            backgroundColor = Color.Transparent,
            contentColor = AppColor.Primary,
            indicator = {
                TabRowDefaults.Indicator(
                    modifier = Modifier.customTabIndicator(
                        currentTabPosition = it[pagerState.currentPage],
                        tabWidth = tabWidths[pagerState.currentPage]
                    )
                )
            }) {
            tabScreensTitle.forEachIndexed { index, tab ->
                Tab(
                    text = {
                        Text(
                            text = tab,
                            onTextLayout = {
                                tabWidths[pagerState.currentPage] =
                                    with(density) { it.size.width.toDp() }
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.W500,
                            fontFamily = FontFamily(Font(R.font.circularstdmedium)),
                            color = if (pagerState.currentPage == index) AppColor.Primary else AppColor.Tertiary
                        )
                    },
                    selected = pagerState.currentPage == index,
                    onClick = { scope.launch { pagerState.scrollToPage(index) } },
                )
            }
        }

        HorizontalPager(
            modifier = Modifier.fillMaxHeight(), count = tabScreensTitle.size, state = pagerState
        ) { page ->
            when (page) {
                0 -> AboutTab(user) {
                    ProfileGallery(
                        urls = listOf(
                            "https://plus.unsplash.com/premium_photo-1664474619075-644dd191935f?fm=jpg&q=60&w=3000&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8aW1hZ2V8ZW58MHx8MHx8fDA%3D",
                            "https://fps.cdnpk.net/images/home/subhome-ai.webp?w=649&h=649",
                            "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                            "https://letsenhance.io/static/03620c83508fc72c6d2b218c7e304ba5/11499/UpscalerAfter.jpg",
                            "https://pixlr.com/images/generator/text-to-image.webp",
                            "https://gratisography.com/wp-content/uploads/2024/03/gratisography-vr-glasses-800x525.jpg",
                            "https://upload.wikimedia.org/wikipedia/commons/b/b6/Image_created_with_a_mobile_phone.png",
                            "https://letsenhance.io/static/03620c83508fc72c6d2b218c7e304ba5/11499/UpscalerAfter.jpg",
                            "https://pixlr.com/images/generator/text-to-image.webp"
                        )
                    ) {
                        onImageClick(it)
                    }
                }

                1 -> RoutesTab(routeLogs)
                2 -> BouldersTab(boulderLogs)
            }
        }
    }
}

@Composable
fun ProfileGallery(urls: List<String>, onImageClick: (String) -> Unit) {

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            Text(
                text = "Gallery",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.oppinsedium))
            )
        }

        Divider()

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.padding(top = 8.dp),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(urls) { url ->
                GlideImage(modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        onImageClick(url)
                    }, imageModel = { url }, imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit
                )
                )
            }
        }
    }
}

@Composable
fun PopupGallery(onClose: () -> Unit, url: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = AppColor.ClearGrey)
            .clickable {
                onClose()
            }, contentAlignment = Alignment.Center
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                imageModel = { url },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Fit
                )
            )
        }
    }
}

fun getUriFromDrawable(context: Context, drawableId: Int): Uri? {
    val drawable = context.getDrawable(drawableId) ?: return null
    val bitmap = (drawable as BitmapDrawable).bitmap

    val file = File(context.cacheDir, "default_profile_image.png")
    FileOutputStream(file).use { fos ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos)
    }

    return Uri.fromFile(file)
}

@Composable
fun PopupUpdateProfile(
    onClose: () -> Unit,
    onUpdate: (UserDto) -> Unit,
    onUpdatePhoto: (Uri) -> Unit
) {

    var country by remember { mutableStateOf(user.country.ifEmpty { "Select a country" }) }
    var city by remember { mutableStateOf(user.city) }
    var age by remember { mutableStateOf(if (user.age != 0) user.age.toString() else "Select your age") }
    var gender by remember { mutableStateOf(user.gender.ifEmpty { "Select your gender" }) }
    var height by remember { mutableStateOf(if (user.height != 0) "${user.height} cm" else "Select your height") }
    var weight by remember { mutableStateOf(if (user.weight != 0) "${user.weight} kg" else "Select your weight") }
    var climbingSince by remember { mutableStateOf(user.climbingSince.ifEmpty { "Climbing since when ?" }) }

//    var selectedImageUri = rememberSelectedImageUri(user.imageUrl, R.drawable.default_profile)
    val context = LocalContext.current
    var selectedImageUri by remember {
        mutableStateOf(user.imageUrl?.let { Uri.parse(it) } ?: getUriFromDrawable(
            context,
            R.drawable.default_profile
        ))
    }
    var newImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher to pick image from gallery
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        newImageUri = uri
    }

    // Check permission and request if necessary
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            if (granted) {
                launcher.launch("image/*")
            }
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(color = AppColor.ClearGrey)
            .clickable {
                onClose()
            }) {
            Column(
                Modifier
                    .padding(horizontal = 34.dp, vertical = 50.dp)
                    .shadow(4.dp, RoundedCornerShape(5))
                    .background(
                        color = Color.White, shape = RoundedCornerShape(5)
                    )
                    .padding(25.dp)
                    .align(Alignment.Center)
                    .clickable { }
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Update Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(Font(R.font.oppinsedium))
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    GlideImage(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        imageModel = { selectedImageUri },
                        imageOptions = ImageOptions(
                            contentScale = ContentScale.FillBounds, alignment = Alignment.Center
                        )
                    )
                    Image(
                        modifier = Modifier
                            .size(30.dp)
                            .clickable {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                                } else {
                                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                            },
                        painter = painterResource(id = R.drawable.upload_image),
                        contentDescription = ""
                    )
                }

                Text(
                    text = "country",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )

                CustomDropdownMenu(
                    list = Country.values().map { it.displayValue }.toList(),
                    defaultSelected = country,
                    color = Black,
                    modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                ) {
                    country = Country.values()[it].displayValue
                }

                Text(
                    text = "city",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )

                TextFieldAnimate(
                    Modifier.padding(top = 10.dp, bottom = 20.dp),
                    placeHolder = R.string.city,
                    initialContent = city
                ) {
                    city = it.text
                }
                Text(
                    text = "gender",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )

                CustomDropdownMenu(
                    list = GENDER.values().map { it.name.lowercase() }.toList(),
                    defaultSelected = gender,
                    color = Black,
                    heightFraction = 0.2f,
                    modifier = Modifier.padding(bottom = 20.dp),
                ) {
                    gender = GENDER.values()[it].name.lowercase()
                }

                Text(
                    text = "age",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )

                CustomDropdownMenu(
                    list = ageList.map { it.toString() },
                    defaultSelected = age,
                    color = Black,
                    modifier = Modifier.padding(bottom = 20.dp),
                ) {
                    age = ageList[it].toString()
                }

                Text(
                    text = "Weight",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )

                CustomDropdownMenu(
                    list = weightList,
                    defaultSelected = weight,
                    color = Black,
                    modifier = Modifier.padding(bottom = 20.dp),
                ) {
                    weight = weightList[it]
                }

                Text(
                    text = "height",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )

                CustomDropdownMenu(
                    list = heightList,
                    defaultSelected = height,
                    color = Black,
                    modifier = Modifier.padding(bottom = 20.dp),
                ) {
                    height = heightList[it]
                }

                Text(
                    text = "Climbing Since",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.oppinsedium))
                )

                CustomDropdownMenu(
                    list = yearList.map { it.toString() },
                    defaultSelected = climbingSince,
                    color = Black,
                    modifier = Modifier.padding(bottom = 20.dp),
                ) {
                    climbingSince = yearList[it].toString()
                }

                AppButton(
                    isActive = true,
                    onClick = {

                        val newCountry =
                            if (country == "Select a country") user.country else Country.values()
                                .find { it.displayValue == country }!!.name
                        val newAge = if (age == "Select your age") user.age.toString() else age
                        val newHeight =
                            if (height == "Select your height") user.height.toString() else height
                        val newWeight =
                            if (weight == "Select your weight") user.weight.toString() else weight
                        val newClimbingSince =
                            if (climbingSince == "Climbing since when ?") user.climbingSince else climbingSince
                        val newGender = if (gender == "Select your gender") user.gender else gender

                        val userDto = UserDto(
                            user.id,
                            user.username,
                            user.password,
                            user.email,
                            newCountry,
                            city,
                            newGender,
                            newAge.toInt(),
                            newHeight.substringBefore(" cm").toInt(),
                            newWeight.substringBefore(" kg").toInt(),
                            newClimbingSince
                        )
                        onUpdate(userDto)
                        if (newImageUri != null) {
                            onUpdatePhoto(newImageUri!!)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth(),
                    textRes = R.string.valid
                )
            }
        }
    }
}

@Composable
fun CustomDropdownMenu(
    list: List<String>, // Menu Options
    defaultSelected: String, // Default Selected Option on load
    color: Color, // Color
    modifier: Modifier,
    heightFraction: Float = 0.5f,//
    onSelected: (Int) -> Unit, // Pass the Selected Option
) {
    var selectedIndex by remember { mutableStateOf(0) }
    var expand by remember { mutableStateOf(false) }
    var stroke by remember { mutableStateOf(1) }
    Box(
        modifier
            .fillMaxWidth()
            .border(
                border = BorderStroke(stroke.dp, color),
                shape = RoundedCornerShape(4.dp)
            )
            .clickable {
                expand = true
                stroke = if (expand) 2 else 1
            },
        contentAlignment = Alignment.CenterStart
    ) {

        Text(
            text = defaultSelected,
            color = Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
        )

        DropdownMenu(
            expanded = expand,
            onDismissRequest = {
                expand = false
                stroke = if (expand) 2 else 1
            },
            properties = PopupProperties(
                focusable = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
            ),
            modifier = Modifier
                .background(White)
                .padding(2.dp)
                .fillMaxHeight(heightFraction)
                .fillMaxWidth(.7f)
        ) {
            list.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        selectedIndex = index
                        expand = false
                        stroke = if (expand) 2 else 1
                        onSelected(selectedIndex)
                    }
                ) {
                    Text(
                        text = item,
                        color = color,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    }
}
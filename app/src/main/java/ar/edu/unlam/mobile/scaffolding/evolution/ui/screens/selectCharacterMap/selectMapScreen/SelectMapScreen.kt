package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.selectCharacterMap.selectMapScreen

import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.data.local.Background
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.ButtonWithBackgroundImage
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.ExitConfirmation
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.SetOrientationScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.mediaPlayer
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.screenSize
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.local.OrientationScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.CombatScreenRoute
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.selectCharacterMap.selectPlayerScreen.viewmodel.SelectCharacterViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.SilverA
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.VioletSky

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SelectMap(
    navController: NavHostController,
    selectCharacterViewModel: SelectCharacterViewModel,
) {
    val context = LocalContext.current
    val audioPosition = selectCharacterViewModel.audioPosition.collectAsState()
    val audio = mediaPlayer(context, audioPosition)
    val isLoading = selectCharacterViewModel.isLoading.collectAsState()
    var showExitConfirmation by rememberSaveable {
        mutableStateOf(false)
    }
    val screenSizeSmall = screenSize(context)

    SetOrientationScreen(
        context = context,
        orientation = OrientationScreen.PORTRAIT.orientation,
    )

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    brush =
                        Brush.verticalGradient(
                            listOf(SilverA, VioletSky),
                            startY = 0f,
                            endY = 600f,
                        ),
                ),
    ) {
        if (isLoading.value) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Scaffold(
                topBar = {
                    TopBar(
                        navController,
                        screenSizeSmall,
                    ) { showExitConfirmation = true }
                },
                content = {
                    ContentView(
                        navController = navController,
                        selectCharacterViewModel = selectCharacterViewModel,
                        context = context,
                        screenSizeSmall = screenSizeSmall,
                    )
                },
            )
        }

        ExitConfirmation(
            show = showExitConfirmation,
            onDismiss = { showExitConfirmation = false },
            onConfirm = {
                selectCharacterViewModel.setAudioPosition(audio.currentPosition)
                navController.popBackStack()
            },
            title = stringResource(id = R.string.ExitConfirmation),
            message = stringResource(id = R.string.ExitSelectCharacter),
        )

        BackHandler {
            showExitConfirmation = true
        }
    }
}

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    screenSizeSmall: Boolean,
    showExitConfirmation: (Boolean) -> Unit,
) {
    val (expanded, setExpanded) = remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier.height(48.dp),
        title = {
            Text(
                text = "Background Selection",
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                textAlign = TextAlign.Start,
                color = Color.White,
                fontSize = if (screenSizeSmall) 16.sp else 20.sp,
            )
        },
        navigationIcon = {
            IconButton(onClick = { showExitConfirmation(true) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(Color.Black),
        actions = {
            IconButton(onClick = { setExpanded(true) }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    tint = Color.White,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { setExpanded(false) },
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier =
                        Modifier
                            .clickable { /*Ranked*/ }
                            .fillMaxWidth(),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.List,
                        contentDescription = null,
                        modifier = Modifier.padding(horizontal = 8.dp),
                    )

                    Text(
                        text = "View Ranked",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(end = 16.dp),
                    )
                }
            }
        },
    )
}

@Composable
fun ContentView(
    navController: NavHostController,
    selectCharacterViewModel: SelectCharacterViewModel,
    context: Context,
    screenSizeSmall: Boolean,
) {
    val backgroundList by selectCharacterViewModel.backgroundData.collectAsState()
    val backgroundSelected by selectCharacterViewModel.background.collectAsState()

    if (backgroundList.isNotEmpty()) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = "Select your combat background...")

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(if (screenSizeSmall) 400.dp else 500.dp),
                ) {
                    LazyRowWithImagesHeroPlayer(
                        backgroundList = backgroundList,
                        selectCharacterViewModel,
                        backgroundSelected,
                    )
                }

                ButtonWithBackgroundImage(
                    imageResId = R.drawable.iv_attack,
                    onClick = {
                        if (backgroundSelected != null) {
                            selectCharacterViewModel.setCombatDataScreen()
                            selectCharacterViewModel.setAudioPosition(0)
                            navController.navigate(CombatScreenRoute)
                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "Please Select background for continue",
                                    Toast.LENGTH_SHORT,
                                ).show()
                        }
                    },
                    modifier =
                        Modifier
                            .width(700.dp)
                            .height(250.dp)
                            .padding(bottom = if (screenSizeSmall) 4.dp else 22.dp),
                ) {
                    Text(
                        text = "Start Combat",
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.font_firestar)),
                        fontStyle = FontStyle.Italic,
                        fontSize = 28.sp,
                        color = Color.Black,
                    )
                }
            }
        }
    } else {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }
}

@Composable
fun LazyRowWithImagesHeroPlayer(
    backgroundList: List<Background>,
    selectCharacterViewModel: SelectCharacterViewModel,
    backgroundSelected: Background?,
) {
    val selectAudio = MediaPlayer.create(LocalContext.current, R.raw.raw_select)
    val cancelSelect = MediaPlayer.create(LocalContext.current, R.raw.raw_cancelselect)
    LazyRow(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(4.dp),
        userScrollEnabled = true,
    ) {
        items(backgroundList) { stage ->
            Card(
                modifier =
                    Modifier
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                        .width(400.dp)
                        .height(500.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            selectCharacterViewModel.setBackground(stage)
                            if (backgroundSelected == stage) cancelSelect.start() else selectAudio.start()
                        }.border(
                            width = 2.dp,
                            color = if (backgroundSelected != null && backgroundSelected == stage) Color.Green else Color.Transparent,
                            shape = RoundedCornerShape(8.dp),
                        ),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = stage.background),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )

                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .align(Alignment.BottomCenter)
                                .background(
                                    colorResource(id = R.color.superhero_item_name),
                                ),
                    ) {
                        Text(
                            text = stage.name,
                            modifier = Modifier.align(Alignment.BottomCenter),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }
            }
        }
    }
}

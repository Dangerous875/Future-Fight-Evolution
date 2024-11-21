package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.homeScreen

import android.annotation.SuppressLint
import android.app.Activity
import android.media.MediaPlayer
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.ButtonWithBackgroundImage
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.ExitConfirmation
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.SetOrientationScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.local.OrientationScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes.SelectPlayerRoute
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes.SignUpScreenRoute
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes.UserProfileScreenRoute
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.homeScreen.viewmodel.HomeScreenViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.ColorWay
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel = hiltViewModel(),
) {
    SetOrientationScreen(
        context = LocalContext.current,
        orientation = OrientationScreen.PORTRAIT.orientation,
    )
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    var showExitConfirmation by rememberSaveable {
        mutableStateOf(false)
    }

    var showAuthenticationFailConfirmation by rememberSaveable {
        mutableStateOf(false)
    }

    ExitConfirmation(
        show = showExitConfirmation,
        onDismiss = { showExitConfirmation = false },
        onConfirm = { activity.finishAffinity() },
        title = stringResource(id = R.string.ExitConfirmation),
        message = stringResource(id = R.string.ExitApp),
    )

    ExitConfirmation(
        show = showAuthenticationFailConfirmation,
        onDismiss = { showAuthenticationFailConfirmation = false },
        onConfirm = { navController.navigate(SelectPlayerRoute) },
        title = "ADVERTENCIA",
        message = "Sin estar logado no rankearas !",
    )

    val audio =
        remember {
            MediaPlayer
                .create(context, R.raw.media_marvel)
                .apply { setVolume(0.6f, 0.6f) }
        }

    DisposableEffect(Unit) {
        audio.start()
        onDispose {
            audio.stop()
            audio.release()
        }
    }

    Scaffold(
        topBar = {
            TopBarHome(navController, homeScreenViewModel) {
                showExitConfirmation = true
            }
        },
        content = {
            ContentViewHome(
                navController = navController,
                homeScreenViewModel = homeScreenViewModel,
            ) { showAuthenticationFailConfirmation = true }
        },
    )

    BackHandler {
        showExitConfirmation = true
    }
}

@Composable
fun ContentViewHome(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    onEnterGame: (Boolean) -> Unit,
) {
    val logo by homeScreenViewModel.logos.collectAsState()
    val auth by homeScreenViewModel.auth.collectAsState()
    val blockVersion by homeScreenViewModel.blockVersion.collectAsState()
    val isLoading by homeScreenViewModel.isLoading.collectAsState()

    if (blockVersion) {
        ShowUpdateDialog(viewModel = homeScreenViewModel)
    } else {
        if (isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(Black),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = Color.Cyan)
            }
        } else {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(top = 48.dp),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = logo),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )

                Image(
                    painter = painterResource(id = R.drawable.iv_logo),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(200.dp)
                            .align(Alignment.TopStart),
                )

                ButtonWithBackgroundImage(
                    imageResId = R.drawable.iv_button,
                    onClick = {
                        homeScreenViewModel.testAnalitic()
                        if (auth.currentUser != null) {
                            navController.navigate(SelectPlayerRoute)
                        } else {
                            onEnterGame(true)
                        }
                    },
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .width(300.dp)
                            .height(80.dp)
                            .padding(bottom = 22.dp),
                ) {
                    Text(
                        text = stringResource(id = R.string.EnterGame),
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.font_firestar)),
                        fontStyle = FontStyle.Italic,
                        fontSize = 28.sp,
                        color = Black,
                    )
                }
            }
        }
    }
}

@Composable
fun ShowUpdateDialog(viewModel: HomeScreenViewModel) {
    val context = LocalContext.current
    val activity = context as Activity

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false),
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier =
                Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth(),
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    imageVector = Icons.Default.SystemUpdate,
                    contentDescription = "Update Icon",
                    tint = Color(0xFF3F51B5),
                    modifier = Modifier.size(48.dp),
                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Actualización Disponible",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F51B5),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Para disfrutar de todas las nuevas funciones, actualiza la aplicación a la última versión.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    OutlinedButton(
                        onClick = { activity.finishAffinity() },
                        colors =
                            ButtonDefaults.buttonColors(
                                contentColor = Color.Red,
                                containerColor = Color.Black,
                            ),
                        modifier = Modifier.weight(1f),
                    ) {
                        Text(text = "Salir :(", fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { viewModel.navigateToPlayStore(context) },
                        colors =
                            ButtonDefaults.buttonColors(
                                containerColor = Color.Green,
                                contentColor = Color.Black,
                            ),
                        modifier = Modifier.weight(1.5f),
                    ) {
                        Text(text = "Actualizar :)", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHome(
    navController: NavHostController,
    homeScreenViewModel: HomeScreenViewModel,
    onChangeValue: (Boolean) -> Unit,
) {
    val auth by homeScreenViewModel.auth.collectAsState()
    val (expanded, setExpanded) = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val isLog = auth.currentUser != null
    var changeColor by remember { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        targetValue = if (changeColor) Color.White else ColorWay,
        animationSpec =
            androidx.compose.animation.core
                .tween(durationMillis = 1000),
        label = "",
    )
    LaunchedEffect(Unit) {
        while (true) {
            delay(1500)
            changeColor = !changeColor
        }
    }

    // top bar
    TopAppBar(
        modifier = Modifier.height(48.dp),
        title = {
            Text(
                text = "Future Fight Evolution",
                fontSize = 20.sp,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                textAlign = TextAlign.Start,
                color = animatedColor,
                fontFamily = FontFamily(Font(R.font.font_firestar)),
                fontStyle = FontStyle.Italic,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(Black),
        actions = {
            IconButton(onClick = {
                if (isLog) {
                    navController.navigate(UserProfileScreenRoute)
                } else {
                    navController.navigate(
                        SignUpScreenRoute,
                    )
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = animatedColor,
                )
            }
            IconButton(onClick = { setExpanded(true) }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = null,
                    tint = animatedColor,
                )
            }

            if (isLog) {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { setExpanded(false) },
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .clickable { navController.navigate(UserProfileScreenRoute) }
                                .fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            tint = animatedColor,
                        )

                        Text(
                            text = "My Profile",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(end = 16.dp),
                            color = animatedColor,
                        )
                    }
                    Spacer(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                    )
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .clickable {
                                    Toast
                                        .makeText(context, "Log out successful", Toast.LENGTH_SHORT)
                                        .show()
                                    auth.signOut()
                                }.fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            tint = animatedColor,
                        )

                        Text(
                            text = "Log out",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(end = 16.dp),
                            color = animatedColor,
                        )
                    }
                    Spacer(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                    )
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .clickable { onChangeValue(true) }
                                .fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            tint = animatedColor,
                        )

                        Text(
                            text = "Exit Game",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(end = 16.dp),
                            color = animatedColor,
                        )
                    }
                }
            } else {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { setExpanded(false) },
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .clickable { navController.navigate(SignUpScreenRoute) }
                                .fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            tint = animatedColor,
                        )

                        Text(
                            text = "Log in",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(end = 16.dp),
                            color = animatedColor,
                        )
                    }
                    Spacer(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                    )

                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .clickable { onChangeValue(true) }
                                .fillMaxWidth(),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                            contentDescription = null,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            tint = animatedColor,
                        )

                        Text(
                            text = "Exit Game",
                            fontSize = 19.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(end = 16.dp),
                            color = animatedColor,
                        )
                    }
                }
            }
        },
    )
}

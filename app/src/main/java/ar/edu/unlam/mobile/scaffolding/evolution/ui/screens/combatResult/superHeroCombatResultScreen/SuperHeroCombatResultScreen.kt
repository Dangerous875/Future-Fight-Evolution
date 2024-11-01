package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroCombatResultScreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.ButtonWithBackgroundImage
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.SetOrientationScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.local.OrientationScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes.CombatScreenRoute
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes.SelectPlayerRoute
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroCombatResultScreen.viewmodel.CombatResultViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.BlackCustom
import coil.compose.rememberAsyncImagePainter

@Composable
fun SuperHeroCombatResult(
    navController: NavHostController,
    viewModel: CombatResultViewModel = hiltViewModel(),
) {
    val permissionLocationGranted by viewModel.permissionLocation.collectAsState()
    val context = LocalContext.current

    SetOrientationScreen(
        context = context,
        orientation = OrientationScreen.PORTRAIT.orientation,
    )

    val permissionLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { permissionsGranted ->
            if (permissionsGranted) {
                viewModel.setPermissionCamera(true)
            } else {
                viewModel.setPermissionCamera(false)
            }
        }

    LaunchedEffect(Unit) {
        permissionLauncher.launch(
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
    }

    LaunchedEffect(permissionLocationGranted) {
        if (permissionLocationGranted) {
            viewModel.updateUserRankingDB()
        }
    }

    if (permissionLocationGranted) {
        ContentView(navController = navController, viewModel = viewModel)
    } else {
        Box(
            modifier = Modifier.fillMaxSize().background(BlackCustom),
            contentAlignment = Alignment.Center,
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "Please push in setting and granted permissions and reset Battle to continue ... ",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.size(16.dp))
                ButtonWithBackgroundImage(
                    imageResId = R.drawable.iv_button,
                    onClick = {
                        openAppSettings(context)
                    },
                    modifier =
                        Modifier
                            .width(300.dp)
                            .height(120.dp)
                            .padding(vertical = 22.dp),
                ) {
                    Text(
                        text = "Go settings",
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.font_firestar)),
                        fontStyle = FontStyle.Italic,
                        fontSize = 28.sp,
                        color = Color.Black,
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                ButtonWithBackgroundImage(
                    imageResId = R.drawable.iv_button,
                    onClick = {
                        viewModel.resetLife()
                        navController.navigate(CombatScreenRoute)
                    },
                    modifier =
                        Modifier
                            .width(300.dp)
                            .height(90.dp),
                ) {
                    Text(
                        text = "Reset Battle",
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.font_firestar)),
                        fontStyle = FontStyle.Italic,
                        fontSize = 28.sp,
                        color = Color.Black,
                    )
                }
            }
        }
    }

    BackHandler {
        navController.navigate(SelectPlayerRoute) {
            popUpTo(SelectPlayerRoute) {
                inclusive = true
            }
        }
    }
}

@Composable
fun ContentView(
    navController: NavHostController,
    viewModel: CombatResultViewModel,
) {
    val result by viewModel.result.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val playerWin by viewModel.playerWin.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(color = Color.Black),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.height(32.dp))
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
                    Image(
                        painter =
                            rememberAsyncImagePainter(
                                result!!
                                    .resultDataScreen!!
                                    .superHeroPlayer.image.url,
                            ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .height(200.dp)
                                .width(200.dp),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(id = if (playerWin) R.drawable.iv_gold_trophy else R.drawable.iv_defeated),
                        contentDescription = null,
                        modifier =
                            Modifier
                                .height(200.dp)
                                .width(200.dp),
                    )
                }

                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .size(4.dp)
                            .padding(top = 6.dp, bottom = 6.dp),
                    color = Color.White,
                )

                Row(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Image(
                        painter =
                            rememberAsyncImagePainter(
                                result!!
                                    .resultDataScreen!!
                                    .superHeroCom.image.url,
                            ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .height(200.dp)
                                .width(200.dp),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(id = if (!playerWin) R.drawable.iv_gold_trophy else R.drawable.iv_defeated),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .height(200.dp)
                                .width(200.dp),
                    )
                }

                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .size(4.dp)
                            .padding(top = 16.dp, bottom = 16.dp),
                    color = Color.White,
                )

                Box(
                    Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = if (playerWin) R.drawable.im_ganador else R.drawable.iv_defeated),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier =
                            Modifier
                                .fillMaxSize(),
                    )
                }

                Spacer(modifier = Modifier.width(80.dp))
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .size(4.dp)
                            .padding(top = 6.dp, bottom = 6.dp),
                    color = Color.White,
                )
                Row(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(top = 32.dp),
                ) {
                    // BUTTON AGAIN
                    ButtonWithBackgroundImage(
                        imageResId = R.drawable.iv_button,
                        onClick = {
                            viewModel.resetLife()
                            navController.navigate(CombatScreenRoute)
                        },
                        modifier =
                            Modifier
                                .width(200.dp)
                                .height(80.dp)
                                .padding(bottom = 22.dp),
                    ) {
                        Text(
                            text = "AGAIN",
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.font_firestar)),
                            fontStyle = FontStyle.Italic,
                            fontSize = 28.sp,
                            color = Color.Black,
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    // BUTTON RANKED
                    ButtonWithBackgroundImage(
                        imageResId = R.drawable.iv_button,
                        onClick = { navController.navigate(Routes.RankedRoute) },
                        modifier =
                            Modifier
                                .width(300.dp)
                                .height(80.dp)
                                .padding(bottom = 22.dp),
                    ) {
                        Text(
                            text = "RANKED",
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.font_firestar)),
                            fontStyle = FontStyle.Italic,
                            fontSize = 28.sp,
                            color = Color.Black,
                        )
                    }
                }
                Row(
                    modifier =
                        Modifier
                            .weight(1f)
                            .align(Alignment.CenterHorizontally),
                ) {
                    // BUTTON EXIT
                    ButtonWithBackgroundImage(
                        imageResId = R.drawable.iv_button,
                        onClick = {
                            navController.navigate(SelectPlayerRoute) {
                                popUpTo(Routes.HomeScreenRoute) {
                                    inclusive = true
                                }
                            }
                        },
                        modifier =
                            Modifier
                                .width(300.dp)
                                .height(80.dp)
                                .padding(bottom = 22.dp),
                    ) {
                        Text(
                            text = "EXIT",
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily(Font(R.font.font_firestar)),
                            fontStyle = FontStyle.Italic,
                            fontSize = 28.sp,
                            color = Color.Black,
                        )
                    }
                }
            }
        }
    }
}

fun openAppSettings(context: Context) {
    val intent =
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", context.packageName, null),
        ).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    context.startActivity(intent)
}

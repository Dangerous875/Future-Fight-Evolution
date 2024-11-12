package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.qrGenerateScreen

import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.QrScannerUtil
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.qrGenerateScreen.viewmodel.ScanResultViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.ColorWay
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.IndigoDye
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.SilverB
import com.journeyapps.barcodescanner.ScanContract
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowScanScreen(
    navController: NavController,
    scanResultViewModel: ScanResultViewModel,
) {
    val context = LocalContext.current
    val qrScannerUtil = remember { QrScannerUtil() }
    val qrScanLauncher =
        rememberLauncherForActivityResult(
            ScanContract(),
        ) { result ->

            result?.contents?.let {
                scanResultViewModel.setScanResult(it)
                navController.navigate(Routes.ScanResultScreen)
            } ?: run {
                Toast.makeText(context, "Escaneo cancelado", Toast.LENGTH_LONG).show()
            }
        }

    val requestCameraPermissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { isGranted ->
            if (isGranted) {
                qrScannerUtil.startQrScan(qrScanLauncher)
            } else {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_LONG).show()
            }
        }

    fun startQrScanWithPermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.CAMERA,
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            qrScannerUtil.startQrScan(qrScanLauncher)
        } else {
            requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

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
    val (expanded, setExpanded) = remember { mutableStateOf(false) }

//

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    brush =
                        Brush.verticalGradient(
                            listOf(SilverB, IndigoDye),
                            startY = 0f,
                            endY = 1100f,
                        ),
                ),
        contentAlignment = Alignment.TopStart,
    ) {
        Column {
            TopAppBar(
                modifier = Modifier.height(35.dp),
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
                actions = {
                    IconButton(onClick = { setExpanded(true) }) {
                        Icon(
                            imageVector = Icons.Filled.MoreVert,
                            contentDescription = null,
                            tint = animatedColor,
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
                                    .clickable {
                                        startQrScanWithPermission()
                                    }.fillMaxWidth(),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.icon_qr),
                                contentDescription = null,
                                modifier = Modifier.padding(start = 5.dp).size(24.dp),
                                tint = animatedColor,
                            )

                            Text(
                                text = "Show Scan",
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
                                    .clickable { navController.navigate(Routes.QRGenerateScreenRoute) }
                                    .fillMaxWidth(),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_qrcode_generate),
                                contentDescription = null,
                                modifier =
                                    Modifier
                                        .padding(start = 5.dp)
                                        .size(24.dp),
                                tint = animatedColor,
                            )

                            Text(
                                text = "Generate Qr",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = Modifier.padding(end = 16.dp),
                                color = animatedColor,
                            )
                        }
                    }
                },
            )

            Box(
                modifier =
                    Modifier
                        .fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "Scan the QR on your friend's device to see their profile or share the QR so they can see yours",
                    fontSize = 30.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = 35.sp,
                )
            }
        }
    }
}

package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.userProfileScreen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CameraEnhance
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases.QrScannerUtil
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.userProfileScreen.profileComponent.UpdateData
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.userProfileScreen.viewmodel.UserProfileScreenViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.BlackCustom
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.Carmine
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.ColorWay
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.CyanWay
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.DarkPurple
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.IndigoDye
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.SilverB
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    navController: NavController,
    auth: FirebaseAuth,
    userProfileScreenViewModel: UserProfileScreenViewModel = hiltViewModel(),
) {
    val showUpdateData by userProfileScreenViewModel.showUpdateData.collectAsState()
    val userData by userProfileScreenViewModel.userData.collectAsState()
    val avatarUrl by userProfileScreenViewModel.avatarUrl.collectAsState()
    val isLoading by userProfileScreenViewModel.isLoading.collectAsState()
    val context = LocalContext.current
    val qrScannerUtil = remember { QrScannerUtil(context) }
    val qrScanLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            qrScannerUtil.handleScanResult(result.resultCode, result.data)
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
    if (isLoading) {
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = Color.Cyan)
        }
    } else {
        var nickname by remember { mutableStateOf(userData!!.nickname) }
        var name by remember { mutableStateOf(userData!!.name) }
        var infoUser by remember { mutableStateOf(userData!!.infoUser) }

        if (showUpdateData) {
            UpdateData(
                onDismiss = { userProfileScreenViewModel.dismissUpdateDataSelected() },
                onUpdateDataAdded = { nameUD, nicknameUD, infoUserUD ->
                    userProfileScreenViewModel.addUpdateData(
                        nameUD,
                        nicknameUD,
                        infoUserUD,
                    )
                },
            )
        }
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
                                    .clickable { qrScannerUtil.startQrScan(qrScanLauncher) }
                                    .fillMaxWidth(),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.icon_qr),
                                contentDescription = null,
                                modifier = Modifier.padding(start = 5.dp),
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
                        .fillMaxSize()
                        .background(
                            brush =
                                Brush.verticalGradient(
                                    listOf(SilverB, IndigoDye),
                                    startY = 0f,
                                    endY = 1100f,
                                ),
                        ),
                contentAlignment = Alignment.Center,
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(1.dp))

                    // Ajuste del logo encima del AVATAR (Queda feo si se ubica en esquina superior derecha)
                    Image(
                        painter = painterResource(id = R.drawable.iv_logo), // probar insertar el Logo de FF
                        contentDescription = "Future Fight Logo",
                        modifier =
                            Modifier
                                .size(130.dp) // Ajusta el tamaño según sea necesario
                                .padding(16.dp),
                    )
                    Spacer(modifier = Modifier.height(1.dp))

                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            modifier =
                                Modifier
                                    .size((180.dp))
                                    .clip(CircleShape)
                                    .border(5.dp, Color.White, CircleShape),
                            painter =
                                rememberAsyncImagePainter(
                                    // model = auth.currentUser?.photoUrl,
                                    model = avatarUrl,
                                    placeholder = painterResource(id = R.drawable.im_default_avatar),
                                    error = painterResource(id = R.drawable.im_default_avatar),
                                ),
                            contentDescription = "Avatar Usuario",
                        )
                        Box(
                            modifier =
                                Modifier
                                    .align(Alignment.BottomEnd)
                                    .offset(10.dp),
                        ) {
                            IconButton(
                                onClick = { navController.navigate(Routes.UploadImageScreenRoute) },
                                modifier =
                                    Modifier
                                        .size(60.dp)
                                        .background(
                                            colorResource(id = R.color.whatsappGreenSoft),
                                            shape = CircleShape,
                                        ).clip(CircleShape),
                            ) {
                                Icon(
                                    Icons.Default.CameraEnhance,
                                    contentDescription = "Tomar Foto",
                                    tint = Color.Black,
                                    modifier = Modifier.size(35.dp),
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Información del Usuario
                    UserInfoCard(
                        label = "Nickname",
                        value = userData!!.nickname!!,
                        onValueChange = { newValue -> nickname = newValue },
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    UserInfoCard(
                        label = "Name",
                        value = userData!!.name!!,
                        onValueChange = { newValue -> name = newValue },
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    UserInfoCard(
                        label = "User information",
                        value = userData!!.infoUser!!,
                        onValueChange = { newValue -> infoUser = newValue },
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    UserInfoField(
                        label = "Registered Email",
                        value = auth.currentUser!!.email!!,
                        onEditField = { },
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        Button(
                            onClick = { userProfileScreenViewModel.updateDataSelected() },
                            border = BorderStroke(width = 2.dp, color = Carmine),
                            // modifier = Modifier.fillMaxWidth(),
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(end = 4.dp),
                            colors =
                                ButtonColors(
                                    containerColor = DarkPurple,
                                    contentColor = IndigoDye,
                                    disabledContentColor = BlackCustom,
                                    disabledContainerColor = CyanWay,
                                ),
                        ) {
                            Text(
                                "Edit profile",
                                color = SilverB,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineMedium,
                            )
                        }
                        Button(
                            onClick = {
                                userProfileScreenViewModel.updateAllData(
                                    name!!,
                                    nickname!!,
                                    infoUser!!,
                                )
                                Toast.makeText(context, "Save succefull", Toast.LENGTH_SHORT).show()
                            },
                            border = BorderStroke(width = 2.dp, color = Carmine),
                            // modifier = Modifier.fillMaxWidth(),
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(start = 4.dp),
                            colors =
                                ButtonColors(
                                    containerColor = DarkPurple,
                                    contentColor = IndigoDye,
                                    disabledContentColor = BlackCustom,
                                    disabledContainerColor = CyanWay,
                                ),
                        ) {
                            Text(
                                "Apply changes",
                                color = SilverB,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserInfoField(
    label: String,
    value: String,
    onEditField: ((String) -> Unit)?,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .background(
                    color = Color.White.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                ).padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(text = label, style = MaterialTheme.typography.titleMedium)
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
        onEditField?.let {
            IconButton(onClick = { it(label) }) {
                Icon(Icons.Default.AlternateEmail, contentDescription = "Editar $label")
            }
        }
    }
}

@Composable
fun UserInfoCard(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    var isEditing by remember { mutableStateOf(false) }
    var currentValue by remember { mutableStateOf(value) }

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White.copy(alpha = 0.2f),
                    // Brush.linearGradient(listOf(Purple80, Purple40)),
                    shape = RoundedCornerShape(8.dp),
                ).padding(16.dp),
    ) {
        Row {
            Column {
                Text(text = label, color = Color.Black, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(6.dp))
                if (isEditing) {
                    OutlinedTextField(
                        value = currentValue,
                        onValueChange = { newValue -> currentValue = newValue },
                        textStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
                        colors = OutlinedTextFieldDefaults.colors(Color.Black),
                    )
                } else {
                    Text(
                        text = currentValue,
                        fontSize = 16.sp,
                        color = BlackCustom,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
//                Text(
//                    text = value,
//                    fontSize = 16.sp,
//                    color = Color.Black,
//                    fontWeight = FontWeight.Bold,
//                    style = MaterialTheme.typography.titleMedium,
//                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = {
                if (isEditing) {
                    onValueChange(currentValue)
                }
                isEditing = !isEditing
            }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_lapiz),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}

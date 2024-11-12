package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.qrGenerateScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.qrGenerateScreen.viewmodel.ScanResultViewModel
import coil.compose.AsyncImage

@Composable
fun ScanResultScreen(scanResultViewModel: ScanResultViewModel) {
    val user by scanResultViewModel.user.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        AsyncImage(
            model = R.drawable.backscannresult,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(id = R.drawable.iv_logo), // probar insertar el Logo de FF
                contentDescription = "Future Fight Logo",
                modifier =
                    Modifier
                        .size(250.dp)
                        .padding(16.dp),
            )

            user?.let { userInfo ->

                userInfo.avatarUrl?.let { avatarUrl ->
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = "Avatar de ${userInfo.userName}",
                        modifier =
                            Modifier
                                .size(300.dp)
                                .clip(CircleShape)
                                .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "NICKNAME: ${userInfo.userName ?: "Desconocido"}",
                    fontSize = 25.sp,
                    color = Color.White,
                    fontFamily = FontFamily(Font(R.font.creepster_regular)),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "VICTORIES: ${userInfo.userVictories ?: 0}",
                    color = Color.White,
                    fontSize = 25.sp,
                    fontFamily = FontFamily(Font(R.font.creepster_regular)),
                )
            } ?: Text(
                text = "Loading user information...",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
            )
        }
    }
}

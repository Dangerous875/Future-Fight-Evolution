package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroRankedScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserRanked
import ar.edu.unlam.mobile.scaffolding.evolution.ui.components.ButtonWithBackgroundImage
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroRankedScreen.viewmodel.SuperHeroRankedViewModel
import coil.compose.AsyncImage

@Composable
fun SuperHeroRanked(
    navController: NavController,
    viewModel: SuperHeroRankedViewModel = hiltViewModel(),
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val usersRanked by viewModel.usersRanked.collectAsState()

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Box(
            modifier =
                Modifier
                    .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.im_avengers_navidad0),
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
            ) {
                Text(
                    text = "Top Players",
                    fontWeight = FontWeight.Normal,
                    fontFamily = FontFamily(Font(R.font.font_firestar)),
                    fontStyle = FontStyle.Italic,
                    fontSize = 24.sp,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn {
                    itemsIndexed(usersRanked) { index, user ->
                        CardView(index + 1, user)
                        Spacer(modifier = Modifier.height(8.dp)) // Espacio entre tarjetas
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                ButtonWithBackgroundImage(
                    imageResId = R.drawable.iv_button,
                    onClick = {
                        navController.navigate(Routes.RankedMapsUsersRoute)
                    },
                    modifier =
                        Modifier
                            .width(300.dp)
                            .height(70.dp),
                ) {
                    Text(
                        text = "See location Rank",
                        fontWeight = FontWeight.Normal,
                        fontFamily = FontFamily(Font(R.font.font_firestar)),
                        fontStyle = FontStyle.Italic,
                        color = Color.Black,
                        fontSize = 20.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun CardView(
    position: Int,
    userRanked: UserRanked,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .shadow(4.dp, RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
        border = BorderStroke(width = 2.dp, color = Color.White),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Posición del jugador
            Text(
                text = "#$position",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(0.2f),
                color = MaterialTheme.colorScheme.primary,
            )

            // Imagen de avatar
            AsyncImage(
                model = userRanked.avatarUrl,
                contentDescription = "Avatar",
                modifier =
                    Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                placeholder = painterResource(R.drawable.im_avengers_anionuevo),
                error = painterResource(R.drawable.im_avengers_anionuevo),
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Nombre del jugador
                Text(
                    text = userRanked.userName ?: "Unknown",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
                Spacer(modifier = Modifier.height(4.dp))
                // Número de victorias
                Text(
                    text = "Victories: ${userRanked.userVictories ?: 0}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.rankedMapScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.UserRanked
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.toLatLng
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.rankedMapScreen.viewmodel.MapRankedViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.BlackCustom
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.CyanWay
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.DarkPurple
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.IndigoDye
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.SilverB
import coil.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun RankedMaps(
    navController: NavController,
    mapRankedViewModel: MapRankedViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val usersRanked by mapRankedViewModel.usersRanked.collectAsState()

    var selectedUser by remember { mutableStateOf<UserRanked?>(null) }
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    var distance by remember { mutableStateOf<Double?>(null) }

    val cameraPositionState =
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LatLng(-34.6037, -58.3816), 10f)
        }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(modifier = Modifier.weight(1f), cameraPositionState = cameraPositionState) {
            usersRanked.forEach { userInfo ->
                Marker(
                    position = userInfo.userLocation!!.toLatLng(),
                    title = userInfo.userName,
                    snippet = userInfo.userVictories.toString(),
                    onClick = {
                        selectedUser = userInfo
                        routePoints =
                            listOf(
                                userInfo.userLocation.toLatLng(),
                                LatLng(-34.67055556, -58.56277778),
                            )
                        distance =
                            mapRankedViewModel.calculateDistance(
                                userInfo.userLocation.toLatLng(),
                                LatLng(-34.67055556, -58.56277778),
                            )
                        true
                    },
                )
            }

            // Dibuja la polilínea si hay puntos en routePoints
            if (routePoints.size > 1) {
                Polyline(
                    points = routePoints,
                    color = Color.Blue,
                    width = 5f,
                )
            }
        }

        if (distance != null) {
            Text(
                text = "Distancia: ${"%.2f".format(distance)} km",
                modifier = Modifier.padding(16.dp),
            )
        }

        selectedUser?.let { userInfo ->
            AlertDialog(
                modifier = Modifier.border(4.dp, Color.White),
                onDismissRequest = { selectedUser = null },
                title = { Text("GoogleMaps UserRank") },
                text = {
                    Column {
                        Card(
                            modifier = Modifier.height(350.dp),
                            colors =
                                CardColors(
                                    containerColor = IndigoDye,
                                    contentColor = DarkPurple,
                                    disabledContentColor = BlackCustom,
                                    disabledContainerColor = CyanWay,
                                ),
                            border = BorderStroke(width = 4.dp, color = SilverB),
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Spacer(modifier = Modifier.size(16.dp))
                                AsyncImage(
                                    model = userInfo.avatarUrl,
                                    contentDescription = "Avatar",
                                    modifier =
                                        Modifier
                                            .size(200.dp)
                                            .clip(CircleShape)
                                            .border(2.dp, Color.White, CircleShape),
                                    placeholder = painterResource(R.drawable.im_avengers_anionuevo),
                                    error = painterResource(R.drawable.im_avengers_anionuevo),
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Name: ${userInfo.userName}",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Victories: ${userInfo.userVictories}",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                )
                                Spacer(modifier = Modifier.size(16.dp))
                                distance?.let { dist ->
                                    Text(
                                        "Distancia: %.2f km".format(dist),
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,
                                    )
                                }
                                Spacer(modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        mapRankedViewModel.openMaps(
                            userInfo.userLocation?.toLatLng()!!,
                            context,
                        )
                        selectedUser = null // Cerrar el diálogo
                    }) {
                        Text("Cómo llegar")
                    }
                },
                dismissButton = {
                    Button(onClick = { selectedUser = null }) {
                        Text("   Cerrar  ")
                    }
                },
            )
        }
    }

    BackHandler {
        navController.popBackStack()
    }
}

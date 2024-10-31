package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.signUpScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes.CreateAccountScreenBetaRoute
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes.HomeScreenRoute
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.DelftBlue
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.IndigoDye
import ar.edu.unlam.mobile.scaffolding.evolution.ui.theme.SilverA
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignUpScreen(
    navController: NavController,
    auth: FirebaseAuth,
) {
    LaunchedEffect(auth) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            navController.navigate(HomeScreenRoute) {
                popUpTo(HomeScreenRoute) { inclusive = true }
            }
        }
    }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(SilverA, IndigoDye),
                        startY = 0f,
                        endY = 600f,
                    ),
                ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Image(
            modifier = Modifier,
            painter = painterResource(id = R.drawable.iv_logo),
            contentDescription = "MarvelFF Logo",
        )

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = stringResource(id = R.string.SignUpTitle),
            color = Color.White,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = stringResource(id = R.string.SignUpSubTitle),
            color = Color.White,
            fontSize = 35.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(54.dp),
            onClick = { navController.navigate(CreateAccountScreenBetaRoute) },
            colors = ButtonDefaults.buttonColors(containerColor = DelftBlue),
        ) {
            Text(
                text = stringResource(id = R.string.SignUpFree),
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.size(8.dp))

        ButtonLogin(R.drawable.ic_google, R.string.Google) { /*TODO*/ }

        Spacer(modifier = Modifier.size(8.dp))

        ButtonLogin(R.drawable.ic_facebook, R.string.Facebook) { /*TODO*/ }

        Spacer(modifier = Modifier.size(24.dp))

        Text(
            modifier = Modifier.clickable { navController.navigate(CreateAccountScreenBetaRoute) },
            text = "Log In",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.weight(1f))
    }

    BackHandler {
        navController.navigate(HomeScreenRoute) {
            popUpTo<HomeScreenRoute> { inclusive = true }
        }
    }
}

@Composable
fun ButtonLogin(
    image: Int,
    message: Int,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = { onClick() },
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(60.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier = Modifier.size(25.dp),
                alignment = Alignment.CenterStart,
                painter = painterResource(id = image),
                contentDescription = "SocialNetworkLogo",
            )
            Text(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(end = 25.dp),
                text = stringResource(id = message),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
            )
        }
    }
}

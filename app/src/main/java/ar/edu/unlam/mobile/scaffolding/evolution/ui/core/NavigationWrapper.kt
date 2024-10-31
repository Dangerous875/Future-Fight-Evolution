package ar.edu.unlam.mobile.scaffolding.evolution.ui.core

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.viewmodel.NavigationWrapperViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.qrGenerateScreen.QRGenerateScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.rankedMapScreen.RankedMaps
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroCombatResultScreen.SuperHeroCombatResult
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroCombatScreen.SuperHeroCombat
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.combatResult.superHeroRankedScreen.SuperHeroRanked
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.cameraScreen.CameraScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.createAccountScreen.CreateAccountScreenBeta
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.homeScreen.ui.HomeScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.signUpScreen.SignUpScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.UploadImageScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.userProfileScreen.UserProfileScreen
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.selectCharacterMap.selectComScreen.SelectCom
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.selectCharacterMap.selectMapScreen.SelectMap
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.selectCharacterMap.selectPlayerScreen.SelectPlayer
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.selectCharacterMap.selectPlayerScreen.viewmodel.SelectCharacterViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.selectCharacterMap.superHeroDetailScreen.SuperHeroDetail

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationWrapper(viewModel: NavigationWrapperViewModel = hiltViewModel()) {
    val auth by viewModel.auth.collectAsState()
    val navController = rememberNavController()
    val selectCharacterViewModel: SelectCharacterViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = Routes.HomeScreenRoute) {
        composable<Routes.HomeScreenRoute> { HomeScreen(navController = navController) }
        composable<Routes.SelectMapRoute> {
            SelectMap(
                navController = navController,
                selectCharacterViewModel = selectCharacterViewModel,
            )
        }
        composable<Routes.SelectPlayerRoute> {
            SelectPlayer(
                navController = navController,
                selectCharacterViewModel = selectCharacterViewModel,
            )
        }
        composable<Routes.SelectComRoute> {
            SelectCom(
                navController = navController,
                selectCharacterViewModel = selectCharacterViewModel,
            )
        }
        composable<Routes.CombatResultRoute> {
            SuperHeroCombatResult(
                navController = navController,
            )
        }
        composable<Routes.CombatScreenRoute> { SuperHeroCombat(navController = navController) }
        composable<Routes.DetailRoute> {
            SuperHeroDetail(
                navController = navController,
                selectCharacterViewModel = selectCharacterViewModel,
            )
        }
        composable<Routes.RankedRoute> { SuperHeroRanked(navController = navController) }
        composable<Routes.CameraScreenRoute> {
            CameraScreen(
                navController = navController,
            )
        }
        composable<Routes.QRGenerateScreenRoute> { QRGenerateScreen(auth) }
        composable<Routes.SignUpScreenRoute> { SignUpScreen(navController = navController, auth) }
        composable<Routes.CreateAccountScreenBetaRoute> {
            CreateAccountScreenBeta(
                navController = navController,
                auth,
            )
        }

        composable<Routes.UserProfileScreenRoute> {
            UserProfileScreen(
                auth = auth,
                navController = navController,
            )
        }
        composable<Routes.RankedMapsUsersRoute> { RankedMaps(navController = navController) }

        composable<Routes.UploadImageScreenRoute> {
            UploadImageScreen(
                auth = auth,
                navController = navController,
            )
        }
    }
}

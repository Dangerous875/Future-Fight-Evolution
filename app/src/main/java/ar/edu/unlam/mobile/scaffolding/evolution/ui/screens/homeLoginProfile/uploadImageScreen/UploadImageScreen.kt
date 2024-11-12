package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes.Routes
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.imageComponent.AbrirCamara
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.imageComponent.AbrirGaleria
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.imageComponent.AddImageToDatabase
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.imageComponent.AddImageToStorage
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.imageComponent.GetImageFromDatabase
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.imageComponent.ImageContent
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

@Composable
fun UploadImageScreen(
    viewModel: UploadImageScreenViewModel = hiltViewModel(),
    navController: NavController,
    auth: FirebaseAuth,
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val galleryLauncher =
        rememberLauncherForActivityResult(contract = GetContent()) { imageUri ->
            imageUri?.let { viewModel.addImageToStorage(imageUri) }
        }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        content = { padding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
            ) {
                AbrirGaleria(
                    openGallery = {
                        val allimage: String = "image/*"
                        galleryLauncher.launch(allimage)
                    },
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
            ) {
                AbrirCamara(
                    openCamera = {
                        navController.navigate(Routes.CameraScreenBetaRoute)
                    },
                )
            }
        },
    )

    AddImageToStorage(addImageToDatabase = { downloadUrl ->
        viewModel.addImageToDatabase(downloadUrl)
    })

    fun showSnackBar() =
        coroutineScope.launch {
            val result = snackBarHostState.showSnackbar("Avatar upload successfully", "Show Me")
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.getImageFromDatabase()
            }
        }

    AddImageToDatabase(
        showSnackBar = { isImageAddedToDatabase ->
            if (isImageAddedToDatabase) {
                showSnackBar()
            }
        },
    )

    GetImageFromDatabase(
        createImageContent = { imageUrl ->
            ImageContent(imageUrl)
        },
    )

    BackHandler {
        navController.navigate(Routes.UserProfileScreenRoute) {
            popUpTo<Routes.UserProfileScreenRoute> { inclusive = true }
        }
    }
}

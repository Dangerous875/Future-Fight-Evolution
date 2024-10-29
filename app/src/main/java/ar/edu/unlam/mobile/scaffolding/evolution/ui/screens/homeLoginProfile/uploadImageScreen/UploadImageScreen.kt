package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.image.AbrirGaleria
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.image.AddImageToDatabase
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.image.AddImageToStorage
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.image.GetImageFromDatabase
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.image.ImageContent
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
                        galleryLauncher.launch(ALL_IMAGES)
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
}

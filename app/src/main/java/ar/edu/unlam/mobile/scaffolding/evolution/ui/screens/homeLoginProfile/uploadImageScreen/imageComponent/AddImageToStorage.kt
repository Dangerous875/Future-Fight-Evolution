package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.imageComponent

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse.Failure
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse.Loading
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse.Success
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.UploadImageScreenViewModel

@Composable
fun AddImageToStorage(
    viewModel: UploadImageScreenViewModel = hiltViewModel(),
    addImageToDatabase: (downloadUrl: Uri) -> Unit,
) {
    when (val addImageToStorageResponse = viewModel.addImageToStorageResponse) {
        is Loading -> ProgressBar()
        is Success ->
            addImageToStorageResponse.data?.let { downloadUrl ->
                LaunchedEffect(downloadUrl) {
                    addImageToDatabase(downloadUrl)
                }
            }

        is Failure -> print(addImageToStorageResponse.e)
    }
}

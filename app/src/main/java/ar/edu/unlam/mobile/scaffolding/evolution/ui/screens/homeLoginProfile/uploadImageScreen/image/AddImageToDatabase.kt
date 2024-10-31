package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse.Failure
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse.Loading
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse.Success
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.UploadImageScreenViewModel

@Composable
fun AddImageToDatabase(
    viewModel: UploadImageScreenViewModel = hiltViewModel(),
    showSnackBar: (isImageAddedToDatabase: Boolean) -> Unit,
) {
    when (val addImageToStorageResponse = viewModel.addImageToDatabaseResponse) {
        is Loading -> ProgressBar()
        is Success ->
            addImageToStorageResponse.data?.let { isImageAddedToDatabase ->
                LaunchedEffect(isImageAddedToDatabase) {
                    showSnackBar(isImageAddedToDatabase)
                }
            }

        is Failure -> print(addImageToStorageResponse.e)
    }
}

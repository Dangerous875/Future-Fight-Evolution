package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.image

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse.Failure
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse.Loading
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse.Success
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.UploadImageScreenViewModel

@Composable
fun GetImageFromDatabase(
    viewModel: UploadImageScreenViewModel = hiltViewModel(),
    createImageContent: @Composable (imageUrl: String) -> Unit,
) {
    when (val getImageFromDatabaseResponse = viewModel.getImageFromDatabaseResponse) {
        is Loading -> ProgressBar()
        is Success ->
            getImageFromDatabaseResponse.data?.let { imageUrl ->
                createImageContent(imageUrl)
            }

        is Failure -> print(getImageFromDatabaseResponse.e)
    }
}

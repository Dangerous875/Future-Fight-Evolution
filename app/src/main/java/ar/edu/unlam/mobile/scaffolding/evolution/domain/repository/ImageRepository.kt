package ar.edu.unlam.mobile.scaffolding.evolution.domain.repository

import android.net.Uri
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse

typealias AddImageToStorageResponse = UserDataResponse<Uri>
typealias AddImageUrlToFirestoreResponse = UserDataResponse<Boolean>
typealias GetImageFromFirestoreResponse = UserDataResponse<String>

interface ImageRepository {
    suspend fun addImageToFirebaseStorage(imageUri: Uri): AddImageToStorageResponse

    suspend fun addImageUrlToFireStore(download: Uri): AddImageUrlToFirestoreResponse

    suspend fun getImageUrlFromFireStore(): GetImageFromFirestoreResponse
}

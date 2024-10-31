package ar.edu.unlam.mobile.scaffolding.evolution.data.repository

import android.net.Uri
import ar.edu.unlam.mobile.scaffolding.evolution.data.database.firestore_collection_IMAGES
import ar.edu.unlam.mobile.scaffolding.evolution.domain.model.UserDataResponse
import ar.edu.unlam.mobile.scaffolding.evolution.domain.repository.AddImageToStorageResponse
import ar.edu.unlam.mobile.scaffolding.evolution.domain.repository.AddImageUrlToFirestoreResponse
import ar.edu.unlam.mobile.scaffolding.evolution.domain.repository.GetImageFromFirestoreResponse
import ar.edu.unlam.mobile.scaffolding.evolution.domain.repository.ImageRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageService
    @Inject
    constructor(
        private val storage: FirebaseStorage,
        private val db: FirebaseFirestore,
        private val auth: FirebaseAuth,
    ) : ImageRepository {
        @Suppress("ktlint:standard:property-naming")
        private val imageName: String = "${auth.uid}.jpg"

        @Suppress("ktlint:standard:property-naming")
        private val url = "url"

        @Suppress("ktlint:standard:property-naming")
        private val createAt = "createdAt"

        override suspend fun addImageToFirebaseStorage(imageUri: Uri): AddImageToStorageResponse =
            try {
                val downloadUrl =
                    storage.reference
                        .child(firestore_collection_IMAGES)
                        .child(auth.currentUser?.uid + imageName)
                        .putFile(imageUri)
                        .await()
                        .storage.downloadUrl
                        .await()
                UserDataResponse.Success(downloadUrl)
            } catch (e: Exception) {
                UserDataResponse.Failure(e)
            }

        override suspend fun addImageUrlToFireStore(download: Uri): AddImageUrlToFirestoreResponse =
            try {
                db
                    .collection(firestore_collection_IMAGES)
                    .document(auth.currentUser?.uid.toString())
                    .set(
                        mapOf(
                            url to download,
                            createAt to FieldValue.serverTimestamp(),
                        ),
                    ).await()
                UserDataResponse.Success(true)
            } catch (e: Exception) {
                UserDataResponse.Failure(e)
            }

        override suspend fun getImageUrlFromFireStore(): GetImageFromFirestoreResponse =
            try {
                val imageUrl =
                    db
                        .collection(firestore_collection_IMAGES)
                        .document(auth.currentUser?.uid.toString())
                        .get()
                        .await()
                        .getString(url)
                UserDataResponse.Success(imageUrl)
            } catch (e: Exception) {
                UserDataResponse.Failure(e)
            }
    }

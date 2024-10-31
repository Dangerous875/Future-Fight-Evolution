package ar.edu.unlam.mobile.scaffolding.evolution.domain.model

sealed class UserDataResponse<out T> {
    data object Loading : UserDataResponse<Nothing>()

    data class Success<out T>(
        val data: T?,
    ) : UserDataResponse<T>()

    data class Failure(
        val e: Exception,
    ) : UserDataResponse<Nothing>()
}

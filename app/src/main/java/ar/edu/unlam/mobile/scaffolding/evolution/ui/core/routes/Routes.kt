package ar.edu.unlam.mobile.scaffolding.evolution.ui.core.routes

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object SelectComRoute : Routes()

    @Serializable
    data object SelectMapRoute : Routes()

    @Serializable
    data object SelectPlayerRoute : Routes()

    @Serializable
    data object CombatResultRoute : Routes()

    @Serializable
    data object CombatScreenRoute : Routes()

    @Serializable
    data object DetailRoute : Routes()

    @Serializable
    data object RankedRoute : Routes()

    @Serializable
    data object HomeScreenRoute : Routes()

    @Serializable
    data object QRGenerateScreenRoute : Routes()

    @Serializable
    data object CameraScreenRoute : Routes()

    @Serializable
    data object SignUpScreenRoute : Routes()

    @Serializable
    data object CreateAccountScreenBetaRoute : Routes()

    @Serializable
    data object UserProfileScreenRoute : Routes()

    @Serializable
    data object RankedMapsUsersRoute : Routes()

    @Serializable
    data object UploadImageScreenRoute : Routes()

    @Serializable
    data object CameraScreenBetaRoute : Routes()

    @Serializable
    data object ShowScanScreenRoute : Routes()

    @Serializable
    data object ScanResultScreen : Routes()
}

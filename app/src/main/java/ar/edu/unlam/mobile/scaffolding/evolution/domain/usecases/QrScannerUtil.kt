package ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases

import androidx.activity.result.ActivityResultLauncher
import com.journeyapps.barcodescanner.ScanOptions

class QrScannerUtil {
    fun startQrScan(launcher: ActivityResultLauncher<ScanOptions>) {
        val options =
            ScanOptions().apply {
                setPrompt("Escanea tu código en el rectángulo")
                setBeepEnabled(true)
                setDesiredBarcodeFormats(ScanOptions.QR_CODE) // Solo QR, se puede cambiar a ALL_CODE_TYPES
                setOrientationLocked(false)
            }
        launcher.launch(options)
    }
}

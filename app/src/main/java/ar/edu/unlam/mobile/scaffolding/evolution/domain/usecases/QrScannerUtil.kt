package ar.edu.unlam.mobile.scaffolding.evolution.domain.usecases

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.google.zxing.integration.android.IntentIntegrator

class QrScannerUtil(
    private val context: Context,
) {
    // Configura e inicia el escáner QR
    fun startQrScan(launcher: ActivityResultLauncher<Intent>) {
        val integrator =
            IntentIntegrator(context as Activity).apply {
                setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                setPrompt("Escanea tu código en el rectángulo")
                setBeepEnabled(true)
            }
        launcher.launch(integrator.createScanIntent())
    }

    fun handleScanResult(
        resultCode: Int,
        data: Intent?,
    ) {
        val scanResult = IntentIntegrator.parseActivityResult(resultCode, resultCode, data)
        if (scanResult != null) {
            if (scanResult.contents == null) {
                Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show()
            } else {
                // val scannedValue = scanResult.contents
            }
        }
    }
}

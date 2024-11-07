package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.qrGenerateScreen

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ar.edu.unlam.mobile.scaffolding.R
import com.google.firebase.auth.FirebaseAuth
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

@Composable
fun QRGenerateScreen(auth: FirebaseAuth) {
    Scaffold {
        Column(
            modifier =
                Modifier
                    .background(Color.Black)
                    .padding(it)
                    .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.size(width = 0.dp, height = 50.dp))

            Text(
                text = "READ THE CODE",
                color = Color.White,
                fontSize = 40.sp,
                fontFamily =
                    FontFamily(
                        Font(
                            R.font.creepster_regular,
                        ),
                    ),
            )
            Spacer(modifier = Modifier.size(width = 0.dp, height = 180.dp))
            Image(bitmap = getQrCodeBitMap("${auth.currentUser?.uid}"), "My qr code")
        }
    }
}

fun getQrCodeBitMap(qrCodeContent: String): ImageBitmap {
    val size = 1024
    val hints =
        hashMapOf<EncodeHintType, Int>().also {
            it[EncodeHintType.MARGIN] = 1
        }
    val bits = QRCodeWriter().encode(qrCodeContent, BarcodeFormat.QR_CODE, size, size, hints)
    val bitmap =
        Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
            for (x in 0 until size) {
                for (y in 0 until size) {
                    val color = if (bits[x, y]) Color.White else Color.Black
                    it.setPixel(x, y, color.toArgb())
                }
            }
        }
    return bitmap.asImageBitmap()
}

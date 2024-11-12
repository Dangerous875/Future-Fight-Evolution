package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.uploadImageScreen.imageComponent

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AbrirCamara(openCamera: () -> Unit) {
    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(46.dp),
        contentAlignment = Alignment.BottomEnd,
    ) {
        Button(onClick = openCamera) {
            Text(
                text = "Take Picture",
                fontSize = 17.sp,
            )
        }
    }
}

package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.cameraScreen

import android.Manifest
import android.media.MediaPlayer
import android.os.Environment
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.io.File
import java.util.concurrent.Executor

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraScreenBeta(navController: NavController) {
    val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    val cameraController =
        remember {
            LifecycleCameraController(context)
        }

    val permissionState =
        rememberMultiplePermissionsState(
            permissions =
                listOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ),
        )
    LaunchedEffect(Unit) {
        permissionState.launchMultiplePermissionRequest()
    }

    val audio =
        MediaPlayer.create(context, R.raw.raw_camera).apply {
            setVolume(1.0f, 1.0f)
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = GetContent()) {}

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    audio.start()
                    Toast
                        .makeText(context, "Picture take successful :)", Toast.LENGTH_SHORT)
                        .show()
                    val executor = ContextCompat.getMainExecutor(context)
                    takePicture(cameraController, executor, directory)
                },
            ) {
                Icon(
                    painterResource(id = R.drawable.ic_camera),
                    contentDescription = "Ironman style camera",
                    tint = Color.White,
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            ButtonBarAction(
                onSwitchCamera = { enabled ->
                    cameraController.cameraSelector =
                        if (enabled) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else {
                            CameraSelector.DEFAULT_BACK_CAMERA
                        }
                },
                onOpenDirectory = {
                    val allimage = "image/*"
                    galleryLauncher.launch(allimage)
                },
                onFlashActivated = { enabled ->
                    cameraController.imageCaptureFlashMode =
                        if (enabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
                },
            )
        },
    ) {
        if (permissionState.allPermissionsGranted) {
            CamaraComposable(cameraController, lifecycle, modifier = Modifier.padding(it))
        } else {
            Text(text = "Permissions has been delegated", modifier = Modifier.padding(it))
        }
    }

    BackHandler {
        navController.popBackStack()
    }

    DisposableEffect(Unit) {
        onDispose {
            audio.stop()
            audio.release()
        }
    }
}

@Composable
fun ButtonBarAction(
    onSwitchCamera: (Boolean) -> Unit,
    onOpenDirectory: () -> Unit,
    onFlashActivated: (Boolean) -> Unit,
) {
    var flashState by rememberSaveable {
        mutableStateOf(false)
    }
    var frontCameraState by rememberSaveable {
        mutableStateOf(false)
    }

    NavigationBar(modifier = Modifier.height(50.dp), containerColor = Color.Black) {
        NavigationBarItem(selected = false, onClick = { onOpenDirectory() }, icon = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.ic_galery),
                contentDescription = "l",
                tint = Color.White,
            )
        }, label = { Text(text = "Galery", color = Color.White, fontWeight = FontWeight.Light) })

        NavigationBarItem(
            selected = false,
            onClick = {
                flashState = !flashState
                onFlashActivated(flashState)
            },
            icon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = if (flashState) R.drawable.ic_flashon else R.drawable.ic_flashoff),
                    contentDescription = "l",
                    tint = Color.White,
                )
            },
            label = {
                Text(
                    text = if (flashState) "Flash on" else "Flash off",
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                )
            },
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                frontCameraState = !frontCameraState
                onSwitchCamera(frontCameraState)
            },
            icon = {
                Icon(
                    modifier = Modifier.size(20.dp),
                    painter = painterResource(id = R.drawable.ic_change),
                    contentDescription = "l",
                    tint = Color.White,
                )
            },
            label = {
                Text(
                    text = if (frontCameraState) "Frontal on" else "Frontal off",
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                )
            },
        )
    }
}

fun takePicture(
    cameraController: LifecycleCameraController,
    executor: Executor,
    directory: File,
) {
    val image = File.createTempFile("img_", ".png", directory)
    val outputDirectory = ImageCapture.OutputFileOptions.Builder(image).build()
    cameraController.takePicture(
        outputDirectory,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                println(outputFileResults.savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                println()
            }
        },
    )
}

@Composable
fun CamaraComposable(
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier = Modifier,
) {
//    val cameraLifeCycle = LocalLifecycleOwner.current
//    cameraController.bindToLifecycle(cameraLifeCycle)
    cameraController.bindToLifecycle(lifecycleOwner)
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView =
                PreviewView(context).apply {
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                }
            previewView.controller = cameraController
            previewView
        },
    )
}

// @OptIn(ExperimentalPermissionsApi::class)
// @Composable
// private fun CaptureImage(permissionState: PermissionState) {
//    LaunchedEffect(Unit) {
//        permissionState.launchPermissionRequest()
//    }
//    if (permissionState.status.isGranted) {
//        CamaraComposable()
//    } else {
//        Text(text = "Permissions to camera delegated")
//    }
// }

package ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.cameraScreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import ar.edu.unlam.mobile.scaffolding.R
import ar.edu.unlam.mobile.scaffolding.evolution.ui.screens.homeLoginProfile.cameraScreen.viewmodel.CameraScreenViewModel
import java.io.File
import java.util.concurrent.Executor

@Composable
fun CameraScreen(
    navController: NavController,
    viewModel: CameraScreenViewModel = hiltViewModel(),
) {
    var uri: Uri? by remember { mutableStateOf(null) }
    val permissionCamera by viewModel.permissionCamera.collectAsState()
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionsGranted ->
            viewModel.setPermissionCamera(permissionsGranted.all { it.value })
        }

    val context = LocalContext.current
    val cameraController = remember { LifecycleCameraController(context) }
    val lifecycle = LocalLifecycleOwner.current
    val directory =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absoluteFile

    val audio =
        MediaPlayer.create(context, R.raw.raw_camera).apply {
            setVolume(1.0f, 1.0f)
        }

    LaunchedEffect(Unit) {
        if (!permissionCamera) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ),
            )
        }
    }

    if (permissionCamera) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.padding(bottom = 8.dp),
                    contentColor = Color.White,
                    containerColor = Color(0xFF279DFC),
                    onClick = {
                        audio.start()
                        val executor = ContextCompat.getMainExecutor(context)
                        takePhoto(cameraController, executor, directory)
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_camera),
                        contentDescription = null,
                    )
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            bottomBar = {
                ButtonBar(
                    onSwitchCamera = { enabled ->
                        cameraController.cameraSelector =
                            if (enabled) {
                                CameraSelector.DEFAULT_FRONT_CAMERA
                            } else {
                                CameraSelector.DEFAULT_BACK_CAMERA
                            }
                    },
                    onOpenDirectory = {
                        openPhotoDirectory(context, directory)
                    },
                    onFlashActivated = { enabled ->
                        cameraController.imageCaptureFlashMode =
                            if (enabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF
                    },
                )
            },
        ) {
            CameraComposable(
                camaraController = cameraController,
                lifecycle = lifecycle,
                modifier = Modifier.padding(it),
            )
        }
    } else {
        Text("La aplicación necesita acceso a la cámara para funcionar correctamente.")
    }

    DisposableEffect(Unit) {
        onDispose {
            audio.stop()
            audio.release()
        }
    }
}

@Composable
fun ButtonBar(
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

@Composable
fun CameraComposable(
    camaraController: LifecycleCameraController,
    lifecycle: LifecycleOwner,
    modifier: Modifier = Modifier,
) {
    camaraController.bindToLifecycle(lifecycle)
    AndroidView(
        modifier = modifier,
        factory = {
            val previaView =
                PreviewView(it).apply {
                    layoutParams =
                        ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT,
                        )
                }

            previaView.controller = camaraController
            previaView
        },
    )
}

private fun openPhotoDirectory(
    context: Context,
    directory: File,
) {
    val intent =
        Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(directory.absolutePath)
            setDataAndType(Uri.parse(directory.absolutePath), "*/*")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
    context.startActivity(intent)
}

private fun takePhoto(
    camaraController: LifecycleCameraController,
    executor: Executor,
    directory: File,
) {
    val image = File.createTempFile("img_", ".jpg", directory)
    val outputDirectory = ImageCapture.OutputFileOptions.Builder(image).build()

    camaraController.takePicture(
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

// fun generateUri(): Uri =
//    FileProvider.getUriForFile(
//        Objects.requireNonNull(this),
//        "ar.edu.unlam.mobile.scaffolding.provider",
//        createFile(),
//    )
//
// private fun createFile(): File {
//    File.createTempFile("hola","mundo")
// }

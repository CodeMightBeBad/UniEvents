package com.unibo.unievents.utils

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import java.io.File

@Composable
fun rememberCameraLauncher(
    onPictureTaken: (Uri) -> Unit = {}
): Pair<Uri?, () -> Unit> {
    var launcherUri by remember { mutableStateOf<Uri?>(null) }
    var pictureUri by remember { mutableStateOf<Uri?>(null) }
    val ctx = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { pictureTaken ->
        if (pictureTaken) launcherUri?.let {
            pictureUri = it
            onPictureTaken(it)
        }
    }

    val takePicture = {
        val file = File.createTempFile("tmp_img", "jpg", ctx.externalCacheDir)
        launcherUri = FileProvider.getUriForFile(ctx, "${ctx.packageName}.provider", file)
        launcher.launch(launcherUri!!)
    }

    return pictureUri to takePicture
}

@Composable
fun rememberGalleryLauncher(
    onImagePicked: (Uri) -> Unit = {}
): () -> Unit {
    val currentOnImagePicked = rememberUpdatedState(onImagePicked)

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { currentOnImagePicked.value(it) }
    }
    return { launcher.launch("image/*") }
}
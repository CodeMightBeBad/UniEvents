package com.unibo.unievents.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import java.io.ByteArrayOutputStream

fun uriToBitmap(imageUri: Uri, contentResolver: ContentResolver): Bitmap {
    val bitmap = when {
        Build.VERSION.SDK_INT < 28 -> {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
        }
        else -> {
            val source = ImageDecoder.createSource(contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        }
    }
    return bitmap
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()

    bitmap.compress(
        Bitmap.CompressFormat.JPEG,
        80,
        stream
    )

    return stream.toByteArray()
}

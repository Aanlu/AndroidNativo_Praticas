package com.example.miniproyecto3.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun crearArchivoFoto(context: Context): File {
    val carpeta = File(context.filesDir, "fotos")
    if (!carpeta.exists()) {
        carpeta.mkdirs()
    }
    return File(carpeta, "foto_${System.currentTimeMillis()}.jpg")
}

fun uriDeArchivo(context: Context, archivo: File): Uri {
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", archivo)
}

fun copiarImagen(context: Context, origen: Uri): Uri? {
    try {
        val entrada = context.contentResolver.openInputStream(origen) ?: return null
        val archivo = crearArchivoFoto(context)
        entrada.use { flujo ->
            archivo.outputStream().use { salida ->
                flujo.copyTo(salida)
            }
        }
        return uriDeArchivo(context, archivo)
    } catch (e: Exception) {
        return null
    }
}

fun formatearFecha(milisegundos: Long): String {
    val formato = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return formato.format(Date(milisegundos))
}

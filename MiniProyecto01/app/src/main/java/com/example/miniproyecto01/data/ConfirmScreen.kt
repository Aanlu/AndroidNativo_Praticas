package com.example.miniproyecto01.data

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmScreen(
    matricula: String,
    nombre: String,
    carrera: String,
    turno: String,
    activo: Boolean,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Confirmacion de Registro") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Text(
                text = "Datos del Alumno",
                fontSize = 22.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            HorizontalDivider()

            Text("Matricula: $matricula", fontSize = 18.sp)
            Text("Nombre: $nombre", fontSize = 18.sp)
            Text("Carrera: $carrera", fontSize = 18.sp)
            Text("Turno: $turno", fontSize = 18.sp)
            Text("Activo: $activo", fontSize = 18.sp)
            Text(
                text = "Estatus: " + if (activo) "Activo" else "Inactivo",
                fontSize = 18.sp,
                color = if (activo) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

            )
        }
    }
}
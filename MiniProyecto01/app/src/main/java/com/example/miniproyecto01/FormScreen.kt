package com.example.miniproyecto01

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.miniproyecto01.data.PreferencesManager

@Composable
fun FormScreen(
    onRegistrar: (String, String, String, String, Boolean) -> Unit
) {
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }

    var matricula by remember { mutableStateOf("") }
    var nombre by remember { mutableStateOf("") }
    var carrera by remember { mutableStateOf("") }
    var turno by remember { mutableStateOf("Matutino") }
    var activo by remember { mutableStateOf(true) }
    var expanded by remember { mutableStateOf(false) }

    val carreras = listOf(
        "Ingeniería de software",
        "Ingeniería Civil",
        "Ingeniería en Procesos Industriales"
    )

    LaunchedEffect(Unit) {
        matricula = preferencesManager.getMatricula()
        nombre = preferencesManager.getNombre()
        carrera = preferencesManager.getCarrera()
        turno = preferencesManager.getTurno()
        activo = preferencesManager.getActivo()
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Sistema de Registro de Estudiantes",
                fontSize = 22.sp,
                style = MaterialTheme.typography.headlineMedium
            )

            HorizontalDivider()

            OutlinedTextField(
                value = matricula,
                onValueChange = { matricula = it },
                label = { Text("Matrícula") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre completo") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = carrera,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Carrera") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    carreras.forEach { opcion ->
                        DropdownMenuItem(
                            text = { Text(opcion) },
                            onClick = {
                                carrera = opcion
                                expanded = false
                            }
                        )
                    }
                }
            }

            Text("Turno", style = MaterialTheme.typography.titleMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RadioButton(
                    selected = (turno == "Matutino"),
                    onClick = { turno = "Matutino" }
                )
                Text(
                    text = "Matutino",
                    modifier = Modifier.clickable { turno = "Matutino" }
                )

                RadioButton(
                    selected = (turno == "Vespertino"),
                    onClick = { turno = "Vespertino" }
                )
                Text(
                    text = "Vespertino",
                    modifier = Modifier.clickable { turno = "Vespertino" }
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Estatus: " + if (activo) "Activo" else "Inactivo",
                    fontSize = 16.sp
                )
                Switch(
                    checked = activo,
                    onCheckedChange = { activo = it }
                )
            }

            HorizontalDivider()

            Button(
                onClick = {
                    if (matricula.isBlank() || nombre.isBlank() || carrera.isBlank()) {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    } else {
                        preferencesManager.saveRegistro(
                            matricula = matricula,
                            nombre = nombre,
                            carrera = carrera,
                            turno = turno,
                            activo = activo
                        )
                        onRegistrar(matricula, nombre, carrera, turno, activo)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Registrar")
            }
        }
    }
}
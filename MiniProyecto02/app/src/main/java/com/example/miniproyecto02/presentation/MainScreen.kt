package com.example.miniproyecto02.presentation

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.miniproyecto02.data.Tarea
import com.example.miniproyecto02.domain.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) }
    val dao = db.tareaDao()
    val scope = rememberCoroutineScope()

    val tareas by dao.obtenerTodas().collectAsState(initial = emptyList())

    var nuevoTitulo by remember { mutableStateOf("") }
    var menuExpandido by remember { mutableStateOf(false) }
    var filtro by remember { mutableStateOf("Todas") }
    var orden by remember { mutableStateOf("Fecha") }

    val tareasFiltradas = when (filtro) {
        "Pendientes" -> tareas.filter { !it.completada }
        "Completadas" -> tareas.filter { it.completada }
        else -> tareas
    }

    val tareasVisibles = if (orden == "Estado") {
        tareasFiltradas.sortedBy { it.completada }
    } else {
        tareasFiltradas.sortedByDescending { it.fecha }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                actions = {
                    IconButton(onClick = { menuExpandido = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Más opciones")
                    }

                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Mostrar todas") },
                            onClick = {
                                filtro = "Todas"
                                menuExpandido = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mostrar pendientes") },
                            onClick = {
                                filtro = "Pendientes"
                                menuExpandido = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mostrar completadas") },
                            onClick = {
                                filtro = "Completadas"
                                menuExpandido = false
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Ordenar por fecha") },
                            onClick = {
                                orden = "Fecha"
                                menuExpandido = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Ordenar por estado") },
                            onClick = {
                                orden = "Estado"
                                menuExpandido = false
                            }
                        )
                        HorizontalDivider()
                        DropdownMenuItem(
                            text = { Text("Borrar completadas") },
                            onClick = {
                                menuExpandido = false
                                Toast.makeText(context, "Tareas completadas eliminadas", Toast.LENGTH_SHORT).show()
                                scope.launch(Dispatchers.IO) {
                                    dao.eliminarCompletadas()
                                }
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nuevoTitulo,
                onValueChange = { nuevoTitulo = it },
                label = { Text("Nueva tarea") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (nuevoTitulo.isNotBlank()) {
                        val titulo = nuevoTitulo
                        nuevoTitulo = ""
                        scope.launch(Dispatchers.IO) {
                            dao.insertar(Tarea(titulo = titulo))
                        }
                    } else {
                        Toast.makeText(context, "Escribe un título", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar tarea")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Filtro: $filtro | Orden: $orden",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (tareasVisibles.isEmpty()) {
                Text("No hay tareas para mostrar")
            }

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(tareasVisibles) { tarea ->
                    TareaItem(
                        tarea = tarea,
                        onCompletadaChange = { marcada ->
                            scope.launch(Dispatchers.IO) {
                                dao.actualizar(tarea.copy(completada = marcada))
                            }
                        },
                        onEditar = { },
                        onEliminar = {
                            scope.launch(Dispatchers.IO) {
                                dao.eliminar(tarea)
                            }
                        }
                    )
                }
            }
        }
    }
}

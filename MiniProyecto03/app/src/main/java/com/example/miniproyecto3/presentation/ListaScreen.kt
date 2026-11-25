package com.example.miniproyecto3.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.miniproyecto3.data.Publicacion
import com.example.miniproyecto3.util.formatearFecha

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaScreen(viewModel: PublicacionViewModel, onNuevaPublicacion: () -> Unit) {
    val publicaciones by viewModel.publicaciones.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Red Social Local") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevaPublicacion) {
                Icon(Icons.Default.Add, contentDescription = "Nueva publicación")
            }
        }
    ) { innerPadding ->
        if (publicaciones.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Aún no hay publicaciones. ¡Crea la primera!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(publicaciones, key = { it.id }) { publicacion ->
                    PublicacionItem(publicacion)
                }
            }
        }
    }
}

@Composable
fun PublicacionItem(publicacion: Publicacion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            if (publicacion.fotoUri.isNotEmpty()) {
                AsyncImage(
                    model = publicacion.fotoUri,
                    contentDescription = publicacion.titulo,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = publicacion.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = publicacion.descripcion,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = formatearFecha(publicacion.fecha),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

package com.example.unilocal.ui.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.unilocal.ui.components.UniPrimaryButton

@Composable
fun PlaceModerationItem(
    place: Place,
    onAuthorize: () -> Unit,
    onReject: () -> Unit
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = place.name, style = MaterialTheme.typography.titleLarge)
            Text(text = place.category, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Creado por: ${place.createdBy}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = place.description, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                UniPrimaryButton(text = "Autorizar", onClick = onAuthorize)
                OutlinedButton(onClick = onReject) {
                    Text(text = "Rechazar")
                }
            }
        }
    }
}

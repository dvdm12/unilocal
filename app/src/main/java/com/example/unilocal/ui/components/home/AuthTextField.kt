package com.example.unilocal.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Tipo de campo a renderizar: Email, Password o Texto normal.
 */
enum class AuthFieldType {
    Email,
    Password,
    Text
}

/**
 * Campo de texto reutilizable para login, registro, recuperación, formularios, etc.
 *
 * @param value Valor actual del campo.
 * @param onValueChange Callback cuando cambia el valor.
 * @param label Texto del label del campo.
 * @param placeholder Texto de ayuda dentro del campo.
 * @param leadingIcon Icono a mostrar a la izquierda.
 * @param fieldType Tipo de campo: Email, Password o Text.
 * @param errorMessage Texto de error a mostrar (opcional).
 * @param singleLine Si debe limitarse a una sola línea. Por defecto: true.
 */
@Composable
fun AuthTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    leadingIcon: ImageVector,
    fieldType: AuthFieldType = AuthFieldType.Text,
    errorMessage: String? = null,
    singleLine: Boolean = true
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label, style = TextStyle(fontSize = 15.sp)) },
            placeholder = { Text(placeholder, style = TextStyle(fontSize = 15.sp)) },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = label
                )
            },
            trailingIcon = {
                if (fieldType == AuthFieldType.Password) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Check else Icons.Default.Close,
                        contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        modifier = Modifier
                            .clickable { isPasswordVisible = !isPasswordVisible }
                            .padding(4.dp)
                    )
                }
            },
            visualTransformation = if (fieldType == AuthFieldType.Password && !isPasswordVisible)
                PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = singleLine,
            isError = errorMessage != null,
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(fontSize = 14.sp)
        )

        if (!errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = 16.dp, top = 2.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AuthTextFieldPreview() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            AuthTextField(
                value = "",
                onValueChange = {},
                label = "Correo",
                placeholder = "correo@ejemplo.com",
                leadingIcon = Icons.Default.Email,
                fieldType = AuthFieldType.Email,
                errorMessage = "Correo inválido"
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuthTextField(
                value = "",
                onValueChange = {},
                label = "Contraseña",
                placeholder = "******",
                leadingIcon = Icons.Default.Lock,
                fieldType = AuthFieldType.Password,
                errorMessage = "Contraseña muy corta"
            )
            Spacer(modifier = Modifier.height(16.dp))
            AuthTextField(
                value = "",
                onValueChange = {},
                label = "Descripción",
                placeholder = "Escribe algo...",
                leadingIcon = Icons.Default.Info,
                fieldType = AuthFieldType.Text,
                singleLine = false
            )
        }
    }
}

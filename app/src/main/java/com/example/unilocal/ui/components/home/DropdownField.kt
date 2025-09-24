package com.example.unilocal.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unilocal.R

@Composable
fun DropdownField(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    expanded = true
                }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                label = { Text(label) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = stringResource(R.string.dropdown_expand_description)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.LightGray,
                    disabledBorderColor = Color.LightGray
                )
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "DropdownField Preview")
@Composable
fun DropdownFieldPreview() {
    var selectedOption by remember { mutableStateOf("Colombia") }
    val options = listOf("Colombia", "México", "Argentina", "Chile", "Perú")

    MaterialTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            DropdownField(
                label = stringResource(R.string.register_country),
                options = options,
                selectedOption = selectedOption,
                onOptionSelected = { selectedOption = it }
            )
        }
    }
}

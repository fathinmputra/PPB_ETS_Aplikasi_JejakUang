package com.example.mymoneynotes.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mymoneynotes.model.TransactionType

@Composable
fun TransactionTypeSelector(
    selectedType: TransactionType,
    onTypeSelected: (TransactionType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        TransactionTypeOption(
            type = TransactionType.INCOME,
            isSelected = selectedType == TransactionType.INCOME,
            color = Color(0xFF4CAF50),
            title = "Pemasukan",
            onSelected = { onTypeSelected(TransactionType.INCOME) },
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        TransactionTypeOption(
            type = TransactionType.EXPENSE,
            isSelected = selectedType == TransactionType.EXPENSE,
            color = Color(0xFFF44336),
            title = "Pengeluaran",
            onSelected = { onTypeSelected(TransactionType.EXPENSE) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TransactionTypeOption(
    type: TransactionType,
    isSelected: Boolean,
    color: Color,
    title: String,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) color else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) color.copy(alpha = 0.1f) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = borderColor
        ),
        modifier = modifier
            .selectable(
                selected = isSelected,
                onClick = onSelected
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = color
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) color else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
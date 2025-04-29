package com.example.mymoneynotes.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mymoneynotes.model.Category
import com.example.mymoneynotes.ui.theme.BalanceNegativeColor
import com.example.mymoneynotes.ui.theme.BalancePositiveColor

@Composable
fun PieChart(
    data: Map<Category, Double>,
    totalExpense: Double,
    isIncome: Boolean = false
) {
    // Ambil warna surface dari MaterialTheme untuk digunakan nanti
    val surfaceColor = MaterialTheme.colorScheme.surface

    val textColor = if (isIncome) BalancePositiveColor else BalanceNegativeColor
    val emptyDataText = if (isIncome) "Belum ada data pemasukan" else "Belum ada data pengeluaran"

    if (data.isEmpty() || totalExpense <= 0) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emptyDataText,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Memperbesar tinggi untuk pie chart yang lebih besar
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val circleRadius = size.minDimension / 2.5f // Memperkecil sedikit untuk padding
                val center = Offset(size.width / 2, size.height / 2)

                var startAngle = -90f

                data.forEach { (category, value) ->
                    val sweepAngle = (value / totalExpense * 360).toFloat()

                    // Draw sector
                    drawArc(
                        color = category.color,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        topLeft = Offset(center.x - circleRadius, center.y - circleRadius),
                        size = Size(circleRadius * 2, circleRadius * 2)
                    )

                    // Draw border
                    drawArc(
                        color = Color.White,
                        startAngle = startAngle,
                        sweepAngle = sweepAngle,
                        useCenter = true,
                        style = Stroke(width = 2.dp.toPx()),
                        topLeft = Offset(center.x - circleRadius, center.y - circleRadius),
                        size = Size(circleRadius * 2, circleRadius * 2)
                    )

                    startAngle += sweepAngle
                }

                // Draw center hole for donut chart - MEMPERBESAR lubang tengah
                drawCircle(
                    color = surfaceColor,
                    radius = circleRadius * 0.65f, // Meningkatkan dari 0.5f menjadi 0.65f
                    center = center
                )
            }

            // Display total in center - perkecil teks agar pas di dalam lubang
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(130.dp) // Memperlebar sedikit dari 120.dp
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Rp${"%.0f".format(totalExpense)}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 14.sp), 
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis 
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Legend
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            data.entries.sortedByDescending { it.value }.forEach { (category, value) ->
                val percentage = value / totalExpense * 100

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(category.color)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Rp${"%.0f".format(value)}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${"%.1f".format(percentage)}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
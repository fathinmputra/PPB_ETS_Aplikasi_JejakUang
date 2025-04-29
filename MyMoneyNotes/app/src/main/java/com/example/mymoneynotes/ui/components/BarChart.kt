package com.example.mymoneynotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.*

@Composable
fun IncomeExpenseBarChart(
    income: Double,
    expense: Double,
    modifier: Modifier = Modifier
) {
    val maxValue = maxOf(income, expense).coerceAtLeast(1.0)
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Perbandingan Pemasukan & Pengeluaran",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // Income Bar
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currencyFormatter.format(income),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4CAF50)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(((income / maxValue) * 150).dp.coerceAtLeast(10.dp))
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(Color(0xFF4CAF50).copy(alpha = 0.7f))
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Pemasukan",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                // Expense Bar
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = currencyFormatter.format(expense),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFF44336)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(((expense / maxValue) * 150).dp.coerceAtLeast(10.dp))
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(Color(0xFFF44336).copy(alpha = 0.7f))
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Pengeluaran",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
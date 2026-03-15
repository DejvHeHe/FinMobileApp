package com.example.finamobileapp.view.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finamobileapp.model.entities.MonthlyGoal
import com.example.finamobileapp.view_model.uiState.GoalUiState
import java.time.LocalDate

@Composable
fun GoalBox(
    currentGoal: MonthlyGoal? = null,
    savings: Int,
    investment: Int,
    goalUiState: GoalUiState,
    onSaveClick: (MonthlyGoal) -> Unit
) {


    var isEditOpen by remember { mutableStateOf(false) }


    var savingGoalInput by remember(currentGoal) {
        mutableStateOf(
            currentGoal?.savingsGoal?.toString() ?: ""
        )
    }
    var investmentGoalInput by remember(currentGoal) {
        mutableStateOf(
            currentGoal?.investmentGoal?.toString() ?: ""
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEEEEE)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Vaše cíle pro tento měsíc:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )


            GoalStatusRow("Spoření:", savings, currentGoal?.savingsGoal ?: 0)
            GoalStatusRow("Investice:", investment, currentGoal?.investmentGoal ?: 0)

            TextButton(
                onClick = { isEditOpen = !isEditOpen },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(if (isEditOpen) "Zavřít" else "Vytvořit / Změnit cíl")
            }

            if (isEditOpen) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    // TextField pro Spoření
                    TextField(
                        value = savingGoalInput,
                        onValueChange = { newValue ->
                            // Povolíme jen číslice
                            if (newValue.all { it.isDigit() }) savingGoalInput = newValue
                        },
                        label = { Text("Cíl spoření") },
                        placeholder = { Text("Např. 5000") }, // Placeholder
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )


                    TextField(
                        value = investmentGoalInput,
                        onValueChange = { newValue ->
                            if (newValue.all { it.isDigit() }) investmentGoalInput = newValue
                        },
                        label = { Text("Cíl investic") },
                        placeholder = { Text("Např. 2000") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    TextButton(
                        onClick = {
                            val sGoal = savingGoalInput.toIntOrNull() ?: 0
                            val iGoal = investmentGoalInput.toIntOrNull() ?: 0
                            onSaveClick(
                                MonthlyGoal(
                                    year = LocalDate.now().year,
                                    month = LocalDate.now().month.value,
                                    sGoal,
                                    iGoal
                                )
                            )
                            isEditOpen = false
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Potvrdit a uložit", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}


@Composable
fun GoalStatusRow(label: String, current: Int, target: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 16.sp)
        Text(
            text = "$current / $target Kč",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = if (current >= target && target > 0) Color(0xFF2E7D32) else Color.Red
        )
    }
}
package com.example.finamobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finamobileapp.components.BalanceBox
import com.example.finamobileapp.components.CategoryBox
import com.example.finamobileapp.components.TypeBox
import com.example.finamobileapp.models.TransactionCategory
import com.example.finamobileapp.models.TypeBoxData

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Dashboard()
        }
    }
}




@Composable
fun Dashboard() {

    Scaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Color(0xFFA69D9D)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text("FinApp", fontSize = 40.sp)

            Row {
                val incomeData = TypeBoxData(
                    title = "Expense",
                    items = mapOf(
                        TransactionCategory.FUN to 45000,
                        TransactionCategory.FOOD to 2000,

                    ),
                    totalSum = 47150
                )
                TypeBox(incomeData)



            }



            Spacer(modifier = Modifier.weight(1f))

            Text("Váš zustatek", fontSize = 28.sp)
            BalanceBox(-5000)


            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DashboardPreview() {
    Dashboard()
}
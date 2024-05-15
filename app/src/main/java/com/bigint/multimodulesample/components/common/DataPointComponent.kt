package com.bigint.multimodulesample.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.bigint.multimodulesample.ui.theme.RickAction
import com.bigint.multimodulesample.ui.theme.RickTextPrimary


data class DataPoint(
    val title: String, val description: String
)

@Composable
fun DataPointComponent(dataPoint: DataPoint, modifier: Modifier = Modifier) {
    Column {
        Text(
            text = dataPoint.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = RickAction
        )

        Text(
            text = dataPoint.description,
            fontSize = 24.sp,
            color = RickTextPrimary
        )
    }
}

@Preview
@Composable
fun DataPointComponentPreview() {
    val data = DataPoint(title = "Last known location", description = "Citadel of Ricks")
    DataPointComponent(dataPoint = data)
}
package com.example.heroesofiu3.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.heroesofiu3.domain.entities.gameField.Cell

@Composable
fun CellInfo(selectedCell: Cell) {
    Column(modifier = Modifier.wrapContentSize()) {

        if (selectedCell.unit != null) {
            Text(
                text = selectedCell.unit.toString(),
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
package com.example.to_docompose.ui.screens

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.result.ResultBackNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle


@Destination(style = DestinationStyle.Dialog::class)
@Composable
fun CustomAlertDialog(
    title: String,
    message: String,
    actionNavigationResult: ResultBackNavigator<CustomAlertDialogActions>
) {
    AlertDialog(
        title = {
            Text(
                text = title,
                fontSize = MaterialTheme.typography.h4.fontSize,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = message,
                fontSize = MaterialTheme.typography.h6.fontSize,
                fontWeight = FontWeight.Bold
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    actionNavigationResult.navigateBack(CustomAlertDialogActions.YES)
                }
            ) {
                Text(text = "Yes")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    actionNavigationResult.navigateBack(CustomAlertDialogActions.NO)
                }
            ) {
                Text(text = "No")
            }
        } ,
        onDismissRequest = {
            actionNavigationResult.navigateBack()
        }
    )
}

enum class CustomAlertDialogActions{
    YES ,
    NO
}


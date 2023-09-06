package com.example.wordpuzzzlesolver

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wordpuzzzlesolver.model.Permutation

/**
 * The UI that displays the input fields for getting a string to permute on
 * and showing its result.
 */
@Composable
fun SolverUI(
    modifier: Modifier = Modifier
) {
    var scrambledTxt by rememberSaveable { mutableStateOf("") }
    var permutedWords by rememberSaveable {mutableStateOf(ArrayList<String>())}
    Column {
        InputButtonField(text = scrambledTxt, modifier = modifier, onButtonClick = {
            permutedWords = Permutation.combination(scrambledTxt)
            println("clicked")
        }, onTextChange = {
            scrambledTxt = it;
        })

        ResultColumn(
            permutedWords = permutedWords, modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SolverUIPreview() {
    SolverUI()
}
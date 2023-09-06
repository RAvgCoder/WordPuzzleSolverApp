package com.example.wordpuzzzlesolver

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview

/**
 * UI takes in input to permute on
 */
@Composable
fun InputButtonField(
    text: String,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit,
    onButtonClick: () -> Unit
) {

    var cannotPermute by remember { mutableStateOf(false) }

    // For Text Field
    @StringRes val txtPlaceHolder = R.string.txt_field_placeHolder
    @StringRes val txtLabel = R.string.txt_field_label
    @StringRes val txtErr = R.string.permutation_error_message_len
    @DrawableRes val errorIcon = R.drawable.baseline_error_outline_24

    // For Button Field
    @StringRes val buttonTxt = R.string.button_txt

    Column(
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            CustomInputValidatorField(
                text = text,
                placeHolder = txtPlaceHolder,
                label = txtLabel,
                errorMessage = txtErr,
                errorIcon = errorIcon,
                isError = cannotPermute
            ) {

                cannotPermute = it.length > 8;
                if (it.length < 12){
                    onTextChange(it)
                }
            }
            CustomValidatorButton(
                text = buttonTxt,
                enabled = !cannotPermute
            ) {
                onButtonClick()
            }
        }
    }
}


@Composable
fun CustomValidatorButton(
    @StringRes text: Int,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    onClickAction: () -> Unit
) {
    Button(
        onClick = { onClickAction() },
        enabled = enabled,
        modifier = modifier
    ) {
        Text(text = stringResource(id = text))
    }
}


/**
 * Custom Input validator field
 * @param text The text to display
 * @param placeHolder Placeholder for the field
 * @param label Label for the field
 * @param errorMessage Message to display when an error is thrown
 * @param errorIcon Icon to display when an error is thown
 * @param isError Boolean that monitors the error state
 * @param modifier Modifier for the Composable
 * @param onTextChange Callback function for when a change has
 *                      occurred in the text field
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomInputValidatorField(
    text: String,
    @StringRes placeHolder: Int,
    @StringRes label: Int,
    @StringRes errorMessage: Int,
    @DrawableRes errorIcon: Int,
    isError: Boolean,
    modifier: Modifier = Modifier,
    onTextChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = text,
        placeholder = { Text(text = stringResource(id = placeHolder)) },
        label = { Text(text = stringResource(id = label)) },
        isError = isError,
        onValueChange = {
            onTextChange(it)
        },
        supportingText = {
            if (isError) {
                Text(
                    text = stringResource(id = errorMessage,text),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            if (isError) {
                Icon(painter = painterResource(id = errorIcon), contentDescription = "Error Icon")
            }
        },
        keyboardOptions = KeyboardOptions(
            autoCorrect = false,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Preview(showSystemUi = true)
@Composable
fun InputUIPreview() {
    InputButtonField(
        text = "abcdefghij",
        modifier = Modifier,
        onButtonClick = {},
        onTextChange = {}
    )
}
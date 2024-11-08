package com.example.inventory.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.inventory.R
import com.example.inventory.ui.AppViewModelProvider
import androidx.compose.runtime.setValue
import com.example.inventory.ui.navigation.NavigationDestination
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import com.example.inventory.InventoryTopAppBar

object SettingsDestination : NavigationDestination {
    override val route = "settings"
    override val titleRes = R.string.settings
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen( navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(factory = AppViewModelProvider.Factory)
)
{

    Scaffold(
        modifier = modifier,
        topBar = {
            InventoryTopAppBar(
                title = stringResource(SettingsDestination.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
    ) { innerPadding ->
        SettingsDetailsBody(
            modifier = modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .verticalScroll(rememberScrollState()),
            viewModel = viewModel,
        )
    }

}


@Composable
private fun SettingsDetailsBody(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ){
        Row(){
            Text(text = " Hide personal data     ", color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp)
            var checked by remember { mutableStateOf(viewModel.hideData()) }
            Switch(checked = checked, onCheckedChange = {
                viewModel.switchHideData()
                checked = it
            })
        }
        Row(){
            Text(text = " Restrict sharing data  ", color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp)
            var checked by remember { mutableStateOf(viewModel.restrictedShare()) }
            Switch(checked = checked, onCheckedChange = {
                viewModel.switchRestrictedShare()
                checked = it
            })
        }
        var checkSetDefault by remember { mutableStateOf(viewModel.setDefault()) }
        Row(){
            Text(text = " Use default quantity   ", color = MaterialTheme.colorScheme.onSurface, fontSize = 20.sp)
            Switch(checked = checkSetDefault, onCheckedChange = {
                viewModel.switchSetDefault()
                checkSetDefault = it
            })
        }
        var default by remember { mutableStateOf(viewModel.default().toString()) }
        if (checkSetDefault){
            Row(){
                Text(text = "Default quantity of items ")
                OutlinedTextField(
                    value = default,
                    onValueChange = {
                        viewModel.newDefault(it)
                        default = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }
}
package com.example.fleshandbloodapppdm.ui.theme.theme.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fleshandbloodapppdm.R
import com.example.fleshandbloodapppdm.ui.theme.theme.theme.FleshAndBloodAppPdmTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height


@Composable
fun LoginView(
    navController: NavController,
) {

    val viewModel: LoginViewModel = hiltViewModel()
    val uiState = viewModel.uiState.value

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.icon_fab),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(8.dp)
        )
        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.username ?: "",
            onValueChange = { viewModel.setUsername(it) },
            label = { Text("Username") }
        )
        TextField(
            modifier = Modifier.padding(8.dp),
            value = uiState.password ?: "",
            visualTransformation = PasswordVisualTransformation(),
            onValueChange = { viewModel.setPassword(it) },
            label = { Text("Password") }
        )
        if (uiState.error != null) {
            Text(
                uiState.error ?: "",
                modifier = Modifier.padding(8.dp),
            )
        }
        Button(onClick = {
            viewModel.login(onLoginSuccess = {
                navController.navigate("decks") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }) {
            Text(
                "Login",
                modifier = Modifier.padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.register(onRegisterSuccess = {
                navController.navigate("decks") {
                    popUpTo("login") { inclusive = true }
                }
            })
        }) {
            Text(
                "Register",
                modifier = Modifier.padding(8.dp)
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    FleshAndBloodAppPdmTheme {
        LoginView(navController = rememberNavController())
    }
}

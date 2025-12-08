package com.example.fleshandbloodapppdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fleshandbloodapppdm.ui.theme.theme.theme.FleshAndBloodAppPdmTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import androidx.compose.runtime.LaunchedEffect
import com.example.fleshandbloodapppdm.ui.theme.theme.card.CardView
import com.example.fleshandbloodapppdm.ui.theme.theme.cards.CardDetailView
import com.example.fleshandbloodapppdm.ui.theme.theme.deck.DecksView
import com.example.fleshandbloodapppdm.ui.theme.theme.login.LoginView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            FleshAndBloodAppPdmTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable("login"){
                            LoginView(navController)
                        }
                        composable("home"){
                            DecksView(navController)
                        }
                        composable("cards/{deckId}"){
                            val deckId = it.arguments?.getString("deckId")
                            CardView(
                                navController = navController,
                                modifier = Modifier,
                                deckId = deckId!!
                            )
                        }
                        composable("cardDetail/{deckId}/{cardId}"){
                            val deckId = it.arguments?.getString("deckId")
                            val docId = it.arguments?.getString("cardID")
                            CardDetailView(
                                navController = navController,
                                docId = docId,
                                deckId = deckId!!
                            )
                        }
                    }
                }
            }
            LaunchedEffect(Unit){
                val currentUser = Firebase.auth.currentUser
                if(currentUser != null){
                    navController.navigate("home")
                }
            }
        }
    }
}

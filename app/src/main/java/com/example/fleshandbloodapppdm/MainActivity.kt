package com.example.fleshandbloodapppdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fleshandbloodapppdm.ui.theme.theme.theme.FleshAndBloodAppPdmTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
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
            FleshAndBloodAppPdmTheme {
                val navController = rememberNavController()

                // If already logged in, go directly to decks
                val currentUser = Firebase.auth.currentUser
                val startDestination = if (currentUser != null) "decks" else "login"

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginView(navController)
                        }
                        composable("decks") {
                            DecksView(navController)
                        }
                        composable("cards/{deckId}") { backStackEntry ->
                            val deckId = backStackEntry.arguments?.getString("deckId")!!
                            CardView(
                                navController = navController,
                                modifier = Modifier,
                                deckId = deckId
                            )
                        }
                        composable("cardDetail/{deckId}") { backStackEntry ->
                            val deckId = backStackEntry.arguments?.getString("deckId")!!
                            CardDetailView(
                                navController = navController,
                                docId = null,
                                deckId = deckId
                            )
                        }

                        composable("cardDetail/{deckId}/{cardId}") { backStackEntry ->
                            val deckId = backStackEntry.arguments?.getString("deckId")!!
                            val cardId = backStackEntry.arguments?.getString("cardId")
                            CardDetailView(
                                navController = navController,
                                docId = cardId,
                                deckId = deckId
                            )
                        }

                    }
                }
            }
        }
    }
}

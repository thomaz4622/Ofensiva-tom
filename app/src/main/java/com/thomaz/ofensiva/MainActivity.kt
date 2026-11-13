package com.thomaz.ofensiva

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = Fundo.toArgb()
        window.navigationBarColor = Grafite.toArgb()
        val store = Store(applicationContext)

        setContent {
            var aba by remember { mutableIntStateOf(0) }
            val nomes = listOf("Início", "Ranking", "Ofensiva", "Conquistas", "Perfil")
            val icones: List<ImageVector> = listOf(
                Icons.Filled.Home,
                Icons.Filled.Leaderboard,
                Icons.Filled.LocalFireDepartment,
                Icons.Filled.EmojiEvents,
                Icons.Filled.Person
            )

            Scaffold(
                containerColor = Fundo,
                bottomBar = {
                    NavigationBar(containerColor = Grafite) {
                        nomes.forEachIndexed { i, n ->
                            NavigationBarItem(
                                selected = aba == i,
                                onClick = { aba = i },
                                icon = { Icon(icones[i], contentDescription = n) },
                                label = { Text(n) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = Laranja,
                                    selectedTextColor = Laranja,
                                    unselectedIconColor = Cinza,
                                    unselectedTextColor = Cinza,
                                    indicatorColor = Fundo
                                )
                            )
                        }
                    }
                }
            ) { padding ->
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    when (aba) {
                        0 -> TelaInicio(store)
                        1 -> TelaRanking(store)
                        2 -> TelaOfensiva(store)
                        3 -> TelaConquistas(store)
                        else -> TelaPerfil(store)
                    }
                }
            }
        }
    }
}

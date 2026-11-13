package com.thomaz.ofensiva

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TelaPerfil(store: Store) {
    var editando by remember { mutableStateOf(false) }
    var texto by remember { mutableStateOf(store.nome) }
    val xp = store.xp()
    val atual = store.melhorAtual()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Perfil",
            color = Branco,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Cartao {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Laranja),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        store.nome.take(1).uppercase(),
                        color = Branco,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        store.nome,
                        color = Branco,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Nível ${numeroNivel(xp)} · ${nomeNivel(xp)}",
                        color = Cinza,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Numero("$atual", "Ofensiva", corDoMarco(atual))
                Numero("${store.melhorRecorde()}", "Recorde", Laranja)
                Numero("$xp", "XP", Branco)
            }
        }

        Cartao {
            Linha("Ofensivas criadas", "${store.ofensivas.size}")
            Linha("Total de dias concluídos", "${store.totalDias()}")
        }

        if (editando) {
            OutlinedTextField(
                value = texto,
                onValueChange = { texto = it },
                label = { Text("Seu nome") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Branco,
                    unfocusedTextColor = Branco,
                    focusedBorderColor = Laranja,
                    unfocusedBorderColor = Cinza,
                    focusedLabelColor = Laranja,
                    unfocusedLabelColor = Cinza,
                    cursorColor = Laranja
                )
            )
            Button(
                onClick = {
                    if (texto.isNotBlank()) store.mudarNome(texto.trim())
                    editando = false
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Laranja)
            ) {
                Text("Salvar", color = Branco)
            }
        } else {
            Button(
                onClick = {
                    texto = store.nome
                    editando = true
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Laranja)
            ) {
                Text("Mudar nome", color = Branco)
            }
        }
    }
}

@Composable
fun Numero(valor: String, nome: String, cor: androidx.compose.ui.graphics.Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(valor, color = cor, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Text(nome, color = Cinza, fontSize = 12.sp)
    }
}

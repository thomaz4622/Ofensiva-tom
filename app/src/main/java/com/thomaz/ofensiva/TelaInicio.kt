package com.thomaz.ofensiva

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate

@Composable
fun TelaInicio(store: Store) {
    var mostrarDialogo by remember { mutableStateOf(false) }
    val hoje = LocalDate.now().toString()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                "Olá, ${store.nome} 👋",
                color = Branco,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text("Mantenha suas chamas acesas", color = Cinza, fontSize = 14.sp)
        }

        if (store.ofensivas.isEmpty()) {
            item {
                Cartao {
                    Text(
                        "Você ainda não tem ofensivas. Toque em \"Nova ofensiva\" para começar!",
                        color = Cinza
                    )
                }
            }
        }

        items(store.ofensivas, key = { it.id }) { o ->
            val atual = ofensivaAtual(o)
            val feitoHoje = hoje in o.dias
            Cartao {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Chama(corDoMarco(atual), 48.dp)
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "${o.emoji} ${o.nome}",
                            color = Branco,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "$atual dias seguidos",
                            color = corDoMarco(atual),
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { store.alternarHoje(o.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (feitoHoje) Color(0xFF27272A) else Laranja
                    )
                ) {
                    Text(
                        if (feitoHoje) "✔ Feito hoje (toque p/ desfazer)" else "Concluir hoje",
                        color = Branco
                    )
                }
                TextButton(onClick = { store.remover(o.id) }) {
                    Text("Excluir", color = Cinza)
                }
            }
        }

        item {
            Button(
                onClick = { mostrarDialogo = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Laranja)
            ) {
                Text("+ Nova ofensiva", color = Branco)
            }
        }
    }

    if (mostrarDialogo) {
        DialogoNova(
            onFechar = { mostrarDialogo = false },
            onSalvar = { nome, emoji ->
                store.adicionar(nome, emoji)
                mostrarDialogo = false
            }
        )
    }
}

@Composable
fun DialogoNova(onFechar: () -> Unit, onSalvar: (String, String) -> Unit) {
    var titulo by remember { mutableStateOf("") }
    var emoji by remember { mutableStateOf("🔥") }
    val opcoes = listOf("🔥", "🏋️", "📚", "💻", "🎓", "🗣️")

    AlertDialog(
        onDismissRequest = onFechar,
        containerColor = Grafite,
        title = { Text("Nova ofensiva", color = Branco) },
        text = {
            Column {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Nome (ex: Treino)") },
                    singleLine = true,
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
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    opcoes.forEach { e ->
                        TextButton(
                            onClick = { emoji = e },
                            contentPadding = PaddingValues(4.dp),
                            modifier = Modifier.defaultMinSize(minWidth = 1.dp)
                        ) {
                            Text(
                                e,
                                fontSize = if (e == emoji) 28.sp else 20.sp
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (titulo.isNotBlank()) onSalvar(titulo.trim(), emoji) }
            ) {
                Text("Criar", color = Laranja)
            }
        },
        dismissButton = {
            TextButton(onClick = onFechar) {
                Text("Cancelar", color = Cinza)
            }
        }
    )
}

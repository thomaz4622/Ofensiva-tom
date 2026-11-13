package com.thomaz.ofensiva

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth
import java.time.temporal.ChronoField

private val SEMANA = listOf("SEG", "TER", "QUA", "QUI", "SEX", "SÁB", "DOM")
private val MESES = listOf(
    "Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho",
    "Julho", "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"
)

@Composable
fun TelaOfensiva(store: Store) {
    var escolhido by remember { mutableStateOf("") }
    val o = store.ofensivas.firstOrNull { it.id == escolhido }
        ?: store.ofensivas.firstOrNull()

    if (o == null) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Cartao {
                Text(
                    "Crie uma ofensiva na aba Início para ver o status aqui.",
                    color = Cinza
                )
            }
        }
        return
    }

    val atual = ofensivaAtual(o)
    val recorde = maiorOfensiva(o)
    val cor = corDoMarco(atual)
    val hoje = LocalDate.now()
    val segunda = hoje.with(ChronoField.DAY_OF_WEEK, 1)
    val feitos = o.dias.toSet()
    val naSemana = (0..6).count { segunda.plusDays(it.toLong()).toString() in feitos }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            store.ofensivas.forEach { x ->
                val sel = x.id == o.id
                Box(
                    Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (sel) Laranja else Grafite)
                        .clickable { escolhido = x.id }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text("${x.emoji} ${x.nome}", color = Branco, fontSize = 14.sp)
                }
            }
        }

        Cartao {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("OFENSIVA", color = Cinza, fontSize = 12.sp, letterSpacing = 2.sp)
                Chama(cor, 96.dp)
                Text(
                    "$atual",
                    color = cor,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold
                )
                Text("Dias consecutivos", color = Branco, fontSize = 14.sp)
                Spacer(Modifier.height(4.dp))
                Text(
                    "Seu recorde pessoal: $recorde dias",
                    color = Cinza,
                    fontSize = 13.sp
                )
                val m = marcoAtual(atual)
                if (m != null) {
                    Text(m.nome, color = m.cor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Cartao {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                SEMANA.forEachIndexed { i, nome ->
                    val dia = segunda.plusDays(i.toLong())
                    val feito = dia.toString() in feitos
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(nome, color = Cinza, fontSize = 11.sp)
                        Spacer(Modifier.height(6.dp))
                        Box(
                            Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(if (feito) cor else Color(0xFF27272A)),
                            contentAlignment = Alignment.Center
                        ) {
                            if (feito) Text("✔", color = Fundo, fontSize = 14.sp)
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
            Text("$naSemana de 7 dias nesta semana", color = Branco, fontSize = 13.sp)
        }

        Cartao {
            Text("Estatísticas pessoais", color = Branco, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Linha("Maior ofensiva", "$recorde dias")
            Linha("Ofensiva atual", "$atual dias")
            Linha("Total de dias concluídos", "${o.dias.distinct().size}")
            Linha("Dias cumpridos", "${porcentagem(o)}%")
            Linha("Vezes que perdeu", "${vezesPerdida(o)}")
        }

        Cartao {
            val mes = YearMonth.now()
            Text(
                "${MESES[mes.monthValue - 1]} ${mes.year}",
                color = Branco,
                fontWeight = FontWeight.Bold
            )
            val noMes = (1..mes.lengthOfMonth()).count {
                mes.atDay(it).toString() in feitos
            }
            Text("$noMes dias concluídos neste mês", color = Cinza, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))

            val deslocamento = mes.atDay(1).dayOfWeek.value - 1
            val totalCelulas = deslocamento + mes.lengthOfMonth()
            val linhas = (totalCelulas + 6) / 7
            for (l in 0 until linhas) {
                Row(Modifier.fillMaxWidth()) {
                    for (c in 0..6) {
                        val n = l * 7 + c - deslocamento + 1
                        Box(
                            Modifier.weight(1f).height(38.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (n in 1..mes.lengthOfMonth()) {
                                val feito = mes.atDay(n).toString() in feitos
                                Box(
                                    Modifier
                                        .size(30.dp)
                                        .clip(CircleShape)
                                        .background(if (feito) cor else Color(0xFF27272A)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "$n",
                                        color = if (feito) Fundo else Cinza,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Linha(nome: String, valor: String) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(nome, color = Cinza, fontSize = 14.sp)
        Text(valor, color = Branco, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

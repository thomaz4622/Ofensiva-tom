package com.thomaz.ofensiva

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class Ofensiva(
    val id: String,
    val nome: String,
    val emoji: String,
    val dias: List<String>
)

data class Marco(val dias: Int, val nome: String, val cor: Color)

val MARCOS = listOf(
    Marco(1, "Primeira chama", Color(0xFFA1A1AA)),
    Marco(7, "Persistente", Color(0xFF22C55E)),
    Marco(15, "Determinado", Color(0xFF3B82F6)),
    Marco(30, "Imparável", Color(0xFFA855F7)),
    Marco(60, "Elite", Color(0xFFF97316)),
    Marco(100, "Lendário", Color(0xFFEF4444)),
    Marco(365, "Imortal", Color(0xFFFACC15))
)

val NIVEIS = listOf(
    "Iniciante" to 0,
    "Aprendiz" to 100,
    "Dedicado" to 500,
    "Veterano" to 1500,
    "Mestre" to 5000,
    "Lendário" to 15000
)

fun corDoMarco(dias: Int): Color =
    MARCOS.lastOrNull { dias >= it.dias }?.cor ?: Color(0xFF52525B)

fun marcoAtual(dias: Int): Marco? = MARCOS.lastOrNull { dias >= it.dias }

fun nomeNivel(xp: Int): String = NIVEIS.last { xp >= it.second }.first

fun numeroNivel(xp: Int): Int = xp / 100 + 1

fun proximoNivel(xp: Int): Pair<String, Int>? = NIVEIS.firstOrNull { it.second > xp }

private fun datas(o: Ofensiva): List<LocalDate> =
    o.dias.map { LocalDate.parse(it) }.distinct().sorted()

fun ofensivaAtual(o: Ofensiva): Int {
    val set = o.dias.toSet()
    var d = LocalDate.now()
    if (d.toString() !in set) d = d.minusDays(1)
    var n = 0
    while (d.toString() in set) {
        n++
        d = d.minusDays(1)
    }
    return n
}

fun maiorOfensiva(o: Ofensiva): Int {
    var melhor = 0
    var atual = 0
    var anterior: LocalDate? = null
    for (d in datas(o)) {
        val a = anterior
        atual = if (a != null && a.plusDays(1) == d) atual + 1 else 1
        if (atual > melhor) melhor = atual
        anterior = d
    }
    return melhor
}

fun vezesPerdida(o: Ofensiva): Int {
    val ds = datas(o)
    if (ds.isEmpty()) return 0
    var quebras = 0
    for (i in 1 until ds.size) {
        if (ds[i - 1].plusDays(1) != ds[i]) quebras++
    }
    if (ds.last().isBefore(LocalDate.now().minusDays(1))) quebras++
    return quebras
}

fun porcentagem(o: Ofensiva): Int {
    val ds = datas(o)
    if (ds.isEmpty()) return 0
    val total = ChronoUnit.DAYS.between(ds.first(), LocalDate.now()) + 1
    return ((ds.size * 100L) / total.coerceAtLeast(1L)).toInt()
}

class Store(context: Context) {
    private val prefs = context.getSharedPreferences("ofensiva", Context.MODE_PRIVATE)

    var ofensivas by mutableStateOf(listOf<Ofensiva>())
        private set

    var nome by mutableStateOf("Thomaz")
        private set

    init {
        carregar()
    }

    private fun carregar() {
        nome = prefs.getString("nome", "Thomaz") ?: "Thomaz"
        val txt = prefs.getString("dados", "[]") ?: "[]"
        val lista = mutableListOf<Ofensiva>()
        try {
            val arr = JSONArray(txt)
            for (i in 0 until arr.length()) {
                val j = arr.getJSONObject(i)
                val dj = j.getJSONArray("dias")
                val dias = (0 until dj.length()).map { dj.getString(it) }
                lista.add(
                    Ofensiva(
                        j.getString("id"),
                        j.getString("nome"),
                        j.getString("emoji"),
                        dias
                    )
                )
            }
        } catch (e: Exception) {
        }
        ofensivas = lista
    }

    private fun salvar() {
        val arr = JSONArray()
        ofensivas.forEach { o ->
            val j = JSONObject()
            j.put("id", o.id)
            j.put("nome", o.nome)
            j.put("emoji", o.emoji)
            val dj = JSONArray()
            o.dias.forEach { dj.put(it) }
            j.put("dias", dj)
            arr.put(j)
        }
        prefs.edit()
            .putString("dados", arr.toString())
            .putString("nome", nome)
            .apply()
    }

    fun adicionar(titulo: String, emoji: String) {
        val nova = Ofensiva(System.currentTimeMillis().toString(), titulo, emoji, emptyList())
        ofensivas = ofensivas + nova
        salvar()
    }

    fun alternarHoje(id: String) {
        val hoje = LocalDate.now().toString()
        ofensivas = ofensivas.map { o ->
            if (o.id != id) o
            else if (hoje in o.dias) o.copy(dias = o.dias - hoje)
            else o.copy(dias = o.dias + hoje)
        }
        salvar()
    }

    fun remover(id: String) {
        ofensivas = ofensivas.filter { it.id != id }
        salvar()
    }

    fun mudarNome(novo: String) {
        nome = novo
        salvar()
    }

    fun totalDias(): Int = ofensivas.sumOf { it.dias.distinct().size }

    fun xp(): Int = totalDias() * 10

    fun melhorAtual(): Int = ofensivas.maxOfOrNull { ofensivaAtual(it) } ?: 0

    fun melhorRecorde(): Int = ofensivas.maxOfOrNull { maiorOfensiva(it) } ?: 0
}

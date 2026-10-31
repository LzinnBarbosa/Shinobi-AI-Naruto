package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.model.NinjaCharacter
import com.example.ui.theme.FireCrimson
import com.example.ui.theme.FlameAmber
import com.example.ui.theme.KonohaBackground
import com.example.ui.theme.KonohaBorder
import com.example.ui.theme.KonohaCard
import com.example.ui.theme.KonohaCardElevated
import com.example.ui.theme.RasenganCyan
import com.example.ui.theme.ScrollGold
import com.example.ui.theme.TextNinjaMuted
import com.example.ui.theme.TextNinjaPrimary
import com.example.ui.theme.TextNinjaSecondary
import com.example.ui.theme.UzumakiOrange
import com.example.ui.viewmodel.GrimoireViewModel
import com.example.ui.viewmodel.ShinobiAiViewModel

@Composable
fun DuelArenaScreen(
    grimoireViewModel: GrimoireViewModel,
    aiViewModel: ShinobiAiViewModel,
    modifier: Modifier = Modifier
) {
    val grimoireState by grimoireViewModel.uiState.collectAsStateWithLifecycle()
    val aiState by aiViewModel.uiState.collectAsStateWithLifecycle()

    val fighter1 = grimoireState.fighter1
    val fighter2 = grimoireState.fighter2

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KonohaBackground),
        contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = FireCrimson.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, FireCrimson.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = "⚔️ DOJO DE DUELOS SHINOBI",
                        style = MaterialTheme.typography.labelMedium,
                        color = FlameAmber,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Simulador Tático de Confrontos",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextNinjaPrimary
                )

                Text(
                    text = "Análise de jutsus, reservas de chakra e estratégias pelo Shinobi-AI",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextNinjaSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Versus Arena Card
        item {
            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = KonohaCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    Brush.horizontalGradient(listOf(UzumakiOrange, FireCrimson))
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Matchup Slots
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Fighter 1 Box
                        FighterSlot(
                            fighter = fighter1,
                            label = "Lutador 1",
                            accentColor = UzumakiOrange,
                            allNinjas = grimoireState.characters,
                            onSelectNinja = { grimoireViewModel.setFighter1(it) },
                            modifier = Modifier.weight(1f)
                        )

                        // VS Indicator & Swap
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(FireCrimson),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "VS",
                                    color = TextNinjaPrimary,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 14.sp
                                )
                            }

                            IconButton(
                                onClick = { grimoireViewModel.swapFighters() },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = "Trocar Lutadores",
                                    tint = FlameAmber
                                )
                            }
                        }

                        // Fighter 2 Box
                        FighterSlot(
                            fighter = fighter2,
                            label = "Lutador 2",
                            accentColor = RasenganCyan,
                            allNinjas = grimoireState.characters,
                            onSelectNinja = { grimoireViewModel.setFighter2(it) },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Randomize Button
                    OutlinedButton(
                        onClick = { grimoireViewModel.randomizeFighters() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = FlameAmber),
                        border = androidx.compose.foundation.BorderStroke(1.dp, KonohaBorder),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Casino,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Sortear Duelo Aleatório", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        // Start Duel Simulation Action
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (fighter1 != null && fighter2 != null) {
                        aiViewModel.startDuelAnalysis(fighter1, fighter2)
                    }
                },
                enabled = fighter1 != null && fighter2 != null && !aiState.isDuelAnalyzing,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_duel_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = UzumakiOrange,
                    contentColor = KonohaBackground
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (aiState.isDuelAnalyzing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = KonohaBackground,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Shinobi-AI Analisando Combate...", fontWeight = FontWeight.Bold)
                } else {
                    Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SIMULAR COMBATE COM SHINOBI-AI",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Duel Simulation Results Area
        if (!aiState.duelAnalysis.isNullOrBlank()) {
            item {
                Spacer(modifier = Modifier.height(18.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = KonohaCardElevated),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, UzumakiOrange.copy(alpha = 0.5f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🔥", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Veredito Tático do Shinobi-AI",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = UzumakiOrange
                                )
                            }

                            if (!aiState.isDuelAnalyzing) {
                                IconButton(
                                    onClick = { aiViewModel.clearDuelAnalysis() },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Limpar Análise",
                                        tint = TextNinjaSecondary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = aiState.duelAnalysis.orEmpty(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextNinjaPrimary,
                            lineHeight = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FighterSlot(
    fighter: NinjaCharacter?,
    label: String,
    accentColor: androidx.compose.ui.graphics.Color,
    allNinjas: List<NinjaCharacter>,
    onSelectNinja: (NinjaCharacter) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = accentColor,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .border(2.dp, accentColor, CircleShape)
                .background(KonohaCardElevated)
                .clickable { expanded = true },
            contentAlignment = Alignment.Center
        ) {
            val image = fighter?.getPrimaryImage()
            if (!image.isNullOrBlank()) {
                AsyncImage(
                    model = image,
                    contentDescription = fighter.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = fighter?.name ?: "Selecionar Ninja",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = TextNinjaPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center
        )

        Text(
            text = fighter?.getVillage() ?: "Toque para escolher",
            style = MaterialTheme.typography.labelSmall,
            color = TextNinjaSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            allNinjas.forEach { ninja ->
                DropdownMenuItem(
                    text = { Text(ninja.name) },
                    onClick = {
                        onSelectNinja(ninja)
                        expanded = false
                    }
                )
            }
        }
    }
}

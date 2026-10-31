package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.NinjaCharacter
import com.example.ui.components.NatureChip
import com.example.ui.theme.FireCrimson
import com.example.ui.theme.FlameAmber
import com.example.ui.theme.KonohaBorder
import com.example.ui.theme.KonohaCard
import com.example.ui.theme.KonohaCardElevated
import com.example.ui.theme.KonohaSurface
import com.example.ui.theme.LeafGreen
import com.example.ui.theme.RasenganCyan
import com.example.ui.theme.ScrollGold
import com.example.ui.theme.TextNinjaMuted
import com.example.ui.theme.TextNinjaPrimary
import com.example.ui.theme.TextNinjaSecondary
import com.example.ui.theme.UzumakiOrange

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CharacterDetailSheet(
    ninja: NinjaCharacter?,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onAskAi: (NinjaCharacter) -> Unit,
    onSetFighter1: (NinjaCharacter) -> Unit,
    onSetFighter2: (NinjaCharacter) -> Unit
) {
    if (ninja == null) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = KonohaSurface,
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // Header with dismiss button and avatar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                UzumakiOrange.copy(alpha = 0.25f),
                                KonohaSurface
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = TextNinjaSecondary
                    )
                }

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .border(3.dp, UzumakiOrange, CircleShape)
                            .background(KonohaCardElevated),
                        contentAlignment = Alignment.Center
                    ) {
                        val imageUrl = ninja.getPrimaryImage()
                        if (!imageUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = imageUrl,
                                contentDescription = ninja.name,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = ninja.name,
                                tint = UzumakiOrange,
                                modifier = Modifier.size(52.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = ninja.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextNinjaPrimary
                    )

                    Text(
                        text = "${ninja.getVillage()} • ${ninja.getClanName()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = UzumakiOrange,
                        fontWeight = FontWeight.Medium
                    )

                    val titles = ninja.personal?.titles
                    val titleText = when (titles) {
                        is List<*> -> titles.filterIsInstance<String>().joinToString(" • ")
                        is String -> titles
                        else -> null
                    }
                    if (!titleText.isNullOrBlank()) {
                        Text(
                            text = "« $titleText »",
                            style = MaterialTheme.typography.bodySmall,
                            color = ScrollGold,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            onDismiss()
                            onAskAi(ninja)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("detail_ask_ai_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = UzumakiOrange,
                            contentColor = KonohaSurface
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Perguntar à IA", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            onSetFighter1(ninja)
                            onDismiss()
                        },
                        modifier = Modifier.testTag("detail_duel_button"),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = FlameAmber
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, FlameAmber),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsKabaddi,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Duelo (L1)")
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Stats Section
                SectionTitle(title = "Dados de Registro Ninja")
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = KonohaCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        InfoRow(label = "Aniversário", value = ninja.personal?.birthdate ?: "Desconhecido")
                        InfoRow(label = "Status", value = ninja.personal?.status ?: "Desconhecido")
                        InfoRow(label = "Ocupação / Rank", value = ninja.personal?.occupation?.toString() ?: "Shinobi")
                        val kgList = ninja.getKekkeiGenkai()
                        if (kgList.isNotEmpty()) {
                            InfoRow(label = "Kekkei Genkai", value = kgList.joinToString(", "))
                        }
                        if (ninja.debut?.manga != null || ninja.debut?.anime != null) {
                            InfoRow(label = "Estreia (Mangá)", value = ninja.debut.manga ?: "N/A")
                            InfoRow(label = "Estreia (Anime)", value = ninja.debut.anime ?: "N/A")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Nature Types
                if (ninja.natureType.isNotEmpty()) {
                    SectionTitle(title = "Transformação da Natureza do Chakra")
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ninja.natureType.forEach { nature ->
                            NatureChip(nature = nature)
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }

                // Signature Jutsus
                if (ninja.jutsu.isNotEmpty()) {
                    SectionTitle(title = "Técnicas & Jutsus Notáveis (${ninja.jutsu.size})")
                    Column(
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ninja.jutsu.forEach { jutsuName ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = KonohaCard,
                                modifier = Modifier.fillMaxWidth(),
                                border = androidx.compose.foundation.BorderStroke(1.dp, KonohaBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "⚡",
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = jutsuName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextNinjaPrimary
                                    )
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(18.dp))
                }

                // Family Tree
                if (!ninja.family.isNullOrEmpty()) {
                    SectionTitle(title = "Laços Familiares & Mestres")
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = KonohaCard),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            ninja.family.forEach { (relation, relativeName) ->
                                InfoRow(label = relation, value = relativeName)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = ScrollGold,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextNinjaSecondary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = TextNinjaPrimary
        )
    }
}

package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.NinjaCharacter
import com.example.ui.theme.ChakraCyan
import com.example.ui.theme.FireCrimson
import com.example.ui.theme.FlameAmber
import com.example.ui.theme.KonohaBorder
import com.example.ui.theme.KonohaCard
import com.example.ui.theme.KonohaCardElevated
import com.example.ui.theme.LeafGreen
import com.example.ui.theme.RasenganCyan
import com.example.ui.theme.ScrollGold
import com.example.ui.theme.TextNinjaMuted
import com.example.ui.theme.TextNinjaPrimary
import com.example.ui.theme.TextNinjaSecondary
import com.example.ui.theme.UzumakiOrange

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NinjaCard(
    ninja: NinjaCharacter,
    onClick: () -> Unit,
    onAskAi: () -> Unit,
    onSelectForDuel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(KonohaBorder, UzumakiOrange.copy(alpha = 0.3f))
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
            .testTag("ninja_card_${ninja.id}"),
        colors = CardDefaults.cardColors(
            containerColor = KonohaCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header with Avatar and Basic Info
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with glowing ring
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .border(2.dp, UzumakiOrange, CircleShape)
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
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = ninja.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextNinjaPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "${ninja.getVillage()} • ${ninja.getClanName()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextNinjaSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val status = ninja.personal?.status ?: "Ativo"
                    Text(
                        text = status,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (status.contains("Falecido", ignoreCase = true)) FireCrimson else LeafGreen,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Signature Jutsus or Nature Types Pills
            if (ninja.natureType.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ninja.natureType.take(3).forEach { nature ->
                        NatureChip(nature = nature)
                    }
                }
            } else if (ninja.jutsu.isNotEmpty()) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ninja.jutsu.take(2).forEach { jutsu ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = KonohaCardElevated,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Text(
                                text = jutsu,
                                style = MaterialTheme.typography.labelSmall,
                                color = ScrollGold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Details prompt
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable(onClick = onClick)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Ver Pergaminho",
                        tint = RasenganCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Ver Pergaminho",
                        style = MaterialTheme.typography.labelSmall,
                        color = RasenganCyan,
                        fontWeight = FontWeight.Medium
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    // Send to duel
                    IconButton(
                        onClick = onSelectForDuel,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("ninja_duel_${ninja.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SportsKabaddi,
                            contentDescription = "Selecionar para Duelo",
                            tint = FlameAmber,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Ask AI
                    IconButton(
                        onClick = onAskAi,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("ninja_ask_ai_${ninja.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Perguntar ao Shinobi-AI",
                            tint = UzumakiOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NatureChip(nature: String) {
    val chipColor = when {
        nature.contains("Fogo", ignoreCase = true) || nature.contains("Katon", ignoreCase = true) -> FireCrimson
        nature.contains("Vento", ignoreCase = true) || nature.contains("Fūton", ignoreCase = true) -> LeafGreen
        nature.contains("Relâmpago", ignoreCase = true) || nature.contains("Raiton", ignoreCase = true) -> FlameAmber
        nature.contains("Terra", ignoreCase = true) || nature.contains("Doton", ignoreCase = true) -> ScrollGold
        nature.contains("Água", ignoreCase = true) || nature.contains("Suiton", ignoreCase = true) -> RasenganCyan
        else -> ChakraCyan
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = chipColor.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, chipColor.copy(alpha = 0.5f))
    ) {
        Text(
            text = nature,
            style = MaterialTheme.typography.labelSmall,
            color = chipColor,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            fontSize = 11.sp
        )
    }
}

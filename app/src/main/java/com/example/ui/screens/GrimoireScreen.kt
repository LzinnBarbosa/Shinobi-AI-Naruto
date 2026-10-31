package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.NinjaCharacter
import com.example.ui.components.NinjaCard
import com.example.ui.theme.KonohaBackground
import com.example.ui.theme.KonohaBorder
import com.example.ui.theme.KonohaCard
import com.example.ui.theme.RasenganCyan
import com.example.ui.theme.ScrollGold
import com.example.ui.theme.TextNinjaMuted
import com.example.ui.theme.TextNinjaPrimary
import com.example.ui.theme.TextNinjaSecondary
import com.example.ui.theme.UzumakiOrange
import com.example.ui.viewmodel.GrimoireViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrimoireScreen(
    viewModel: GrimoireViewModel,
    onNavigateToAiChat: (NinjaCharacter) -> Unit,
    onNavigateToDuel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val villages = listOf("Konohagakure", "Sunagakure", "Amegakure", "Akatsuki", "Kirigakure", "Kumogakure")
    val clans = listOf("Uchiha", "Uzumaki", "Senju", "Hyūga", "Hatake")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(KonohaBackground),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.banner_konoha),
                    contentDescription = "Monumento dos Hokages Konoha",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                // Gradient Scrim
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    KonohaBackground.copy(alpha = 0.7f),
                                    KonohaBackground
                                )
                            )
                        )
                )
                // Title on banner
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "GRIMÓRIO NINJA",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = UzumakiOrange,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Pergaminhos Oficiais & Histórias da Vontade do Fogo",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextNinjaSecondary
                    )
                }

                // Refresh Action
                IconButton(
                    onClick = { viewModel.loadCharacters(forceRefresh = true) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Recarregar Grimório",
                        tint = TextNinjaPrimary
                    )
                }
            }
        }

        // Search Bar
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_input"),
                    placeholder = {
                        Text("Buscar ninja, técnica ou clã...", color = TextNinjaMuted)
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = UzumakiOrange
                        )
                    },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Limpar busca",
                                    tint = TextNinjaSecondary
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = KonohaCard,
                        unfocusedContainerColor = KonohaCard,
                        focusedBorderColor = UzumakiOrange,
                        unfocusedBorderColor = KonohaBorder,
                        focusedTextColor = TextNinjaPrimary,
                        unfocusedTextColor = TextNinjaPrimary
                    ),
                    singleLine = true
                )
            }
        }

        // Filter Chips (Villages)
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.selectedVillage == null,
                        onClick = { viewModel.setVillageFilter(null) },
                        label = { Text("Todas as Vilas") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = UzumakiOrange,
                            selectedLabelColor = KonohaBackground,
                            containerColor = KonohaCard,
                            labelColor = TextNinjaSecondary
                        )
                    )

                    villages.forEach { village ->
                        FilterChip(
                            selected = uiState.selectedVillage == village,
                            onClick = { viewModel.setVillageFilter(village) },
                            label = { Text(village) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = UzumakiOrange,
                                selectedLabelColor = KonohaBackground,
                                containerColor = KonohaCard,
                                labelColor = TextNinjaSecondary
                            )
                        )
                    }
                }

                // Filter Chips (Clans)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.selectedClan == null,
                        onClick = { viewModel.setClanFilter(null) },
                        label = { Text("Todos os Clãs") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ScrollGold,
                            selectedLabelColor = KonohaBackground,
                            containerColor = KonohaCard,
                            labelColor = TextNinjaSecondary
                        )
                    )

                    clans.forEach { clan ->
                        FilterChip(
                            selected = uiState.selectedClan == clan,
                            onClick = { viewModel.setClanFilter(clan) },
                            label = { Text(clan) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ScrollGold,
                                selectedLabelColor = KonohaBackground,
                                containerColor = KonohaCard,
                                labelColor = TextNinjaSecondary
                            )
                        )
                    }
                }
            }
        }

        // Count / Status Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${uiState.filteredCharacters.size} Shinobis encontrados",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextNinjaSecondary
                )

                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = UzumakiOrange,
                        strokeWidth = 2.dp
                    )
                }
            }
        }

        // Ninja List Items
        if (uiState.filteredCharacters.isEmpty() && !uiState.isLoading) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "📜 Nenhum ninja encontrado",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = TextNinjaPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tente buscar por outro termo, técnica ou limpe os filtros.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextNinjaSecondary
                        )
                    }
                }
            }
        } else {
            items(
                items = uiState.filteredCharacters,
                key = { it.id }
            ) { ninja ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    NinjaCard(
                        ninja = ninja,
                        onClick = { viewModel.selectCharacter(ninja) },
                        onAskAi = {
                            viewModel.selectCharacter(null)
                            onNavigateToAiChat(ninja)
                        },
                        onSelectForDuel = {
                            viewModel.setFighter1(ninja)
                            onNavigateToDuel()
                        }
                    )
                }
            }
        }
    }
}

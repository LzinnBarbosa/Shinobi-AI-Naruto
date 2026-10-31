package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.SportsKabaddi
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.SportsKabaddi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.KonohaBackground
import com.example.ui.theme.KonohaCard
import com.example.ui.theme.KonohaSurface
import com.example.ui.theme.TextNinjaMuted
import com.example.ui.theme.TextNinjaPrimary
import com.example.ui.theme.UzumakiOrange
import com.example.ui.viewmodel.GrimoireViewModel
import com.example.ui.viewmodel.ShinobiAiViewModel

enum class MainTab {
    GRIMOIRE,
    DUELS,
    SHINOBI_AI
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    grimoireViewModel: GrimoireViewModel = viewModel(),
    aiViewModel: ShinobiAiViewModel = viewModel()
) {
    var selectedTab by remember { mutableStateOf(MainTab.GRIMOIRE) }
    val grimoireState by grimoireViewModel.uiState.collectAsStateWithLifecycle()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Handle back button: if not on Grimoire, return to Grimoire
    BackHandler(enabled = selectedTab != MainTab.GRIMOIRE) {
        selectedTab = MainTab.GRIMOIRE
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = KonohaBackground,
        bottomBar = {
            NavigationBar(
                containerColor = KonohaSurface,
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("main_navigation_bar"),
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = selectedTab == MainTab.GRIMOIRE,
                    onClick = { selectedTab = MainTab.GRIMOIRE },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == MainTab.GRIMOIRE) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                            contentDescription = "Grimório"
                        )
                    },
                    label = { Text("Grimório") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KonohaBackground,
                        selectedTextColor = UzumakiOrange,
                        indicatorColor = UzumakiOrange,
                        unselectedIconColor = TextNinjaMuted,
                        unselectedTextColor = TextNinjaMuted
                    ),
                    modifier = Modifier.testTag("tab_grimoire")
                )

                NavigationBarItem(
                    selected = selectedTab == MainTab.DUELS,
                    onClick = { selectedTab = MainTab.DUELS },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == MainTab.DUELS) Icons.Filled.SportsKabaddi else Icons.Outlined.SportsKabaddi,
                            contentDescription = "Duelos"
                        )
                    },
                    label = { Text("Duelos") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KonohaBackground,
                        selectedTextColor = UzumakiOrange,
                        indicatorColor = UzumakiOrange,
                        unselectedIconColor = TextNinjaMuted,
                        unselectedTextColor = TextNinjaMuted
                    ),
                    modifier = Modifier.testTag("tab_duels")
                )

                NavigationBarItem(
                    selected = selectedTab == MainTab.SHINOBI_AI,
                    onClick = { selectedTab = MainTab.SHINOBI_AI },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == MainTab.SHINOBI_AI) Icons.Filled.AutoAwesome else Icons.Outlined.AutoAwesome,
                            contentDescription = "Shinobi-AI"
                        )
                    },
                    label = { Text("Shinobi-AI") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = KonohaBackground,
                        selectedTextColor = UzumakiOrange,
                        indicatorColor = UzumakiOrange,
                        unselectedIconColor = TextNinjaMuted,
                        unselectedTextColor = TextNinjaMuted
                    ),
                    modifier = Modifier.testTag("tab_shinobi_ai")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "tab_transition"
            ) { tab ->
                when (tab) {
                    MainTab.GRIMOIRE -> GrimoireScreen(
                        viewModel = grimoireViewModel,
                        onNavigateToAiChat = { ninja ->
                            aiViewModel.askAboutNinja(ninja)
                            selectedTab = MainTab.SHINOBI_AI
                        },
                        onNavigateToDuel = {
                            selectedTab = MainTab.DUELS
                        }
                    )
                    MainTab.DUELS -> DuelArenaScreen(
                        grimoireViewModel = grimoireViewModel,
                        aiViewModel = aiViewModel
                    )
                    MainTab.SHINOBI_AI -> ShinobiAiScreen(
                        viewModel = aiViewModel
                    )
                }
            }

            // Character Details Sheet
            if (grimoireState.selectedCharacter != null) {
                CharacterDetailSheet(
                    ninja = grimoireState.selectedCharacter,
                    sheetState = sheetState,
                    onDismiss = { grimoireViewModel.selectCharacter(null) },
                    onAskAi = { ninja ->
                        aiViewModel.askAboutNinja(ninja)
                        selectedTab = MainTab.SHINOBI_AI
                    },
                    onSetFighter1 = { ninja ->
                        grimoireViewModel.setFighter1(ninja)
                        selectedTab = MainTab.DUELS
                    },
                    onSetFighter2 = { ninja ->
                        grimoireViewModel.setFighter2(ninja)
                        selectedTab = MainTab.DUELS
                    }
                )
            }
        }
    }
}

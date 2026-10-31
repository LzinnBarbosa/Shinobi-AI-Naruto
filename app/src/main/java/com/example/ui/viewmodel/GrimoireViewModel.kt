package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.NinjaCharacter
import com.example.data.repository.NinjaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GrimoireUiState(
    val characters: List<NinjaCharacter> = emptyList(),
    val filteredCharacters: List<NinjaCharacter> = emptyList(),
    val searchQuery: String = "",
    val selectedVillage: String? = null,
    val selectedClan: String? = null,
    val isLoading: Boolean = false,
    val selectedCharacter: NinjaCharacter? = null,
    val fighter1: NinjaCharacter? = null,
    val fighter2: NinjaCharacter? = null
)

class GrimoireViewModel(
    private val repository: NinjaRepository = NinjaRepository()
) : ViewModel() {

    private val _rawCharacters = MutableStateFlow<List<NinjaCharacter>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _selectedVillage = MutableStateFlow<String?>(null)
    private val _selectedClan = MutableStateFlow<String?>(null)
    private val _isLoading = MutableStateFlow(false)
    private val _selectedCharacter = MutableStateFlow<NinjaCharacter?>(null)
    private val _fighter1 = MutableStateFlow<NinjaCharacter?>(null)
    private val _fighter2 = MutableStateFlow<NinjaCharacter?>(null)

    val uiState: StateFlow<GrimoireUiState> = combine(
        combine(_rawCharacters, _searchQuery, _selectedVillage) { list, query, village ->
            Triple(list, query, village)
        },
        combine(_selectedClan, _isLoading, _selectedCharacter) { clan, loading, selected ->
            Triple(clan, loading, selected)
        },
        combine(_fighter1, _fighter2) { f1, f2 ->
            Pair(f1, f2)
        }
    ) { (rawList, query, village), (clan, loading, selected), (f1, f2) ->
        val filtered = rawList.filter { ninja ->
            val matchesQuery = query.isBlank() ||
                ninja.name.contains(query, ignoreCase = true) ||
                ninja.jutsu.any { it.contains(query, ignoreCase = true) } ||
                ninja.getClanName().contains(query, ignoreCase = true)

            val matchesVillage = village == null ||
                ninja.getVillage().contains(village, ignoreCase = true) ||
                ninja.getAffiliations().any { it.contains(village, ignoreCase = true) }

            val matchesClan = clan == null ||
                ninja.getClanName().contains(clan, ignoreCase = true)

            matchesQuery && matchesVillage && matchesClan
        }

        GrimoireUiState(
            characters = rawList,
            filteredCharacters = filtered,
            searchQuery = query,
            selectedVillage = village,
            selectedClan = clan,
            isLoading = loading,
            selectedCharacter = selected,
            fighter1 = f1,
            fighter2 = f2
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GrimoireUiState(isLoading = true)
    )

    init {
        loadCharacters()
    }

    fun loadCharacters(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = repository.getCharacters(forceRefresh)
                _rawCharacters.value = list
                // Pre-assign fighters if not set
                if (_fighter1.value == null && list.isNotEmpty()) {
                    _fighter1.value = list.firstOrNull { it.name.contains("Naruto") } ?: list.first()
                }
                if (_fighter2.value == null && list.size > 1) {
                    _fighter2.value = list.firstOrNull { it.name.contains("Sasuke") } ?: list[1]
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setVillageFilter(village: String?) {
        _selectedVillage.value = if (_selectedVillage.value == village) null else village
    }

    fun setClanFilter(clan: String?) {
        _selectedClan.value = if (_selectedClan.value == clan) null else clan
    }

    fun selectCharacter(ninja: NinjaCharacter?) {
        _selectedCharacter.value = ninja
    }

    fun setFighter1(ninja: NinjaCharacter) {
        _fighter1.value = ninja
    }

    fun setFighter2(ninja: NinjaCharacter) {
        _fighter2.value = ninja
    }

    fun swapFighters() {
        val temp = _fighter1.value
        _fighter1.value = _fighter2.value
        _fighter2.value = temp
    }

    fun randomizeFighters() {
        val list = _rawCharacters.value
        if (list.size >= 2) {
            val shuffled = list.shuffled()
            _fighter1.value = shuffled[0]
            _fighter2.value = shuffled[1]
        }
    }
}

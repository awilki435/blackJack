package com.andrewwilkinson.blackjack.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import com.andrewwilkinson.blackjack.ui.models.StoreItem
import com.andrewwilkinson.blackjack.ui.repositories.StoreRepository

class StoreState{
    var _store = mutableStateListOf<StoreItem>()
    val store: List<StoreItem> get() = _store
    fun addStoreItem(item: StoreItem){
        _store.add(item)
    }
    fun storeSort(){
        _store.sortBy {it.price}
    }
}
class StoreViewModel(application: Application): AndroidViewModel(application) {
    val uiState = StoreState()
    suspend fun getStore(): List<StoreItem>{
        val newStore = StoreRepository.getItems()
        for(item in newStore){
            uiState.addStoreItem(item)
        }
        uiState.storeSort()
        return uiState.store
    }
}
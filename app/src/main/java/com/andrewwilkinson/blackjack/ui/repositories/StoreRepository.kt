package com.andrewwilkinson.blackjack.ui.repositories

import com.andrewwilkinson.blackjack.ui.models.StoreItem
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

object StoreRepository {
    var storeItemCache = StoreItem()
    private var store = mutableListOf<StoreItem>()

     private suspend fun getStore(){
        try {
            val snapshot = Firebase.firestore.collection("store")
                .get()
                .await()
            val newStore = snapshot.toObjects<StoreItem>()
            for(item in newStore){
                store.add(item)
            }
        } catch (_: Exception) {
        }
    }
    suspend fun addItem(){
    }
    suspend fun deleteItem(){
    }
    suspend fun editItem(){
    }
    suspend fun getItems(): MutableList<StoreItem>{
        if(store.isEmpty()){
            getStore()
        }
        return store
    }
}
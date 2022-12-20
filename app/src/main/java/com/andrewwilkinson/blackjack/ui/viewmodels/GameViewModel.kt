package com.andrewwilkinson.blackjack.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.andrewwilkinson.blackjack.R
import com.andrewwilkinson.blackjack.ui.models.Card
import com.andrewwilkinson.blackjack.ui.models.User
import com.andrewwilkinson.blackjack.ui.repositories.UserRepository
import kotlin.random.Random

class GameState{
    private val _cards = mutableStateListOf<Card>()
    private val _userCards = mutableStateListOf<Card>()
    val userCards: List<Card> get() = _userCards
    private val _aiCards = mutableStateListOf<Card>()
    val aiCards: List<Card> get() = _aiCards
    var sumUser by mutableStateOf(0)
    var sumAi by mutableStateOf(0)
    var aiDone by mutableStateOf(false)
    var userDone by mutableStateOf(false)
    var showing by mutableStateOf(false)
    var gameOver by mutableStateOf(false)
    var bet by mutableStateOf(false)
    var win by mutableStateOf(false)
    var lose by mutableStateOf(false)
    var tie by mutableStateOf(false)
    var betAmount by mutableStateOf(300)
    var redeal by mutableStateOf(false)
    var loading by mutableStateOf(false)
    fun addCard(card: Card){
        _cards.add(card)
    }
    fun clearCards(){
        _cards.clear()
    }
    fun addUserCard(){
        val index = Random.nextInt(from = 0, until = 52)
        _userCards.add(_cards[index])
    }
    fun addAICard(){
        val index = Random.nextInt(from = 0, until = 52)
        _aiCards.add(_cards[index])
    }
    fun getAICard(index: Int): Card{
        return try {
            val newCard = _aiCards[index]
            newCard
        }catch(e: Exception){
            Card()
        }
    }
    fun clearUserCards(){
        _userCards.clear()
    }
    fun clearAICards(){
        _aiCards.clear()
    }
}

class GameViewModel(application: Application): AndroidViewModel(application) {
    val uiState = GameState()

    fun dealCards(){
        uiState.clearUserCards()
        uiState.clearAICards()
        uiState.clearCards()
        cardSetup()
        uiState.addUserCard()
        uiState.addUserCard()
        uiState.addAICard()
        uiState.addAICard()
    }
    fun sumUser(): Int{
        for(card in uiState.userCards){
            if(card.number == 1 && uiState.sumUser < 10){
                uiState.sumUser += 11
            }
            else if(card.number == 1 && uiState.sumUser > 11){
                uiState.sumUser += 1
            }
            else {
                uiState.sumUser += card.number!!
            }
        }
        return uiState.sumUser
    }
    fun addUser(){
        if(uiState.aiDone) {
            uiState.sumUser = 0
            uiState.addUserCard()
            sumUser()
        }
    }
    private fun addAi(){
        uiState.sumAi = 0
        uiState.addAICard()
        sumAi()
    }
    fun sumAi(): Int{
        for(card in uiState.aiCards){
            if(card.number == 11 && uiState.sumAi < 10){
                uiState.sumAi += 11
            }
            else if(card.number == 11 && uiState.sumAi > 11){
                uiState.sumAi += 1
            }
            else {
                uiState.sumAi += card.number!!
            }
        }
        return uiState.sumAi
    }
    private fun aiTurn(){
        if(uiState.sumAi< 17){
            addAi()
        }else{
            uiState.aiDone = true
        }
    }
    private suspend fun gameOver(){
        uiState.gameOver = true
        val currentUser = UserRepository.getCurrentUser()
        if(uiState.sumAi > uiState.sumUser && uiState.sumAi <= 21){
            uiState.lose = true
            UserRepository.updateUser(
                User(id = currentUser.id,
                    name = currentUser.name,
                    userId = currentUser.userId,
                    email = currentUser.email,
                    money = (currentUser.money!! - uiState.betAmount)
                )
            )
        }
        else if(uiState.sumAi < uiState.sumUser && uiState.sumUser <= 21){
            uiState.win = true
            UserRepository.updateUser(
                User(id = currentUser.id,
                    name = currentUser.name,
                    userId = currentUser.userId,
                    email = currentUser.email,
                    money = (currentUser.money!! + (uiState.betAmount * 2))
                )
            )
        }
        else if(uiState.sumAi > 21){
            uiState.win = true
            UserRepository.updateUser(
                User(id = currentUser.id,
                    name = currentUser.name,
                    userId = currentUser.userId,
                    email = currentUser.email,
                    money = (currentUser.money!! + (uiState.betAmount * 2))
                )
            )
        }
        else if(uiState.sumUser > 21){
            uiState.lose = true
            UserRepository.updateUser(
                User(id = currentUser.id,
                    name = currentUser.name,
                    userId = currentUser.userId,
                    email = currentUser.email,
                    money = (currentUser.money!! - uiState.betAmount)
                )
            )
        }
        else{
            uiState.tie = true
        }
        uiState.sumAi = 0
        uiState.sumUser = 0
        Thread.sleep(1000)
        dealCards()
        uiState.sumAi = sumAi()
        uiState.sumUser = sumUser()
        uiState.gameOver = false
        uiState.win = false
        uiState.lose = false
        uiState.tie = false
        uiState.redeal = true
        uiState.userDone = false
        uiState.aiDone = false
        uiState.showing = false
        uiState.bet = false

    }
    suspend fun startGame(){
        if(uiState.bet){
            while(!uiState.aiDone){
                aiTurn()
                Thread.sleep(500)
            }
            if(uiState.userDone && uiState.aiDone) {
                gameOver()
            }
        }
    }
    fun betUpdate(){
        uiState.showing = true
        uiState.bet = true
    }
    private fun cardSetup(){
        uiState.addCard(Card(number = 1, front = "Ace of hearts", R.drawable.aceofhearts))
        uiState.addCard(Card(number = 2, front = "2 of hearts", R.drawable.twoofhearts))
        uiState.addCard(Card(number = 3, front = "3 of hearts", R.drawable.threeofhearts))
        uiState.addCard(Card(number = 4, front = "4 of hearts", R.drawable.fourofhearts))
        uiState.addCard(Card(number = 5, front = "5 of hearts", R.drawable.fiveofhearts))
        uiState.addCard(Card(number = 6, front = "6 of hearts", R.drawable.sixofhearts))
        uiState.addCard(Card(number = 7, front = "7 of hearts", R.drawable.sevenofhearts))
        uiState.addCard(Card(number = 8, front = "8 of hearts", R.drawable.eightofhearts))
        uiState.addCard(Card(number = 9, front = "9 of hearts", R.drawable.nineofhearts))
        uiState.addCard(Card(number = 10, front = "10 of hearts", R.drawable.tenofhearts))
        uiState.addCard(Card(number = 10, front = "Jack of hearts", R.drawable.jackofhearts))
        uiState.addCard(Card(number = 10, front = "Queen of hearts", R.drawable.queenofhearts))
        uiState.addCard(Card(number = 10, front = "King of hearts", R.drawable.kingofhearts))
        uiState.addCard(Card(number = 1, front = "Ace of diamonds", R.drawable.aceofdiamonds))
        uiState.addCard(Card(number = 2, front = "2 of diamonds", R.drawable.twoofdiamonds))
        uiState.addCard(Card(number = 3, front = "3 of diamonds", R.drawable.threeofdiamonds))
        uiState.addCard(Card(number = 4, front = "4 of diamonds", R.drawable.fourofdiamonds))
        uiState.addCard(Card(number = 5, front = "5 of diamonds", R.drawable.fiveofdiamonds))
        uiState.addCard(Card(number = 6, front = "6 of diamonds", R.drawable.sixofdiamonds))
        uiState.addCard(Card(number = 7, front = "7 of diamonds", R.drawable.sevenofdiamonds))
        uiState.addCard(Card(number = 8, front = "8 of diamonds", R.drawable.eightofdiamonds))
        uiState.addCard(Card(number = 9, front = "9 of diamonds", R.drawable.nineofdiamonds))
        uiState.addCard(Card(number = 10, front = "10 of diamonds", R.drawable.tenofdiamonds))
        uiState.addCard(Card(number = 10, front = "Jack of diamonds", R.drawable.jackofdiamonds))
        uiState.addCard(Card(number = 10, front = "Queen of diamonds", R.drawable.queenofdiamonds))
        uiState.addCard(Card(number = 10, front = "King of diamonds", R.drawable.kingofdiamonds))
        uiState.addCard(Card(number = 1, front = "Ace of clubs", R.drawable.aceofclubs))
        uiState.addCard(Card(number = 2, front = "2 of clubs", R.drawable.twoofclubs))
        uiState.addCard(Card(number = 3, front = "3 of clubs", R.drawable.threeofclubs))
        uiState.addCard(Card(number = 4, front = "4 of clubs", R.drawable.fourofclubs))
        uiState.addCard(Card(number = 5, front = "5 of clubs", R.drawable.fiveofclubs))
        uiState.addCard(Card(number = 6, front = "6 of clubs", R.drawable.sixofclubs))
        uiState.addCard(Card(number = 7, front = "7 of clubs", R.drawable.sevenofclubs))
        uiState.addCard(Card(number = 8, front = "8 of clubs", R.drawable.eightofclubs))
        uiState.addCard(Card(number = 9, front = "9 of clubs", R.drawable.nineofclubs))
        uiState.addCard(Card(number = 10, front = "10 of clubs", R.drawable.tenofclubs))
        uiState.addCard(Card(number = 10, front = "Jack of clubs", R.drawable.jackofclubs))
        uiState.addCard(Card(number = 10, front = "Queen of clubs", R.drawable.queenofclubs))
        uiState.addCard(Card(number = 10, front = "King of clubs", R.drawable.kingofclubs))
        uiState.addCard(Card(number = 1, front = "Ace of spades", R.drawable.aceofspades))
        uiState.addCard(Card(number = 2, front = "2 of spades", R.drawable.twoofspades))
        uiState.addCard(Card(number = 3, front = "3 of spades", R.drawable.threeofspades))
        uiState.addCard(Card(number = 4, front = "4 of spades", R.drawable.fourofspades))
        uiState.addCard(Card(number = 5, front = "5 of spades", R.drawable.fiveofspades))
        uiState.addCard(Card(number = 6, front = "6 of spades", R.drawable.sixofspades))
        uiState.addCard(Card(number = 7, front = "7 of spades", R.drawable.sevenofspades))
        uiState.addCard(Card(number = 8, front = "8 of spades", R.drawable.eightofspades))
        uiState.addCard(Card(number = 9, front = "9 of spades", R.drawable.nineofspades))
        uiState.addCard(Card(number = 10, front = "10 of spades", R.drawable.tenofspades))
        uiState.addCard(Card(number = 10, front = "Jack of spades", R.drawable.jackofspades))
        uiState.addCard(Card(number = 10, front = "Queen of spades", R.drawable.queenofspades))
        uiState.addCard(Card(number = 10, front = "King of spades", R.drawable.kingofspades))
    }
}
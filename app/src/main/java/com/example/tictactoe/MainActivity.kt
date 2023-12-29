package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tictactoe.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.playOfflineBtn.setOnClickListener {
            createOfflineGame()
        }

        binding.createOnlineGameBtn.setOnClickListener {
            createOnlineGame()
        }

        binding.joinOnlineGameBtn.setOnClickListener {
            joinOnlineGame()
        }
    }

    private fun joinOnlineGame() {

        val gameId=binding.createGameEt.text.toString()
        if(gameId.isEmpty()){
            binding.createGameEt.setError("Enter game Id")
            return
        }
        GameData.myID="0"
        Firebase.firestore.collection("games")
            .document(gameId)
            .get()
            .addOnSuccessListener {
                val model=it?.toObject(GameModel::class.java)
                if(model==null){
                    binding.createGameEt.setError("Enter valid game Id")
                }else{
                    model.gameStatus=GameStatus.JOiNED
                    GameData.saveGameModel(model)
                    startGame()
                }
            }

    }

    private fun createOnlineGame() {
        GameData.myID="X"
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.CREATED,
                gameId = Random.nextInt(1000,9999).toString()

            )
        )
        startGame()
    }

    private fun createOfflineGame() {
        GameData.saveGameModel(
            GameModel(
                gameStatus = GameStatus.JOiNED
            )
        )
        startGame()
    }

    fun startGame(){
          startActivity(Intent(this,GameActivity::class.java))
    }
}
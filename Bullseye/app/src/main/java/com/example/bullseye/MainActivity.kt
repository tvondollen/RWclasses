package com.example.bullseye

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import com.example.bullseye.databinding.ActivityMainBinding
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private var sliderValue = 0
    private var targetValue = newTargetValue()
    private var totalScore = 0
    private var currentRound = 1


    //binding is of type ActivityMainBinding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        //inflate() converts XML into View object
        binding = ActivityMainBinding.inflate(layoutInflater)

        //root view in this case will be constraintlayout
        val view = binding.root
        setContentView(view)

        //reset game
        startNewGame()

        binding.hitMeButton.setOnClickListener {
            showResult()

            //update score
            totalScore += pointsForCurrentRound()
            binding.gameScoreTextView?.text = totalScore.toString() //? makes variable nullable
        }

        binding.startOverButton?.setOnClickListener {
            startNewGame()
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sliderValue = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private fun differenceAmount() = abs(targetValue - sliderValue)

    private fun newTargetValue() = Random.nextInt(1,100)

    private fun pointsForCurrentRound(): Int {
        val maxScore = 100
        val difference = differenceAmount()
        var bonus = 0

        if (difference == 0) {
            bonus = 100
        } else if (difference == 1) {
            bonus = 50
        }
        return maxScore - difference + bonus
    }

    private fun startNewGame() {
        //resetting values
        totalScore = 0
        currentRound = 1
        sliderValue = 50
        targetValue = newTargetValue()

        //resetting UI values
        binding.gameScoreTextView?.text = totalScore.toString()
        binding.gameRoundTextView?.text = currentRound.toString()
        binding.targetTextView.text = targetValue.toString()
        binding.seekBar.progress = sliderValue
    }

    private fun showResult() {
        //assigning strings
        val dialogTitle = alertTitle()
        val dialogMessage =
            getString(R.string.result_dialog_message, sliderValue, pointsForCurrentRound())
        //val dialogMessage = "The slider's value is $sliderValue"

        //creating alert dialog
        //context background info of activity. In this case, it's the parent

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)

        //lambda dismisses dialog
        builder.setPositiveButton(R.string.result_dialog_button_text) { dialog, _ ->
            dialog.dismiss()

            targetValue = newTargetValue()
            binding.targetTextView.text = targetValue.toString()

            currentRound += 1
            binding.gameRoundTextView?.text = currentRound.toString()
        }

        builder.create().show()
    }

    private fun alertTitle(): String {
        val difference = differenceAmount()

        //when instead of if statement
        val title: String = when {
            difference == 0 -> {
                getString(R.string.alert_title_1)
            }
            difference < 5 -> {
                getString(R.string.alert_title_2)
            }
            difference <= 10 -> {
                getString(R.string.alert_title_3)
            }
            else -> {
                getString(R.string.alert_title_4)
            }
        }

        return title
    }
}
package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    // Creates a private variable that is classified as null
    private var tvInput: TextView? = null

    // Creates variable booleans for the two id's
    var lastNumeric: Boolean = false
    var lastDot: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Identifies the null variable
        tvInput = findViewById(R.id.tvInput)
    }

    // Question mark added so that if the input is null there will be no execution but
    // if it is not it will execute code
    // View is the actual button that is calling the method
    // Is easier than writing 50+ lines to add input for each button using onClick
    // Added two booleans to make sure there can be a return and no multiple dots in a row
    fun onDigit(view: View){
        tvInput?.append((view as Button).text)
        lastNumeric = true
        lastDot = false
    }

    // Allows the clear button to return an empty string
    fun onClear(view: View){
        tvInput?.text = ""
    }

    // If lastNumeric is true and lastDot is not true it will run the code
    // Flags added at the end
    fun onDecimalPoint(view: View){
        if(lastNumeric && !lastDot)
            tvInput?.append(".")
        lastNumeric = false
        lastDot = true
    }

    // Checks if tvInput text exists and if it does executes the code
    // The .let is a lambda you use for nullables
    fun onOperator(view: View){
        tvInput?.text?.let{
            // Gets the view and calls on it/appends whatever button has as
            // text to the existing input text
            // If there isn't a number it does not execute operation
            if(lastNumeric && !isOperatorAdded(it.toString()))
                tvInput?.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }

    }

    // Takes tvValue and makes an actual string out of it
    fun onEqual(view: View){
        if (lastNumeric){
            var tvValue = tvInput?.text.toString()
            var prefix = ""

            try{
                // Creates a substring that starts at index 1 of the string
                // in order to avoid crashing from -
                if (tvValue.startsWith("-")){
                   prefix = "-"
                    tvValue = tvValue.substring(1)
                }
                if (tvValue.contains("-")){
                    // Uses item - to split text "99-1 -> 99 1
                    val splitValue = tvValue.split("-")
                    var one = splitValue[0] // 99
                    var two = splitValue[1] // 1
                    // If 1 was 99 and the prefix was -, now 1 will be 99
                    if(prefix.isNotEmpty()){
                        one = prefix + one
                    }

                    // Creates two doubles into a string/text format
                    tvInput?.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString())
            }else if (tvValue.contains("+")){
                val splitValue = tvValue.split("+")
                var one = splitValue[0]
                var two = splitValue[1]
                if(prefix.isNotEmpty()){
                    one = prefix + one
                }

                tvInput?.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())
            }else if (tvValue.contains("/")){
                val splitValue = tvValue.split("/")
                var one = splitValue[0]
                var two = splitValue[1]
                if(prefix.isNotEmpty()){
                    one = prefix + one
                }

                tvInput?.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())
            }else if (tvValue.contains("*")){
                val splitValue = tvValue.split("*")
                var one = splitValue[0]
                var two = splitValue[1]
                if(prefix.isNotEmpty()){
                    one = prefix + one
                }

                tvInput?.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
            }


            // Catches calculations that won't work mathematically and adds to log if we get error
            }catch (e: java.lang.ArithmeticException){
                e.printStackTrace()
            }
        }
    }

    // Removes .0 for any calculations made
    private fun removeZeroAfterDot(result: String) : String{
        var value = result
        if(result.contains(".0"))
            value = result.substring(0, result.length - 2)

        return value
    }

    // Checks if minus is at the beginning and then ignores it so negative values can be run
    // Checks if any of these strings are given to return true
    // Checks if operator is added
    private fun isOperatorAdded(value: String) : Boolean{
        return if (value.startsWith("-")){
            false
        }else{
            value.contains("/")
                    || value.contains("*")
                    || value.contains("+")
                    || value.contains("-")
        }
    }
}
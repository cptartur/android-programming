package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private var memory: Int = 0
    private var input: Int = 0
    private var total: Int = 0
    private var operation: Operation? = null
    private var readNewInput: Boolean = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.display)

        setListeners()
        display.text = "0"
    }

    private fun setListeners() {
        val button0 = findViewById<Button>(R.id.button0)
        val button1 = findViewById<Button>(R.id.button1)
        val button2 = findViewById<Button>(R.id.button2)
        val button3 = findViewById<Button>(R.id.button3)
        val button4 = findViewById<Button>(R.id.button4)
        val button5 = findViewById<Button>(R.id.button5)
        val button6 = findViewById<Button>(R.id.button6)
        val button7 = findViewById<Button>(R.id.button7)
        val button8 = findViewById<Button>(R.id.button8)
        val button9 = findViewById<Button>(R.id.button9)

        val buttonClear = findViewById<Button>(R.id.button_clear)
        val buttonEquals = findViewById<Button>(R.id.button_equals)

        val buttonAdd = findViewById<Button>(R.id.button_add)
        val buttonSubtract = findViewById<Button>(R.id.button_subtract)
        val buttonMultiply = findViewById<Button>(R.id.button_multiply)
        val buttonDivide = findViewById<Button>(R.id.button_divide)


        val digits: List<Button> = listOf(
            button0, button1, button2, button3, button4, button5, button6, button7, button8, button9
        )
        val operations: List<Button> = listOf(
            buttonAdd, buttonSubtract, buttonMultiply, buttonDivide
        )

        for (button in digits) {
            button.setOnClickListener { digitPressed(it) }
        }
        for (button in operations) {
            button.setOnClickListener { operationPressed(it) }
        }
        buttonClear.setOnClickListener { clearPressed(it) }
        buttonEquals.setOnClickListener{ equalsPressed() }
    }

    private fun calculate() {
        total = when (operation) {
            Operation.ADD -> memory + input
            Operation.SUBTRACT -> memory - input
            Operation.MULTIPLY -> memory * input
            Operation.DIVIDE -> memory / input
            null -> 0
        }
        display.text = total.toString()
        memory = total
    }

    private fun calculate2(op: Operation?, x: Int, y: Int): Int {
        return when (operation) {
            Operation.ADD -> x + y
            Operation.SUBTRACT -> x - y
            Operation.MULTIPLY -> x * y
            Operation.DIVIDE -> x / y
            null -> 0
        }
    }

    private fun equalsPressed() {
        total = calculate2(operation, memory, input)
        memory = total
        readNewInput = true
        display.text = total.toString()
    }

    private fun operationPressed(view: View) {
        when {
            total != 0 -> {
                memory = total
                total = 0
            }
            operation == null -> {
                memory = input
            }
            else -> {
                memory = calculate2(operation, memory, input)
                display.text = memory.toString()
            }
        }
        input = memory
        operation = when (view.id) {
            R.id.button_add -> Operation.ADD
            R.id.button_subtract -> Operation.SUBTRACT
            R.id.button_multiply -> Operation.MULTIPLY
            R.id.button_divide -> Operation.DIVIDE
            else -> null
        }
        readNewInput = true
    }

    private fun clearPressed(view: View) {
        total = 0
        memory = 0
        input = 0
        operation = null
        display.text = total.toString()
    }

    private fun digitPressed(view: View) {
        if (readNewInput) {
            readNewInput = false
            input = 0
        }
        input *= 10
        when (view.id) {
            R.id.button1 -> input += 1
            R.id.button2 -> input += 2
            R.id.button3 -> input += 3
            R.id.button4 -> input += 4
            R.id.button5 -> input += 5
            R.id.button6 -> input += 6
            R.id.button7 -> input += 7
            R.id.button8 -> input += 8
            R.id.button9 -> input += 9
        }
        display.text = input.toString()
    }
}
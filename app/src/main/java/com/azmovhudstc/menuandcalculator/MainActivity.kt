package com.azmovhudstc.menuandcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.objecthunter.exp4j.ExpressionBuilder
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Toast.makeText(this, "Powered by Hasanxon", Toast.LENGTH_LONG).show()
        placeholder.isSelected = true



        num0.setOnClickListener { doSomething("0") }
        num1.setOnClickListener { doSomething("1") }
        num2.setOnClickListener { doSomething("2") }
        num3.setOnClickListener { doSomething("3") }
        num4.setOnClickListener { doSomething("4") }
        num5.setOnClickListener { doSomething("5") }
        num6.setOnClickListener { doSomething("6") }
        num7.setOnClickListener { doSomething("7") }
        num8.setOnClickListener { doSomething("8") }
        num9.setOnClickListener { doSomething("9") }
        numDot.setOnClickListener { doSomething(".") }
        clear.setOnClickListener { doSomething("clear") }
        actionBack.setOnClickListener { doSomething("backspace") }
        actionDivide.setOnClickListener { doSomething("÷") }
        actionMultiply.setOnClickListener { doSomething("×") }
        actionMinus.setOnClickListener { doSomething("-") }
        actionAdd.setOnClickListener { doSomething("+") }
        startBracket.setOnClickListener { doSomething("(") }
        closeBracket.setOnClickListener { doSomething(")") }
        actionEquals.setOnClickListener { doSomething("=") }
    }

    private fun doSomething(p0: String) {
        //Toast.makeText(this, placeholder.text.toString(), Toast.LENGTH_LONG).show()
        if (placeholder.text.toString().toIntOrNull() == 0) {
            placeholder.text = ""
            findResult()
        }
        //if (placeholder.text.toString().isEmpty())
        if (placeholder.text.toString().isEmpty()) {
            when (p0) {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0" -> {
                    placeholder.append(p0)
                    findResult()
                }
                "00" -> {
                    placeholder.append("0")
                    findResult()
                }
                "-" -> {//**************************************************************************
                    placeholder.append(p0)
                    findResult()
                }
                "." -> {
                    placeholder.append("0.")
                    findResult()
                }
            }

        } else {

            when (p0) {
                "clear" -> {
                    placeholder.text = ""
                    answer.text = ""
                }
                "backspace" -> {
                    placeholder.text = placeholder.text.toString()
                        .substring(0, placeholder.text.toString().length - 1)
                    findResult()
                }
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "00" -> {
                    if ((!isNotLastOperatorDot(placeholder.text.toString())) || isNotLastDigitZero(
                            placeholder.text.toString()
                        )
                    ) {
                        placeholder.append(p0)
                        findResult()
                    } else {
                        //remove 0 and append p0 255x0 --> 255x${p0}  for example: 255x0  --> 255x1
                        placeholder.text = placeholder.text.toString()
                            .substring(0, placeholder.text.toString().length - 1)
                        if (p0 == "00") {
                            /*even no need just make placeholder.append(p0) explicit of "00"
                             so that the output will not become 255x00 where x is multiplication
                             symbol whee is only used as an example*/
                            placeholder.append("0")
                            findResult()
                        } else {
                            placeholder.append(p0)
                            findResult()

                        }
                    }
                }
                "." -> {
                    if (isLastCharOperator(placeholder.text.toString())) {
                        placeholder.append("0.")
                    } else if (isNotLastOperatorDot(placeholder.text.toString()) && isNotLastCharDot(
                            placeholder.text.toString()
                        )
                    ) {
                        placeholder.append(p0)
                    }
                }
                "%", "÷", "×", "+" ,"(",")"-> {
                    if (isLastCharOperator(placeholder.text.toString()) && placeholder.text.toString() != "-" && isNotLastOperatorMinusWithAdjacentOperator(
                            placeholder.text.toString()
                        )
                    ) {
                        placeholder.text = placeholder.text.toString()
                            .substring(0, placeholder.text.toString().length - 1)
                        placeholder.append(p0)
                    } else if (placeholder.text.toString() != "-" && isNotLastOperatorMinusWithAdjacentOperator(
                            placeholder.text.toString()
                        )
                    ) {
                        placeholder.append(p0)
                    } else if (!isNotLastOperatorMinusWithAdjacentOperator(placeholder.text.toString())) {
                        placeholder.text = placeholder.text.toString()
                            .substring(0, placeholder.text.toString().length - 2)
                        placeholder.append(p0)
                    }
                }
                "-" -> {
                    if (isNotLastCharMinus(placeholder.text.toString()) && isNotLastCharDot((placeholder.text.toString())) && !isLastCharPlus()) {
                        placeholder.append(p0)
                    } else if (isLastCharPlus()) {
                        placeholder.text = placeholder.text.toString()
                            .substring(0, placeholder.text.toString().length - 1)
                        placeholder.append(p0)
                    }
                }
                "=" -> {
                    applyResult()
                }
            }
        }
    }

    private fun isNotLastOperatorMinusWithAdjacentOperator(p0: String): Boolean {
        val regex = Regex("(?<=\\D)-$")
        val output: String? = regex.find(p0)?.value
        return output == null
    }

    private fun applyResult() {
        try {
            val p0 = placeholder.text.toString()
            var inputExpression = p0.replace('×', '*')
            inputExpression = inputExpression.replace('÷', '/')
            val expression = ExpressionBuilder(inputExpression).build()
            val result = expression.evaluate()
            val longResult = result.toLong()
            if (result == longResult.toDouble()) {
                answer.text = longResult.toString()
            } else {
                answer.text = result.toString()
            }
            placeholder.text = answer.text
            answer.text = ""
        } catch (e: Exception) {
            Log.e("Exception ❣❣❣❣❣ ✌✌✌ ", "message" + e.message)
            if (e.message == "Expression can not be empty")
                answer.text = ""
        }

    }

    private fun isLastCharPlus(): Boolean {
        return placeholder.text.toString().isNotEmpty() && placeholder.text.toString()
            .takeLast(1) == "+"
    }

    private fun isNotLastDigitZero(p0: String): Boolean {
        /*
        * This is used to check weather the expression after operator contains 0
        * Like in Expression 255x0
        * it will return false and then the else statement will work
        * as we append 1 in a normal expression like 255x5  -->  255x51
        * but when we append 1 with 0 it should give output like 255x0  -->  255x1*/
        val regex = Regex("\\d+$")
        val output: String? = regex.find(p0)?.value
        return output?.toIntOrNull() != 0
    }

    private fun isLastCharOperator(p0: String): Boolean {
        val regex = Regex("\\D$")
        val output: String? = regex.find(p0)?.value
        return output != null
    }

    private fun isNotLastCharDot(p0: String): Boolean {
        val regex = Regex("\\.$")
        val output: String? = regex.find(p0)?.value
        return output == null
    }

    private fun isNotLastCharMinus(p0: String): Boolean {
        val regex = Regex("-$")
        val output: String? = regex.find(p0)?.value
        return output == null
    }

    private fun isNotLastOperatorDot(p0: String): Boolean {
        val regex = Regex("\\.(?:\\d)+$")
        val output: String? = regex.find(p0)?.value
        return output == null
    }

    private fun findResult() {
        try {
            val p0 = placeholder.text.toString()
            var inputExpression = p0.replace('×', '*')
            inputExpression = inputExpression.replace('÷', '/')
            val expression = ExpressionBuilder(inputExpression).build()
            val result = expression.evaluate()
            val longResult = result.toLong()
            if (result == longResult.toDouble()) {
                answer.text = longResult.toString()
            } else {
                answer.text = result.toString()
            }
        } catch (e: Exception) {
            Log.e("Exception ❣❣❣❣❣ ✌✌✌ ", "message" + e.message)
            if (e.message == "Expression can not be empty")
                answer.text = ""
        }
    }

}


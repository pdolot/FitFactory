package com.example.fitfactory.utils

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.example.fitfactory.R
import com.example.fitfactory.constants.RegularExpression
import com.example.fitfactory.di.Injector
import com.google.android.material.textfield.TextInputEditText
import javax.inject.Inject

class Validator(var editText: EditText) {
    private var text = editText.text.toString()

    @Inject
    lateinit var context: Context

    init {
        Injector.component.inject(this)
    }

    fun validatePassword(): Boolean {
        return if (text.length >= 6) true else {
            Toast.makeText(context, "Zbyt krótkie hasło", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun validateEmail(): Boolean {
        return if (RegularExpression.EMAIL.toRegex().find(text) == null) {
            Toast.makeText(context, "Niepoprawny email", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    fun validateTextField(): Boolean {
        return when {
            text.isBlank() -> {
                Toast.makeText(context, "Uzupełnij pole ${editText.hint}", Toast.LENGTH_SHORT)
                    .show()
                false
            }
            RegularExpression.TEXT.toRegex().find(text) == null -> {
                Toast.makeText(context, "Błędne pole ${editText.hint}", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
}

class SuperValidator(
    var editText: EditText?,
    var errorEnabled: Boolean,
    var regex: Regex?,
    var maxLength: Int,
    var minLength: Int,
    var message: String? = null,
    var canBeEmpty: Boolean,
    var exactLength: Int? = null
) {

    private var context: Context? = null

    fun validate(): Boolean {
        val text = editText?.text.toString()
        val length = text.length

        if (!canBeEmpty && length == 0) {
            setError("Pole nie może być puste")
            return false
        }

        if (length > 0){
            exactLength?.let {
                if (exactLength != length) {
                    var lengthMessage = editText?.context?.resources?.getQuantityString(R.plurals.characterLength, exactLength ?: 0, exactLength)
                        ?: "Pole musi zawierać $exactLength znaków"
                    setError(lengthMessage)
                    return false
                }

                return checkRegex(text)
            }

            return if (length !in minLength..maxLength) {
                setError("Za krótka wartość")
                false
            }else{
                checkRegex(text)
            }

        }else{
            return true
        }

    }

    private fun checkRegex(text: String): Boolean{
        if (regex != null) {
            return if (regex?.find(text) == null) {
                setError(message)
                false
            } else {
                true
            }
        }
        return true
    }

    private fun setError(message: String? = "Błąd") {
        if (errorEnabled) {
            val isInputEditText = editText is TextInputEditText
            if (isInputEditText) {
                (editText as TextInputEditText).error = message
            } else {
                Toast.makeText(editText?.context, message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    class Builder {
        private var editText: EditText? = null
        private var errorEnabled = false
        private var regex: Regex? = null
        private var maxLength: Int = Int.MAX_VALUE
        private var minLength: Int = 1
        private var message: String? = null
        private var canBeEmpty = true
        private var exactLength: Int? = null

        fun on(editText: EditText): Builder {
            this.editText = editText
            return this
        }

        fun enableError(message: String? = null): Builder {
            this.message = message
            this.errorEnabled = true
            return this
        }

        fun disableError(): Builder {
            this.errorEnabled = false
            return this
        }

        fun canBeEmpty(canBeEmpty: Boolean): Builder {
            this.canBeEmpty = canBeEmpty
            return this
        }

        fun withRegex(regex: Regex): Builder {
            this.regex = regex
            return this
        }

        fun maxLength(max: Int): Builder {
            this.maxLength = max
            return this
        }

        fun minLength(min: Int): Builder {
            this.minLength = min
            return this
        }

        fun withExactLength(length: Int): Builder {
            this.exactLength = length
            return this
        }

        fun build() = SuperValidator(
            editText,
            errorEnabled,
            regex,
            maxLength,
            minLength,
            message,
            canBeEmpty,
            exactLength
        )
    }
}
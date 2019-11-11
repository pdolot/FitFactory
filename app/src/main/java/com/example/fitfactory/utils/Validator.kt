package com.example.fitfactory.utils

import android.content.Context
import android.widget.EditText
import android.widget.Toast
import com.example.fitfactory.constants.RegularExpression
import com.example.fitfactory.di.Injector
import javax.inject.Inject

class Validator(var editText: EditText) {
    private var text = editText.text.toString()

    @Inject
    lateinit var context: Context

    init {
        Injector.component.inject(this)
    }

    fun validatePassword(): Boolean{
        return if (text.length >= 6) true else {
            Toast.makeText(context,"Zbyt krótkie hasło", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun validateEmail(): Boolean {
        return if (RegularExpression.EMAIL.toRegex().find(text) == null) {
            Toast.makeText(context,"Niepoprawny email", Toast.LENGTH_SHORT).show()
            false
        } else true
    }

    fun validateTextField(): Boolean {
        return when {
            text.isBlank() -> {
                Toast.makeText(context,"Uzupełnij pole ${editText.hint}", Toast.LENGTH_SHORT).show()
                false
            }
            RegularExpression.TEXT.toRegex().find(text) == null -> {
                Toast.makeText(context,"Błędne pole ${editText.hint}", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }
}
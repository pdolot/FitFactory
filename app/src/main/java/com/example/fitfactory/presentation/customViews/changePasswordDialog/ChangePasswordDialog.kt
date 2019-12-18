package com.example.fitfactory.presentation.customViews.changePasswordDialog

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitfactory.R
import kotlinx.android.synthetic.main.view_change_password.view.*


class ChangePasswordDialog @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_change_password, this)
        userPassword.transformationMethod = PasswordTransformationMethod()
        userOldPassword.transformationMethod = PasswordTransformationMethod()

        listOf(userPassword, userOldPassword).forEach {
            it.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) error.visibility = View.GONE
            }
        }
    }

    fun getPasswords(): Pair<String, String>{
        return Pair(userOldPassword.text.toString(), userPassword.text.toString())
    }

    fun checkCorrectness(): Boolean{
        val oldPassword = userOldPassword.text.toString()
        val newPassword = userPassword.text.toString()
        return when {
            oldPassword.isBlank() -> {
                userOldPassword.error = "Pole nie może być puste"
                false
            }
            newPassword.isBlank() -> {
                userPassword.error = "Pole nie może być puste"
                false
            }
            oldPassword == newPassword -> {
                userPassword.error = "Nowe hasło nie może być takie same jak stare"
                false
            }
            else -> true
        }
    }

    fun setErrorMessage(message: String){
        error.visibility = View.VISIBLE
        error.text = message
    }
}
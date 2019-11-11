package com.example.fitfactory.presentation.customViews

import android.content.Context
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.fitfactory.R
import kotlinx.android.synthetic.main.view_sign_in_dialog.view.*

class SignInDialog @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_sign_in_dialog, this)
        userPassword.transformationMethod = PasswordTransformationMethod()
    }

    fun getPassword():String{
        return userPassword.text.toString()
    }

    fun getUsername():String{
        return userName.text.toString()
    }
}
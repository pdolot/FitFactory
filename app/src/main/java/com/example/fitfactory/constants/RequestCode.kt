package com.example.fitfactory.constants

class RequestCode {
    companion object {
        const val REQUESTING_LOCATION_UPDATES_KEY: String = "LOCATION_UPDATES"
        const val GOOGLE_SIGN_IN_REQUEST_CODE: Int = 79
        const val REQUEST_CHECK_SETTINGS: Int = 89
        const val LOCATION_REQUEST_CODE: Int = 99
        const val CAMERA_REQUEST_CODE: Int = 101
    }
}

class RestConstant{
    companion object{
        const val BEARER: String = "Bearer"
    }
}

class RegularExpression{
    companion object{
        const val EMAIL: String = "^[a-zA-Z\\d\\.]+?@[a-zA-Z\\d]+?\\..{2,}[^\\.\$&+,:;=?@#|'<>.^*()%!-]\$"
        const val TEXT: String = "^[a-zA-z]+\$"
    }
}
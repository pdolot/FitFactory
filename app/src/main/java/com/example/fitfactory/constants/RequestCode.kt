package com.example.fitfactory.constants

class RequestCode {
    companion object {
        const val REQUESTING_LOCATION_UPDATES_KEY: String = "LOCATION_UPDATES"
        const val GOOGLE_SIGN_IN_REQUEST_CODE: Int = 79
        const val REQUEST_CHECK_SETTINGS: Int = 89
        const val LOCATION_REQUEST_CODE: Int = 99
        const val CAMERA_REQUEST_CODE: Int = 101
        const val GALLERY_REQUEST_CODE: Int = 111
        const val READ_EXTERNAL_STORAGE_REQUEST_CODE: Int = 121
    }
}

object Payment{
    const val CARD_NO = "4242424242424242"
}


class PhotoServiceRequestCode{
    companion object{
        const val FILE_PROVIDER = "com.example.fitfactory.fileprovider"
        const val REQUEST_TAKE_PHOTO = 131
    }
}

class RestConstant{

    companion object{
        const val BEARER: String = "Bearer"
        const val BASE_URL: String = "http://192.168.1.106:8080"
        const val BASE_RETROFIT_URL: String = "http://192.168.1.101:8080"
    }

}

class RegularExpression{
    companion object{
        const val EMAIL: String = "^[a-zA-Z\\d\\.]+?@[a-zA-Z\\d]+?\\..{1,}[^\\.\$&+,:;=?@#|'<>.^*()%!-]\$"
        const val TEXT: String = "^[a-zA-z]+\$"
    }
}
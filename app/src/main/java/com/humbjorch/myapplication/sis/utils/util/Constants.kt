package com.humbjorch.myapplication.sis.utils.util


object Constants {

    //Firebase
    const val PATH_FIREBASE_STORAGE = "profilePhotos"
    const val USERS_COLLECTION = "userCollection"
    const val PATH_CHILD_FIREBASE_STORAGE = "usersPhotos"

    enum class StatusRequest {
        LOADING(),
        SUCCESS(),
        ERROR()
    }



}


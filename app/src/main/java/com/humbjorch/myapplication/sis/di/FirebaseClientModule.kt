package com.humbjorch.myapplication.sis.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.humbjorch.myapplication.sis.utils.Constantes
import javax.inject.Inject


class FirebaseClientModule @Inject constructor() {
    val auth : FirebaseAuth get() = FirebaseAuth.getInstance()
    val userCollection = Firebase.firestore.collection(Constantes.USERS_COLLECTION)
    val storage = FirebaseStorage.getInstance().getReference(Constantes.PATH_FIREBASE_STORAGE).child(
        Constantes.PATH_CHILD_FIREBASE_STORAGE
    )
}
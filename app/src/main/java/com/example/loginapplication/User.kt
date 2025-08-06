package com.example.loginapplication

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User : RealmObject() {
    @PrimaryKey
    var id: String = ""
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var createdAt: Long = System.currentTimeMillis()
    var lastLoginAt: Long = 0
} 
package com.example.loginapplication

import io.realm.Realm
import io.realm.RealmConfiguration
import java.util.UUID

object RealmManager {
    
    fun initRealm() {
        val config = RealmConfiguration.Builder()
            .name("login_app.realm")
            .schemaVersion(1)
            .build()
        Realm.setDefaultConfiguration(config)
    }
    
    fun createUser(username: String, email: String, password: String): Boolean {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.executeTransaction {
                val user = User()
                user.id = UUID.randomUUID().toString()
                user.username = username
                user.email = email
                user.password = password // 実際のアプリではハッシュ化すべき
                it.insert(user)
            }
            true
        } catch (e: Exception) {
            false
        } finally {
            realm.close()
        }
    }
    
    fun authenticateUser(username: String, password: String): User? {
        val realm = Realm.getDefaultInstance()
        return try {
            val user = realm.where(User::class.java)
                .equalTo("username", username)
                .equalTo("password", password)
                .findFirst()
            
            if (user != null) {
                realm.executeTransaction {
                    user.lastLoginAt = System.currentTimeMillis()
                }
            }
            
            user
        } finally {
            realm.close()
        }
    }
    
    fun getUserByUsername(username: String): User? {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where(User::class.java)
                .equalTo("username", username)
                .findFirst()
        } finally {
            realm.close()
        }
    }
    
    fun getAllUsers(): List<User> {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.where(User::class.java).findAll().toList()
        } finally {
            realm.close()
        }
    }
    
    fun deleteUser(userId: String): Boolean {
        val realm = Realm.getDefaultInstance()
        return try {
            realm.executeTransaction {
                val user = realm.where(User::class.java)
                    .equalTo("id", userId)
                    .findFirst()
                user?.deleteFromRealm()
            }
            true
        } catch (e: Exception) {
            false
        } finally {
            realm.close()
        }
    }
} 
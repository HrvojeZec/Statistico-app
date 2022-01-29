package com.example.statistico.models

class User(val id: String? = "", val username: String? = "", val password: String? = "") {
    val search_name: String = username?.lowercase() ?: ""
}

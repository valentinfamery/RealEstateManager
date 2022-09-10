package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.repository.UserRepository

class LoginUser(private val repo :UserRepository) {
    suspend operator fun invoke(email: String, password: String) = repo.loginUser(email, password)
}
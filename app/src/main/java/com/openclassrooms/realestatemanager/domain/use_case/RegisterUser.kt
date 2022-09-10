package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.repository.UserRepository

class RegisterUser(private val repo : UserRepository) {
    suspend operator fun invoke(userName: String, userEmailAddress: String, userLoginPassword: String) = repo.registerUser(userName, userEmailAddress, userLoginPassword)
}
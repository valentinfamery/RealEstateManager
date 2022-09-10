package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.repository.UserRepository

class SendPasswordResetEmail(private val repo : UserRepository) {
    suspend operator fun invoke(email: String) = repo.sendPasswordResetEmail(email)
}
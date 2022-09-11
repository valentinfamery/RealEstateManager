package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.repository.UserRepository

class SetUserEmail(private val repo : UserRepository) {
    suspend operator fun invoke(email: String?) = repo.setUserEmail(email)
}
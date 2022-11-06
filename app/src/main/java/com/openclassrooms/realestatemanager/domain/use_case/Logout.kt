package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.repository.UserRepository

class Logout(private val repo : UserRepository) {
    suspend operator fun invoke() = repo.logout()
}
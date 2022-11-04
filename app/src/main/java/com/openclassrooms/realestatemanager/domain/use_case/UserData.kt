package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.repository.UserRepository

class UserData(private val repo : UserRepository) {
    operator suspend fun invoke() = repo.userData()
}
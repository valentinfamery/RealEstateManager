package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.repository.UserRepository

class SetUsername(private val repo : UserRepository) {
    operator fun invoke(username: String?) = repo.setUsername(username)
}
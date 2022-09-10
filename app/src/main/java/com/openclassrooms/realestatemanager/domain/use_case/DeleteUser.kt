package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.repository.UserRepository

class DeleteUser(private val repo : UserRepository) {
    operator fun invoke() = repo.deleteUser()
}
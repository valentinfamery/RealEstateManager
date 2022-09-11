package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.repository.UserRepository

class SetPhotoUrl(private val repo :UserRepository) {
    suspend operator fun invoke(photoUrl: String?) = repo.setPhotoUrl(photoUrl)
}
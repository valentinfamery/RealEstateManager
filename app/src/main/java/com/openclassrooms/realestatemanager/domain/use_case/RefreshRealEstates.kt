package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository

class RefreshRealEstates(private val repo: RealEstateRepository) {
    suspend operator fun invoke() = repo.refreshRealEstatesFromFirestore()
}
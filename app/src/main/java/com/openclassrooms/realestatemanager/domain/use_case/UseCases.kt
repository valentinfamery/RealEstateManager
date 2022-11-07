package com.openclassrooms.realestatemanager.domain.use_case

data class UseCases(
    val refreshRealEstates: RefreshRealEstates,
    val createRealEstate: CreateRealEstate,
    val registerUser: RegisterUser,
    val loginUser: LoginUser,
    val sendPasswordResetEmail: SendPasswordResetEmail,
    val deleteUser: DeleteUser,
    val userData: UserData
)

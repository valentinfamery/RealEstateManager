package com.openclassrooms.realestatemanager.domain.use_case

data class UseCases(
    val getRealEstates: GetRealEstates,
    val createRealEstate: CreateRealEstate,
    val registerUser: RegisterUser,
    val logout: Logout,
    val loginUser: LoginUser,
    val sendPasswordResetEmail: SendPasswordResetEmail,
    val deleteUser: DeleteUser,
    val userData: UserData,
    val getUsers: GetUsers,
    val setUserEmail: SetUserEmail,
    val setUsername: SetUsername,
    val setPhotoUrl: SetPhotoUrl
)

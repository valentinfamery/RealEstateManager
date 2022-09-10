package com.openclassrooms.realestatemanager.di

import android.content.Context
import com.openclassrooms.realestatemanager.database.RealEstateRoomDatabase
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.data.repository.RealEstateRepositoryImpl
import com.openclassrooms.realestatemanager.data.repository.UserRepositoryImpl
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.repository.UserRepository
import com.openclassrooms.realestatemanager.domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    fun provideRealEstatesRef(
        db: FirebaseFirestore
    ) = db.collection("real_estates")

    @Provides
    fun provideUsersRef(db: FirebaseFirestore) = db.collection("users")

    @Provides
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    fun provideStorageRef(
        firebaseStorage: FirebaseStorage
    ) = firebaseStorage.reference

    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    fun provideAuthUI() = AuthUI.getInstance()

    @Provides
    fun provideRealEstateRoomDatabase(@ActivityContext context: Context) = RealEstateRoomDatabase.getInstance(context)

    @Provides
    fun provideRealEstateDao(roomDatabase: RealEstateRoomDatabase): RealEstateDao {
        return roomDatabase.realEstateDao()
    }

    @Provides
    fun provideRealEstatesRepository(
        realEstatesRef: CollectionReference,
        storageRef : StorageReference,
        @ActivityContext context: Context,
        realEstateDao: RealEstateDao
    ): RealEstateRepository = RealEstateRepositoryImpl(realEstatesRef,storageRef,context,realEstateDao)

    @Provides
    fun provideUsersRepository(
        firebaseAuth : FirebaseAuth,
        authUI: AuthUI,
        usersRef : CollectionReference,
        @ActivityContext context: Context?
    ): UserRepository = UserRepositoryImpl(firebaseAuth,authUI,usersRef,context)

    @Provides
    fun provideUseCases(userRepository: UserRepository,realEstateRepository: RealEstateRepository) = UseCases(
        getRealEstates = GetRealEstates(realEstateRepository),
        createRealEstate = CreateRealEstate(realEstateRepository),
        registerUser = RegisterUser(userRepository),
        logout = Logout(userRepository),
        loginUser = LoginUser(userRepository),
        sendPasswordResetEmail = SendPasswordResetEmail(userRepository),
        deleteUser = DeleteUser(userRepository),
        userData = UserData(userRepository),
        getUsers = GetUsers(userRepository),
        setUserEmail = SetUserEmail(userRepository),
        setUsername = SetUsername(userRepository),
        setPhotoUrl = SetPhotoUrl(userRepository)
    )

}
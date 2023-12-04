package com.danmurphyy.scannerappforshop.domain.di

import com.danmurphyy.scannerappforshop.domain.repository.AuthRepository
import com.danmurphyy.scannerappforshop.domain.repository.AuthRepositoryImp
import com.danmurphyy.scannerappforshop.domain.repository.ItemsRepository
import com.danmurphyy.scannerappforshop.domain.repository.ItemsRepositoryImp
import com.danmurphyy.scannerappforshop.domain.useCase.AuthenticationUseCase
import com.danmurphyy.scannerappforshop.domain.useCase.ItemsUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ScannerModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseFireStore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        fireStore: FirebaseFirestore
    ): AuthRepository {
        return AuthRepositoryImp(firebaseAuth, fireStore)
    }

    @Provides
    @Singleton
    fun provideAuthenticationUseCase(authRepository: AuthRepository): AuthenticationUseCase {
        return AuthenticationUseCase(authRepository)
    }

    @Provides
    @Singleton
    fun provideItemsRepository(
        fireStore: FirebaseFirestore
    ): ItemsRepository {
        return ItemsRepositoryImp(fireStore)
    }

    @Provides
    @Singleton
    fun provideItemsUseCase(
        itemsRepository: ItemsRepository
    ): ItemsUseCase {
        return ItemsUseCase(itemsRepository)
    }
}
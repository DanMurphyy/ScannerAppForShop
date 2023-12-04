package com.danmurphyy.scannerappforshop.domain.repository

import com.danmurphyy.scannerappforshop.model.ModelUser
import com.danmurphyy.scannerappforshop.utils.Constants
import com.danmurphyy.scannerappforshop.utils.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception
import javax.inject.Inject

class AuthRepositoryImp @Inject constructor(
    private var firebaseAuth: FirebaseAuth,
    private var firebaseFireStore: FirebaseFirestore,
) : AuthRepository {

    override fun emailSignIn(
        email: String,
        password: String,
    ): Flow<Resource<Boolean>> = callbackFlow {
        try {
            trySend(Resource.Loading)
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    trySend(Resource.Success(true))
                }
                .addOnFailureListener {
                    trySend(
                        Resource.Error(
                            it.localizedMessage ?: "An Error Occurred in addOnFailureListener"
                        )
                    )
                }
            awaitClose()
        } catch (e: Exception) {
            trySend(
                Resource.Error(
                    e.localizedMessage ?: "An Error Occurred in createUserProfile (Catch)"
                )
            )
        } finally {
            // Close the callbackFlow to signal completion.
            close()
        }
    }

    override fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getUserId(): String {
        return firebaseAuth.currentUser?.uid ?: ""
    }

    override fun createUserProfile(email: String, password: String, modelUser: ModelUser)
            : Flow<Resource<Boolean>> = callbackFlow {
        try {
            trySend(Resource.Loading)
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { authResult ->
                    val userId = authResult.user?.uid ?: ""
                    firebaseFireStore.collection(Constants.Users)
                        .document(userId)
                        .set(modelUser)
                        .addOnSuccessListener {
                            trySend(Resource.Success(true))
                        }
                        .addOnFailureListener {
                            trySend(
                                Resource.Error(
                                    it.localizedMessage
                                        ?: "An Error Occurred in addOnFailureListener"
                                )
                            )
                        }
                }
                .addOnFailureListener {
                    trySend(
                        Resource.Error(
                            it.localizedMessage ?: "An Error Occurred in addOnFailureListener"
                        )
                    )
                }
            awaitClose()
        } catch (e: Exception) {
            trySend(
                Resource.Error(
                    e.localizedMessage ?: "An Error Occurred in createUserProfile (Catch)"
                )
            )
        } finally {
            // Close the callbackFlow to signal completion.
            close()
        }

    }

    override fun resetPassword(email: String): Flow<Resource<Boolean>> = callbackFlow {
        try {
            trySend(Resource.Loading)
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener {
                    trySend(Resource.Success(true))
                }
                .addOnFailureListener {
                    trySend(
                        Resource.Error(
                            it.localizedMessage ?: "An Error Occurred in addOnFailureListener"
                        )
                    )
                }
            awaitClose()
        } catch (e: Exception) {
            trySend(
                Resource.Error(
                    e.localizedMessage ?: "An Error Occurred in createUserProfile (Catch)"
                )
            )
        } finally {
            // Close the callbackFlow to signal completion.
            close()
        }
    }

}
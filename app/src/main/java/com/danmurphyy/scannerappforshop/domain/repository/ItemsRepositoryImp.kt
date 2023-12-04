package com.danmurphyy.scannerappforshop.domain.repository

import android.util.Log
import com.danmurphyy.scannerappforshop.model.ModelItems
import com.danmurphyy.scannerappforshop.model.ModelUser
import com.danmurphyy.scannerappforshop.utils.Constants
import com.danmurphyy.scannerappforshop.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ItemsRepositoryImp @Inject constructor(
    private val fireStore: FirebaseFirestore,
) : ItemsRepository {
    override fun getUserData(userId: String): Flow<Resource<ModelUser>> = channelFlow {
        try {
            trySend(Resource.Loading)
            fireStore.collection(Constants.Users)
                .document(userId)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        trySend(
                            Resource.Error(exception.localizedMessage ?: "An Error Occurred")
                        )
                        return@addSnapshotListener
                    }
                    snapshot?.let { document ->
                        if (document.exists()) {
                            val userData = document.toObject(ModelUser::class.java)
                            if (userData != null) {
                                trySend(Resource.Success(userData))
                            } else {
                                trySend(Resource.Error("User data is null"))
                            }
                        } else {
                            trySend(Resource.Error("User document does not exist"))
                        }
                    }
                }
            awaitClose()
        } catch (e: Exception) {
            trySend(
                Resource.Error(
                    e.localizedMessage ?: "An Error Occurred in getUserData (Catch)"
                )
            )
        } finally {
            // Close the channel when the collection is complete
            close()
        }
    }

    override fun addItem(userId: String, itemModelItems: ModelItems): Flow<Resource<Boolean>> =
        channelFlow {
            try {
                trySend(Resource.Loading)
                // Check if the item with the same barcode already exists
                val existingItemSnapshot = fireStore.collection(Constants.Users)
                    .document(userId)
                    .collection(Constants.Items)
                    .whereEqualTo(Constants.ItemBarCode, itemModelItems.itemBarCode)
                    .get()
                    .await()

                if (!existingItemSnapshot.isEmpty) {
                    trySend(Resource.Error("Item with this barcode already exists"))
                    return@channelFlow
                }
                // If the barcode is unique, add the new item
                fireStore.collection(Constants.Users)
                    .document(userId)
                    .collection(Constants.Items)
                    .document()
                    .set(itemModelItems)
                    .addOnFailureListener {
                        trySend(Resource.Error("Adding Item Failed due to ${it.localizedMessage}"))
                    }
                    .addOnSuccessListener {
                        trySend(Resource.Success(true))
                    }

                awaitClose()
            } catch (e: Exception) {
                trySend(
                    Resource.Error(
                        e.localizedMessage ?: "An Error Occurred in addItem (Catch)"
                    )
                )
            } finally {
                // Close the channel when the collection is complete
                close()
            }
        }

    override fun searchItemInfo(userId: String, itemBarCode: String): Flow<Resource<ModelItems>> =
        channelFlow {
            Log.d("SearchFragmentNow", "searchItemInfo Imp $userId $itemBarCode")
            try {
                trySend(Resource.Loading)
                val querySnapshot = fireStore.collection(Constants.Users)
                    .document(userId)
                    .collection(Constants.Items)
                    .whereEqualTo(Constants.ItemBarCode, itemBarCode)
                    .get()
                    .await()

                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val itemInfo = document.toObject(ModelItems::class.java)
                    trySend(Resource.Success(itemInfo!!))
                } else {
                    trySend(Resource.Error("Item not found"))
                }

                awaitClose()
            } catch (e: Exception) {
                trySend(
                    Resource.Error(
                        e.localizedMessage ?: "An Error Occurred in getItemData (Catch)"
                    )
                )
            } finally {
                // Close the channel when the collection is complete
                close()
            }

        }
}
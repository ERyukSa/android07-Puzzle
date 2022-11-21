package com.juniori.puzzle.data.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.juniori.puzzle.data.Resource
import com.juniori.puzzle.domain.entity.UserInfoEntity
import com.juniori.puzzle.domain.repository.AuthRepository
import com.juniori.puzzle.util.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override fun getCurrentUserInfo(): Resource<UserInfoEntity> {
        return firebaseAuth.currentUser?.let { firebaseUser ->
            Resource.Success(
                UserInfoEntity(
                    firebaseUser.uid,
                    firebaseUser.email ?: "",
                    firebaseUser.photoUrl?.toString() ?: ""
                )
            )
        } ?: kotlin.run { Resource.Failure(Exception()) }
    }

    override suspend fun requestLogin(acct: GoogleSignInAccount): Resource<UserInfoEntity> {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)

        return try {
            val result = firebaseAuth.signInWithCredential(credential).await()

            result.user?.let { firebaseUser ->
                Resource.Success(
                    UserInfoEntity(
                        firebaseUser.uid,
                        firebaseUser.displayName ?: "",
                        firebaseUser.photoUrl?.toString() ?: ""
                    )
                )
            }  ?: kotlin.run { Resource.Failure(Exception()) }
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Failure(e)
        }
    }

    override fun requestLogout(): Resource<Unit> {
        return try {
            firebaseAuth.signOut()
            Resource.Success(Unit)
        } catch (exception: Exception) {
            Resource.Failure(exception)
        }
    }
}

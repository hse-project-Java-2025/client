package org.hse.smartcalendar.net2024

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInManager(
    private val username: String,
    private val password: String
) {
    fun signIn(
        onFailure: (() -> Unit)? = null,
        onError: (() -> Unit)? = null,
        onSuccess: () -> Unit
    ) {
        val retrofitCall = RetrofitClientPreAuth.retrofitCall
        val registerInfo = UserSignInBody(username, password)
        retrofitCall.loginUser(registerInfo).enqueue(object : Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                println("failure")
                onFailure?.invoke()
            }

            override fun onResponse(
                call: Call<String>,
                response: Response<String>
            ) {
                if (response.code() == 200) {
//                    var user: User?
//                    response.body().let {
//                        user = it?.user?.convertUser()
//                    }
//                    val token = response.body()!!.token
//                    AuthManager().saveAuthData(user, token)
                    onSuccess()
                } else {
                    println("response but wrong code")
                    onError?.invoke()
                }
            }
        })
    }
    data class UserSignInBody(
        val username: String,
        val password: String
    )
}
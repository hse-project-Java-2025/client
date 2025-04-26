package org.hse.smartcalendar.net2024

import org.hse.smartcalendar.network.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignUpManager(private val email: String) {
    private var userName = ""
    private var password = ""

    fun initData(userName: String, password: String) {
        this.userName = userName
        this.password = password
    }

    fun signUp(onSuccess: () -> Unit) {
        val retrofitCall = RetrofitClientPreAuth.retrofitCall
        val registerInfo = UserSignUpBody(userName, email, password)
        retrofitCall.registerUser(registerInfo).enqueue(object : Callback<RegisterResponse> {
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                println("failure on registration")
            }

            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
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
                    println("wrong code on registration")
                }
            }
        })
    }

    data class UserSignUpBody(
        val username: String,
        val email: String,
        val password: String
    )
}

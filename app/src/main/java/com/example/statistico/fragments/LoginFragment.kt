package com.example.statistico.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.statistico.data.Firebase
import com.example.statistico.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var loginBinding: FragmentLoginBinding

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false)

        initializeUI()
        return loginBinding.root
    }

    private fun initializeUI() {
        loginBinding.btnLogin.setOnClickListener {
            val username = loginBinding.eTNickname.text.toString().trim()
            val password = loginBinding.eTPassword.text.toString().trim()

            when {
                username.length < 5 -> {
                    Toast.makeText(this.context, "Username has to be at least 5 characters long.", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(this.context, "Password has to be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Firebase.login(username, password)
                }
            }
        }
    }
}
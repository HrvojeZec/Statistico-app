package com.example.statistico.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.statistico.data.Firebase
import com.example.statistico.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var registerBinding: FragmentRegisterBinding

    companion object {
        fun newInstance(): RegisterFragment {
            return RegisterFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false)

        initializeUI()
        return registerBinding.root
    }

    private fun initializeUI() {
        registerBinding.btnRegister.setOnClickListener {
            val username = registerBinding.eTNickname.text.toString().trim()
            val password = registerBinding.eTPassword.text.toString().trim()

            when {
                username.length < 5 -> {
                    Toast.makeText(this.context, "Username has to be at least 5 characters long.", Toast.LENGTH_SHORT).show()
                }
                password.length < 6 -> {
                    Toast.makeText(this.context, "Password has to be at least 6 characters long.", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    Firebase.preRegisterCheck(username, password)
                }
            }
        }
    }
}
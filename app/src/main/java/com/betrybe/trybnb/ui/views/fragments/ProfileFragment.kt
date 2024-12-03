package com.betrybe.trybnb.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.betrybe.trybnb.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val loginInputLayout = view.findViewById<TextInputLayout>(R.id.login_input_profile)
        val passwordInputLayout = view.findViewById<TextInputLayout>(R.id.password_input_profile)
        val loginEditText = view.findViewById<TextInputEditText>(R.id.login_edit_text)
        val passwordEditText = view.findViewById<TextInputEditText>(R.id.password_edit_text)
        val loginButton = view.findViewById<View>(R.id.login_button_profile)

        loginButton.setOnClickListener {
            val loginText = loginEditText.text.toString()
            val passwordText = passwordEditText.text.toString()

            var isValid = true

            if (loginText.isEmpty()) {
                loginInputLayout.error = getString(R.string.login_required_error)
                isValid = false
            } else {
                loginInputLayout.error = null // Remove erro se o campo estiver preenchido
            }

            if (passwordText.isEmpty()) {
                passwordInputLayout.error = getString(R.string.password_required_error)
                isValid = false
            } else {
                passwordInputLayout.error = null
            }

            if (isValid) {
                // Exemplo: Redirecionar ou exibir mensagem de sucesso
            }
        }
    }
}

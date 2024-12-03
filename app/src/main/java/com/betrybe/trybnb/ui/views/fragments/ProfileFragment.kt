import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.betrybe.trybnb.R
import com.betrybe.trybnb.common.ApiIdlingResource
import com.betrybe.trybnb.data.api.RetrofitInstance
import com.betrybe.trybnb.data.models.LoginRequest
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*

import com.google.android.material.snackbar.Snackbar

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
                login(loginText, passwordText, view)
            } else {
                showSnackbar(view, "Preencha todos os campos.")
            }
        }
    }

    private fun login(username: String, password: String, view: View) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                ApiIdlingResource.increment()

                val loginRequest = LoginRequest(username, password) // Criando o objeto com as credenciais
                val response = RetrofitInstance.create().doLoginRequest(loginRequest)

                ApiIdlingResource.decrement()

                if (response.isSuccessful) {
                    showSnackbar(view, "Login feito com sucesso!")
                } else {
                    showSnackbar(view, "Erro ao autenticar. Tente novamente.")
                }

            } catch (e: Exception) {
                ApiIdlingResource.decrement()
                showSnackbar(view, "Erro ao autenticar. Tente novamente.")
            }
        }
    }

    private fun showSnackbar(view: View, message: String) {
        if (isAdded && context != null) {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}

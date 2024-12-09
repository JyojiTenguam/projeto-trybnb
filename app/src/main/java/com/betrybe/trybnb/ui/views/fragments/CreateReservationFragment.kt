package com.betrybe.trybnb.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.betrybe.trybnb.R
import com.betrybe.trybnb.common.ApiIdlingResource
import com.betrybe.trybnb.data.api.RetrofitInstance
import com.betrybe.trybnb.data.models.BookingDates
import com.betrybe.trybnb.data.models.ReservationItem
import com.betrybe.trybnb.databinding.FragmentCreateReservationBinding
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.HttpURLConnection

class CreateReservationFragment : Fragment() {

    private var _binding: FragmentCreateReservationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fields = ReservationFields(
            binding.firstNameCreateReservation,
            binding.lastNameCreateReservation,
            binding.checkinCreateReservation,
            binding.checkoutCreateReservation,
            binding.additionalNeedsCreateReservation,
            binding.totalPriceCreateReservation
        )

        binding.createReservationButton.setOnClickListener {
            if (areFieldsValid(fields)) {
                createBooking(fields)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun areFieldsValid(fields: ReservationFields): Boolean {
        return fields.run {
            firstNameInput.editText?.text?.isNotBlank() == true &&
                    lastNameInput.editText?.text?.isNotBlank() == true &&
                    checkinInput.editText?.text?.isNotBlank() == true &&
                    checkoutInput.editText?.text?.isNotBlank() == true &&
                    additionalNeedsInput.editText?.text?.isNotBlank() == true &&
                    totalPriceInput.editText?.text?.isNotBlank() == true
        }
    }

    private fun createBooking(fields: ReservationFields) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ApiIdlingResource.increment()
                val api = RetrofitInstance.create()
                val bookingDates = BookingDates(
                    fields.checkinInput.editText?.text.toString().trim(),
                    fields.checkoutInput.editText?.text.toString().trim()
                )
                val isDepositPaid = binding.root.findViewById<CheckBox>(R.id.depositpaid_create_reservation).isChecked
                val reservationItem = ReservationItem(
                    fields.firstNameInput.editText?.text.toString().trim(),
                    fields.lastNameInput.editText?.text.toString().trim(),
                    fields.totalPriceInput.editText?.text.toString().trim().toInt(),
                    isDepositPaid,
                    bookingDates,
                    fields.additionalNeedsInput.editText?.text.toString().trim()
                )

                val response = api.createReservation(reservationItem)
                showToast(response.isSuccessful)

                ApiIdlingResource.decrement()
            } catch (e: HttpException) {
                handleAPIError(e)
            } catch (e: IOException) {
                handleNetworkError(e)
            }
        }
    }

    private fun handleAPIError(exception: HttpException) {
        when (exception.code()) {
            HttpURLConnection.HTTP_NOT_FOUND -> displayErrorMessage("Recurso não encontrado")
            HttpURLConnection.HTTP_UNAUTHORIZED -> displayErrorMessage("Falha na autorização. Faça login novamente")
            else -> displayErrorMessage("Erro ao processar a solicitação")
        }
    }

    private fun handleNetworkError(exception: IOException) {
        displayErrorMessage("Erro de conexão. Verifique sua conexão de internet")
    }

    private fun displayErrorMessage(message: String) {
        val confirmationMessage = requireView().findViewById<MaterialTextView>(R.id.confirmation_message)
        confirmationMessage.text = message
        confirmationMessage.visibility = View.VISIBLE
    }

    private fun showToast(isSuccessful: Boolean) {
        val messageResId = if (isSuccessful) R.string.reserva_feita_com_sucesso else R.string.error_message
        activity?.runOnUiThread {
            Toast.makeText(requireContext(), getString(messageResId), Toast.LENGTH_SHORT).show()
        }
    }
}

data class ReservationFields(
    val firstNameInput: TextInputLayout,
    val lastNameInput: TextInputLayout,
    val checkinInput: TextInputLayout,
    val checkoutInput: TextInputLayout,
    val additionalNeedsInput: TextInputLayout,
    val totalPriceInput: TextInputLayout
)

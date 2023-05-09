package com.example.mystoryapps.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapps.R
import com.example.mystoryapps.ViewModelFactory
import com.example.mystoryapps.databinding.FragmentLoginBinding
import com.example.mystoryapps.main.MainActivity
import com.example.mystoryapps.preferences.UserPreferences
import com.example.mystoryapps.preferences.UserPreferences.Companion.preferenceDefaultValue
import com.example.mystoryapps.preferences.dataStore
import com.example.mystoryapps.viewModel.AutentikasiViewModel

class LoginFragment : Fragment(){

    private lateinit var binding : FragmentLoginBinding
    private lateinit var autentikasiViewModel: AutentikasiViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPreferences.getInstance((activity as AutentikasiActivity).dataStore)

        val autentikasiViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AutentikasiViewModel::class.java]

        binding.btnDaftar.setOnClickListener{
            val fragment = SignupFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.auth, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        binding.masuk.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.inputPassword.text.toString()

            autentikasiViewModel.login(requireContext(), email, password)

            autentikasiViewModel.getUserPreferences("Token").observe(viewLifecycleOwner) { token ->
                Log.e("AutentikasiActivity", "Token berubah : $token")
                if (token != preferenceDefaultValue) {
                    Log.e("AutentikasiActivity : ", "Menuju ke MainActivity")
                    if (autentikasiViewModel.login(requireContext(), email, password) != null) {
                        var alertDialog: AlertDialog? = null
                        alertDialog = AlertDialog.Builder(requireContext()).apply {
                            setTitle("Yeah!")
                            setMessage("Login Berhasil")
                            setPositiveButton("OK") { _, _ ->
                                alertDialog?.dismiss()

                                val intent = Intent(requireContext(), MainActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                requireActivity().finish()
                            }
                            create()
                        }.show()
                    }
                }
            }

        }

        autentikasiViewModel?.isLoading?.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        animation()

    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun animation(){
        val logo = ObjectAnimator.ofFloat(binding.StoryApps, View.ALPHA, 1f).setDuration(500)
        val textLogin = ObjectAnimator.ofFloat(binding.textLogin, View.ALPHA, 1f).setDuration(500)
        val layoutEmail = ObjectAnimator.ofFloat(binding.layoutEmail, View.ALPHA, 1f).setDuration(500)
        val layoutPassword = ObjectAnimator.ofFloat(binding.layoutPassword, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.daftarAkun, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.masuk, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(logo,textLogin,layoutEmail,layoutPassword,register,login)
            startDelay = 500
        }.start()

    }

}

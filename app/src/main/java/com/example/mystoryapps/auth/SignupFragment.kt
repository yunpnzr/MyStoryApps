package com.example.mystoryapps.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapps.R
import com.example.mystoryapps.ViewModelFactory
import com.example.mystoryapps.databinding.FragmentSignupBinding
import com.example.mystoryapps.preferences.UserPreferences
import com.example.mystoryapps.viewModel.AutentikasiViewModel

class SignupFragment : Fragment(){

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    private lateinit var binding : FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = UserPreferences.getInstance((activity as AutentikasiActivity).dataStore)
        val autentikasiViewModel = ViewModelProvider(this, ViewModelFactory(pref))[AutentikasiViewModel::class.java]

            binding.btnMasuk.setOnClickListener {
                val fragment = LoginFragment()
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.auth, fragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }

            binding.register.setOnClickListener {
                val name = binding.registNama.text.toString()
                val email = binding.registEmail.text.toString()
                val password = binding.registPassword.text.toString()

                if (password.length < 8) {
                    Toast.makeText(requireContext(), "Password kurang dari 8 karakter", Toast.LENGTH_SHORT).show()
                } else {
                    autentikasiViewModel.register(requireContext(),name, email, password)
                    autentikasiViewModel._registerResponse.observe(viewLifecycleOwner) { validation ->
                        if (validation != null) {
                            val dialog = AlertDialog.Builder(requireContext()).apply {
                                setTitle("Yeah!")
                                setMessage("Daftar Akun Berhasil")
                                setCancelable(false)
                            }.create()
                            dialog.show()

                            Handler().postDelayed({
                                dialog.dismiss()

                                val intent = Intent(requireContext(), AutentikasiActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                            }, 1000)
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
        val textRegister = ObjectAnimator.ofFloat(binding.textRegister, View.ALPHA, 1f).setDuration(500)
        val layoutNama = ObjectAnimator.ofFloat(binding.layoutNama, View.ALPHA, 1f).setDuration(500)
        val layoutEmail = ObjectAnimator.ofFloat(binding.layoutEmail, View.ALPHA, 1f).setDuration(500)
        val layoutPassword = ObjectAnimator.ofFloat(binding.layoutPassword, View.ALPHA, 1f).setDuration(500)
        val loginAkun = ObjectAnimator.ofFloat(binding.loginAkun, View.ALPHA, 1f).setDuration(500)
        val register = ObjectAnimator.ofFloat(binding.register, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(logo,textRegister,layoutNama,layoutEmail,layoutPassword,loginAkun,register)
            startDelay = 500
        }.start()

    }

}
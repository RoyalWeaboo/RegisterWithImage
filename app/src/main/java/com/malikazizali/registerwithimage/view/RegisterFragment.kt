package com.malikazizali.registerwithimage.view

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.malikazizali.registerwithimage.databinding.FragmentRegisterBinding
import com.malikazizali.registerwithimage.databinding.FragmentUserBinding
import com.malikazizali.registerwithimage.viewmodel.AuthViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class RegisterFragment : Fragment() {
    lateinit var binding : FragmentRegisterBinding
    private var imageMultiPart: MultipartBody.Part? = null
    private var imageUri: Uri? = Uri.EMPTY
    private var imageFile: File? = null
    lateinit var viewModelRegister : AuthViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelRegister = ViewModelProvider(this).get(AuthViewModel::class.java)
        binding.registerProgressBar.visibility = View.GONE
        binding.btnAddImage.setOnClickListener {
            openGallery()
        }
        binding.btnRegister.setOnClickListener {
            val em = binding.email.editText?.text.toString()
            val pass = binding.password.editText?.text.toString()
            val conPass = binding.conPassword.editText?.text.toString()
            if (em != "" && pass != ""){
                if(pass==conPass) {
                    binding.registerProgressBar.visibility = View.VISIBLE
                    postDataUser()
                }else{
                    Toast.makeText(requireActivity(), "Password do not match !", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireActivity(), "Email and Password can't be empty !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun postDataUser(){

        val fullName = binding.namaLengkap.editText?.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val email = binding.email.editText?.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val password = binding.password.editText?.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val phoneNumber = binding.phoneNumber.editText?.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val address = binding.address.editText?.text.toString().toRequestBody("multipart/form-data".toMediaType())
        val city = binding.location.editText?.text.toString().toRequestBody("multipart/form-data".toMediaType())

        viewModelRegister.addLiveDataUser.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.registerProgressBar.visibility = View.GONE
                Toast.makeText(requireActivity(), "Registered new user !", Toast.LENGTH_SHORT)
                    .show()
            }else{
                binding.registerProgressBar.visibility = View.GONE
                Toast.makeText(requireActivity(), "Register Failed !", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        viewModelRegister.postApiRegister(fullName, email, password, phoneNumber, address, imageMultiPart!!,city)
    }

    fun openGallery(){
        getContent.launch("image/*")
    }

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val contentResolver: ContentResolver = requireActivity().contentResolver
                val type = contentResolver.getType(it)
                imageUri = it

                val fileNameimg = "${System.currentTimeMillis()}.png"
                binding.profileImgFrame.setImageURI(it)
                Toast.makeText(requireActivity(), "Image : "+"$imageUri"+" choosen", Toast.LENGTH_SHORT).show()

                val tempFile = File.createTempFile("profile-image-", fileNameimg, null)
                imageFile = tempFile
                val inputstream = contentResolver.openInputStream(uri)
                tempFile.outputStream().use    { result ->
                    inputstream?.copyTo(result)
                }
                val requestBody: RequestBody = tempFile.asRequestBody(type?.toMediaType())
                imageMultiPart = MultipartBody.Part.createFormData("image", tempFile.name, requestBody)
            }
        }

}
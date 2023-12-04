package com.example.aifordrive.ui.transform

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aifordrive.R
import com.example.aifordrive.databinding.FragmentTransformBinding
import kotlin.random.Random

class TransformFragment : Fragment() {

    private var _binding: FragmentTransformBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val transformViewModel = ViewModelProvider(this).get(TransformViewModel::class.java)
        _binding = FragmentTransformBinding.inflate(inflater, container, false)

        // Set a random avatar
        val avatars = listOf(
            R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3, R.drawable.avatar_4,
            R.drawable.avatar_5, R.drawable.avatar_6, R.drawable.avatar_7, R.drawable.avatar_8,
            R.drawable.avatar_9, R.drawable.avatar_10, R.drawable.avatar_11, R.drawable.avatar_12,
            R.drawable.avatar_13, R.drawable.avatar_14, R.drawable.avatar_15, R.drawable.avatar_16,
            // ... include all avatar drawable IDs
        )
        val randomAvatarId = avatars[Random.nextInt(avatars.size)]
        binding.imageViewAvatar.setImageResource(randomAvatarId)

        binding.buttonRandomAvatar.setOnClickListener {
            val randomAvatarId = avatars[Random.nextInt(avatars.size)]
            binding.imageViewAvatar.setImageResource(randomAvatarId)
        }

        binding.buttonImportFromFile.setOnClickListener {
            openGallery()
        }

        binding.buttonTakePhoto.setOnClickListener {
            captureImage()
        }

        // Set up the submit button action
        binding.buttonSubmit.setOnClickListener {
            val username = binding.editTextUsername.text.toString()
            val password = binding.editTextPassword.text.toString()

            transformViewModel.setUsername(username)
            transformViewModel.setPassword(password)

            // Perform validation and possibly send data to a server
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Constant for Intent request code
    companion object {
        private const val PICK_IMAGE_REQUEST = 1
        private const val CAPTURE_IMAGE_REQUEST = 2
    }

    // Function to open the gallery
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Handling the result of the gallery intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            PICK_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val selectedImageUri: Uri? = data?.data
                    binding.imageViewAvatar.setImageURI(selectedImageUri)
                }
            }
            CAPTURE_IMAGE_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    binding.imageViewAvatar.setImageBitmap(imageBitmap)
                }
            }
        }
    }


    // Function to capture an image
    private fun captureImage() {
        // Check for camera permission, request if not granted
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, CAPTURE_IMAGE_REQUEST)
            } catch (e: ActivityNotFoundException) {
                // Handle exception
            }
        } else {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), CAPTURE_IMAGE_REQUEST)
        }
    }


    // Handling permission request results
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CAPTURE_IMAGE_REQUEST -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    captureImage()
                } else {
                    // Permission denied, handle accordingly
                }
                return
            }
            // Other 'when' lines to check for other permissions this app might request
        }
    }


}

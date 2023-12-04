package com.example.aifordrive.ui.reflow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aifordrive.databinding.FragmentReflowBinding

class ReflowFragment : Fragment() {

    private var _binding: FragmentReflowBinding? = null
    private val binding get() = _binding!!

    private lateinit var chatAdapter: ChatAdapter
    private lateinit var reflowViewModel: ReflowViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reflowViewModel = ViewModelProvider(this)[ReflowViewModel::class.java]
        _binding = FragmentReflowBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupMessageSending()
        observeMessages()

        return binding.root
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(mutableListOf())
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
    }

    private fun observeMessages() {
        reflowViewModel.messages.observe(viewLifecycleOwner) { messages ->
            chatAdapter.submitList(messages)
            if (messages.isNotEmpty()) {
                binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
            }
        }
    }

    private fun setupMessageSending() {
        binding.sendButton.setOnClickListener {
            val message = binding.messageEditText.text.toString()
            if (message.isNotBlank()) {
                reflowViewModel.sendMessage(message) // Send message using ViewModel
                binding.messageEditText.text.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


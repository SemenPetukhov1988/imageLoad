package ru.netology.nmedia.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.netology.nmedia.databinding.FragmentImageBinding
import ru.netology.nmedia.view.load


class ImageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentImageBinding.inflate(
            inflater,
            container,
            false
        )
        val imageurl = arguments?.getString("image")

        binding.attachmentImage.load(imageurl)

        return binding.root
    }

}
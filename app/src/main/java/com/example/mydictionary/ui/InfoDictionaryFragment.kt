package com.example.mydictionary.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mydictionary.R
import com.example.mydictionary.adapter.AdapterRvInfo
import com.example.mydictionary.databinding.FragmentInfoDictionaryBinding
import com.example.mydictionary.vm.DictionaryResource
import com.example.mydictionary.vm.MyViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [InfoDictionaryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class InfoDictionaryFragment : Fragment(), MediaPlayer.OnPreparedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentInfoDictionaryBinding
    private val viewModel: MyViewModel by activityViewModels()
    lateinit var adapterRv: AdapterRvInfo
    private var mediaPlayer: MediaPlayer? = null

//    private val viewModel: MyViewModel by viewModels(
//        ownerProducer = { requireParentFragment() }
//    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoDictionaryBinding.inflate(inflater, container, false)



        viewModel.live.observe(viewLifecycleOwner) {
            when (it) {
                is DictionaryResource.Loading -> {
                    Log.d("TAG777", "Loading....")
                }
                is DictionaryResource.Error -> {
                    Log.d("TAG777", "Erorr berildi")
                }
                is DictionaryResource.SuccessList -> {
                    val position = arguments?.getInt("Index")
                    if (position != null) {
                        val myRespository = it.myRespository.get(position)

                        adapterRv =
                            AdapterRvInfo(myRespository.meanings[0].definitions)
                        binding.rv.adapter = adapterRv

                        binding.apply {
                            title.setText(myRespository.word)
                            if (myRespository.phonetics.size > 1) {
                                text.setText("[ ${myRespository.phonetics.get(1).text} ]")
                            }
                            text2.setText(myRespository.meanings.get(0).partOfSpeech.toString())
                            sound.setOnClickListener {
                                if (myRespository.phonetics.get(0).audio.isNotEmpty()) {
                                    soundBtn(myRespository.phonetics.get(0).audio)
                                } else if (myRespository.phonetics.get(1).audio.isNotEmpty()) {
                                    soundBtn(myRespository.phonetics.get(1).audio)
                                } else {
                                    Toast.makeText(requireContext(), "no sound", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                            shareBtn.setOnClickListener {
                                if (!myRespository.phonetics.get(1).sourceUrl.isNullOrEmpty()) {
                                    shareBtn(myRespository.phonetics.get(1).sourceUrl)
                                } else if (!myRespository.sourceUrls[0].isNullOrEmpty()) {
                                    shareBtn(myRespository.sourceUrls[0])
                                }
                            }
                        }
                    }

                }
                is DictionaryResource.Success -> {
                    val position = arguments?.getInt("Index")
                    if (position != null) {
                        val myRespository = it.myRespository

                        adapterRv = AdapterRvInfo(myRespository.meanings[0].definitions)
                        binding.rv.adapter = adapterRv

                        binding.apply {
                            title.setText(myRespository.word)
                            if (myRespository.phonetics.size > 1) {
                                text.setText("[ ${myRespository.phonetics.get(1).text} ]")
                            }
                            text2.setText(myRespository.meanings.get(0).partOfSpeech.toString())
                            sound.setOnClickListener {
                                if (!myRespository.phonetics.isNullOrEmpty()) {
                                    for (i in myRespository.phonetics) {
                                        if (!i.audio.isNullOrEmpty()) {
                                            soundBtn(i.audio)
                                        }
                                    }
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "no sound",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }
                            shareBtn.setOnClickListener {
                                if (!myRespository.phonetics.isNullOrEmpty()) {
                                    if (!myRespository.phonetics.get(1).sourceUrl.isNullOrEmpty()) {
                                        shareBtn(myRespository.phonetics.get(1).sourceUrl)
                                    } else if (!myRespository.sourceUrls[0].isNullOrEmpty()) {
                                        shareBtn(myRespository.sourceUrls[0])
                                    }
                                } else if (!myRespository.sourceUrls.isNullOrEmpty()) {
                                    if (!myRespository.sourceUrls[0].isNullOrEmpty()) {
                                        shareBtn(myRespository.sourceUrls[0])
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        binding.apply {
            backBtn.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment InfoDictionaryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InfoDictionaryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun shareBtn(url: String) {
        if (url.isNotEmpty()) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, url);
            startActivity(
                Intent.createChooser(
                    shareIntent,
                    url
                )
            )

        } else {
            Toast.makeText(requireContext(), "Url not", Toast.LENGTH_SHORT).show()
        }
    }

    fun soundBtn(url: String) {
        Log.d("LOG888", "${url}")
        Log.d("LOG888", "URL : ${url.substring(url.length - 3, url.length)}")

        if (url.length > 5 && url.substring(url.length - 3, url.length).equals("mp3")) {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setDataSource(url)
            mediaPlayer?.setOnPreparedListener(this@InfoDictionaryFragment)
            mediaPlayer?.prepareAsync()
        } else {
            Toast.makeText(requireContext(), "Sound format exeption", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPrepared(p0: MediaPlayer?) {
        mediaPlayer?.start()
    }
}
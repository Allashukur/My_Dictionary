package com.example.mydictionary.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydictionary.R
import com.example.mydictionary.adapter.AdapterRv
import com.example.mydictionary.databinding.FragmentHomeBinding
import com.example.mydictionary.model.My_Dictionary
import com.example.mydictionary.vm.DictionaryResource
import com.example.mydictionary.vm.MyViewModel
import kotlinx.coroutines.*
import uz.mobiler.mvvmg23.utils.NetworkHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.CoroutineContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), CoroutineScope {
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

    lateinit var binding: FragmentHomeBinding
    lateinit var job: Job
    private val myViewModel: MyViewModel by activityViewModels()
    lateinit var adapterrv: AdapterRv
    lateinit var otherList: ArrayList<My_Dictionary>
    lateinit var networkHelper: NetworkHelper
    lateinit var list: ArrayList<My_Dictionary>

    //    @RequiresApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        job = Job()
        networkHelper = NetworkHelper(requireContext())


        myViewModel.getMyDictionaryDataList(requireContext()).observe(requireActivity(), Observer {
            when (it) {
                is DictionaryResource.SuccessList -> {
                    binding.progress.visibility = View.INVISIBLE
                    otherList = ArrayList()
                    otherList.addAll(it.myRespository)
                    adapterrv = AdapterRv(it.myRespository, object : AdapterRv.onClick {
                        override fun onClickListener(postion: Int) {
                            var bundle = Bundle()
                            bundle.putInt("Index", postion)
                            findNavController().navigate(R.id.infoDictionaryFragment, bundle)
                        }

                    })
                    binding.rv.layoutManager =
                        LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                    binding.rv.adapter = adapterrv
                    Log.d("SATA", "SUCCES : " + it.myRespository)
                }
                is DictionaryResource.Loading -> {
                    Log.d("SATA", "LOAD : " + it.toString())
                }
                is DictionaryResource.Error -> {
//                    Toast.makeText(requireContext(), "${it.message}", Toast.LENGTH_SHORT).show()
                }
            }


        })
        binding.searchBtn.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                val queryText = text?.lowercase(Locale.getDefault())
                if (queryText != null && queryText.isNotEmpty()) {
                    myViewModel.getMyDictionaryData(queryText, requireContext())
                        .observe(requireActivity(), Observer {
                            when (it) {
                                is DictionaryResource.Loading -> {
                                    Log.d("TAG777", "Loading..")
                                }
                                is DictionaryResource.Success -> {
                                    list = ArrayList<My_Dictionary>()
                                    Log.d("TAG777", "${it.myRespository}")

                                    list.add(it.myRespository)
                                    list.addAll(otherList)

                                    adapterrv =
                                        AdapterRv(list, object : AdapterRv.onClick {
                                            override fun onClickListener(postion: Int) {

                                                var bundle = Bundle()
                                                bundle.putInt("Index", postion)
                                                findNavController().navigate(
                                                    R.id.infoDictionaryFragment,
                                                    bundle
                                                )
                                            }

                                        })
                                    binding.rv.adapter = adapterrv
//
                                }
                                is DictionaryResource.Error -> {
                                    if (list.size > 3) {
                                        list.removeAt(0)
                                        binding.rv.adapter?.notifyDataSetChanged()
                                    }
                                    Log.d("TAG777", "Error")
                                }
                            }
                        })

                }
                return false
            }

        })

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


    override fun onDestroy() {
        job.cancel()
        super.onDestroy()

    }

    override fun onStart() {
        super.onStart()
        binding.searchBtn.apply {
            isIconified = true
//            onActionViewCollapsed()
        }
    }
}
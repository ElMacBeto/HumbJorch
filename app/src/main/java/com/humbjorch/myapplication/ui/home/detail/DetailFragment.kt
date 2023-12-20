package com.humbjorch.myapplication.ui.home.detail

import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.humbjorch.myapplication.R
import com.humbjorch.myapplication.data.datSource.ResponseStatus
import com.humbjorch.myapplication.data.model.FactsEntity
import com.humbjorch.myapplication.databinding.FragmentDetailBinding
import com.humbjorch.myapplication.sis.utils.alerts.CustomToastWidget
import com.humbjorch.myapplication.sis.utils.alerts.TypeToast
import com.humbjorch.myapplication.sis.utils.getDrawableFavorite
import com.humbjorch.myapplication.sis.utils.latitudeLongitudeFormat
import com.humbjorch.myapplication.sis.utils.loadImageUrl
import com.humbjorch.myapplication.sis.utils.util.share
import com.humbjorch.myapplication.ui.home.HomeViewModel
import com.humbjorch.myapplication.ui.home.MainActivity
import com.humbjorch.myapplication.ui.home.MainActivity.Companion.CHANGE_HOME_LIST
import com.humbjorch.myapplication.ui.login.LoginSessionViewModel
import dagger.hilt.android.AndroidEntryPoint
const val  FACT = "fact"
@AndroidEntryPoint
class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private var fact: FactsEntity? = null
    private val viewModel: LoginSessionViewModel by viewModels()
    private val viewModelHome: HomeViewModel by viewModels()
    private var isChecked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(LayoutInflater.from(context), container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            fact = it.getParcelable(FACT)
        }

    }

    private fun setListenerActions() {
        binding.btnShare.setOnClickListener {
            share(requireContext(), requireActivity(), takeScreenshot())
        }
        binding.imgFavorite.setOnClickListener {
            isChecked = !fact!!.isFavorite
            viewModelHome.updateData(fact!!)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fact = fact
        setVies()
        setListenerActions()
        setObservers()
    }

    private fun setObservers() {
        viewModelHome.getUpdateLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResponseStatus.Loading -> {}

                is ResponseStatus.Success -> {
                    if ((it.data as Int) > 0) {
                        binding.imgFavorite.setImageResource(isChecked.getDrawableFavorite())
                        CustomToastWidget.show(
                            activity = requireActivity(),
                            message = requireActivity().getString(R.string.updateSuccess),
                            type = TypeToast.SUCCESS
                        )
                        CHANGE_HOME_LIST = true
                    }
                }

                is ResponseStatus.Error -> {
                    CustomToastWidget.show(
                        activity = requireActivity(),
                        message = it.messageId,
                        type = TypeToast.ERROR
                    )
                }
            }
        }
    }

    private fun setVies() {
        binding.imgPhotoProfile.loadImageUrl(viewModel.getImageUrl())
        binding.tvEmail.text = viewModel.getEmail()
        binding.imgFavorite.setImageResource(fact!!.isFavorite.getDrawableFavorite())
        // TODO: latitudeLongitudeFormat
        binding.tvLat.latitudeLongitudeFormat("Latitude", viewModel.getLatitude())
        binding.tvLong.latitudeLongitudeFormat("Longitude", viewModel.getLongitude())
    }
    private fun takeScreenshot(): Bitmap {
        val bitmap = Bitmap.createBitmap(
            binding.containerShare.width,
            binding.containerShare.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        binding.containerShare.draw(canvas)
        return bitmap
    }
}
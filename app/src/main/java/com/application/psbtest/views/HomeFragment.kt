package com.application.psbtest.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.application.psbtest.AppContext
import com.application.psbtest.R
import com.application.psbtest.ValuteDataClass
import com.application.psbtest.adapters.RecyclerViewAdapter
import com.application.psbtest.databinding.FragmentHomeBinding
import com.application.psbtest.models.ModelImp
import com.application.psbtest.presenters.HomePresenter
import com.application.psbtest.straings_interactor.StringsInteractorImp

class HomeFragment : Fragment(), HomeView {
    private lateinit var presenter: HomePresenter

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        presenter = HomePresenter(this, StringsInteractorImp(AppContext.context), ModelImp())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showUpdate()

        presenter.startPresenter()

        binding.buttonUpdate.setOnClickListener {
            presenter.startPresenter()
            binding.buttonUpdate.visibility = View.GONE
        }
    }

    override fun showButtonForUpdate(){
        binding.buttonUpdate.visibility = View.VISIBLE
    }

    override fun showUpdate(){
        binding.layoutDate.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
        binding.idLoaderGo.visibility = View.VISIBLE
        binding.idLoaderGo.startAnimation(
            AnimationUtils.loadAnimation(
                AppContext.context,
                R.anim.anim_new
            ))
    }

    override fun showDate(date:String) {
        binding.layoutDate.visibility = View.VISIBLE
        binding.updateDate.text = date
    }

    override fun setRecyclerView(arrayAllData:MutableList<ValuteDataClass>) = with(binding){
        recyclerView.visibility = View.VISIBLE
        idLoaderGo.visibility = View.GONE
        idLoaderGo.clearAnimation()

        val adapterNewApp = RecyclerViewAdapter(arrayAllData)
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapterNewApp
    }

    override fun showToast(text: String) {
        Toast.makeText(AppContext.context, text, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter.onDestroy()
        _binding = null
    }
}
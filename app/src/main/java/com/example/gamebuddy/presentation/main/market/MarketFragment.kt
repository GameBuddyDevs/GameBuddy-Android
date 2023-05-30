package com.example.gamebuddy.presentation.main.market

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gamebuddy.databinding.FragmentMarketBinding
import com.example.gamebuddy.presentation.auth.BaseAuthFragment
import com.example.gamebuddy.util.*
import timber.log.Timber

class MarketFragment : BaseAuthFragment(), MarketAdapter.OnClickListener {
    private val viewModel: MarketViewModel by activityViewModels()
    private var _binding: FragmentMarketBinding? = null
    private val binding get() = _binding!!
    private var marketAdapter: MarketAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMarketBinding.inflate(inflater, container, false)
        updateEnvironment(apiType = ApiType.APPLICATION, deploymentType = DeploymentType.PRODUCTION)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        viewModel.onTriggerEvent(MarketEvent.GetAvatars)
        viewModel.onTriggerEvent(MarketEvent.GetCoin)
        initRecyclerView()
        collectState()
    }

    private fun collectState() {
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)
            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onTriggerEvent(MarketEvent.OnRemoveHeadFromQueue)
                    }
                }
            )

            marketAdapter?.apply {
                submitList(state.avatars)
            }
            binding.tvCoinPoint.text = state.coin.toString()
        }
    }
    private fun buyItem(avatarId: String,price: Int){
        viewModel.onTriggerEvent(MarketEvent.OnSetAvatarId(avatarId))
        viewModel.onTriggerEvent(MarketEvent.BuyItem)

        val text = binding.tvCoinPoint.text
        val numbersOnly = text.replace(Regex("[^0-9]"), "").toInt()
        binding.tvCoinPoint.text = (numbersOnly - price).toString()
    }
    private fun initRecyclerView() {
        binding.rvAvatarList.apply {
            layoutManager = GridLayoutManager(this@MarketFragment.context,2)
            marketAdapter = MarketAdapter(this@MarketFragment)
            adapter = marketAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemClick(position: Int, avatarId: String) {
        Timber.d("onItemClick Message $position : $avatarId")
    }

    override fun onBuyClick(position: Int, avatarId: String, price: Int) {
        val coin = viewModel.uiState.value?.coin
        if(coin!! < price ){
            Toast.makeText(context,"You don't have enough money",Toast.LENGTH_LONG).show()
        }else{
            buyItem(avatarId,price)
        }

    }
    override fun updateEnvironment(
        apiType: ApiType,
        deploymentType: DeploymentType
    ) {
        val index = EnvironmentManager.environments.indexOfFirst { it.apiType == apiType }
        EnvironmentManager.environments[index] = EnvironmentModel(
            apiType = apiType,
            deploymentType = deploymentType,
            path = "application/"
        )
    }
}
package com.example.gamebuddy.util

import timber.log.Timber

data class EnvironmentModel(
    val apiType: ApiType,
    val deploymentType: DeploymentType
) {

    var baseUrl = ""

    init {
        Timber.d("apiType: ${apiType.url}, deploymentType: $deploymentType")
        setBaseUrl(
            apiType = apiType,
            deploymentType = deploymentType
        )
    }

    private fun setBaseUrl(apiType: ApiType, deploymentType: DeploymentType) {
        val prefix = when (deploymentType) {
            DeploymentType.ALPHA -> "alpha-"
            DeploymentType.BETA -> "beta-"
            DeploymentType.PRODUCTION -> ""
        }
        Timber.d("prefix: $prefix")
        baseUrl = "http://${prefix}${apiType.url}"
    }

    override fun toString(): String {
        return baseUrl
    }
}

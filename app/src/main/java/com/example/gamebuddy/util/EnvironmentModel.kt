package com.example.gamebuddy.util

import timber.log.Timber

/**
 * @param apiType: The type of the API. It tells us which API we are going to use. (AUTH, APPLICATION, MATCH)
 * @param deploymentType: The type of the deployment. It tells us which environment we are going to use. (ALPHA, BETA, PRODUCTION)
 * @param path: The suffix of the URL. It is used to add a path to the URL. (auth/, application/, match/)
 * */
data class EnvironmentModel(
    val apiType: ApiType,
    val deploymentType: DeploymentType,
    val path: String = ""
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
        baseUrl = "http://${prefix}${apiType.url}${path}}"
        Timber.d("baseUrlssssss: $baseUrl")
    }

    override fun toString(): String {
        return baseUrl
    }
}

package com.example.gamebuddy.util

data class EnvironmentModel(
    val apiType: ApiType,
    val deploymentType: DeploymentType
) {

    var baseUrl = ""

    init {
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
        baseUrl = "http://${prefix}${apiType.url}"
    }

    override fun toString(): String {
        return baseUrl
    }
}

package com.example.gamebuddy.util

object EnvironmentManager {

    val environments = mutableListOf(
        EnvironmentModel(
            apiType = ApiType.AUTH,
            deploymentType = DeploymentType.PRODUCTION
        ),
        EnvironmentModel(
            apiType = ApiType.APPLICATION,
            deploymentType = DeploymentType.PRODUCTION
        ),
        EnvironmentModel(
            apiType = ApiType.MATCH,
            deploymentType = DeploymentType.PRODUCTION
        )
    )

    fun getBaseUrl(apiType: ApiType): String {
        return environments.first { it.apiType == apiType }.baseUrl
    }

    fun getEnvironment(apiType: ApiType): EnvironmentModel {
        return environments.first { it.apiType == apiType }
    }
}
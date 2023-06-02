package com.example.gamebuddy.util

object EnvironmentManager {

    val environments = mutableListOf(
        EnvironmentModel(
            apiType = ApiType.AUTH,
            deploymentType = DeploymentType.PRODUCTION,
            path = "auth/"
        ),
        EnvironmentModel(
            apiType = ApiType.APPLICATION,
            deploymentType = DeploymentType.PRODUCTION,
            path = "application/"
        ),
        EnvironmentModel(
            apiType = ApiType.MATCH,
            deploymentType = DeploymentType.PRODUCTION,
            path = "match/"
        ),
        EnvironmentModel(
            apiType = ApiType.MESSAGE,
            deploymentType = DeploymentType.PRODUCTION,
            path = "messages/"
        ),
        EnvironmentModel(
            apiType = ApiType.COMMUNITY,
            deploymentType = DeploymentType.PRODUCTION,
            path = "community/"
        ),
    )

    fun getBaseUrl(apiType: ApiType): String {
        return environments.first { it.apiType == apiType }.baseUrl
    }

    fun getEnvironment(apiType: ApiType): EnvironmentModel {
        return environments.first { it.apiType == apiType }
    }
}
package com.example.gamebuddy.presentation

import com.example.gamebuddy.util.ApiType
import com.example.gamebuddy.util.DeploymentType

interface EnvironmentChangingListener {

    fun updateEnvironment(apiType: ApiType, deploymentType: DeploymentType)

}
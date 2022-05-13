@file:Suppress("unused", "unused", "unused")

package org.maurycy.utils

import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Liveness

@Suppress("unused")
@Liveness
class MyLivenessCheck : HealthCheck {

    override fun call(): HealthCheckResponse {
        return HealthCheckResponse.up("alive")
    }

}
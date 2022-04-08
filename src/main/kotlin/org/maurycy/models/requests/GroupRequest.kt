package org.maurycy.models.requests

import org.maurycy.models.requests.ModelRequestIF
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table


data class GroupRequest(
    val name: String,
    val description: String
) : ModelRequestIF
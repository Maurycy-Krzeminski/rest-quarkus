package org.maurycy.models.requests


data class GroupRequest(
    val name: String,
    val description: String
) : ModelRequestIF
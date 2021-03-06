package org.maurycy.models.requests

import org.maurycy.enums.TaskStatus
import javax.persistence.EnumType
import javax.persistence.Enumerated


data class TaskRequest(

    val name: String,
    val description: String,

    @Enumerated(EnumType.STRING)
    val status: TaskStatus,

    val group: Long,

    val userCreator: Long,

    val userAssigned: Long,

    ) : ModelRequestIF






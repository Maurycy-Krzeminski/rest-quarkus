package org.maurycy.models.requests

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.maurycy.models.Group
import org.maurycy.models.User
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

class UserInGroupRequest(
    val userId: Long,
    val groupId: Long
) : ModelRequestIF




package org.maurycy.models

import org.maurycy.enums.UserStatus
import org.maurycy.models.requests.UserRequest
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "\"User\"")
class User : ModelIF<UserRequest> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    override var id: Long? = null
    var email: String=""
    var userName: String=""
    var password: String=""

    @Enumerated(EnumType.STRING)
    var userStatus: UserStatus = UserStatus.INACTIVE



}




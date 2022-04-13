package org.maurycy.models

import org.maurycy.enums.UserStatus
import org.maurycy.models.requests.UserRequest
import java.util.Objects
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Suppress("EqualsOrHashCode")
@Entity
@Table(name = "\"User\"")
class User : ModelIF<UserRequest> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null
    var email: String? = null
    var userName: String? = null
    var password: String? = null

    @Enumerated(EnumType.STRING)
    var userStatus: UserStatus? = null
    override fun hashCode(): Int {
        return Objects.hashCode(id.toString()+email+userName+password+userStatus.toString())
    }

}




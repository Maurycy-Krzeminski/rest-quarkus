package org.maurycy.models

import org.maurycy.models.requests.UserInGroupRequest
import java.util.Objects
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Suppress("EqualsOrHashCode")
@Entity
@Table(name = "\"UserInGroup\"")
class UserInGroup : ModelIF<UserInGroupRequest> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    override var id: Long? = null

    @OneToOne
    @JoinColumn(name = "id")
    var userId: User? = null

    @OneToOne
    @JoinColumn(name = "id")
    var groupId: Group? = null

    override fun hashCode(): Int {
        return Objects.hashCode(id.toString()+ userId?.id.toString()+groupId?.id.toString())
    }

}




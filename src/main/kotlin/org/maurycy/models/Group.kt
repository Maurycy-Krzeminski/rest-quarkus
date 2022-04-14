package org.maurycy.models

import org.maurycy.models.requests.GroupRequest
import java.util.Objects
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@Suppress("EqualsOrHashCode")
@Entity
@Table(name = "\"Group\"")
class Group : ModelIF<GroupRequest> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    var name: String? = null
    var description: String? = null
    override fun hashCode(): Int {
        return Objects.hashCode(id.toString()+name+description)
    }

}


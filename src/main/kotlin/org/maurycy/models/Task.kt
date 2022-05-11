package org.maurycy.models

import org.maurycy.enums.TaskStatus
import org.maurycy.models.requests.TaskRequest
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
@Table(name = "\"Task\"")
open class Task : ModelIF<TaskRequest> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    open var name: String? = null
    open var description: String? = null

    @Enumerated(EnumType.STRING)
    open var status: TaskStatus? = null

    open var groupId: Long? = null

    open var userCreatorId: Long? = null

    open var userAssignedId: Long? = null

    override fun hashCode(): Int {
        return Objects.hashCode(id.toString() + name + description + status.toString() +
                groupId?.toString() + userCreatorId?.toString()+ userAssignedId?.toString())
    }
}




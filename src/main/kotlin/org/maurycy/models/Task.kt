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
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Suppress("EqualsOrHashCode")
@Entity
@Table(name = "\"Task\"")
class Task : ModelIF<TaskRequest> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    override var id: Long? = null

    var name: String? = null
    var description: String? = null

    @Enumerated(EnumType.STRING)
    var status: TaskStatus? = null

    @OneToOne
    @JoinColumn(name = "id")
    var group: Group? = null

    @OneToOne
    @JoinColumn(name = "id")
    var userCreator: User? = null

    @OneToOne
    @JoinColumn(name = "id")
    var userAssigned: User? = null

    override fun hashCode(): Int {
        return Objects.hashCode(id.toString() + name + description + status.toString() +
                group?.id.toString() + userCreator?.id.toString()+ userAssigned?.id.toString())
    }
}




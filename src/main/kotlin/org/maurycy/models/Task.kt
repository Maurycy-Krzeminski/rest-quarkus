package org.maurycy.models

import org.maurycy.enums.TaskStatus
import org.maurycy.models.requests.TaskRequest
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "\"Task\"")
class Task : ModelIF<TaskRequest> {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    override var id: Long? = null

    lateinit var name: String
    lateinit var description: String

    @Enumerated(EnumType.STRING)
    lateinit var status: TaskStatus

    @OneToOne
    @JoinColumn(name = "id")
    lateinit var group: Group

    @OneToOne
    @JoinColumn(name = "id")
    lateinit var userCreator: User

    @OneToOne
    @JoinColumn(name = "id")
    lateinit var userAssigned: User

}




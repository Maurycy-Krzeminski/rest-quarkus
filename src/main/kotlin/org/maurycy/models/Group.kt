package org.maurycy.models

import org.maurycy.models.requests.GroupRequest
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "\"Group\"")
class Group : ModelIF<GroupRequest> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    override var id: Long? = null

    var name: String? = null
    var description: String? = null


}


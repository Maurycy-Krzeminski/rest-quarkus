package org.maurycy.models

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import org.maurycy.models.requests.UserInGroupRequest
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "\"UserInGroup\"")
class UserInGroup : ModelIF<UserInGroupRequest> {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    override var id: Long? = null

    @OneToOne
    @JoinColumn(name = "id")
    var userId: User? = null

    @OneToOne
    @JoinColumn(name = "id")
    var groupId: Group? = null

}




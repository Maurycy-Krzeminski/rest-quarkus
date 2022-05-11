package org.maurycy.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "\"UserInGroup\"")
open class UserInGroup{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    @OneToOne
    open var user: User? = null

    @OneToOne
    open var group: Group? = null
}
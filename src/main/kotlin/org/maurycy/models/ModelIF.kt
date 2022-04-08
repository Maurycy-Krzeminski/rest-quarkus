package org.maurycy.models

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase

interface ModelIF<T:Any>:PanacheEntityBase {
        val id:Long?
}

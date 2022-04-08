package org.maurycy.models

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntityBase
import org.maurycy.models.requests.ModelRequestIF

interface ModelIF<T:Any>:PanacheEntityBase {
        val id:Long?
}

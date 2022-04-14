package org.maurycy.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheQuery
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import org.maurycy.models.Group
import org.maurycy.models.Task
import org.maurycy.models.User
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepository:PanacheRepository<User>
@ApplicationScoped
class GroupRepository:PanacheRepository<Group>
@ApplicationScoped
class TaskRepository:PanacheRepository<Task>{
    fun findByUser(user:User): PanacheQuery<Task> {
        return find("userAssigned",user)
    }
}

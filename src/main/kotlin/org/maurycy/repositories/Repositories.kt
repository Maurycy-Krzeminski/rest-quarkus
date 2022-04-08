package org.maurycy.repositories

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import org.maurycy.models.Group
import org.maurycy.models.Task
import org.maurycy.models.User
import org.maurycy.models.UserInGroup
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class UserRepository:PanacheRepository<User>
@ApplicationScoped
class GroupRepository:PanacheRepository<Group>
@ApplicationScoped
class TaskRepositories:PanacheRepository<Task>
@ApplicationScoped
class UserInGroupRepository:PanacheRepository<UserInGroup>
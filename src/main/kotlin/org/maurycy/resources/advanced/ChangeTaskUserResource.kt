package org.maurycy.resources.advanced

import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.maurycy.models.requests.ChangeTaskUserRequest
import org.maurycy.repositories.TaskRepository
import org.maurycy.repositories.UserRepository
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.core.Response


@Path("/TransferTaskAssignee")
@Tag(name = "5. Transfer task from old user to new user assignee")
class ChangeTaskUserResource(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository
) {

    @PUT
    @Transactional
    fun create(@Valid changeTaskUserRequest: ChangeTaskUserRequest): Response {

        if (changeTaskUserRequest.newUserId != null) {
            if (changeTaskUserRequest.oldUserId != null) {
                val oldUser = userRepository.findById(changeTaskUserRequest.oldUserId)
                if (oldUser != null) {
                    if (oldUser.userName == null) {
                        return Response.status(404).entity("old user not found").build()
                    }
                    val newUser = userRepository.findById(changeTaskUserRequest.newUserId)
                    if (newUser != null) {
                        if (newUser.userName == null) {
                            return Response.status(404).entity("new user not found").build()
                        }
                        val tasks = taskRepository.findByUser(oldUser).list().toMutableList()

                        tasks.forEach { task ->
                            if (task.name == null) {
                                tasks.remove(task)
                            } else {
                                task.userAssignedId = newUser.id
                            }
                        }
                        if (tasks.isEmpty()) return Response.ok("not task modified").build()
                        return Response.ok(tasks).build()
                    }
                }
            }
        }
        return Response.status(404).build()
    }

}
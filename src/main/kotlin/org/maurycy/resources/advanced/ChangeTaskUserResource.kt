package org.maurycy.resources.advanced

import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.maurycy.models.requests.ChangeTaskUserRequest
import org.maurycy.repositories.TaskRepository
import org.maurycy.repositories.UserRepository
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.core.Response


@Path("/Tasks/ChangeTaskUser")
@Tag(name = "5. Change task user resource")
class ChangeTaskUserResource(
    private val userRepository: UserRepository,
    private val taskRepository: TaskRepository
) {

    @POST
    @Transactional
    fun create(@Valid changeTaskUserRequest: ChangeTaskUserRequest): Response {

        if (changeTaskUserRequest.newUserId != null) {
            if (changeTaskUserRequest.oldUserId != null) {
                val oldUser = userRepository.findById(changeTaskUserRequest.oldUserId)
                if (oldUser != null) {
                    val newUser = userRepository.findById(changeTaskUserRequest.newUserId)
                    if(newUser!=null){
                        val tasks = taskRepository.findByUser(oldUser)
                        tasks.list().forEach {
                            task ->
                            task.userAssigned?.id = newUser.id
                            task.userAssigned?.userName = newUser.userName
                            task.userAssigned?.email = newUser.email
                            task.userAssigned?.userStatus = newUser.userStatus
                            task.userAssigned?.password = newUser.password
                        }
                        return Response.ok(tasks).build()
                    }
                }
            }
        }
        return Response.noContent().build()
    }

}
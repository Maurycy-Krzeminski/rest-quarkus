package org.maurycy.resources.basic

import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import org.maurycy.models.Task
import org.maurycy.models.requests.TaskRequest
import org.maurycy.repositories.GroupRepository
import org.maurycy.repositories.TaskRepository
import org.maurycy.repositories.UserRepository
import org.maurycy.utils.PageRequest
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.BeanParam
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.HeaderParam
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/Tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class TaskResource(
    private val taskRepository: TaskRepository,
    private val groupRepository: GroupRepository,
    private val userRepository: UserRepository
) {
    @GET
    fun getAll(@BeanParam pageRequest: PageRequest): Response {
        val all = taskRepository.findAll(Sort.by("id"))
        val list = all.page(Page.of(pageRequest.pageNum, pageRequest.pageSize))
            .list()
        val count = all.count()
        val response = Response.ok(list).header("count", count)

        return response.build()
    }

    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") id:Long): Response? {
        val task = taskRepository.findById(id) ?: return Response.noContent().build()
        return Response.ok(task).tag(task.hashCode().toString()).build()
    }

    @POST
    @Transactional
    fun create(): Response {
        val task = Task()
        taskRepository.persist(task)
        return Response.ok(task).build()
    }

    @PUT
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id: Long, @Valid taskRequest: TaskRequest, @HeaderParam("etag") eTag: String): Response {
        val task = taskRepository.findById(id)
        val userCreator = userRepository.findById(taskRequest.userCreator)
        val userAssigned = userRepository.findById(taskRequest.userAssigned)
        val group = groupRepository.findById(taskRequest.group)
        if (task != null) {
            if(eTag!=task.hashCode().toString()) return Response.notModified(task.hashCode().toString()).build()
            if (userAssigned != null) {
                if (group != null) {
                    if (userCreator != null) {
                        task.name = taskRequest.name
                        task.description = taskRequest.description
                        task.status = taskRequest.status
                        task.group = group
                        task.userCreator = userCreator
                        task.userAssigned = userAssigned
                        return Response.ok(task).build()
                    }
                }
            }
        }
        return Response.noContent().build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id: Long): Response {
        taskRepository.deleteById(id)
        return Response.noContent().build()
    }
}
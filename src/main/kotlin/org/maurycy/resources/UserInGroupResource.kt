package org.maurycy.resources

import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import org.maurycy.PageRequest
import org.maurycy.models.UserInGroup
import org.maurycy.models.requests.UserInGroupRequest
import org.maurycy.repositories.GroupRepository
import org.maurycy.repositories.UserInGroupRepository
import org.maurycy.repositories.UserRepository
import javax.transaction.Transactional
import javax.validation.Valid
import javax.ws.rs.BeanParam
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/UserInGroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UserInGroupResource(
    private val userInGroupRepository: UserInGroupRepository,
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) {
    @GET
    fun getAll(@BeanParam pageRequest: PageRequest): Response {
        val all = userInGroupRepository.findAll(Sort.by("id"))
        val list = all.page(Page.of(pageRequest.pageNum, pageRequest.pageSize))
            .list()
        val count = all.count()

        val response = Response.ok(list).header("count", count)

        return response.build()
    }

    @POST
    @Transactional
    fun create(): Response {
        val userInGroup = UserInGroup()
        userInGroupRepository.persist(userInGroup)
        return Response.ok(userInGroup).build()
    }

    @PUT
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id: Long, @Valid userInGroupRequest: UserInGroupRequest): Response {
        val userInGroup = userInGroupRepository.findById(id)

        if (userInGroup != null) {
            val user = userRepository.findById(userInGroupRequest.userId)
            val group = groupRepository.findById(userInGroupRequest.groupId)
            if (user != null) {
                if (group != null) {
                    userInGroup.userId = user
                    userInGroup.groupId = group
                    return Response.ok(userInGroup).build()
                }
            }
        }
        return Response.noContent().build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id: Long): Response {
        userInGroupRepository.deleteById(id)
        return Response.noContent().build()
    }

}
package org.maurycy.resources.advanced

import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.maurycy.models.User
import org.maurycy.models.UserInGroup
import org.maurycy.repositories.GroupRepository
import org.maurycy.repositories.UserInGroupRepository
import org.maurycy.repositories.UserRepository
import org.maurycy.utils.PageRequest
import javax.transaction.Transactional
import javax.ws.rs.BeanParam
import javax.ws.rs.Consumes
import javax.ws.rs.DELETE
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

@Path("/groups/{gid}/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "4. User in group resource")
class UserInGroupResource(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
    private val userInGroupRepository: UserInGroupRepository
) {

    @Context
    lateinit var uriInfo: UriInfo

    @GET
    fun getAll(
        @BeanParam pageRequest: PageRequest,
        @PathParam("gid") gid: Long,
    ): Response {
        val group = groupRepository.findById(gid) ?: return Response.status(404).build()
        val usersInGroup = userInGroupRepository.findByGroup(group).list()
        val users = mutableListOf<User>()
        usersInGroup.forEach {
            if (it.user != null) {
                users.add(it.user!!)
            }
        }

        return Response.ok(users).build()
    }

    @GET
    @Path("/{uid}")
    fun getById(
        @PathParam("gid") gid: Long,
        @PathParam("uid") uid: Long
    ): Response? {
        val user = userRepository.findById(uid) ?: return Response.status(404).build()
        val group = groupRepository.findById(gid) ?: return Response.status(404).build()

        val checkList = userInGroupRepository.findByGroup(group).list()
        checkList.forEach {
            val uri = uriInfo.absolutePathBuilder.path(it.user?.id.toString()).build()
            if (it.user == user) return Response.created(uri).entity(it).build()
        }
        return Response.status(404).build()
    }

    @POST
    @Transactional
    fun create(
        @PathParam("gid") gid: Long,
        uid: Long
    ): Response {
        val user = userRepository.findById(uid) ?: return Response.status(404).build()
        val group = groupRepository.findById(gid) ?: return Response.status(404).build()

        val checkList = userInGroupRepository.findByGroup(group).list()

        checkList.forEach {
            val uri = uriInfo.absolutePathBuilder.path(it.user?.id.toString()).build()
            if (it.user == user) return Response.created(uri).entity(it).build()
        }


        val userInGroup = UserInGroup()
        userInGroup.group = group
        userInGroup.user = user
        userInGroupRepository.persist(userInGroup)

        val uri = uriInfo.absolutePathBuilder.path(userInGroup.id.toString()).build()
        return Response.created(uri).entity(userInGroup).build()
    }

    @DELETE
    @Transactional
    @Path("/{uid}")
    fun delete(
        @PathParam("gid") gid: Long,
        @PathParam("uid") uid: Long
    ): Response {
        val user = userRepository.findById(uid) ?: return Response.status(404).build()
        val group = groupRepository.findById(gid) ?: return Response.status(404).build()

        val checkList = userInGroupRepository.findByGroup(group).list()

        checkList.forEach {
            if (it.user == user) {
                userInGroupRepository.delete(it)
                return Response.noContent().build()
            }
        }
        return Response.noContent().build()
    }

}
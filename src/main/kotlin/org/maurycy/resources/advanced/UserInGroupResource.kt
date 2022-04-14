package org.maurycy.resources.advanced

import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import org.maurycy.utils.PageRequest
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
import javax.ws.rs.HeaderParam
import javax.ws.rs.POST
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

@Path("/UserInGroups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UserInGroupResource(
    private val userInGroupRepository: UserInGroupRepository,
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) {
    val PAGE_NUM = "pageNum"

    @Context
    lateinit var uriInfo: UriInfo

    @GET
    fun getAll(@BeanParam pageRequest: PageRequest): Response {
        val all = userInGroupRepository.findAll(Sort.by("id"))
        val list = all.page(Page.of(pageRequest.pageNum, pageRequest.pageSize))
            .list()
        val count = all.count().toInt()
        var pagesCount = count / pageRequest.pageSize
        if (count % pageRequest.pageSize != 0) {
            pagesCount += 1
        }
        if (list.isEmpty()) {
            return Response.noContent().build()
        }

        val response = Response.ok(list).header("X-count", count).header("X-pages", pagesCount)
        val page = pageRequest.pageNum
        val beforeUri = uriInfo.requestUriBuilder.replaceQueryParam(PAGE_NUM, page - 1).build()
        val nextUri = uriInfo.requestUriBuilder.replaceQueryParam(PAGE_NUM, page + 1).build()
        if (page + 1 < pagesCount) {
            response.header("X-next-page", nextUri)
        }
        if (page - 1 >= 0) {
            response.header("X-previous-page", beforeUri)
        }
        return response.build()
    }
    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") id:Long): Response? {
        val userInGroup = userInGroupRepository.findById(id) ?: return Response.status(404).build()
        return Response.ok(userInGroup).tag(userInGroup.hashCode().toString()).build()
    }

    @POST
    @Transactional
    fun create(): Response {
        val userInGroup = UserInGroup()
        userInGroupRepository.persist(userInGroup)
        val uri = uriInfo.absolutePathBuilder.path(userInGroup.id.toString()).build()
        return Response.created(uri).entity(userInGroup).build()
    }

    @PUT
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id: Long, @Valid userInGroupRequest: UserInGroupRequest,  @HeaderParam("etag") eTag: String): Response {
        val userInGroup = userInGroupRepository.findById(id)

        if (userInGroup != null) {
            if(eTag!=userInGroup.hashCode().toString()) return Response.notModified(userInGroup.hashCode().toString()).build()
            val user = userRepository.findById(userInGroupRequest.userId)
            val group = groupRepository.findById(userInGroupRequest.groupId)
            if (user != null) {
                if (group != null) {
                    userInGroup.userId = user.id
                    userInGroup.groupId = group.id
                    return Response.ok(userInGroup).build()
                }
            }
        }
        return Response.status(404).build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id: Long): Response {
        userInGroupRepository.deleteById(id)
        return Response.noContent().build()
    }

}
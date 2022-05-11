package org.maurycy.resources.basic

import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.maurycy.models.User
import org.maurycy.models.requests.UserRequest
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
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo

@Path("/Users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "1. User resource")
class UserResource(
    private val userRepository: UserRepository
) {
    @Suppress("PropertyName")
    val PAGE_NUM = "pageNum"

    @Context
    lateinit var uriInfo: UriInfo

    @Suppress("ControlFlowWithEmptyBody")
    @GET
    fun getAll(@BeanParam pageRequest: PageRequest): Response {
        val all = userRepository.findAll(Sort.by("id"))
        val allCount = all.list().toMutableList()
        val list = all.page(Page.of(pageRequest.pageNum, pageRequest.pageSize))
            .list().toMutableList()
        while (list.removeIf{
            it.email==null
        }){}
        while (allCount.removeIf{
                it.email==null
            }){}

        val count = allCount.count()
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
    fun getById(@PathParam("id") id: Long): Response? {
        val user = userRepository.findById(id) ?: return Response.status(404).build()
        if(user.email==null)return Response.status(404).build()
        return Response.ok(user).tag(user.hashCode().toString()).build()
    }

    @POST
    @Transactional
    fun create(): Response {
        val user = User()
        userRepository.persist(user)
        val uri = uriInfo.absolutePathBuilder.path(user.id.toString()).build()
        return Response.created(uri).entity(user).build()
    }

    @PUT
    @Transactional
    @Path("/{id}")
    fun update(
        @PathParam("id") id: Int,
        @Valid userRequest: UserRequest,
        @HeaderParam("etag") eTag: String
    ): Response {
        val user = userRepository.findById(id.toLong())

        if (user != null) {
            if (eTag != user.hashCode().toString()) return Response.notModified(user.hashCode().toString()).build()
            user.userName = userRequest.userName
            user.userStatus = userRequest.userStatus
            user.email = userRequest.email
            user.password = userRequest.password
            return Response.ok(user).build()
        }
        return Response.status(404).build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id: Long): Response {
        userRepository.deleteById(id)
        return Response.noContent().build()
    }


}
package org.maurycy.resources.basic

import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.maurycy.utils.PageRequest
import org.maurycy.models.Group
import org.maurycy.models.requests.GroupRequest
import org.maurycy.repositories.GroupRepository
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

@Path("/Groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "2. Group resource")
class GroupResource(
    private val groupRepository: GroupRepository
) {
    val PAGE_NUM = "pageNum"

    @Context
    lateinit var uriInfo: UriInfo
    @GET
    fun getAll(@BeanParam pageRequest: PageRequest): Response {
        val all = groupRepository.findAll(Sort.by("id"))
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
        val group = groupRepository.findById(id) ?: return Response.status(404).build()
        return Response.ok(group).tag(group.hashCode().toString()).build()
    }

    @POST
    @Transactional
    fun create(): Response {
        val group = Group()
        groupRepository.persist(group)
        val uri = uriInfo.absolutePathBuilder.path(group.id.toString()).build()
        return Response.created(uri).entity(group).build()
    }

    @PUT
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id:Long, @Valid groupRequest: GroupRequest, @HeaderParam("etag") eTag: String): Response {
        val group = groupRepository.findById(id)

        if (group != null) {
            if(eTag!=group.hashCode().toString()) return Response.notModified(group.hashCode().toString()).build()
            group.name = groupRequest.name
            group.description = groupRequest.description
            return Response.ok(group).build()
        }
        return Response.noContent().build()
    }
    @DELETE
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id:Long):Response{
        groupRepository.deleteById(id)
        return Response.noContent().build()
    }
}
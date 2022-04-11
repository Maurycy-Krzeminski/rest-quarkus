package org.maurycy.resources

import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
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
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/Groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class GroupResource(
    private val groupRepository: GroupRepository
) {
    @GET
    fun getAll(@BeanParam pageRequest: PageRequest): Response {
        val all = groupRepository.findAll(Sort.by("id"))
        val list = all.page(Page.of(pageRequest.pageNum, pageRequest.pageSize))
            .list()
        val count = all.count()

        val response = Response.ok(list).header("count",count)

        return response.build()
    }

    @GET
    @Path("/{id}")
    fun getById(@PathParam("id") id:Long): Response? {
        val group = groupRepository.findById(id) ?: return Response.noContent().build()
        return Response.ok(group).tag(group.hashCode().toString()).build()
    }

    @POST
    @Transactional
    fun create(): Response {
        val group = Group()
        groupRepository.persist(group)
        return Response.ok(group).build()
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
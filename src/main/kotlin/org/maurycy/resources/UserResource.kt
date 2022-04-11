package org.maurycy.resources

import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import org.maurycy.utils.PageRequest
import org.maurycy.models.User
import org.maurycy.models.requests.UserRequest
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
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/Users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
class UserResource(
    private val userRepository: UserRepository
) {

    @GET
    fun getAll(@BeanParam pageRequest: PageRequest):Response{
        val all = userRepository.findAll(Sort.by("id"))
        val list = all.page(Page.of(pageRequest.pageNum, pageRequest.pageSize))
            .list()
        val count = all.count()

        val response =Response.ok(list).header("count",count)

        return response.build()
    }
    @POST
    @Transactional
    fun create():Response{
        val user = User()
        userRepository.persist(user)
        return Response.ok(user).build()
    }

    @PUT
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id:Long, @Valid userRequest: UserRequest,  @HeaderParam("etag") eTag: String):Response{
        val user = userRepository.findById(id)

        if (user != null) {
            if(eTag!=user.hashCode().toString()) return Response.notModified(user.hashCode().toString()).build()
            user.userName=userRequest.userName
            user.userStatus=userRequest.userStatus
            user.email=userRequest.email
            user.password=userRequest.password
            println("works")
            return Response.ok().build()
        }
        return Response.noContent().build()
    }

    @DELETE
    @Transactional
    @Path("/{id}")
    fun update(@PathParam("id") id:Long):Response{

        userRepository.deleteById(id)
        return Response.noContent().build()

    }
}
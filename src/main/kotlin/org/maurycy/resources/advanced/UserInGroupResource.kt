package org.maurycy.resources.advanced

import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.maurycy.repositories.GroupRepository
import org.maurycy.repositories.UserRepository
import javax.ws.rs.Consumes
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.UriInfo

@Path("/groups/{gid}/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "4. User in group resource")
class UserInGroupResource(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository
) {
    val PAGE_NUM = "pageNum"

    @Context
    lateinit var uriInfo: UriInfo


}
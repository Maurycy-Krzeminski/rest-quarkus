package org.maurycy

import javax.ws.rs.DefaultValue
import javax.ws.rs.QueryParam

class PageRequest {
    @QueryParam("pageNum")
    @DefaultValue("0")
    val pageNum = 0

    @QueryParam("pageSize")
    @DefaultValue("10")
    val pageSize = 0

}
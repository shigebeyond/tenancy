package net.jkcode.jkmvc.tenancy.resolver

import net.jkcode.jkmvc.http.HttpRequest

/**
 * 基于请求头的租户解析器
 */
class HeaderTenantResolver: ITenantResolver
{
    /**
     * 从请求头中解析租户id
     */
    public override fun resolveTenantId(req: HttpRequest): String?{
        return req.getHeader(ITenantResolver.parameterName)
    }
}

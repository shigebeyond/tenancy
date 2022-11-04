package net.jkcode.jkmvc.tenancy.resolver

import net.jkcode.jkmvc.http.HttpRequest

/**
 * 基于uri(路由参数)的租户解析器
 */
class PathTenantResolver: ITenantResolver
{
    /**
     * 从uri(路由参数)中解析租户id
     */
    public override fun resolveTenantId(req: HttpRequest): String?{
        return req.routeParams.getOrDefault(ITenantResolver.parameterName, null)
    }
}

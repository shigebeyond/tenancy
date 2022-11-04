package net.jkcode.jkmvc.tenancy.resolver

import net.jkcode.jkmvc.http.HttpRequest

/**
 * 基于请求参数的租户解析器
 */
class ParamTenantResolver: ITenantResolver
{
    /**
     * 从请求参数中解析租户id
     */
    public override fun resolveTenantId(req: HttpRequest): String?{
        return req.getParameter(ITenantResolver.parameterName)
    }
}

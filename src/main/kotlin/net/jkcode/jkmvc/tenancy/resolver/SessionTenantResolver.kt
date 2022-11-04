package net.jkcode.jkmvc.tenancy.resolver

import net.jkcode.jkmvc.http.HttpRequest

/**
 * 基于session的租户解析器
 */
class SessionTenantResolver: ITenantResolver
{
    /**
     * 从session中解析租户id
     */
    public override fun resolveTenantId(req: HttpRequest): String?{
        return req.session.getAttribute(ITenantResolver.parameterName) as String?
    }
}

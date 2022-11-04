package net.jkcode.jkmvc.tenancy.resolver

import net.jkcode.jkmvc.http.HttpRequest

/**
 * 基于域名的租户解析器
 */
class DomainTenantResolver: ITenantResolver
{
    /**
     * 从域名中解析租户id
     */
    public override fun resolveTenantId(req: HttpRequest): String?{
        // 租户标识 = 第二级域名
        val parts = req.serverName.split('.', limit = 2)
        if(parts.size == 1) // 只有一级
            return null

        return parts[0]
    }
}

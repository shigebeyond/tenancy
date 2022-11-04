package net.jkcode.jkmvc.tenancy.resolver

import net.jkcode.jkmvc.http.HttpRequest
import net.jkcode.jkmvc.tenancy.TenantModel
import net.jkcode.jkutil.cache.ICache
import net.jkcode.jkutil.common.Config
import net.jkcode.jkutil.common.IConfig
import net.jkcode.jkutil.singleton.NamedConfiguredSingletons

/**
 * 租户解析器
 */
interface ITenantResolver
{
    // 可配置的单例
    companion object: NamedConfiguredSingletons<ITenantResolver>() {
        /**
         * 单例类的配置，内容是哈希 <单例名 to 单例类>
         */
        public override val instsConfig: IConfig = Config.instance("tenant-resolver", "yaml")

        /**
         * 租户应用的配置
         */
        public val config: IConfig = Config.instance("tenant", "yaml")

        /**
         * 解析器中用到的参数名
         */
        public val parameterName: String = config["resolverParameterName"]!!
    }


    /**
     * 通过请求信息来解析租户id
     */
    public fun resolveTenantId(req: HttpRequest): String?

    /**
     * 通过请求信息来解析租户
     */
    public fun resolveTenant(req: HttpRequest): TenantModel?{
        val tenantId = resolveTenantId(req)
        if(tenantId.isNullOrBlank())
            return null

        return TenantModel.findByPk(tenantId) ?: throw Exception("未找到租户: $tenantId")
    }

}

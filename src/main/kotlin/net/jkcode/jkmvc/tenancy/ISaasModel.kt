package net.jkcode.jkmvc.tenancy

import net.jkcode.jkmvc.orm.IOrm

/**
 * saas多租户下的模型基类
 */
interface ISaasModel : IOrm {

    /**
     * 获得租户id
     */
    val tenantId: Int
        get() = this.get("tenantId")

    /**
     * create前置处理
     */
    override fun beforeCreate() {
        // 添加租户id
        val tenantId = TenantContext.current().id
        this.set("tenantId", tenantId)
    }
}

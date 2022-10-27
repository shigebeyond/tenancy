package net.jkcode.jkmvc.tenancy

import net.jkcode.jkutil.ttl.HttpRequestScopedTransferableThreadLocal
import net.jkcode.jkutil.ttl.SttlCurrentHolder

/**
 * 租户上下文
 */

object TenantContext : SttlCurrentHolder<TenantModel>(HttpRequestScopedTransferableThreadLocal()) // http请求域的可传递的 ThreadLocal
{
    /**
     * 设置当前租户
     */
    fun setCurrentById(id: String) {
        val talent = TenantModel.findByPk<TenantModel>(id) ?: throw Exception("未找到租户: $id")
        setCurrent(talent)
    }
}
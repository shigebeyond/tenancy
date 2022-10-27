package net.jkcode.jkmvc.tenancy

import net.jkcode.jkmvc.http.controller.Controller

/**
 * saas多租户下控制器基类
 *   不要扩展JkFilter的前置，而是要扩展controller的前置，因为TenantContext的请求级作用域要在JkFilter中处理
 */
abstract class ISaasController : Controller() {

    /**
     * 前置处理
     *   识别租户标识
     */
    override fun before() {
        // 租户标识 = 第三级域名
        val talentId = req.serverName.substringBefore('.')
        // 设为当前租户
        TenantContext.setCurrentById(talentId)
    }

}

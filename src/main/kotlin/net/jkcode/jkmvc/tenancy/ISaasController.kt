package net.jkcode.jkmvc.tenancy

import net.jkcode.jkmvc.http.controller.Controller

/**
 * saas多租户下控制器基类
 *   不要扩展JkFilter的前置，而是要扩展controller的前置，因为TenantContext的请求级作用域要在JkFilter中处理
 */
abstract class ISaasController : Controller() {

    /**
     * 前置处理
     *   识别当前租户标识, 并将租户信息写到请求属性中
     *   通过 TenantModel.current() 来获得请求属性中的当前租户
     */
    override fun before() {
        // 租户标识 = 第二级域名
        val talentId = req.serverName.substringBefore('.')
        // 设为当前租户
        val talent = TenantModel.findByPk<TenantModel>(talentId) ?: throw Exception("未找到租户: $talentId")
        req.setAttribute("tenant", talent)
    }


    /**
     * 当前租户域名下的跳转
     */
    fun tenantRedirect(uri: String){
        val turl = TenantModel.current().buildTenantUrl(uri)
        redirect(turl)
    }

}


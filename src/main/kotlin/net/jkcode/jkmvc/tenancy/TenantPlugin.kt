package net.jkcode.jkmvc.tenancy

import net.jkcode.jkmvc.http.handler.HttpRequestHandler
import net.jkcode.jkutil.common.IPlugin

/**
 * 多租户的插件
 * @author shijianhang<772910474@qq.com>
 * @date 2022-10-09 5:02 PM
 */
class TenantPlugin: IPlugin {

    /**
     * 初始化
     */
    override fun doStart() {
        // http server端扩展
        // 添加拦截器
        (HttpRequestHandler.interceptors as MutableList).add(HttpServerTenantInterceptor())
    }

    /**
     * 关闭
     */
    override fun close() {
    }
}
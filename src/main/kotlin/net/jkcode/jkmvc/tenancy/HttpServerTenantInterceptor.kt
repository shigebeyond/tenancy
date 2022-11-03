package net.jkcode.jkmvc.tenancy

import net.jkcode.jkutil.common.trySupplierFuture
import net.jkcode.jkmvc.http.HttpRequest
import net.jkcode.jkmvc.http.IHttpRequestInterceptor
import org.slf4j.MDC
import java.util.concurrent.CompletableFuture

/**
 * 服务端的http请求拦截器
 *   识别租户id
 *
 * @author shijianhang<772910474@qq.com>
 * @date 2022-10-08 2:53 PM
 */
class HttpServerTenantInterceptor: IHttpRequestInterceptor {

    /**
     * 前置处理
     *   1 识别当前租户标识, 并将租户信息写到请求属性中
     *   通过 TenantModel.current() 来获得请求属性中的当前租户
     *   2 切换日志：添加日志变量
     *   由于不同租户写不同的日志，因此只能通过拦截器扩展，不能通过controller扩展, 不然就太晚了, 路由/db等日志早在确认租户之前就打了
     *   3 切换上传子目录
     *
     * @param req
     * @param action 被拦截的处理
     * @return
     */
    public override fun intercept(req: HttpRequest, action: () -> Any?): CompletableFuture<Any?> {
        // 1 前置处理
        // 租户标识 = 第二级域名
        val parts = req.serverName.split('.', limit = 2)
        val hasTenantId = parts.size > 1
        if(hasTenantId) {
            val tenantId = parts[0]
            // 1.1 设为当前租户
            val tenant = TenantModel.findByPk<TenantModel>(tenantId) ?: throw Exception("未找到租户: $tenantId")
            req.setAttribute("tenant", tenant)

            // 1.2 添加日志变量
            MDC.put("tenant", tenantId)

            // 1.3 修改上传子目录
            req.uploadSubDir = tenantId
        }

        // 2 转future
        val future = trySupplierFuture(action)

        // 3 后置处理
        return future.whenComplete{ r, ex ->
            // 清楚日志变量
            if(hasTenantId)
                MDC.remove("tenant")
        }
    }

}
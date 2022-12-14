package net.jkcode.jkmvc.tenancy

import net.jkcode.jkmvc.db.IDb
import net.jkcode.jkmvc.http.HttpRequest
import net.jkcode.jkmvc.orm.Orm
import net.jkcode.jkmvc.orm.OrmCacheMeta
import net.jkcode.jkmvc.orm.OrmMeta

/**
 * 租户模型
 */
class TenantModel(id:String? = null): Orm(id), ITenant {

    // 伴随对象就是元数据
    companion object m: OrmMeta(TenantModel::class, "租户模型", "tenant", "id", OrmCacheMeta("lru") /* 有缓存 */){

        /**
         * 获得当前租户
         */
        @JvmStatic
        fun current(): TenantModel {
            return HttpRequest.current().getAttribute("tenant") as TenantModel
        }

        /**
         * 构建当前租户下的url
         */
        @JvmStatic
        @JvmOverloads
        public fun buildRootUrl(path: String = ""): String {
            val req = HttpRequest.current()
            val server = req.serverName
            val rootDomain = server.substringAfter('.')
            return "http://$rootDomain:${req.serverPort}/$path"
        }

    }

    // 代理属性读写
    override var id:String by property(); // 租户标识

    override var name:String by property(); // 租户名

    /**
     * 构建租户下的url
     */
    override fun buildTenantUrl(path: String): String {
        val req = HttpRequest.current()
        val server = req.serverName
        val rootDomain = server.substringAfter('.')
        return "http://${this.id}.$rootDomain:${req.serverPort}/$path"
    }

}
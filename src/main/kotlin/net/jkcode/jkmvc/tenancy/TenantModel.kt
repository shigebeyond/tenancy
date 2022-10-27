package net.jkcode.jkmvc.tenancy

import net.jkcode.jkmvc.http.HttpRequest
import net.jkcode.jkmvc.orm.Orm
import net.jkcode.jkmvc.orm.OrmCacheMeta
import net.jkcode.jkmvc.orm.OrmMeta

/**
 * 租户模型
 */
class TenantModel(id:String? = null): Orm(id) {

    // 伴随对象就是元数据
    companion object m: OrmMeta(TenantModel::class, "租户模型", "tenant", "id")

    // 代理属性读写
    public var id:String by property(); // 租户标识

    public var name:String by property(); // 租户名

    /**
     * 新建租户后置处理
     *   如创建db
     */
    override fun afterCreate() {

    }

    /**
     * 新建租户后置处理
     *   如删除db
     */
    override fun afterDelete() {

    }

    /**
     * 构建租户下的url
     */
    public fun buildTenantUrl(path: String): String {
        val req = HttpRequest.current()
        val server = req.serverName
        val rootDomain = server.substringAfter('.')
        return "http://${this.id}.$rootDomain:${req.serverPort}/$path"
    }
}
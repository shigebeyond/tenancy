package net.jkcode.jkmvc.tenancy

import net.jkcode.jkmvc.db.IDb

/**
 * 租户接口
 */
interface ITenant {

    var id: String

    var name: String

    /**
     * 构建租户下的url
     */
    fun buildTenantUrl(path: String): String

    /**
     * 获得当前租户对应的动态db名
     *   应对不同租户使用不同db, 譬如100个租户分配给3个db, 一个租户的数据都存在同一个db中
     *   不能使用sharding-jdbc, 因为他的分库策略都要落到具体表中，而多租户有不能兼容的特性: 第一是表很多，第二是有动态表(表名是不确定的, 是租户自定义的)
     *   自行实现: 如直接新建一个存储字段来返回, 如按租户id首字母范围分3段对应3个db, 如hash(租户id)%3
     */
    fun dynDbName(): String{
        TODO("Not yet implemented")
    }
}
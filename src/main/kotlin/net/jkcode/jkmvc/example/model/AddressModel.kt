package net.jkcode.jkmvc.example.model

import net.jkcode.jkmvc.orm.Orm
import net.jkcode.jkmvc.orm.OrmMeta
import net.jkcode.jkmvc.tenancy.ISaasModel
import net.jkcode.jkmvc.tenancy.SaasOrmMeta

/**
 * 地址模型
 */
class AddressModel(id:Int? = null): Orm(id), ISaasModel {
    // 伴随对象就是元数据
    companion object m: SaasOrmMeta(AddressModel::class)

    // 代理属性读写
    public var id:Int by property();

    public var userId:Int by property();

    public var addr:String by property();

    public var tel:String by property();

    public var isHome:Int by property();

}
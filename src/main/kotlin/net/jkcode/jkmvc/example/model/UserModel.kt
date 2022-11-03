package net.jkcode.jkmvc.example.model 

import net.jkcode.jkmvc.orm.Orm
import net.jkcode.jkmvc.tenancy.ISaasModel
import net.jkcode.jkmvc.tenancy.SaasOrmMeta

/**
 * 用户模型
 */
class UserModel(id:Int? = null): Orm(id), ISaasModel {
	// 伴随对象就是元数据
	companion object m: SaasOrmMeta(UserModel::class, "用户模型", "user", "id")

	// 代理属性读写
	/**
	 * 用户编号
	 */
	public var id:Int by property()

	/**
	 * 用户名
	 */
	public var username:String by property()

	/**
	 * 密码
	 */
	public var password:String by property()

	/**
	 * 中文名
	 */
	public var name:String by property()

	/**
	 * 年龄
	 */
	public var age:Int by property()

	/**
	 * 头像
	 */
	public var avatar:String by property()

}
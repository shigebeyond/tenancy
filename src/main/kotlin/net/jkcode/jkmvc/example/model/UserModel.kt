package net.jkcode.jkmvc.example.model 

import net.jkcode.jkmvc.orm.Orm
import net.jkcode.jkmvc.tenancy.ISaasModel
import net.jkcode.jkmvc.tenancy.SaasOrmMeta

/**
 * 用户模型
 */
class UserModel(id:Int? = null): Orm(id), ISaasModel {
	// 伴随对象就是元数据
	companion object m: SaasOrmMeta(UserModel::class, "用户模型", "user", "id"){
		init {
			hasOne("home", AddressModel::class){ query, lazy ->
				if(lazy)
					query.where("is_home", 1)
				else
					query.on("is_home", 1, false)
			}
			hasMany("addresses", AddressModel::class)
		}
	}

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

	/**
	 * 关联地址：一个用户有一个家庭地址
	 */
	public var home: AddressModel? by property();

	/**
	 * 关联地址：一个用户有多个地址
	 */
	public var addresses:List<AddressModel> by listProperty();

}
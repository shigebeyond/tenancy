[GitHub](https://github.com/shigebeyond/tenancy) | [Gitee](https://gitee.com/shigebeyond/tenancy) 

基于 [jkmvc](https://github.com/shigebeyond/jkmvc)框架的多租户实现

## 租户id识别
直接通过二级域名来识别，实现看 `ISaasController`
```
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

}
```

而`TenantModel.current()`可以获得当前的租户
```
/**
 * 获得当前租户
 */
@JvmStatic
fun current(): TenantModel {
    return HttpRequest.current().getAttribute("tenant") as TenantModel
}
```

## 多租户数据隔离
### 思路
多租户在实现上主要有三种方式：
1. 独立数据库
2. 同一数据库，不同表
3. 同一数据库，同一张表，通过字段（租户id）来区分

本项目的实现是第3种

### 实现
1. model父类 ISaasModel -- 插入时写入租户id
```
interface ISaasModel : IOrm {

    /**
     * 获得租户id
     */
    val tenantId: Int
        get() = this.get("tenantId")

    /**
     * create前置处理
     */
    override fun beforeCreate() {
        // 添加租户id
        val tenantId = TenantModel.current().id
        this.set("tenantId", tenantId)
    }
}
```

2. model元数据类 SaasOrmMeta -- 查询时过滤租户id

```
open class SaasOrmMeta{
    ......
    
    /**
     * 简化实现, 对常规查询不预编译sql
     */
    override val precompileSql: Boolean = false

    /**
     * 查询时过滤租户id
     */
    override fun queryBuilder(convertingValue: Boolean, convertingColumn: Boolean, withSelect: Boolean, reused: Boolean): OrmQueryBuilder {
        val query = super.queryBuilder(convertingValue, convertingColumn, withSelect, reused)
        // 过滤租户id
        val tenantId = TenantModel.current().id
        query.where("tenant_id", tenantId)
        return query
    }

}
```

3. demo

```
/**
 * 用户模型
 */
class UserModel(id:Int? = null): Orm(id), ISaasModel {
	// 伴随对象就是元数据
	companion object m: SaasOrmMeta(UserModel::class, "用户模型", "user", "id"){}

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
```
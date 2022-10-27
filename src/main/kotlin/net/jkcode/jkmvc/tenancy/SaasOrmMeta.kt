package net.jkcode.jkmvc.tenancy

import net.jkcode.jkmvc.orm.*
import kotlin.reflect.KClass

/**
 * saas多租户下数据模型的元数据
 *
 * @author shijianhang<772910474@qq.com>
 * @date 2022-10-17 3:38 PM
 */
open class SaasOrmMeta(
        model: KClass<out IOrm>, // 模型类
        label: String = model.modelName, // 模型中文名
        table: String = model.modelName, // 表名，假定model类名, 都是以"Model"作为后缀
        primaryKey: DbKeyNames = DbKeyNames("id"), // 主键
        cacheMeta: OrmCacheMeta? = null, // 缓存配置
        dbName: String = "default", // 数据库名
        pkEmptyRule: PkEmptyRule = PkEmptyRule.default, // 检查主键为空的规则
        checkingTablePrimaryKey: Boolean = true // 是否检查表与主键是否存在
): OrmMeta(model, label, table, primaryKey, cacheMeta, dbName, pkEmptyRule, checkingTablePrimaryKey){

    public constructor(
            model: KClass<out IOrm>, // 模型类
            label: String, // 模型中文名
            table: String, // 表名，假定model类名, 都是以"Model"作为后缀
            primaryKey: String, // 主键
            cacheMeta: OrmCacheMeta? = null, // 缓存配置
            dbName: String = "default", // 数据库名
            pkEmptyRule: PkEmptyRule = PkEmptyRule.default, // 检查主键为空的规则
            checkingTablePrimaryKey: Boolean = true // 是否检查表与主键是否存在
    ) : this(model, label, table, DbKeyNames(primaryKey), cacheMeta, dbName, pkEmptyRule, checkingTablePrimaryKey)

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
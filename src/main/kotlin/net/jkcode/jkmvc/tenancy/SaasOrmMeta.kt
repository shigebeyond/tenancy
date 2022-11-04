package net.jkcode.jkmvc.tenancy

import net.jkcode.jkmvc.db.Db
import net.jkcode.jkmvc.db.IDb
import net.jkcode.jkmvc.orm.*
import net.jkcode.jkmvc.orm.relation.IRelation
import net.jkcode.jkmvc.query.DbExpr
import net.jkcode.jkutil.common.Config
import net.jkcode.jkutil.common.IConfig
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

    companion object{
        /**
         * 租户应用的配置
         */
        public val config: IConfig = Config.instance("tenant", "yaml")

        /**
         * 是否使用动态db, 即不同租户使用不同db, 详见 ITenant.dynDbName() 实现
         */
        public val dynDb: Boolean = config["dynDb"]!!
    }

    /**
     * 简化实现, 对常规查询不预编译sql
     */
    override val precompileSql: Boolean = false

    /**
     * 兼容多租户的动态db
     */
    override val db: IDb
        get(){
            var dbName2: String
            if(dynDb) // 动态db
                dbName2 = TenantModel.current().dynDbName()
            else // 静态db
                dbName2 = this.dbName
            return Db.instance(dbName2)
        }

    /**
     * 获得缓存的key
     *   加上租户id作为后缀
     * @param pk
     * @return
     */
    protected override fun getCacheKey(pk: DbKeyValues): String {
        val tenantId = TenantModel.current().id
        return pk.columns.joinToString("_", "orm:$dbName:$name:$tenantId")
    }

    /**
     * 查询时过滤租户id
     */
    override fun queryBuilder(convertingValue: Boolean, convertingColumn: Boolean, withSelect: Boolean): OrmQueryBuilder {
        val query = super.queryBuilder(convertingValue, convertingColumn, withSelect)
        // 过滤租户id
        val tenantId = TenantModel.current().id
        query.where("$name.tenant_id", tenantId)
        return query
    }

    /**
     * 添加关联关系
     * @param name
     * @param relation
     * @return
     */
    public override fun addRelation(name: String, relation: IRelation): OrmMeta {
        // 如果关联模型也是saas模型
        if(relation.ormMeta is SaasOrmMeta){
            relation.conditions.addQueryAction { query, lazy ->
                // 关联查询时过滤租户id
                val tenantId = TenantModel.current().id
                if(!lazy) // 只处理同时联查的情况, 不处理递延联查, 因为关联模型自身queryBuilder()就会过滤租户id
                    query.on("$name.tenant_id", tenantId as Any, false)
            }
        }
        return super.addRelation(name, relation)
    }

}
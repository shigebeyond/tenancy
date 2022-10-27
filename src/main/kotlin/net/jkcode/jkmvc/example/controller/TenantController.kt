package net.jkcode.jkmvc.example.controller

import net.jkcode.jkmvc.http.controller.Controller
import net.jkcode.jkmvc.http.fromRequest
import net.jkcode.jkmvc.http.isPost
import net.jkcode.jkmvc.orm.OrmQueryBuilder
import net.jkcode.jkmvc.orm.isLoaded
import net.jkcode.jkmvc.tenancy.TenantModel
import java.util.*

/**
 * 租户管理
 */
class TenantController : Controller() {

    /**
     * 列表页
     * @return [List<TenantModel>]
     */
    public fun index() {
        val query: OrmQueryBuilder = TenantModel.queryBuilder()
        // 统计用户个数 | count tenants
        val counter: OrmQueryBuilder = query.clone() as OrmQueryBuilder // 复制query builder
        val count = counter.count()
        // 查询所有用户 | find all tenants
        val tenants = query.findModels<TenantModel>()
        // 渲染视图 | render view
        res.renderView(view("tenant/index", mapOf("count" to count, "tenants" to tenants)))
    }

    /**
     * 详情页
     * @param id:int=0 用户id
     * @return [TenantModel]
     */
    public fun detail() {
        val id: String? = req["id"] // req["xxx"]
        val tenant = TenantModel(id)
        if (!tenant.isLoaded()) {
            res.renderHtml("用户[$id]不存在")
            return
        }

        val view = view("tenant/detail")
        view["tenant"] = tenant; // 设置视图参数 | set view data
        res.renderView(view)
    }

    /**
     * 新建页
     * @param tenant:TenantModel
     */
    public fun new() {
        if (req.isPost) { // 保存
            val tenant = TenantModel()
            tenant.fromRequest(req)
            tenant.create();
            redirect("tenant/index");
            return
        }

        val view = view()
        res.renderView(view)
    }

    /**
     * 编辑页
     * @param tenant:TenantModel
     */
    public fun edit() {
        val id: String = req["id"]!!
        val tenant = TenantModel(id)
        if (!tenant.isLoaded()) {
            res.renderHtml("用户[" + req["id"] + "]不存在")
            return
        }

        if (req.isPost) { // 保存
            tenant.name = req["name"]!!
            tenant.update()
            redirect("tenant/index");
            return
        }
        val view = view()
        view["tenant"] = tenant;
        res.renderView(view)
    }

    /**
     * 删除
     * @param id:int=0 用户id
     */
    public fun delete() {
        val id: String? = req["id"]
        // 查询单个用户 | find a tenant
        val tenant = TenantModel(id)
        if (!tenant.isLoaded()) {
            res.renderHtml("用户[$id]不存在")
            return
        }
        // 删除 | delete tenant
        tenant.delete();
        // 重定向到列表页 | redirect to list page
        redirect("tenant/index");
    }
}
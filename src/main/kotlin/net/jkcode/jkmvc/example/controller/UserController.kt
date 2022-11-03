package net.jkcode.jkmvc.example.controller

import net.jkcode.jkmvc.example.model.UserModel
import net.jkcode.jkmvc.http.controller.Controller
import net.jkcode.jkmvc.http.fromRequest
import net.jkcode.jkmvc.http.isPost
import net.jkcode.jkmvc.http.isUpload
import net.jkcode.jkmvc.orm.OrmQueryBuilder
import net.jkcode.jkmvc.orm.isLoaded
import net.jkcode.jkmvc.tenancy.TenantCache
import net.jkcode.jkmvc.tenancy.TenantModel
import net.jkcode.jkutil.common.dateFormat
import net.jkcode.jkutil.common.format
import net.jkcode.jkutil.common.httpLogger
import java.util.*

/**
 * 多租户下用户管理
 */
class UserController : Controller() {

    /**
     * 缓存
     */
    val cache = TenantCache.instance("jedis")

    /**
     * 当前租户域名下的跳转
     */
    fun tenantRedirect(uri: String){
        val turl = TenantModel.current().buildTenantUrl(uri)
        redirect(turl)
    }

    override fun before() {
        // 缓存最新访问时间
        cache.put("visitTime", Date().format(true))
    }

    /**
     * 列表页
     * @return [List<UserModel>]
     */
    public fun index() {
        val query: OrmQueryBuilder = UserModel.queryBuilder()
        // 统计用户个数 | count users
        val counter: OrmQueryBuilder = query.clone() as OrmQueryBuilder // 复制query builder
        val count = counter.count()
        // 查询所有用户 | find all users
        val users = query.findModels<UserModel>()
        // 渲染视图 | render view
        val data = mapOf(
                "count" to count,
                "users" to users,
                "visitTime" to cache.get("visitTime")
        )
        res.renderView(view("user/index", data))
    }

    /**
     * 详情页
     * @param id:int=0 用户id
     * @return [UserModel]
     */
    public fun detail() {
        val id: Int = req.getNotNull("id")
        val user = UserModel(id)
        if (!user.isLoaded()) {
            res.renderHtml("用户[$id]不存在")
            return
        }

        val view = view("user/detail")
        view["user"] = user; // 设置视图参数 | set view data
        res.renderView(view)
    }

    /**
     * 新建页
     * @param user:UserModel
     */
    public fun new() {
        if (req.isPost) { // 保存
            val user = UserModel()
            user.fromRequest(req)
            user.create();
            tenantRedirect("user/index");
            return
        }

        val view = view()
        res.renderView(view)
    }

    /**
     * 编辑页
     * @param user:UserModel
     */
    public fun edit() {
        val id: Int = req.getNotNull("id")
        val user = UserModel(id)
        if (!user.isLoaded()) {
            res.renderHtml("用户[" + req["id"] + "]不存在")
            return
        }

        if (req.isPost) { // 保存
            user.fromRequest(req)
            user.update()
            tenantRedirect("user/index");
            return
        }
        val view = view()
        view["user"] = user;
        res.renderView(view)
    }

    /**
     * 删除
     * @param id:int=0 用户id
     */
    public fun delete() {
        val id: Int = req.getNotNull("id")
        // 查询单个用户 | find a user
        val user = UserModel(id)
        if (!user.isLoaded()) {
            res.renderHtml("用户[$id]不存在")
            return
        }
        // 删除 | delete user
        user.delete();
        // 重定向到列表页 | redirect to list page
        tenantRedirect("user/index");
    }

    /**
     * 上传头像
     * upload avatar
     * @param id:int=0 用户id
     * @param avatar:net.jkcode.jkmvc.http.PartFile 上传文件
     */
    public fun uploadAvatar() {
        val id: Int = req.getNotNull("id")
        val user = UserModel(id)
        if (!user.isLoaded()) {
            res.renderHtml("用户[" + req["id"] + "]不存在")
            return
        }

        // 处理上传文件
        if (req.isUpload) {
            user.avatar = req.storePartFileAndGetRelativePath("avatar")!!
            user.update()
        }

        tenantRedirect("user/detail/$id");
    }

    public fun addresses() {
        val id: Int = req.getNotNull("id")
        val user = UserModel(id)
        if (!user.isLoaded()) {
            res.renderHtml("用户[" + req["id"] + "]不存在")
            return
        }

        httpLogger.debug("递延联查一对多")
        val addresses = user.addresses
        httpLogger.debug("递延联查一对一")
        val home = user.home

        httpLogger.debug("同时联查")
        val user2 = UserModel.queryBuilder().with("home").with("addresses").where("user.id", id).findModel<UserModel>()
        res.renderText("测试关联查询")
    }

}
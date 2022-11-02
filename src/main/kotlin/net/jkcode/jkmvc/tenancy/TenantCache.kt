package net.jkcode.jkmvc.tenancy

import net.jkcode.jkutil.cache.ICache
import java.lang.UnsupportedOperationException
import java.util.concurrent.CompletableFuture

/**
 * 多租户下缓存操作接口
 *    代理调用ICache实例，只是key加上租户id前缀
 *    只支持key是string类型, 不支持byte类型
 * @author shijianhang
 * @create 2022-10-27 下午8:20
 **/
class TenantCache(protected val target:ICache /* 被代理的缓存实例 */): ICache {

    companion object{

        /**
         * 获得租户缓存单例
         */
        @JvmStatic
        public fun instance(name: String): TenantCache {
            val t = ICache.instance(name)
            return TenantCache(t)
        }
    }

    /**
     * 当前租户id做key前缀
     */
    protected fun actualKey(key: Any): String {
        return TenantModel.current().id + "." + key
    }

    /**
     * 根据键获得值
     *
     * @param key 键
     * @return
     */
    override fun get(key: Any): Any? {
        return target.get(actualKey(key))
    }

    /**
     * 根据键获得值, 如果无则构建
     *   如果源数据是null, 则缓存空对象, 防止缓存穿透
     *
     * @param key 键
     * @param expireSeconds 过期秒数
     * @param waitMillis 等待的毫秒数
     * @param dataLoader 回源函数, 兼容函数返回类型是CompletableFuture, 同一个key的并发下只调用一次
     * @return
     */
    override fun getOrPut(key: Any, expireSeconds: Long, waitMillis: Long, dataLoader: () -> Any?): CompletableFuture<Any?> {
        return target.getOrPut(actualKey(key), expireSeconds, waitMillis, dataLoader)
    }

    /**
     * 设置键值
     *   如果源数据是null, 则缓存空对象, 防止缓存穿透
     *
     * @param key 键
     * @param value 值
     * @param expireSencond 过期秒数
     */
    override fun put(key: Any, value: Any?, expireSencond: Long) {
        target.put(actualKey(key), value, expireSencond)
    }

    /**
     * 删除指定的键的值
     * @param key 要删除的键
     */
    override fun remove(key: Any) {
        target.remove(actualKey(key))
    }

    /**
     * 清空缓存
     */
    override fun clear() {
        throw UnsupportedOperationException("not implemented")
    }
}
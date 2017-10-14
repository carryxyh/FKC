package com.twodfire.redis;

import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ICacheService {

    /**
     * 回收redis连接到连接池
     * 
     * @param redis
     */
    void returnResource(Jedis redis);

    /**
     * 取得redis的连接,使用完成之后 ,需要把连接返回连接池,不建议自己获取连接池,风险比较大
     * 
     * @return
     */
    @Deprecated
    Jedis getResource();

    /**
     * 存储 object
     *
     * @param key
     * @param value
     */
    @Deprecated
    void setObject(String key, Object value);

    /**
     * 存储 object
     *
     * @param key
     * @param value
     * @param expireSecond 过期秒数
     */
    void setObject(String key, Object value, int expireSecond);

    /**
     * 返回obj 请使用getObject
     * 
     * @param key
     * @return
     */
    @Deprecated
    Object getObjet(String key);

    /**
     * 返回obj
     *
     * @param key
     * @return
     */
    Object getObject(String key);

    /**
     * 存储 object
     *
     * @param key
     * @param value
     */
    @Deprecated
    void setObjectByZip(String key, Object value);

    /**
     * 存储 object
     *
     * @param key
     * @param value
     * @param expireSecond 过期秒数
     */
    void setObjectByZip(String key, Object value, int expireSecond);

    /**
     * 返回obj
     *
     * @param key
     * @return
     */
    Object getObjectByZip(String key);

    /**
     * 返回多个值
     *
     * @param keys
     * @return
     */
    <T> List<T> mget(byte[]... keys);

    /**
     * 返回多个值
     *
     * @param keys
     * @return
     */
    List<String> mget(String... keys);

    /**
     * 存储string
     *
     * @param key
     * @param value
     */
    @Deprecated
    void set(String key, String value);

    /**
     * 存储string
     *
     * @param key
     * @param value
     * @param expireSecond 过期秒数
     */
    void set(String key, String value, int expireSecond);

    /**
     * 取出string
     * 
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 删除key
     * 
     * @param key
     * @return
     */
    long del(String... key);

    /**
     * 设置摸个hash值
     * 
     * @param key
     * @param field
     * @return
     */
    String hget(String key, String field);

    /**
     * 设置hash 的某个值
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    @Deprecated
    Long hset(String key, String field, String value);

    /**
     * 设置hash 的某个值
     * 
     * @param key
     * @param field
     * @param value
     * @param expireSecond
     * @return
     */
    Long hset(String key, String field, String value, int expireSecond);

    /**
     * set hash map
     *
     * @param key
     * @param hash
     * @param expireSecond 过期秒数
     */
    void hmset(String key, Map<String, String> hash, int expireSecond);

    /**
     * set hash map
     *
     * @param key
     * @param hash
     */
    @Deprecated
    void hmset(String key, Map<String, String> hash);

    /**
     * 删除hash 中的某个field
     *
     * @param key
     * @param fields
     * @return
     */
    boolean hdel(String key, String... fields);

    /**
     * 返回hash map
     *
     * @param key
     * @return
     */
    Map<String, String> hgetAll(String key);

    /**
     * 返回多个hash value
     * 
     * @param key
     * @param fields
     * @return
     */
    List<String> hmget(String key, String... fields);

    /**
     * 在list尾部插入数据
     *
     * @param key
     * @param length list的长度，如果超过，就会除去最早进入的元素
     * @param values
     */
    void rpushLength(String key, long length, String... values);

    /**
     * 在list尾部插入数据
     *
     * @param key
     * @param values
     * @param expireSecond 整个list过期
     */
    void rpush(String key, int expireSecond, String... values);

    /**
     * 在list尾部插入数据,没有有效期
     * 
     * @param key
     * @param values
     */
    void rpush(String key, String... values);

    /**
     * 返回list里的所有数据
     *
     * @param key
     */
    List<String> lrange(String key, long start, long end);

    /**
     * 返回list里的长度
     *
     * @param key
     */
    long llen(String key);

    /**
     * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作： count > 0: 从头往尾移除值为 value 的元素。 count < 0:
     * 从尾往头移除值为 value 的元素。 count = 0: 移除所有值为 value 的元素。
     * 
     * @param key
     * @param count
     * @param value
     */
    long lrem(String key, int count, String value);

    /**
     * 执行原子加1操作
     * 
     * @param key
     * @return 返回加1后的值
     */
    long incr(String key);

    /**
     * 执行原子减1操作
     *
     * @param key
     * @return 返回减1后的值
     */
    long decr(String key);

    /**
     * 将 key 所储存的值加上增量 increment
     *
     * @param key
     * @return 加上增量 increment 值
     */
    long incrBy(String key, long increment);

    /**
     * 将 key 所储存的值减去减量 decrement
     *
     * @param key
     * @return 减去减量 decrement的值
     */
    long decrBy(String key, long decrement);

    /**
     * 不存在则设置
     *
     * @param key
     * @param value
     * @param expireSecond 过期
     */
    Long setnx(String key, int expireSecond, String value);

    /**
     * 不存在则设置
     *
     * @param key
     * @param value
     */
    @Deprecated
    Long setnx(String key, String value);

    // add by pijiu 20141215 添加有序操作对象方法 start
    /**
     * 有序存储object
     * 
     * @param key
     * @param score
     * @param value
     * @param expireSecond
     * @return 1:成功 0:不成功
     */
    int zadd(String key, double score, String value, int expireSecond);

    /**
     * 获取有序存储object的score值
     * 
     * @param key
     * @param value
     * @return score值
     */
    double zscore(String key, String value);

    /**
     * 删除对应value值
     * 
     * @param key
     * @param value
     * @return 1:成功 0:不成功
     */
    int zrem(String key, String value);

    /**
     * 返回有序集key中，指定区间内的成员
     * 
     * @param key
     * @param start
     * @param end
     * @param isAsc 1:按score值递增(从小到大),2:按score值递增(从大到小)
     * @return List<String>
     */
    List<String> zrange(String key, long start, long end, int isAsc);

    /**
     * 返回有序集 key 中成员 member 的排名。
     * 
     * @param key
     * @param member
     * @return
     */
    long zrank(String key, String member);

    /**
     * 返回有序集合的成员数
     * 
     * @param key
     * @return
     */
    long zcard(String key);

    /**
     * 在给定的索引之内删除所有成员的有序集合
     * 
     * @param key
     * @return
     */
    long zremrangebyrank(String key, long start, long stop);

    List<String> hvals(String key);

    /**
     * 从list队尾删除key对应value，返回value
     * 
     * @param key
     * @return
     */
    String rpop(String key);

    /**
     * 在list头部插入数据
     * 
     * @param key
     * @param expireSecond
     * @param values
     */
    void lpush(String key, int expireSecond, String... values);

    /**
     * 在list头部插入数据,没有有效期
     * 
     * @param key
     * @param values
     */
    void lpush(String key, String... values);

    /**
     * 从list头部删除key对应value，返回value
     * 
     * @param key
     * @return
     */
    String lpop(String key);

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。 当 key 不是集合类型时，返回一个错误。
     * 
     * @param key
     * @param values
     * @return
     */
    long sadd(String key, String... values);

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。 当 key 不是集合类型时，返回一个错误。
     * 
     * @param key
     * @param expireSecond
     * @param values
     * @return
     */
    long sadd(String key, int expireSecond, String... values);

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。 当 key 不是集合类型，返回一个错误。
     * 
     * @param key
     * @param values
     * @return
     */
    long srem(String key, String... values);

    /**
     * 返回集合 key 中的所有成员。 不存在的 key 被视为空集合。
     * 
     * @param key
     * @return
     */
    Set<String> smembers(String key);

    /**
     * 判断Key是否存在
     *
     * @param key
     * @return
     */
    boolean exists(byte[] key);

    /**
     * 判断Key是否存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);

    /**
     * Return the all the elements in the sorted set at key with a score between min and max (including elements with
     * score equal to min or max).
     * 
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Set<String> zrangeByScore(final String key, final double min, final double max);

    public Set<String> zrevrangeByScore(final String key, final double max, final double min);

    /**
     * BLPOP (and BRPOP) is a blocking list pop primitive. You can see this commands as blocking versions of LPOP and
     * RPOP able to block if the specified keys don't exist or contain empty lists.
     * 
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(final String key, final double min, final double max);
}

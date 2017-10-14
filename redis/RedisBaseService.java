package com.twodfire.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

import java.io.IOException;
import java.util.*;

public abstract class RedisBaseService implements ICacheService {

    private static final Log    logger                        = LogFactory.getLog(RedisBaseService.class);

    // 默认过期秒数
    private static final int    DEFAULT_EXPIRE_SECOND         = -1;

    // 一个小时
    public static final int     ONE_HOUR                      = 60 * 60;

    // 半个小时
    public static final int     THIRTY_MINUTE                 = 30 * 60;
    // 12 个小时
    public static final int     TWELVE_HOUR                   = 12 * 60 * 60;

    // 一天
    public static final int     ONE_DAY                       = 24 * 60 * 60;

    // 一星期
    public static final int     ONE_WEEK                      = 7 * 24 * 60 * 60;

    protected int               database                      = 0;

    // 最大redis连接池数量
    protected int               maxTotal                      = 200;
    // 最大空闲的redis连接池数
    protected int               maxIdle                       = 20;
    // 最小空闲的redis连接池数
    protected int               minIdle                       = 20;

    // 最大等待时间（毫秒）
    protected int               maxWaitMillis                 = 30000;

    protected Pool<Jedis>       pool;

    private static final String STATUS_CODE                   = "OK";

    private long                timeBetweenEvictionRunsMillis = 30000L;

    private boolean             testOnBorrow                  = false;

    private boolean             testWhileIdle                 = true;

    private boolean             testOnReturn                  = false;

    private int                numTestsPerEvictionRun         = -1;

    private long                minEvictableIdleTimeMillis    =  60000L;


    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    /**
     * @return the timeBetweenEvictionRunsMillis
     */
    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    /**
     * @param timeBetweenEvictionRunsMillis the timeBetweenEvictionRunsMillis to set
     */
    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    /**
     * @return the testOnBorrow
     */
    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    /**
     * @param testOnBorrow the testOnBorrow to set
     */
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    /**
     * @return the maxWaitMillis
     */
    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    /**
     * @param maxWaitMillis the maxWaitMillis to set
     */
    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void returnResource(Jedis redis) {
        if (redis != null) {
            redis.close();
        }

    }

    public void returnBrokenResource(Jedis redis) {
        if (redis != null) {
            redis.close();
        }
    }

    public abstract Jedis getResource();

    /**
     * 存储 object
     *
     * @param key
     * @param value
     */
    public void setObject(String key, Object value) {
        setObject(key, value, DEFAULT_EXPIRE_SECOND);
    }

    /**
     * 存储 object
     *
     * @param key
     * @param value
     * @param expireSecond 过期秒数
     */
    public void setObject(String key, Object value, int expireSecond) {
        if (null == value || null == key) return;

        Jedis jedis = getResource();
        try {
            if (expireSecond != -1) {
                jedis.setex(key.getBytes(), expireSecond, DataProcessUtil.serialize(value));
            } else {
                jedis.set(key.getBytes(), DataProcessUtil.serialize(value));
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    public Object getObjet(String key) {
        return getObject(key);
    }

    /**
     * 返回obj
     *
     * @param key
     * @return
     */
    public Object getObject(String key) {
        Object obj = null;
        if (null != key) {
            Jedis jedis = getResource();
            try {
                byte[] value = jedis.get(key.getBytes());
                if (null != value) {
                    obj = DataProcessUtil.deserialize(value);
                }
            } catch (IOException e) {
                logger.error(e.getMessage());
            } finally {
                returnResource(jedis);
            }
        }
        return obj;
    }

    /**
     * 存储 object
     *
     * @param key
     * @param value
     */
    public void setObjectByZip(String key, Object value) {
        setObjectByZip(key, value, DEFAULT_EXPIRE_SECOND);
    }

    /**
     * 存储 object
     *
     * @param key
     * @param value
     * @param expireSecond 过期秒数
     */
    public void setObjectByZip(String key, Object value, int expireSecond) {
        if (null == value || null == key) return;

        Jedis jedis = getResource();

        try {
            byte[] serializeValue = DataProcessUtil.serialize(value);
            byte[] zipValue = DataProcessUtil.compress(serializeValue);
            if (expireSecond != -1) {
                jedis.setex(key.getBytes(), expireSecond, zipValue);
            } else {
                jedis.set(key.getBytes(), zipValue);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 返回obj
     *
     * @param key
     * @return
     */
    public Object getObjectByZip(String key) {
        Object obj = null;
        if (null != key) {
            Jedis jedis = getResource();
            try {
                byte[] zipValue = jedis.get(key.getBytes());
                if (null != zipValue) {
                    byte[] unzipValue = DataProcessUtil.decompress(zipValue);
                    obj = DataProcessUtil.deserialize(unzipValue);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
            } finally {
                returnResource(jedis);
            }
        }
        return obj;
    }

    public boolean exists(byte[] key) {
        Jedis jedis = getResource();
        boolean flag = false;
        try {
            flag = jedis.exists(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return flag;
    }

    public boolean exists(String key) {
        Jedis jedis = getResource();
        boolean flag = false;
        try {
            flag = jedis.exists(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }
        return flag;
    }

    /**
     * 返回obj
     *
     * @param keys
     * @return
     */
    public <T> List<T> mget(byte[]... keys) {
        List<T> objs = new ArrayList<T>();
        Jedis jedis = getResource();
        try {
            List<byte[]> value = jedis.mget(keys);
            if (null != value) {
                for (byte[] bytes : value) {
                    if (null == bytes) continue;
                    T obj = (T) DataProcessUtil.deserialize(bytes);
                    objs.add(obj);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        } finally {
            returnResource(jedis);
        }

        return objs;

    }

    /**
     * 返回obj
     *
     * @param keys
     * @return
     */
    public List<String> mget(String... keys) {
        List<String> values = null;
        Jedis jedis = getResource();
        try {
            values = jedis.mget(keys);
        } finally {
            returnResource(jedis);
        }
        return values;
    }

    public void mset(byte[]... keysvalues) {
        Jedis jedis = getResource();
        try {
            jedis.mset(keysvalues);
        } finally {
            returnResource(jedis);
        }
    }

    public void mset(String... keysvalues) {
        Jedis jedis = getResource();
        try {
            jedis.mset(keysvalues);
        } finally {
            returnResource(jedis);
        }
    }

    public void msetnx(byte[]... keysvalues) {
        Jedis jedis = getResource();
        try {
            jedis.msetnx(keysvalues);
        } finally {
            returnResource(jedis);
        }
    }

    public void msetnx(String... keysvalues) {
        Jedis jedis = getResource();
        try {
            jedis.msetnx(keysvalues);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 存储string
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        set(key, value, DEFAULT_EXPIRE_SECOND);
    }

    /**
     * 存储string
     *
     * @param key
     * @param value
     * @param expireSecond 过期秒数
     */
    public void set(String key, String value, int expireSecond) {
        Jedis jedis = getResource();
        try {

            if (expireSecond != -1) {
                jedis.setex(key, expireSecond, value);
            } else {
                jedis.set(key, value);
            }
        } finally {
            returnResource(jedis);
        }
    }

    public String get(String key) {
        String value = null;
        Jedis jedis = getResource();
        try {
            value = jedis.get(key);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public long del(String... key) {
        long value = 0;
        Jedis jedis = getResource();
        try {
            value = jedis.del(key);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    public String hget(String key, String field) {
        String result = "";
        Jedis jedis = getResource();
        try {
            result = jedis.hget(key, field);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    public Long hset(String key, String field, String value) {
        return hset(key, field, value, DEFAULT_EXPIRE_SECOND);
    }

    public Long hset(String key, String field, String value, int expireSecond) {
        Long result = new Long(0);
        Jedis jedis = getResource();
        try {
            result = jedis.hset(key, field, value);
            if (expireSecond != -1) {
                jedis.expire(key, expireSecond);
            }
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * set hash map
     *
     * @param key
     * @param hash
     * @param expireSecond 过期秒数
     */
    public void hmset(String key, Map<String, String> hash, int expireSecond) {
        Jedis jedis = getResource();
        try {
            jedis.hmset(key, hash);
            if (expireSecond != -1) {
                jedis.expire(key, expireSecond);
            }
        } finally {
            returnResource(jedis);
        }

    }

    /**
     * set hash map
     *
     * @param key
     * @param hash
     */
    public void hmset(String key, Map<String, String> hash) {
        hmset(key, hash, DEFAULT_EXPIRE_SECOND);
    }

    /**
     * 删除hash 中的某个field
     *
     * @param key
     * @param fields
     * @return
     */
    public boolean hdel(String key, String... fields) {
        Long result = new Long(0);
        Jedis jedis = getResource();
        try {
            result = jedis.hdel(key, fields);
        } finally {
            returnResource(jedis);
        }
        if (result.intValue() == 1) {
            return true;
        }
        return false;
    }

    /**
     * 返回hash map
     *
     * @param key
     * @return
     */
    public Map<String, String> hgetAll(String key) {
        Map<String, String> value = null;
        Jedis jedis = getResource();
        try {
            value = jedis.hgetAll(key);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 返回多个hash value
     *
     * @param key
     * @param fields
     * @return
     */
    public List<String> hmget(String key, String... fields) {
        List<String> result = new ArrayList<String>();
        Jedis jedis = getResource();
        try {
            result = jedis.hmget(key, fields);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 在list尾部插入数据
     *
     * @param key
     * @param length
     * @param values
     */
    public void rpushLength(String key, long length, String... values) {
        Jedis jedis = getResource();
        try {
            if (jedis.llen(key).longValue() == length) {
                for (int i = 0; i < values.length; i++) {
                    jedis.lpop(key);
                }
            }
            jedis.rpush(key, values);
        } finally {
            returnResource(jedis);
        }
    }

    /**
     * 在list尾部插入数据
     *
     * @param key
     * @param values
     * @param expireSecond 过期
     */
    public void rpush(String key, int expireSecond, String... values) {
        Jedis jedis = getResource();
        try {
            jedis.rpush(key, values);
            if (expireSecond != -1) {
                jedis.expire(key, expireSecond);
            }
        } finally {
            returnResource(jedis);
        }

    }

    /**
     * 在list尾部插入数据,没有有效期
     *
     * @param key
     * @param values
     */
    public void rpush(String key, String... values) {
        rpush(key, -1, values);
    }

    /**
     * 从list队尾删除key对应value，返回value
     *
     * @param key
     * @return
     */
    @Override
    public String rpop(String key) {
        Jedis jedis = getResource();
        String value = null;
        try {
            value = jedis.rpop(key);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 在list头部插入数据
     *
     * @param key
     * @param values
     * @param expireSecond 过期
     */
    public void lpush(String key, int expireSecond, String... values) {
        Jedis jedis = getResource();
        try {
            jedis.lpush(key, values);
            if (expireSecond != -1) {
                jedis.expire(key, expireSecond);
            }
        } finally {
            returnResource(jedis);
        }

    }

    /**
     * 在list头部插入数据,没有有效期
     *
     * @param key
     * @param values
     */
    public void lpush(String key, String... values) {
        rpush(key, -1, values);
    }

    /**
     * 从list头部删除key对应value，返回value
     *
     * @param key
     * @return
     */
    @Override
    public String lpop(String key) {
        Jedis jedis = getResource();
        String value = null;
        try {
            value = jedis.lpop(key);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。 当 key 不是集合类型时，返回一个错误。
     *
     * @param key
     * @param values
     * @return
     */
    @Override
    public long sadd(String key, String... values) {
        return sadd(key, -1, values);
    }

    /**
     * 将一个或多个 member 元素加入到集合 key 当中，已经存在于集合的 member 元素将被忽略。 当 key 不是集合类型时，返回一个错误。
     *
     * @param key
     * @param expireSecond
     * @param values
     * @return
     */
    @Override
    public long sadd(String key, int expireSecond, String... values) {
        Long result = new Long(0);
        Jedis jedis = getResource();
        try {
            result = jedis.sadd(key, values);
            if (expireSecond != -1) {
                jedis.expire(key, expireSecond);
            }
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 移除集合 key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。 当 key 不是集合类型，返回一个错误。
     *
     * @param key
     * @param values
     * @return
     */
    @Override
    public long srem(String key, String... values) {
        Long result = new Long(0);
        Jedis jedis = getResource();
        try {
            result = jedis.srem(key, values);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 返回集合 key 中的所有成员。 不存在的 key 被视为空集合。
     *
     * @param key
     * @return
     */
    @Override
    public Set<String> smembers(String key) {
        Set<String> result = new HashSet<>();
        Jedis jedis = getResource();
        try {
            result = jedis.smembers(key);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 返回list里的所有数据
     *
     * @param key
     */
    public List<String> lrange(String key, long start, long end) {
        List<String> result = new ArrayList<String>();
        Jedis jedis = getResource();
        try {
            result = jedis.lrange(key, start, end);
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 返回list里的长度
     *
     * @param key
     */
    public long llen(String key) {
        Jedis jedis = getResource();
        long result = 0;
        try {
            result = jedis.llen(key);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 从存于 key 的列表里移除前 count 次出现的值为 value 的元素。 这个 count 参数通过下面几种方式影响这个操作： count > 0: 从头往尾移除值为 value 的元素。 count < 0:
     * 从尾往头移除值为 value 的元素。 count = 0: 移除所有值为 value 的元素。
     *
     * @param key
     * @param count
     * @param value
     */
    public long lrem(String key, int count, String value) {
        long result = 0;
        Jedis jedis = getResource();
        try {
            result = jedis.lrem(key, count, value);
        } finally {
            returnResource(jedis);
        }
        return result;
    }

    /**
     * 执行原子加1操作
     *
     * @param key
     * @return 返回加1后的值
     */
    public long incr(String key) {
        Long value = null;
        Jedis jedis = getResource();
        try {
            value = jedis.incr(key);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 执行原子减1操作
     *
     * @param key
     * @return 返回减1后的值
     */
    public long decr(String key) {
        Long value = null;
        Jedis jedis = getResource();
        try {
            value = jedis.decr(key);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 将 key 所储存的值加上增量 increment
     *
     * @param key
     * @return 加上增量 increment 值
     */
    public long incrBy(String key, long increment) {
        Long value = null;
        Jedis jedis = getResource();
        try {
            value = jedis.incrBy(key, increment);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 将 key 所储存的值减去减量 decrement
     *
     * @param key
     * @return 减去减量 decrement的值
     */
    public long decrBy(String key, long decrement) {
        Long value = null;
        Jedis jedis = getResource();
        try {
            value = jedis.decrBy(key, decrement);
        } finally {
            returnResource(jedis);
        }
        return value;
    }

    /**
     * 不存在则设置
     *
     * @param key
     * @param value
     * @param expireSecond 过期
     */
    public Long setnx(String key, int expireSecond, String value) {

        Long result = new Long(0);
        Jedis jedis = getResource();
        try {
            if (expireSecond == -1) {
                result = jedis.setnx(key, value);
            } else {
                String statusCode = jedis.set(key, value, "NX", "EX", expireSecond);
                // 正确状态
                if (null != statusCode && statusCode.equals(STATUS_CODE)) {
                    result = 1l;
                } else {
                    result = 0l;
                }
            }
        } finally {
            returnResource(jedis);
        }

        return result;
    }

    /**
     * 不存在则设置
     *
     * @param key
     * @param value
     */
    public Long setnx(String key, String value) {
        return setnx(key, -1, value);
    }

    /**
     * 有序存储object
     *
     * @param key
     * @param score
     * @param value
     * @param expireSecond
     * @return 1:成功 0:不成功
     */
    public int zadd(String key, double score, String value, int expireSecond) {
        int ret = 0;
        if (null == value || null == key) return ret;
        Jedis jedis = getResource();
        try {
            ret = jedis.zadd(key, score, value).intValue();
            if (expireSecond != -1) {
                jedis.expire(key.getBytes(), expireSecond);
            }
        } finally {
            returnResource(jedis);
        }
        return ret;
    }

    /**
     * 获取有序存储object的score值
     *
     * @param key
     * @param value
     * @return score值
     */
    public double zscore(String key, String value) {
        double val = -1;
        Jedis jedis = getResource();
        try {
            val = jedis.zscore(key, value);
        } finally {
            returnResource(jedis);
        }
        return val;
    }

    /**
     * 删除对应value值
     *
     * @param key
     * @param value
     * @return 1:成功 0:不成功
     */
    public int zrem(String key, String value) {

        int ret = 0;
        if (null == value || null == key) return ret;
        Jedis jedis = getResource();
        try {
            ret = jedis.zrem(key, value).intValue();
        } finally {
            returnResource(jedis);
        }
        return ret;
    }

    public List<String> zrange(String key, long start, long end, int isAsc) {
        if (null == key) return null;
        List<String> values = new ArrayList<>();
        Jedis jedis = getResource();

        try {
            Set<String> a = null;
            if (isAsc == 1) {
                a = jedis.zrange(key, start, end);
            } else {
                a = jedis.zrevrange(key, start, end);
            }
            for (String a1 : a) {
                values.add(a1);
            }
        } finally {
            returnResource(jedis);
        }
        return values;
    }

    /**
     * 返回有序集 key 中成员 member 的排名。
     *
     * @param key
     * @param member
     * @return
     */
    @Override
    public long zrank(String key, String member) {
        Long pos = new Long(0);
        if (null == key) return pos;
        Jedis jedis = getResource();

        try {
            pos = jedis.zrank(key, member);
        } finally {
            returnResource(jedis);
        }
        return pos == null ? 0 : pos;
    }

    @Override
    public long zcard(String key) {
        if (null == key) return 0;
        Long pos;
        Jedis jedis = getResource();
        try {
            pos = jedis.zcard(key);
        } finally {
            returnResource(jedis);
        }
        return pos == null ? 0 : pos;
    }

    @Override
    public long zremrangebyrank(String key, long start, long stop) {
        if (null == key) return 0;
        Long pos;
        Jedis jedis = getResource();
        try {
            pos = jedis.zremrangeByRank(key, start, stop);
        } finally {
            returnResource(jedis);
        }
        return pos == null ? 0 : pos;
    }

    @Override
    public List<String> hvals(String key) {
        if (null == key) return null;
        Jedis jedis = getResource();
        List<String> values = null;
        try {
            values = jedis.hvals(key);
        } finally {
            returnResource(jedis);
        }
        return values;
    }

    public Set<String> zrangeByScore(final String key, final double min, final double max) {
        if (null == key) return null;
        Jedis jedis = getResource();
        Set<String> values = null;
        try {
            values = jedis.zrangeByScore(key, min, max);
        } finally {
            returnResource(jedis);
        }
        return values;
    }

    public Set<String> zrevrangeByScore(final String key, final double max, final double min) {
        if (null == key) return null;
        Jedis jedis = getResource();
        Set<String> values = null;
        try {
            values = jedis.zrevrangeByScore(key, max, min);
        } finally {
            returnResource(jedis);
        }
        return values;
    }

    /**
     * BLPOP (and BRPOP) is a blocking list pop primitive. You can see this commands as blocking versions of LPOP and
     * RPOP able to block if the specified keys don't exist or contain empty lists.
     * 
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(final String key, final double min, final double max) {
        if (null == key) return null;
        Jedis jedis = getResource();
        Long count = null;
        try {
            count = jedis.zcount(key, min, max);
        } finally {
            returnResource(jedis);
        }
        return count;
    }
}

package com.dfire.redis.spring.boot;

import com.twodfire.redis.RedisSentinelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.util.Assert;
import org.springframework.util.MethodInvoker;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@ConditionalOnClass({ JedisConnection.class, RedisOperations.class, Jedis.class ,RedisSentinelService.class})
public class RedisServiceAutoConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceAutoConfiguration.class);

    @Configuration
    @EnableConfigurationProperties({RedisProperties.class, RedisServiceProperties.class})
    public static class RedisServiceAutoConfiguredRegistrar  implements ApplicationContextAware, BeanPostProcessor, InitializingBean {

        private static String DYNAMIC_BEAN_CLASS = SpringBootRedisService.class.getCanonicalName();

        private BeanDefinitionRegistry beanDefinitonRegistry;

        @Resource
        private RedisConnectionFactory redisConnectionFactory;

        @Resource
        private RedisServiceProperties redisServiceProperties;

        private void registerDynamicBean(BeanDefinitionRegistry beanDefinitonRegistry, String dynamicBeanClass, RedisServiceProperties redisServiceProperties) {
            Map<String, BeanDefinition> beanDefinitionMap = createBeanDefinitionMap(dynamicBeanClass, redisServiceProperties);
            for (Map.Entry<String, BeanDefinition> entiry : beanDefinitionMap.entrySet()) {
                beanDefinitonRegistry.registerBeanDefinition(entiry.getKey(),entiry.getValue());
            }
        }

        private Map<String, BeanDefinition> createBeanDefinitionMap(String dynamicBeanClass, RedisServiceProperties redisServiceProperties) {
            Map<String, BeanDefinition> beanDefinitionMap = new HashMap<String, BeanDefinition>();
             if ( redisServiceProperties.getRedis() != null ){
                 createRedisBeanDefinition(beanDefinitionMap,dynamicBeanClass,redisServiceProperties.getRedis(), true);
            }
            if ( redisServiceProperties.getMulti() != null &&  redisServiceProperties.getMulti().size() > 0 ){
                for ( RedisServiceProperties.MultiRedisProperties properties :  redisServiceProperties.getMulti()) {
                    if ( redisServiceProperties.getRedis() != null ) {
                        copyDefaultProperties(redisServiceProperties.getRedis(), properties);
                    }
                    createRedisBeanDefinition(beanDefinitionMap,dynamicBeanClass,properties, false);
                }
            }
            return beanDefinitionMap;
        }

        private void copyDefaultProperties(RedisServiceProperties.MultiRedisProperties redis, RedisServiceProperties.MultiRedisProperties properties) {
            if ( properties.getSentinel() == null ) properties.setSentinel(redis.getSentinel());
            if ( properties.getPool() == null ) properties.setPool(redis.getPool());

            if ( properties.getSentinel() != null && redis.getSentinel() != null && properties.getSentinel().getMaster() == null ){
                properties.getSentinel().setMaster(redis.getSentinel().getMaster());
            }
            if ( properties.getSentinel() != null && redis.getSentinel() != null && properties.getSentinel().getNodes() == null ){
                properties.getSentinel().setNodes(redis.getSentinel().getNodes());
            }

            if ( properties.getCluster() == null ) properties.setCluster(redis.getCluster());
            if ( properties.getCluster() != null && redis.getCluster() != null && properties.getCluster().getNodes() == null ) {
                properties.getCluster().setNodes(redis.getCluster().getNodes());
            }

            if ( properties.getCluster() != null && redis.getCluster() != null && properties.getCluster().getMaxRedirects() == null ) {
                properties.getCluster().setMaxRedirects(redis.getCluster().getMaxRedirects());
            }
        }

        private void createRedisBeanDefinition(Map<String, BeanDefinition> beanDefinitionMap, String dynamicBeanClass, RedisServiceProperties.MultiRedisProperties properties, boolean primary) {
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(dynamicBeanClass);
            if (properties.getName() == null ){
                beanDefinitionBuilder.getBeanDefinition().setAttribute("id", properties.getName());
            }

            RedisServiceProperties.MultiRedisProperties.Pool pool = properties.getPool();
            if ( pool != null ) {
                beanDefinitionBuilder.addPropertyValue("testOnBorrow", pool.getTestOnBorrow());
                beanDefinitionBuilder.addPropertyValue("timeBetweenEvictionRunsMillis", pool.getTimeBetweenEvictionRunsMillis());
                beanDefinitionBuilder.addPropertyValue("maxIdle", pool.getMaxIdle());
                beanDefinitionBuilder.addPropertyValue("minIdle", pool.getMinIdle());
                beanDefinitionBuilder.addPropertyValue("maxTotal", pool.getMaxTotal());
            }
            beanDefinitionBuilder.addPropertyValue("database", properties.getDatabase());
            if ( properties.getSentinel() != null){
                beanDefinitionBuilder.addPropertyValue("sentinels", properties.getSentinel().getNodes() );
                beanDefinitionBuilder.addPropertyValue("masterName",  properties.getSentinel().getMaster());
            }
            beanDefinitionBuilder.addPropertyValue("properties", properties);
            AbstractBeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
            beanDefinition.setPrimary(primary);
            if ( primary ) {
                ConstructorArgumentValues values = new ConstructorArgumentValues();
                values.addIndexedArgumentValue(0,redisConnectionFactory);
                beanDefinition.setConstructorArgumentValues(values);
            }
            beanDefinition.setBeanClassName(dynamicBeanClass);
            beanDefinition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
            beanDefinitionMap.put(getBeanId(properties), beanDefinition);
        }

        private String getBeanId(RedisServiceProperties.MultiRedisProperties properties) {
            if ( properties.getName() != null){
                return properties.getName();
            }
            String id = SpringBootRedisService.class.getCanonicalName();
            return id.substring(0,1).toLowerCase() + id.substring(1);
        }

        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            beanDefinitonRegistry = (BeanDefinitionRegistry) applicationContext.getAutowireCapableBeanFactory();
        }

        public void afterPropertiesSet() throws Exception {
            registerDynamicBean(beanDefinitonRegistry, DYNAMIC_BEAN_CLASS, redisServiceProperties);
        }

        public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }

        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            return bean;
        }
    }

    static class SpringBootRedisService extends RedisSentinelService implements InitializingBean, DisposableBean {


        private RedisServiceProperties.MultiRedisProperties properties;

        private RedisSentinelConfiguration sentinelConfiguration;

        private RedisClusterConfiguration clusterConfiguration;

        private InternalJedisConnectionFactory connectionFactory;

        private RedisConnectionFactory redisConnectionFactory = null;

        public SpringBootRedisService(){};

        public SpringBootRedisService(JedisConnectionFactory redisConnectionFactory){
            this.redisConnectionFactory = redisConnectionFactory;
        };

        public RedisServiceProperties.MultiRedisProperties getProperties() {
            return properties;
        }

        public void setProperties(RedisServiceProperties.MultiRedisProperties properties) {
            this.properties = properties;
        }

        public void destroy() throws Exception {
            connectionFactory.destroy();
        }

        public void afterPropertiesSet() throws Exception {
            if ( redisConnectionFactory == null ) {
                connectionFactory = redisConnectionFactory();
                connectionFactory.afterPropertiesSet();
            }
        }

        @Override
        public Jedis getResource() {
            if ( redisConnectionFactory == null  ) {
                return connectionFactory.fetchJedisConnector();
            }else{
                try {
                    return fetchJedisConnector(redisConnectionFactory);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            return null;
        }

        protected InternalJedisConnectionFactory redisConnectionFactory()
                throws UnknownHostException {
            return applyProperties(createJedisConnectionFactory());
        }

        protected final InternalJedisConnectionFactory applyProperties(
                InternalJedisConnectionFactory factory) {
            factory.setHostName(this.properties.getHost());
            factory.setPort(this.properties.getPort());
            if (this.properties.getPassword() != null) {
                factory.setPassword(this.properties.getPassword());
            }
            factory.setDatabase(this.properties.getDatabase());
            if (this.properties.getTimeout() > 0) {
                factory.setTimeout(this.properties.getTimeout());
            }
            return factory;
        }

        protected final RedisSentinelConfiguration getSentinelConfig() {
            if (this.sentinelConfiguration != null) {
                return this.sentinelConfiguration;
            }
            RedisProperties.Sentinel sentinelProperties = this.properties.getSentinel();
            if (sentinelProperties != null) {
                RedisSentinelConfiguration config = new RedisSentinelConfiguration();
                config.master(sentinelProperties.getMaster());
                config.setSentinels(createSentinels(sentinelProperties));
                this.sentinelConfiguration = config;
                return config;
            }
            return null;
        }

        /**
         * Create a {@link RedisClusterConfiguration} if necessary.
         * @return {@literal null} if no cluster settings are set.
         */
        protected final RedisClusterConfiguration getClusterConfiguration() {
            if (this.clusterConfiguration != null) {
                return this.clusterConfiguration;
            }
            if (this.properties.getCluster() == null) {
                return null;
            }
            RedisProperties.Cluster clusterProperties = this.properties.getCluster();
            RedisClusterConfiguration config = new RedisClusterConfiguration(
                    clusterProperties.getNodes());

            if (clusterProperties.getMaxRedirects() != null) {
                config.setMaxRedirects(clusterProperties.getMaxRedirects());
            }
            this.clusterConfiguration = config;
            return config;
        }

        private List<RedisNode> createSentinels(RedisProperties.Sentinel sentinel) {
            List<RedisNode> nodes = new ArrayList<RedisNode>();
            for (String node : StringUtils
                    .commaDelimitedListToStringArray(sentinel.getNodes())) {
                try {
                    String[] parts = StringUtils.split(node, ":");
                    Assert.state(parts.length == 2, "Must be defined as 'host:port'");
                    nodes.add(new RedisNode(parts[0], Integer.valueOf(parts[1])));
                }
                catch (RuntimeException ex) {
                    throw new IllegalStateException(
                            "Invalid redis sentinel " + "property '" + node + "'", ex);
                }
            }
            return nodes;
        }

        private InternalJedisConnectionFactory createJedisConnectionFactory() {
            JedisPoolConfig poolConfig = this.properties.getPool() != null
                    ? jedisPoolConfig() : new JedisPoolConfig();

            if (getSentinelConfig() != null) {
                return new InternalJedisConnectionFactory(getSentinelConfig(), poolConfig);
            }
            if (getClusterConfiguration() != null) {
                return new InternalJedisConnectionFactory(getClusterConfiguration(), poolConfig);
            }
            return new InternalJedisConnectionFactory(poolConfig);
        }

        private JedisPoolConfig jedisPoolConfig() {
            JedisPoolConfig config = new JedisPoolConfig();
            RedisServiceProperties.MultiRedisProperties.Pool props = this.properties.getPool();
            config.setMaxTotal(props.getMaxActive());
            config.setMaxIdle(props.getMaxIdle());
            config.setMinIdle(props.getMinIdle());
            config.setMaxWaitMillis(props.getMaxWait());
            config.setTimeBetweenEvictionRunsMillis(props.getTimeBetweenEvictionRunsMillis());
            config.setMaxWaitMillis(props.getMaxWait());
            config.setTestOnBorrow(props.getTestOnBorrow());
            return config;
        }

        private Jedis fetchJedisConnector(RedisConnectionFactory redisConnectionFactory) throws Exception {
            MethodInvoker invoker = new MethodInvoker();
            invoker.setTargetMethod("fetchJedisConnector");
            invoker.setTargetObject(redisConnectionFactory);
            invoker.setTargetClass(invoker.getTargetClass());
            invoker.prepare();
            Jedis jedis = (Jedis) invoker.invoke();
            jedis.select(this.properties.getDatabase());
            return jedis;
        }

        private static class InternalJedisConnectionFactory extends JedisConnectionFactory{

            public InternalJedisConnectionFactory(JedisPoolConfig poolConfig) {
                super(poolConfig);
            }

            public InternalJedisConnectionFactory(RedisSentinelConfiguration sentinelConfig, JedisPoolConfig poolConfig) {
                super(sentinelConfig, poolConfig);
            }

            public InternalJedisConnectionFactory(RedisClusterConfiguration clusterConfig, JedisPoolConfig poolConfig) {
                super(clusterConfig, poolConfig);
            }

            @Override
            public Jedis fetchJedisConnector() {
                Jedis jedis = super.fetchJedisConnector();
                jedis.select(getDatabase());
                return jedis;
            }
        }
    }

}

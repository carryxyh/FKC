/**
 * Created by xiuyuhang on 17/2/8.
 * <p/>
 * 以cart-soa的使用场景为例，该插件主要是用来深克隆Item。在插件的poolobjfactory包中实现了一个Adapter，那么建议自己实现一个类集成自Adapter，然后重写掉makeObject方法。
 *
 * @Override
 * public PooledObject<Kryo> makeObject() throws Exception {
 *  Kryo kryo = new Kryo();
 *  kryo.register(Item.class);
 *  return new DefaultPooledObject<>(kryo);
 * }
 * 这就在kryo生产出来的时候直接在kryo中注册了Item的class信息，通过阅读源码，这样可以在第一次使用这个生产出来的kryo时，减少操作。
 *
 * <p/>
 * 如果有需要可以自定义Serializer（继承自kryo的包中的Serializer），大部分情况下（包括默认情况下）使用的都是kryo中提供的FieldSerializer，所以可以进一步把代码给处理成这样：
 * @Override
 * public PooledObject<Kryo> makeObject() throws Exception {
 *  Kryo kryo = new Kryo();
 *  kryo.register(Item.class, new FieldSerializer(kryo, Item.class));
 *  return new DefaultPooledObject<>(kryo);
 * }
 */
package dfire.ziyuan.poolobjfactory;
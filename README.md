#### 年前没机会分享我写的插件了，这里也做个总结吧：<br/>
#### 1.深克隆有几种实现方法：<br/>
----
（1）最简单的，get set get set get set巴拉巴拉。（缺点：显而易见，不通用，而且代码非常的丑陋！）<br/>
（2）对象流读入，対向流读出，这其实就是一个序列化反序列化。（缺点：流是非常占用资源的，不停地new  close会消耗CPU，一个请求过来要进行几次new操作，会导致GC非常频繁，还有就是原生速度比较慢）<br/>

#### 2.针对以上第二种实现方法，设计了一个深克隆的插件，特点和实现如下：
----
（1）针对原生序列化反序列化慢，使用kryo做序列化反序列化处理，kryo底层其实还是用了原生的流，只不过进行了一些特殊处理，首先，它存储空间更小，字段的存储是变长的，跟mysql的varchar和char一样，还有就是针对string做了特殊的结束标识符（最后一位byte+x70），kryo在初始化的时候提前加载了一些类的解析器，比如ArrayList甚至enum类型的解析器。***对于序列化过的类，kryo会保存类的信息，下次再进行序列化的时候，会很快。***<br/>
（2）针对流的生命周期，很容易也很方便的一种方式就是对象池，由对象池来管理kryo的生命周期。选用的是apache的commons-pool里的genericObjectPool。这个池会自动的管理对象，比于线程池来说这个玩意儿更符合一个池的定义，用的时候从里面borrow一个，用过之后return返还一个，jdk里的线程池是没有return的概念的，return实际上是内部做的。<br/>
下面简单的说一下genericObjectPool的底层实现：底层是一个**queue**，期中维护的是**IDLE OBJ（就是空闲的对象）**，还有一个**map，来维护所有对象**，毕竟一个对象借走，还是要归还的，那么所有对象都要被池管理，自然需要一个管理所有对象的集合。<br/>
那么这个池的工作原理是怎样的呢。**创建这个池的时候需要制定一个对象工厂**，这个工厂不止维护对象的创建，还负责进行销毁、激活、失效等操作。类似工厂的流水线，一个产品生产出来，需要检查质量，一个产品销毁掉，需要检查是否能销毁等。<br/>
**borrow的时候调用工厂的相应方法来创建并且验证和激活一个对象。**<br/>
**return的时候会调用工厂的相应方法来验证对象是否可用，如果池中对象过多，会由工厂来销毁对象。**<br/>
个性化定制的参数，在具体的config中。<br/>
genericObjectPool内部有一个Evictor，驱逐者，用来定时检查管理对象是否长时间不归还，这种情况其实是发生了泄露，这时候Evictor会把这些对象从管理的对象去驱逐出去。
（3）kryo生命周期的管理：这里没有用池，而是实现了一个更轻量级的，也是kryo官方推荐的缓存模式，我是使用了一个队列来维护kryo。**这个队列维护的是软引用**（这里有兴趣的同学可以想一下为什么我不用弱引用而使用软引用）。同样，用的时候borrowOne，用完重置之后returnOne。<br/>
（4）其他相关设计：这里由于我考虑到自身的low逼水平以及实现方法多样，还是用SPI做了一个可扩展。大家可以自己实现Incubator的接口来自定义孵化器（为什么用孵化器这个名字，就是因为传入一个对象，生成一个对象，传入的对象好比生成对象的双亲）默认使用的是defaultIncubator，如果需要改，要在META-INF/services中写入你自己的incubator实现<br/>

#### 3.用法：
--------
```
A a = new A();
Incubator<A> incubator = IncubatorFactory.INSTANCE.getDefaultIncubator();
A theNewOne = incubator.born(a);
```

#### 4.现阶段的开发情况
----
年前不上线，还没有完全完善，年后会出一个1.0.0版本，最好经过压测（主要是调整pool的相关参数），就可以在项目中使用了~

----------

##### 年后的第一版本正式完成并通过测试，期间修复和重新设计一些问题：
----------
（1）对象流废弃。<br/>
在之前的设计中是使用***对象流 + kryo***混合方式完成，在后面实现过程中发现并不可取，这里换成完全使用**kryo**的方式实现<br/>
（2）缓存kryo的kryoPool实现了两种方式的实现。<br/>
第一种：仍然是**queue**的实现方式，这种方式比较轻量，但是不如第二种方式更加完善（**默认采用这种方式**）.<br/>
第二种：**二次封装了apache-commons的genericObjectPool**，并加入了防泄漏策略，调用方可以自己定义一些防泄漏策略。<br/>
（3）注释完善<br/>
（4）关于使用apache-commons实现kryoPool时的一些自定义扩展：<br/>
以cart-soa的使用场景为例，该插件主要是用来深克隆Item。在插件的***poolobjfactory***包中实现了一个Adapter，那么建议自己实现一个类继承自Adapter，然后重写掉**makeObject**方法。<br/>
```
@Override
public PooledObject<Kryo> makeObject() throws Exception {
	Kryo kryo = new Kryo();
	kryo.register(Item.class);
	return new DefaultPooledObject<>(kryo);
}
```
这就在kryo生产出来的时候直接在kryo中注册了Item的class信息，通过阅读源码，这样可以在第一次使用这个生产出来的kryo时，减少操作。<br/>
如果有需要可以自定义**Serializer（继承自kryo的包中的Serializer）**，大部分情况下（包括默认情况下）使用的都是kryo中提供的**FieldSerializer**，所以可以进一步把代码给处理成这样：<br/>
```
@Override
public PooledObject<Kryo> makeObject() throws Exception {
	Kryo kryo = new Kryo();
	kryo.register(Item.class, new FieldSerializer(kryo, Item.class));
	return new DefaultPooledObject<>(kryo);
}
```

官方文档上还提供了更好的预处理方式：
```
    Kryo kryo = new Kryo();
    FieldSerializer someClassSerializer = new FieldSerializer(kryo, SomeClass.class);
    CollectionSerializer listSerializer = new CollectionSerializer();
    listSerializer.setElementClass(String.class, kryo.getSerializer(String.class));
    listSerializer.setElementsCanBeNull(false);
    someClassSerializer.getField("list").setClass(LinkedList.class, listSerializer);
    kryo.register(SomeClass.class, someClassSerializer);
    // ...
    SomeClass someObject = ...
    someObject.list = new LinkedList();
    someObject.list.add("thishitis");
    someObject.list.add("bananas");
    kryo.writeObject(output, someObject);
```
不多说了，这就是更精确到每个字段使用的Serializer，并且标识这个集合中的类型。官方文档上说这能够省略1-2个字节每个元素。

##### 总结一下kryo和hessian的优劣，摘抄自大神的文章：
从序列化后的字节可以看出以下几点：
1. Kryo序列化后比Hessian小很多。（kryo优于hessian）
2. 由于Kryo没有将类field的描述信息序列化，所以Kryo需要以自己加载该类的filed。这意味着如果该类没有在kryo中注册，或者该类是第一次被kryo序列化时，kryo需要时间去加载该类（hessian优于kryo）
3. 由于2的原因，如果该类已经被kryo加载过，那么kryo保存了其类的信息，就可以很快的将byte数组填入到类的field中,而hessian则需要解析序列化后的byte数组中的field信息，对于序列化过的类，kryo优于hessian。
4. hessian使用了固定长度存储int和long，而kryo则使用的变长，实际中，很大的数据不会经常出现。(kryo优于hessian)
5. hessian将序列化的字段长度写入来确定一段field的结束，而kryo对于String将其最后一位byte+x70用于标识结束（kryo优于hessian）

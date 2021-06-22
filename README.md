# redis-stream
采用redis stream 做消息队列

- 待完善 

  1. 优化项目启动时 判断 消费者组是否创建；

  2. 消息队列目前传递数据采用Map<String,String> 格式，优化为 传递对象

     ![image-20210622145629827](https://gitee.com/y_73/pic-go/raw/master/img/20210622145636.png)
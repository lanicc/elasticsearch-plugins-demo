

## 搜索查询——自定义查询



自定义一种查询，类似于 [terms](https://www.elastic.co/guide/en/elasticsearch/reference/5.5/query-dsl-terms-query.html)

terms 查询的写法入下

```json
{
    "terms": {
        "id": [1, 3]
    }
}
```



我们定义一种支持按照分隔符，分隔后再进行 `term` 匹配的查询，名字叫 `spiltToTerms`，写法入下

```json
{
    "spiltToTerms": {
        "sep": ",",
        "field": "id",
        "terms": "1,3"
    }
}
```

`spiltToTerms` 查询的实现是：先使用分隔符 `sep` 对 `terms` 进行分隔，分隔后再构造一个 `terms` 查询



### 体验

可以执行 docker 目录下的脚本

- setup.sh: 安装es环境，包含elasticsearch和kibana
- package.sh: 源码编译打包
- install.sh: 打包好的插件安装到es容器，并重启生效
- destory.sh: 销毁docker容器


#### 使用docker安装es环境

```yaml
# https://www.elastic.co/guide/en/elasticsearch/reference/5.5/docker.html
# https://www.elastic.co/guide/en/kibana/5.5/_configuring_kibana_on_docker.html
version: '2'
services:
  es_for_plugins_demo:
    image: docker.elastic.co/elasticsearch/elasticsearch:5.3.3
    container_name: es_for_plugins_demo
    environment:
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
    ulimits:
      memlock:
        soft: -1
        hard: -1
    mem_limit: 1g
    ports:
      - 9200:9200
    networks:
      - es_net
  kibana_for_plugins_demo:
    image: docker.elastic.co/kibana/kibana:5.3.3
    container_name: kibana_for_plugins_demo
    environment:
      ELASTICSEARCH_URL: http://es_for_plugins_demo:9200
    ports:
      - 5601:5601
    networks:
      - es_net

volumes:
  esdata1:
    driver: local
  esdata2:
    driver: local

networks:
  es_net:

```



执行命令

```bash
docker-compose -p es-for-plugin-demo up -d
#默认密码elastic:changeme
```



#### 编译源码打包插件

项目根目录下执行

```bash
mvn clean package -Dmaven.test.skip=true
```



#### 安装插件

执行脚本

```bash
es_container=es_for_plugins_demo
docker exec $es_container /usr/share/elasticsearch/bin/elasticsearch-plugin remove es-plugin-search-demo
docker cp ../search-plugin-demo/target/releases/search-plugin-demo-1.0-SNAPSHOT.zip $es_container:/usr/share/elasticsearch/search-plugin-demo-1.0-SNAPSHOT.zip
docker exec $es_container /usr/share/elasticsearch/bin/elasticsearch-plugin install file:///usr/share/elasticsearch/search-plugin-demo-1.0-SNAPSHOT.zip

docker restart $es_container

```



#### 登录kibana体验

http://localhost:5601/app/kibana#/dev_tools/console



```javascript
# 创建索引
PUT a

# 添加数据
PUT a/doc/1
{
  "id": 1
}

PUT a/doc/2
{
  "id": 2
}


PUT a/doc/3
{
  "id": 3
}

```



使用自定义的查询进行搜索

```javascript
GET a/doc/_search
{
  "query": {
    "spiltToTerms": {
      "sep": ",",
      "field": "id",
      "terms": "1,3"
    }
  }
}
```




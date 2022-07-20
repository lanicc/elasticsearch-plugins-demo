es_container=es_for_plugins_demo
docker exec $es_container /usr/share/elasticsearch/bin/elasticsearch-plugin remove es-plugin-search-demo
docker cp ../search-plugin-demo/target/releases/search-plugin-demo-1.0-SNAPSHOT.zip $es_container:/usr/share/elasticsearch/search-plugin-demo-1.0-SNAPSHOT.zip
docker exec $es_container /usr/share/elasticsearch/bin/elasticsearch-plugin install file:///usr/share/elasticsearch/search-plugin-demo-1.0-SNAPSHOT.zip

docker restart $es_container

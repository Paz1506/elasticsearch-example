[![Build Status](https://travis-ci.org/Paz1506/cloud-example.svg?branch=master)](https://travis-ci.org/Paz1506/cloud-example)
[![codebeat badge](https://codebeat.co/badges/cb36db99-1583-4546-b82e-981b36a56bca)](https://codebeat.co/projects/github-com-paz1506-elasticsearch-example-master)

# elasticsearch-example
Example application with elasticsearch

> **Note:** This application is a simple example. It doesn't contains production code, best practices, etc. 

**Using with local ES cluster**

1) install ES and run local instance ([instructions](https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started-install.html))
2) configure transport client ([Config.class](https://github.com/Paz1506/elasticsearch-example/blob/master/src/main/java/com/zaytsevp/elasticsearchexample/springdataexample/Config.java))
3) create test index 'zps' and several documents, for example:

<table width=100% align="center">
  <tr>
    <td bgcolor="#ccffcc" align="center" colspan="3">Add new documents before testing</td>
  </tr>
 <tr>
   <td width="5%"><b>1</b></td>
    <td><b><font color="green">curl -X POST http://localhost:9200/zps/posts/1001 -d '{"title": "Java 8 Optional In Depth", "category":"Java", "published_date":"23-FEB-2017", "author":"Rambabu Posa"}'</font></b></td>
  </tr>
  <tr>
    <td width="5%"><b>2</b></td>
    <td><b><font color="green">curl -X POST http://localhost:9200/zps/posts/1002 -d '{"title": "Elastic Search Basics", "category":"ElasticSearch", "published_date":"03-MAR-2017", "author":"Rambabu Posa"}'</font></b></td>
  </tr>
  <tr>
    <td width="5%"><b>3</b></td>
    <td><b><font color="green">curl -X POST http://localhost:9200/zps/posts/1003 -d '{"title": "Spring + Spring Data + ElasticSearch", "category":"Spring", "published_date":"11-MAR-2017", "author":"Rambabu Posa"}'</font></b></td>
  </tr>
  <tr>
    <td width="5%"><b>4</b></td>
    <td><b><font color="green">curl -X POST http://localhost:9200/zps/posts/1004 -d '{"title": "Spring + Spring Data + ElasticSearch", "category":"Spring Boot", "published_date":"23-MAR-2017", "author":"Rambabu Posa"}'</font></b></td>
  </tr>
  </table>
  
  4) run ElasticsearchOperationsTest.java

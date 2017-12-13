#!groovy
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovy.json.internal.Charsets

elasticEndpoint = $/http://host:9200/$

http = new HTTPBuilder( elasticEndpoint )
http.encoders.charset = Charsets.UTF_8

requestBody =
'''
{
  "version": true,
  "size": 500,
  "sort": [
    {
      "@timestamp": {
        "order": "desc",
        "unmapped_type": "boolean"
      }
    }
  ],
  "query": {
    "bool": {
      "must": [
        {
          "query_string": {
            "query": "type: xxx AND message:  666 AND message: HTTP",
            "analyze_wildcard": true
          }
        },
        {
          "range": {
            "@timestamp": {
              "gte": 1512557791421,
              "lte": 1513162591421,
              "format": "epoch_millis"
            }
          }
        }
      ],
      "must_not": []
    }
  },
  "_source": {
    "excludes": []
  },
  "aggs": {
    "2": {
      "date_histogram": {
        "field": "@timestamp",
        "interval": "3h",
        "time_zone": "Europe/Berlin",
        "min_doc_count": 1
      }
    }
  },
  "stored_fields": [
    "*"
  ],
  "script_fields": {},
  "docvalue_fields": [
    "@timestamp",
    "sample_time",
    "sql_exec_start",
    "trace_timestamp"
  ],
  "highlight": {
    "pre_tags": [
      "@kibana-highlighted-field@"
    ],
    "post_tags": [
      "@/kibana-highlighted-field@"
    ],
    "fields": {
      "*": {
        "highlight_query": {
          "bool": {
            "must": [
              {
                "query_string": {
                  "query": "type: xxx AND message:  666 AND message: HTTP",
                  "analyze_wildcard": true,
                  "all_fields": true
                }
              },
              {
                "range": {
                  "@timestamp": {
                    "gte": 1512557791421,
                    "lte": 1513162591421,
                    "format": "epoch_millis"
                  }
                }
              }
            ],
            "must_not": []
          }
        }
      }
    },
    "fragment_size": 2147483647
  }
}
'''

http.request(POST) {
    uri.path = '/_search'
    requestContentType = JSON
    body = requestBody

    headers.'Content-Type' = "application/json"

    response.success = { resp, reader ->
        reader.hits.hits.each {hit ->
            println hit._source.message
        }
    }

    response.failure = { resp ->
        println resp
    }
}

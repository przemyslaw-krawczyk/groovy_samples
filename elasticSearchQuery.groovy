import groovy.json.JsonSlurper
import static java.net.URLEncoder.encode as urlEncode

elasticEndpoint = $/http://elsticServer:9200/_search/$
timestampUrlStr = $/@timestamp:[2016-10-12T15:20:18.373Z TO 2016-10-13T10:00:18.374Z]/$
restUrlStrBase = $/ AND type:someTypeApplication AND host:(someHost)/$
restUrlStr = restUrlStrBase + $/ AND _exists_:someField AND someField:>=50/$

urlStr = (elasticEndpoint + '?size=10000&' + 'q=' + urlEncode(timestampUrlStr + restUrlStr))

url = urlStr.toURL()
reqProp = ['User-Agent': 'Groovy Sample Script']
timeouts = 10000

content = ''//url.getText(connectTimeout: timeouts, readTimeout: timeouts, useCaches: true, allowUserInteraction: false, requestProperties: reqProp)

url.newReader(connectTimeout: timeouts, useCaches: true, requestProperties: reqProp).withReader { reader ->
    content+= reader.readLine()
}

response = new JsonSlurper().parseText(content)
hits = response.hits.hits
println "There are ${response.hits.total} hits."
hits.each {hit ->
    println hit._source.message
}
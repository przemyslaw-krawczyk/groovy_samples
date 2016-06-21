def url = $/http://test:21112/my-app/rs/monitor/nodes/status/5/count/$.toURL()
def reqProp = ['User-Agent': 'Groovy Sample Script', 'Authorization': 'Basic ZXI6YWxmcmVzY29idWZmZXIxMjM=']
// Simple Integer enhancement to make
// 10.seconds be 10 * 1000 ms.
Integer.metaClass.getSeconds = { ->
    delegate * 1000
}

// Get content of URL with parameters.
def content = url.getText(connectTimeout: 10.seconds, readTimeout: 10.seconds,
                          useCaches: true, allowUserInteraction: false,
                          requestProperties: reqProp)
                          
println "Content is ${content}"

url.newReader(connectTimeout: 10.seconds, useCaches: true, requestProperties: reqProp).withReader { reader ->
    println reader.readLine()
}
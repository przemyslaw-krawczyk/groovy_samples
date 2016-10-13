@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7' )

import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovy.json.internal.Charsets

http = new HTTPBuilder( args[0] )
http.encoders.charset = Charsets.UTF_8

line = 1
System.in.withReader("UTF-8") { reader ->
    reader.eachLine { requestBody ->
        time = System.currentTimeMillis()
        http.request(POST) {
            uri.path = '/my/services/Service01'
            requestContentType = XML
            body = requestBody
            soapAction = 'unknownSOAPAction'
            if (requestBody ==~ "Service01SOAPAction1") {
                soapAction = 'Service01SOAPAction1'
            }
            if (requestBody ==~ "Service01SOAPAction2") {
                soapAction = 'Service01SOAPAction2'
            }

            someDataFromRequest = 'someDataFromRequestEmpty'
            someDataFromRequestPattern = /.*<someData>(\d*)<\/someData>.*/
            if (requestBody ==~ someDataFromRequestPattern) {
                someDataFromRequest = (requestBody =~ someDataFromRequestPattern)[0][1]
            }

            headers.SOAPAction = soapAction
            headers.'Content-Type' = "text/xml;charset=UTF-8"

            response.success = { resp ->

                timeAfter = System.currentTimeMillis() - time
                println "Line = ${sprintf('%1\$06d', line)}; Date = ${new Date()}; Response status: ${resp.statusLine}; someDataFromResponse = ${someDataFromResponse}; time ${timeAfter} ms"
                if (resp.statusLine.statusCode != 200/* || timeAfter > 1500*/) {
                    println requestBody
                }
            }

            response.failure = { resp ->
                timeAfter = System.currentTimeMillis() - time
                println "Line = ${sprintf('%1\$06d', line)}; Date = ${new Date()}; Response status: ${resp.statusLine}; someDataFromResponse = ${someDataFromResponse}; time ${timeAfter} ms"
                if (resp.statusLine.statusCode != 200/* || timeAfter > 1500*/) {
                    println requestBody
                }
            }
        }
        line++
    }
}
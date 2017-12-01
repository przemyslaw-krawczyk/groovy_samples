#!groovy
import groovy.json.*
line = 1
System.in.withReader("UTF-8") { reader ->
    reader.eachLine { requestParam ->
        curl = $/curl -H "Content-Type: application/json" -X POST --data '{"jsonrpc":"2.0","method":"eth_getBalance","params":["$requestParam", "latest"],"id":1}' https://api.myetherapi.com/eth/$
        println "I am abaut to run $curl"
        ret = curl.execute().text
        retObject = new JsonSlurper().parseText(ret)
        retObject.result
        retObject.id
        println "Ret is ${ret}"
        line++
    }
}
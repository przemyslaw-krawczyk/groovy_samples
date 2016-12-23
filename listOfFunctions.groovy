mapaDanych = [d1: "d1str", d2: "d2str", d3:"d3str"]

listaFunkcji = []
mapaDanych.each {entry ->
    listaFunkcji << { clos ->
         clos(entry.key, entry.value)
    }
}

mapaDanych.d2 = "Ja ..."

listaFunkcji.each {funkcja -> 
    funkcja {key, val -> println "KeyVal: ${key}:${val}"}
}


obj = ""
4.times { count ->
    obj.metaClass."run_${count}" = {println "I'm ${count}"}
}

obj.run_3()
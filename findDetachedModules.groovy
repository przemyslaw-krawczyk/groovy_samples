#!groovy

def startDir
if (binding.variables['args']) {//startDir from args
    startDir = args[0] as File
} else {//startDir from properties in order to run in GMavenPlugin - example pom fragment below
    startDir = project.properties['args'] as File
}

//Processor: decisions and actions
processor = [
    shallIDoWithDir:    {File dir ->
        new File("pom.xml", dir).exists()
    },

    doWithDir: { File dir ->
        File pomFile = new File("pom.xml", dir)
        def pom
        try {
            pom = new XmlSlurper().parseText(pomFile.text)
        } catch (Exception e) {
            println "Bad pom: ${pomFile.canonicalPath}"
            return
        }
        def modules = pom?.modules?.module*.text()
        if (modules) {
            def modulesSubDirs = []
            dir.eachDir {subDir -> if (new File("pom.xml", subDir).exists()) modulesSubDirs << subDir}
            modulesSubDirs = modulesSubDirs*.name

            def modulesDiff = modulesSubDirs - modules
            if (modulesDiff) {
                println """\
MODULE: ${dir.getCanonicalPath() - startDir.getCanonicalPath()}
    POM: ${modules}
    MISSING IN POM: ${modulesDiff}"""
            }
        }
    },

    shallIGoIntoDir: {
        true
    }
]
//END processor

//main script: walk into startDir
walk(startDir, processor)

// Go into a directory, do something with dir and decide weather: to go or not to go into subdirectory
def walk(File dir, processor) {
    //only dirs are walked
    if (dir.isDirectory()) {
        if (processor.shallIDoWithDir(dir)) {
            processor.doWithDir(dir)
            dir.eachDir { subDir ->
                if (processor.shallIGoIntoDir(subDir)) {
                    //walk into subDir
                    walk(subDir, processor)
                }
            }
        }
    } else {
        throw new IllegalArgumentException("File must be a directory!! Not a file:-)")
    }
}
/*
    <build>
        <plugins>
            <plugin>
                <groupId>org.codehaus.gmaven</groupId>
                <artifactId>gmaven-plugin</artifactId>
                <version>1.5</version>
                <executions>
                    <execution>
                        <phase>generate-resources</phase>
                        <goals>
                            <goal>execute</goal>
                        </goals>
                        <configuration>
                            <properties>
                                <args>${project.basedir}/..</args>
                            </properties>
                            <source>${pom.basedir}/src/main/groovy/findDetachedModules.groovy</source>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
*/
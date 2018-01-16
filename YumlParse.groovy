package scripts.cm

@Grab(group='org.yaml', module='snakeyaml', version='1.17')
import org.yaml.snakeyaml.*

String yamlText = '''
pkgcolls:
  pkgcol1:
    software:Foo
      version:baz
  pkgcol2:
    software:baz
      version:Foo
'''

Yaml yaml = new Yaml()
def result = yaml.load(yamlText)
// or from a file
// new File('/tmp/test.yml').withReader('UTF-8') { reader ->
//    def result = yaml.load(reader)
//    ....
// }

result.pkgcolls.each {
    assert it.key == 'pkgcol1' || it.key == 'pkgcol2'
}

println result.inspect()



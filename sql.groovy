import groovy.sql.Sql

//if you want to load particular jar for test purpose
this.getClass().classLoader.rootLoader.addURL(new File("/home/pkrawczy/sandbox/ojdbc7.jar").toURL())

def db = [
        url:'jdbc:oracle:thin:@host:1521:db1',
        user:'user',
        password:'pass',
        driver:'oracle.jdbc.OracleDriver'
]

def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)

sql.eachRow('''
    SELECT
        TYPE,
        COUNT(*) AS CNT
    FROM SCH.TABLE 
    WHERE 
        STATUS = 'W'
    GROUP BY TYPE
    ORDER BY CID
'''
) { row ->
    println "${row.ctype} ($row.cnt)"
}
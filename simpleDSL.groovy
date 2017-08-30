class MailInfo {
    String user
    String pass
    String toString() {
        "user: ${user}, pass ${pass}"
    }
}
class Scanner {
    MailInfo mailInfo
    String cron
    
    String toString() {
        "mailInfo: ${mailInfo}, cron: ${cron}"
    }    
}

MailInfo.metaClass.user = { String user->
    delegate.user = user
}
MailInfo.metaClass.pass = { String pass->
    delegate.pass = pass
}
Scanner.metaClass.cron = {String cron ->
    delegate.cron = cron
}

Scanner.metaClass.mailInfo = {Closure closure ->
    closure.delegate = delegate.mailInfo
    closure()
    delegate.mailInfo
}

def scanner(closure) {
    scanner = new Scanner(mailInfo: new MailInfo(), cron: null)
    closure.delegate = scanner
    closure()
    scanner
}

scanner {
    cron "*/5 * * * * MON-FRI"
    mailInfo {
        user "aaa"
        pass "sss"
    }    
}
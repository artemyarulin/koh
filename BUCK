cljc_module('environment',
            tests = 'environment_test.cljc')

cljc_module('string',
            tests = 'string_test.cljc')

cljc_module('xml',
            src = ['xml.cljc','xml/client_parser.cljs','xml/server_parser.clj'],
            tests = 'xml_test.cljc',
            modules = ':environment',
            deps = '[org.clojure/data.xml "0.1.0-beta1"]')

cljc_module('xml-p',
            tests = 'xml_p_test.cljc',
            modules = ':xml',
            deps = '[funcool/promesa "1.4.0"]')

cljc_module('environment',
            tests = 'environment_test.cljc')

cljc_module('string',
            tests = 'string_test.cljc')

cljc_module('xml',
            src = ['xml.cljc','xml/client_parser.cljs','xml/server_parser.clj'],
            tests = 'xml_test.cljc',
            deps = '[org.clojure/data.xml "0.1.0-beta1"]',
            modules = ':environment')

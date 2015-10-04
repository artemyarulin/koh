# koh

[![Circle CI](https://circleci.com/gh/artemyarulin/koh.svg?style=svg)](https://circleci.com/gh/artemyarulin/koh)

Write cross platform ClojureScript without noticing it! Container of all platform depended code for ClojureSript with support of browser, node and react-native environment

# Functions

- `err(str)` - returns platform depended error object (`js/Error` in case of JS environment)
- `err?(obj)` - returns `true` if object an error
- `parse-json(str)` - parses string and returns either parsed object or `err` object
- `to-json(obj)` - converts object to JSON string
- `http(method headers data url cb)` - makes an HTTP request with specified parameters. Returns `core.async` channel which would contains either `err` or response object
- `xpath(str xpath-queries is-html)` - runs xpath queries against string

# React Native

Because RN using `JSCore` environments which is just a JavaScript interpreter and doesn't have many features from browser object model we have to use couple of additional RN plugins:

- [react-native-xml](https://github.com/artemyarulin/react-native-xml) - For making XPath queries
- [react-native-raw-http](https://github.com/artemyarulin/react-native-raw-http) - To get access to low level HTTP API

# Clojure

Definitely we will support it.

# To think about:

- Should we really add `core.async` dependency? If we will stick with callback style we can make life easier for somebody? Maybe two different distribution? It is any how possible?

# koh [![Circle CI](https://circleci.com/gh/artemyarulin/koh.svg?style=svg)](https://circleci.com/gh/artemyarulin/koh) 
[![Clojars Project](http://clojars.org/koh/latest-version.svg)](http://clojars.org/koh)

Container of all platform depended code for ClojureSript with support of browser, node and react-native environment

# Functions

- `err(str)` - returns platform depended error object (`js/Error` in case of JS environment)
- `err?(obj)` - returns `true` if object an error
- `enable-print!()` - enable printing to console
- `parse-json(str)` - parses string and returns either parsed object or `err` object
- `to-json(obj)` - converts object to JSON string
- `http(method headers data url cb)` - makes an HTTP request with specified parameters. Returns `core.async` channel which would contains either `err` or response object
- `xpath(html? string xpath-queries)` - runs xpath queries against string
- `parse-xml(string html?)` - parses string as xml/html and returns same data structure as [data.xml](https://github.com/clojure/data.xml)

# React Native

Because RN using `JSCore` environments which is just a JavaScript interpreter and doesn't have many features from browser object model we have to use couple of additional RN plugins:

- [react-native-xml](https://github.com/artemyarulin/react-native-xml) - For making XPath queries. It is expected to have `GLOBAL.rnmxmlQueryHTML` and `GLOBAL.rnmxmlQueryXml` to be available
- [react-native-raw-http](https://github.com/artemyarulin/react-native-raw-http) - To get access to low level HTTP API. Not used yet, we are using standard `fetch` object for now

# Clojure

Definitely we will support it.

# koh

[![Clojars Project](http://clojars.org/koh/latest-version.svg)](http://clojars.org/koh)

Set of cross platform helpers which works on JVM, in browser, on Node and in React Native environment.

### koh.string

- `displace [s & args]` - Replaces token like {0}, {1} in a string using supplied parameters
- `str->int [def-value s]` - Converts string to int. If it's not possible - def-value returned instead

### koh.environment

- `platform` - Returns current execution platform. One of `:browser`, `:node`, `:rnative` or `:jvm`

### koh.xml

- `parse [string html? cb]` - Parses supplied string into XML tree, calling cb value with err and xml. html? flag supported on non JVM environment and allows to parse mailformed XML, such as HTML


### React Native

Following library has to be installed for working with XML: [react-native-xml](https://github.com/artemyarulin/react-native-xml). It is expected to have `GLOBAL.rnmxmlParseString` to be available

### Buck build system

[Buck](https://buckbuild.com) is used as a build system with this library using support macroses from [clojure-clojurescript-buck](https://github.com/artemyarulin/clojure-clojurescript-buck).

#### TODO

- Build system concerns: Explain why Buck, how to build, how to test, how to contribute, explain that this it's extraction from monorepo(should we?)
- Release to Clojars as a one `koh` library and as an each module separately

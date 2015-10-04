# koh
Container of all platform depended code for ClojureScript with support of browser, node and react-native environment

# Functions

- `err(str)` - returns platform depended error object (`js/Error` in case of JS environment)
- `is-err?(obj)` - returns `true` if object an error
- `parse-json(str)` - parses string and returns either parsed object or `err` object
- `to-json(obj)` - converts object to JSON string
- `http(method headers data url)` - makes an HTTP request with specified parameters. Returns `core.async` channel which would contains either `err` or response object
- `xpath(str xpath-queries is-html)` - runs xpath queries against string

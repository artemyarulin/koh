var React = require('react-native'),
    RNMXml = React.NativeModules.RNMXml

require('react-native-eval')

// We need to export RNMXml functions as global in order to make it accessible by CLJS
GLOBAL.rnmxmlQueryHtml = RNMXml.queryHtml
GLOBAL.rnmxmlQueryXml = RNMXml.queryXml

// Min RN app
React.AppRegistry.registerComponent('app', () => React.createClass({render: () => false}))

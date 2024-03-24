const path = require('path')
//引入开发环境配置
const DEV_ENV = require('./dev.env')
let PROXY_URL = DEV_ENV.PROXY_URL
console.log('PROXY_API:'+PROXY_URL)
module.exports = {
    dev: {
        env: DEV_ENV,
        host: 'localhost', // can be overwritten by process.env.HOST
        port: 8088, // can be overwritten by process.env.PORT, if port is in use, a free one will be determined
        autoOpenBrowser: false,
        assetsSubDirectory: 'static',
        assetsPublicPath: '/',
        staticPath: '/static/',
        proxyTable: {
            '/api': {
                target: "http://127.0.0.1:9007",
                //target: PROXY_URL,
                changeOrigin: true,
                cookieDomainRewrite: {
                    "*": ''
                },
                pathRewrite: {
                    '^/api': ''
                }
            },
        },
        // CSS Sourcemaps off by default because relative paths are "buggy"
        // with this option, according to the CSS-Loader README
        // (https://github.com/webpack/css-loader#sourcemaps)
        // In our experience, they generally work as expected,
        // just be aware of this issue when enabling this option.
        cssSourceMap: false
    },
    build: {
        devEnv: require('./dev.env'),
        sitEnv: require('./sit.env'),
        proEnv: require('./pro.env'),
        index: path.resolve(__dirname, '../dist/index.html'),
        assetsRoot: path.resolve(__dirname, '../dist'),
        assetsSubDirectory: 'static',
        assetsPublicPath: './', //请根据自己路径配置更改
        staticPath: '/static/', //请根据自己路径配置更改
        productionSourceMap: true,
        // Gzip off by default as many popular static hosts such as
        // Surge or Netlify already gzip all static assets for you.
        // Before setting to `true`, make sure to:
        // npm install --save-dev compression-webpack-plugin
        productionGzip: false,
        productionGzipExtensions: ['js', 'css'],
        // Run the build command with an extra argument to
        // View the bundle analyzer report after build finishes:
        // `npm run build --report`
        // Set to `true` or `false` to always turn it on or off
        bundleAnalyzerReport: process.env.npm_config_report
    },
}

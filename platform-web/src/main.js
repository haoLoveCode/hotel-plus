import Vue from 'vue';
import App from './App';
import router from './router';
import store from './store';
import ElementUI from 'element-ui';
import { Message } from 'element-ui';
import 'assets/custom-theme/index.css'; // 换肤版本element-ui css
import NProgress from 'nprogress'; // Progress 进度条
import 'nprogress/nprogress.css';// Progress 进度条 样式
import 'normalize.css/normalize.css';// normalize.css 样式格式化
import 'assets/iconfont/iconfont'; // iconfont 具体图标见https://github.com/PanJiaChen/vue-element-admin/wiki
import * as filters from './filters'; // 全局vue filter
import IconSvg from 'components/Icon-svg';// svg 组件
import errLog from 'store/errLog';// error log组件

//----视频播放--start------
import vueMiniPlayer from 'vue-mini-player'
import 'vue-mini-player/lib/vue-mini-player.css'
Vue.use(vueMiniPlayer)
//----视频播放--start------

//----地区选择--start------
import { pcaa } from 'area-data-vue';
import 'area-linkage-vue/dist/index.css';
import AreaLinkageVue from 'area-linkage-vue';
//----地区选择--end------

import { isAuth } from '@/utils'
import baseUtils from '@/utils/baseUtils'
import bizConstants from '@/utils/bizConstants'
import commonOptions from '@/utils/commonOptions'
import Api from '@/services';
//水印插件
import watermark from 'watermark-dom'
import moment from 'moment';

Vue.component('icon-svg', IconSvg)
Vue.use(ElementUI);
//----地区选择--start------
Vue.prototype.$pcaa = pcaa;
Vue.use(AreaLinkageVue)
//----地区选择--end------
Vue.prototype.isAuth = isAuth     // 权限方法
Vue.prototype.$baseUtils = baseUtils     // 基础的方法方法
Vue.prototype.$bizConstants = bizConstants     // 枚举类
Vue.prototype.$commonOptions = commonOptions     // 列表选项类
Vue.prototype.$isNull = baseUtils.isNull     // 判断空的方法
Vue.prototype.handleImageUrl = baseUtils.handleImageUrl     // 处理图片的方法
Vue.prototype.$platformText = '国内领先的酒店信息平台'     // 显示的平台名称和介绍
Vue.prototype.$showMoreStatus = true     // 是否显示更多
Vue.prototype.$mainThemeColor = '#27408B' //系统主要颜色
//Vue.prototype.$mainThemeColor = '#304156' //系统主要颜色
// register global utility filters.
Object.keys(filters).forEach(key => {
    Vue.filter(key, filters[key])
});
//不重定向白名单
const whiteList = [
    bizConstants.LOGIN_PAGE,
    '/reset',
    '/404',
    '/',
];
router.beforeEach(async (to, from, next) => {
    //console.log('to:'+to)
    //开启Progress
    NProgress.start();
    // 在免登录白名单，直接进入
    if (whiteList.indexOf(to.path) !== -1) {
        console.log('命中白名单')
        next();
        return;
    }
    next();
    NProgress.done(); // 结束Progress
});
//在完成页面加载的时候调用
router.afterEach(() => {
    //是否显示水印
    let advertisingShow = true
    //此处设置水印 必须延迟
    setTimeout(()=>{
        let currentDate = moment().format("YYYY-MM-DD HH:mm:ss");
        let waterMarkText;
        if(advertisingShow === false){
            let userName = store.getters.userName
            if(userName){
                waterMarkText = userName+ ' ' + currentDate
            }else{
                waterMarkText = '欢迎访问系统'+' '+currentDate
            }
        }else{
            waterMarkText = '天行歌者，版权所有。软件/毕设代做微信联系:SkywalkingPro'+' '+currentDate
        }
        console.log('水印内容:'+waterMarkText)
        let markParams = {
            watermark_txt: waterMarkText, //水印内容
            watermark_color:'#0a0a0a',            //水印字体颜色
            watermark_fontsize:'18px',          //水印字体大小
            watermark_width:300,                //水印宽度
            watermark_height:150,               //水印长度
            watermark_parent_node:'#app',       //水印的上一级节点，最好挂在根结点
        }
        watermark.init(markParams)
    },1000)
    NProgress.done(); // 结束Progress
});
Vue.config.productionTip = false;
// 生产环境错误日志
if (process.env == 'pro') {
    Vue.config.errorHandler = function (err, vm) {
        console.log(err, window.location.href);
        errLog.pushLog({
            err,
            url: window.location.href,
            vm
        })
    };
}
new Vue({
    el: '#app',
    router,
    store,
    template: '<App/>',
    components: { App }
})

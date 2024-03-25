import Vue from 'vue';
import Router from 'vue-router';
import bizConstants from '@/utils/bizConstants'
const _import = require('./'+process.env.NODE_ENV);
// in development env not use Lazy Loading,because Lazy Loading large page will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '@/views/layout/Layout';
/* error page */
const Err404 = _import('error/404');
/* login */
const LoginPage = _import('login/LoginPage');
Vue.use(Router);
//固定的路由界面
export const constantRouterMap = [
    {
        path: "/loginPage",
        component: LoginPage,
        hidden: true
    },
    {
        path: "/",
        component: require('@/views/mainPage/MainLayout'),
        name: '主页',
        children: [
            {
                path: "/mainPage",
                component: require('@/views/mainPage/MainPage'),
                name: '主页',
            },
            {
                path: "/memberCenter",
                component: require('@/views/biz/MemberCenter'),
                name: '个人中心',
            },
            {
                path: "/noticeDataView",
                component: require('@/views/biz/NoticeDataView'),
                name: '公告内容',
            },
            {
                path: "/roomDataView",
                component: require('@/views/biz/RoomDataView'),
                name: '房间信息',
            },
            {
                path: "/orderListView",
                component: require('@/views/biz/tradeOrder/OrderListView'),
                name: '订单列表',
            },
        ]
    }
]
//异步路由Map
export const asyncRouterMap = [
    {
        path: '*',
        redirect: bizConstants.LOGIN_PAGE,
        hidden: true
    }
];
export default new Router({
    // mode: 'history', //后端支持可开
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRouterMap,
    asyncRouterMap:asyncRouterMap
});

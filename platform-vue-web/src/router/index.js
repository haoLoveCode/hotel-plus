import Vue from 'vue';
import Router from 'vue-router';
import bizConstants from '@/utils/bizConstants'
const _import = require('./'+process.env.NODE_ENV);
// in development env not use Lazy Loading,because Lazy Loading large page will cause webpack hot update too slow.so only in production use Lazy Loading
/* layout */
import Layout from '../views/layout/Layout';
/* error page */
const Err404 = _import('error/404');
/* login */
const LoginPage = _import('login/LoginPage');
const Index = _import('index/index');
Vue.use(Router);
//固定的路由界面
export const constantRouterMap = [
    {
        path: bizConstants.LOGIN_PAGE,
        component: LoginPage,
        hidden: true
    },
    {
        path: '/404',
        component: Err404,
        hidden: true
    },
    {
        path: '/', //此处设定系统首页就是登录界面
        redirect: bizConstants.LOGIN_PAGE,
        name: '登录界面',
        hidden: true
    },
    {
        path: '/mainPage',
        component: Layout,
        icon: 'el-icon-s-home',
        showOnly: true,
        children: [
            {
                path: 'index',
                component: Index,
                name: '首页',
                meta: ""
            }
        ]
    },
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

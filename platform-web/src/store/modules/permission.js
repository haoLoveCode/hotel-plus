import { asyncRouterMap, constantRouterMap } from 'src/router';
import Layout from '@/views/layout/Layout';
import LayoutSystem from '@/views/layout/LayoutSystem';
import bizConstants from '@/utils/bizConstants';
import {Message} from "element-ui";

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param asyncRouterMap
 * @param permissionList
 */
let hasPermission = (permissionList, route) => {
    if (route.meta) {
        //根据权限Code进行判断
        let perm = permissionList.find((item)  => item.permissionCode == route.meta)
        //console.log('perm:'+JSON.stringify(perm))
        if(!perm){
            //console.log('不具备权限的meta:'+route.meta)
            return false
        }else{
            //console.log('具备权限的meta:'+route.meta)
            return true
        }
    }
    return true
}
//动态过滤路由
let filterAsyncRouter = (asyncRouterMap, permissionList) => {
    //console.log('filterAsyncRouter:permissionList:'+JSON.stringify(permissionList))
    const accessedRouters = asyncRouterMap.filter(route => {
        //如果是菜单权限才进行判断
        let valid = hasPermission(permissionList, route);
        if (valid) {
            if (route.children && route.children.length > 0) {
                route.children = filterAsyncRouter(route.children, permissionList)
            }
            return true
        }
        return false
    })
    return accessedRouters
}
function view (path) {
    return function (resolve) {
        import(`@/views/${path}.vue`).then(mod => {
            resolve(mod)
        })
    }
}
//处理子菜单
let handleChildren = (itemMenu) => {
    let childrenList = []
    //处理子菜单
    if(!itemMenu.children || itemMenu.children.length === 0){
        return childrenList;
    }
    itemMenu.children.forEach((childrenItem) => {
        //如果是文件夹则如此处理 必须不是顶级菜单而且是文件夹类型才如此处理
        if(childrenItem.parentId != bizConstants.TOP_MENU_NODE &&
            childrenItem.permissionType === 3){
            try{
                let menuItem = {
                    path: '/'+childrenItem.path,
                    component: LayoutSystem,
                    icon: childrenItem.permissionIcon,
                    name: childrenItem.name,
                    meta: childrenItem.permissionCode,
                    children: []
                }
                console.log(childrenItem.name)
                //如果还存在子文件夹，则继续循环
                if(childrenItem.children && childrenItem.children.length >0){
                    let childrenList = handleChildren(childrenItem);
                    menuItem.children = childrenList
                }
                childrenList.push(menuItem);
            }catch (error)  {
                Message.error('挂载文件夹菜单信息错误，错误菜单信息为:'+childrenItem.path);
            }
        }
        //如果是具体的页面则如此处理
        if(childrenItem.permissionType === 1){
            try{
                const view = require('@/views/' + childrenItem.path + '.vue')
                let childrenMenu = {
                    path: childrenItem.path,
                    icon: childrenItem.permissionIcon,
                    component: view,
                    name: childrenItem.name,
                    meta: childrenItem.path
                }
                childrenList.push(childrenMenu)
            }catch (error)  {
                Message.error('挂载菜单信息错误，错误菜单信息为:'+childrenItem.path);
            }
        }
    })
    return childrenList;
}
const permission = {
    state: {
        routers: constantRouterMap, // 页面路由
        addRouters: [], // 根据权限过滤后的路由
        defaultOpened: [],   // 左侧默认展开的菜单
    },
    mutations: {
        SET_ROUTERS: (state, routers) => {
            state.addRouters = routers;
            state.routers = constantRouterMap.concat(routers);
        },
        SET_DEFAULT_OPENED: (state, defaultOpened) => {
            state.defaultOpened = defaultOpened;
        }
    },
    actions: {
        //设置路由信息
        setRouter({ commit , dispatch }, data) {
            let menuPermissionList = data.menuPermissionList
            let treePermissionList = data.treePermissionList
            asyncRouterMap.splice(0,asyncRouterMap.length)
            if(!treePermissionList || treePermissionList.length ===0 ){
                Message.error('菜单信息不存在，无法渲染');
            }
            treePermissionList.forEach((itemMenu) => {
                //最顶级菜单 而且必须为文件夹
                if(itemMenu.parentId == bizConstants.TOP_MENU_NODE
                    && itemMenu.permissionType === 3 ){
                    let menuItem = {
                        path: '/'+itemMenu.path,
                        component: Layout,
                        icon: itemMenu.permissionIcon,
                        name: itemMenu.name,
                        meta: itemMenu.permissionCode,
                        children: []
                    }
                    let childrenList = handleChildren(itemMenu);
                    menuItem.children = childrenList;
                    asyncRouterMap.push(menuItem);
                }
            })
            return new Promise(resolve => {
                let accessedRouters = menuPermissionList
                let defaultOpened = []
                accessedRouters = filterAsyncRouter(asyncRouterMap,accessedRouters)
                for (let item of accessedRouters) {
                    if (item.expandStatus) {
                        defaultOpened.push(item.name)
                    }
                }
                commit('SET_ROUTERS', accessedRouters);
                commit('SET_DEFAULT_OPENED', defaultOpened);
                resolve();
            })
        },
    }
};
export default permission;

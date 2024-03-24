import Api from '@/services';
import baseUtils from '@/utils/baseUtils';

const authUser = {
    state: {
        token: '',
        userName: '',
        authUserId: '',
        roleList: [],
        menuPermissionList: [],
        buttonPermissionList: [],
        permissionList: [],
        treePermissionList: [],
        loginStatus: false,
        groupType: null,
        btnCode: {},
        userData: {},
    },
    mutations: {
        //设置登录后的信息
        SET_AUTH_USER_META: (state, data) => {
            if (data) {
                state.token = data.token;
                state.roleList = data.roleList;
                state.permissionList = data.permissionList;
                state.buttonPermissionList = data.buttonPermissionList;
                state.menuPermissionList = data.menuPermissionList;
                state.treePermissionList = data.treePermissionList;
                state.userData = data.userData;
                state.userName = data.userData.userName;
                state.authUserId = data.userData.authUserId;
                state.loginStatus = false;
                state.btnCode = data.btnCode;
            } else {
                state.token = '';
                state.roleList = [];
                state.permissionList = [];
                state.buttonPermissionList = [];
                state.menuPermissionList = [];
                state.treePermissionList = [];
                state.userData = {};
                state.userName = '';
                state.authUserId = '';
                state.loginStatus = true;
                state.btnCode = {};
            }
        },
    },
    actions: {
        // 前端登出
        LogOut({commit}) {
            return new Promise(async (resolve, reject) => {
                try {
                    const response = await Api.logOut()
                    if (response.success) {
                        console.log('前端登出-成功')
                        commit('SET_AUTH_USER_META');
                        resolve();
                    } else {
                        reject(error);
                    }
                } catch (error) {
                    console.error(error)
                    reject(error);
                }
            });
        },
        //设置登录后的信息
        setLoginMeta({commit, dispatch}, data) {
            return new Promise(async (resolve, reject) => {
                try {
                    if (!data) {
                        reject(data);
                    }
                    let permissionList = data.permissionList
                    if (!permissionList) {
                        reject(data);
                    }
                    //选出菜单权限
                    let menuPermissionList = permissionList.filter(item => item.permissionType == 1)
                    if (menuPermissionList) {
                        localStorage.setItem('menuPermissionList', JSON.stringify(menuPermissionList));
                        data.menuPermissionList = menuPermissionList
                    }
                    //选出按钮权限
                    let buttonPermissionList = permissionList.filter(item => item.permissionType == 2)
                    if (buttonPermissionList) {
                        localStorage.setItem('buttonPermissionList', JSON.stringify(buttonPermissionList));
                        data.buttonPermissionList = buttonPermissionList
                    }
                    //设置用户信息
                    commit('SET_AUTH_USER_META', data);
                    resolve(data);
                } catch (error) {
                    reject(error);
                }
            });
        },
        // 登录方法
        Login({commit, dispatch}, data) {
            return new Promise(async (resolve, reject) => {
                try {
                    const response = await Api.authUserLogin(data)
                    if (response.success) {
                        let data = response.data
                        if (!baseUtils.isNull(data)) {
                            let permissionList = data.permissionList
                            if (!baseUtils.isNull(data)) {

                            }
                            //选出菜单权限
                            let menuPermissionList = permissionList.filter(item => item.permissionType == 1)
                            if (menuPermissionList) {
                                localStorage.setItem('menuPermissionList', JSON.stringify(menuPermissionList));
                                data.menuPermissionList = menuPermissionList
                            }
                            //选出按钮权限
                            let buttonPermissionList = permissionList.filter(item => item.permissionType == 2)
                            if (buttonPermissionList) {
                                localStorage.setItem('buttonPermissionList', JSON.stringify(buttonPermissionList));
                                data.buttonPermissionList = buttonPermissionList
                            }
                            localStorage.setItem('userData', JSON.stringify(data.userData));
                            localStorage.setItem('permissionList', JSON.stringify(permissionList));
                            localStorage.setItem('roleList', JSON.stringify(data.roleList));
                            localStorage.setItem('treePermissionList', JSON.stringify(data.treePermissionList));
                            localStorage.setItem('token', response.data.token);
                            //设置用户信息
                            commit('SET_AUTH_USER_META', data);
                            resolve(data);
                        } else {
                            reject(response);
                        }
                    } else {
                        reject(response);
                    }
                } catch (error) {
                    reject(error);
                }
            });
        },
    }
};
export default authUser;

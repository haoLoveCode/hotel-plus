import Api from '@/services';
import baseUtils from '@/utils/baseUtils';

const authUser = {
    state: {
        token: '',
        userData: {},
    },
    mutations: {
        //设置登录后的信息
        SET_AUTH_USER_META: (state, data) => {
            if (data) {
                state.token = data.token;
                state.userData = data.userData;
            } else {
                state.token = '';
                state.userData = {};
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
                    let token = data.token
                    if (!token) {
                        reject(data);
                    }

                    localStorage.setItem('userData', JSON.stringify(data));
                    localStorage.setItem('token', token);
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
                    const response = await Api.userLogin(data)
                    if (response.success) {
                        let data = response.data
                        if (!baseUtils.isNull(data)) {
                            //选出菜单权限
                            let token = data.token
                            if (data) {
                                localStorage.setItem('data', );
                            }
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

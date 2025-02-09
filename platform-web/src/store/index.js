import Vue from 'vue';
import Vuex from 'vuex';
import app from './modules/app';
import authUser from './modules/authUser';
import permission from './modules/permission';
import getters from './getters';
Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        app,
        authUser: authUser,
        permission:permission,
    },
    getters
});

export default store

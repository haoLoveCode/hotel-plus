import axios from 'axios';
import router from '../router';
import { Message } from 'element-ui';
import baseUtils from '@/utils/baseUtils'
import bizConstants from '@/utils/bizConstants'
const request = axios.create({
    withCredentials: false,
    timeout: 50000,
    headers: {
        'content-type': 'application/json;charset=UTF-8',
    },
});
//拦截器,request 之前
request.interceptors.request.use((config) => {
    const reDefineConfig = Object.assign({}, config);
    const {
        needToken,
        method,
    } = reDefineConfig;
    const token = localStorage.getItem('token') || '';
    reDefineConfig.headers['content-type'] = 'application/json;charset=UTF-8';
    let requestUrl = reDefineConfig.url
    //是否需要返回登录界面
    let needLogin = reDefineConfig.needToken
    console.log('requestUrl:'+requestUrl)
    console.log('needLogin:'+needLogin)
    //如果是需要token但是没有token
    if(needToken === true && baseUtils.isNull(token)){
        Message.warning({
            message:`访问非登录接口但是没有token`,
            duration:2000
        });
        router.push(bizConstants.LOGIN_PAGE);
        return;
    }else{
        reDefineConfig.headers.token = token;
    }
    reDefineConfig.data = reDefineConfig.data || {};
    reDefineConfig.params = reDefineConfig.params || {};
    if (String(method).toUpperCase() === 'GET') {
        const obj = {};
        for (const i in reDefineConfig.params) {
            const str = reDefineConfig.params[i];
            obj[i] = (typeof str === 'string') ? str.trim() : str;
        }
        reDefineConfig.params = Object.assign(obj, {
            timestamp: new Date().getTime(), // hack get 请求被缓存的问题
        });
    }else if (String(method).toUpperCase() === 'POST') {
        const obj = {};
        for (const i in reDefineConfig.params) {
            const str = reDefineConfig.params[i];
            obj[i] = (typeof str === 'string') ? str.trim() : str;
        }
        reDefineConfig.data =  config.data;
    }else if (String(method).toUpperCase() === 'PUT') {
        const obj = {};
        for (const i in reDefineConfig.params) {
            const str = reDefineConfig.params[i];
            obj[i] = (typeof str === 'string') ? str.trim() : str;
        }
        reDefineConfig.data =  config.data;
    } else {
        const obj = {};
        for (const i in reDefineConfig.data) {
            const str = reDefineConfig.data[i];
            obj[i] = (typeof str === 'string') ? str.trim() : str;
        }
        reDefineConfig.data =  config.data;
    }
    return reDefineConfig;
}, (error) => Promise.reject(error));
//拦截器,response之后
request.interceptors.response.use((response) => {
    let handleData = handleResponse(response);
    return handleData;
}, (error) => {
    console.log('请求发生错误:'+JSON.stringify(error))
    let response = error.response
    let message = error.message
    if (response) {
        const { status, data, config } = response;
        console.error(`
            XHR request error, the request url is ${config.url},
            status is ${status},
            message is ${data.message}`);
        return Promise.reject(error.response.data);
    }else{
        return Promise.reject({ message: message });
    }
    return Promise.reject({ message: error.response });
});
//处理文件下载
const handleDownLoad = (response) => {
    const content = response.data
    const blob = new Blob([content])
    const fileName = `${new Date().getTime()}-导出结果.xlsx`
    if ('download' in document.createElement('a')) {
        const docLink = document.createElement('a')
        docLink.download = fileName
        docLink.style.display = 'none'
        docLink.href = URL.createObjectURL(blob)
        document.body.appendChild(docLink)
        docLink.click()
        URL.revokeObjectURL(docLink.href)
        document.body.removeChild(docLink)
    } else {
        navigator.msSaveBlob(blob, fileName)
    }
};
//处理结果
const handleResponse = (response) => {
    let data = response.data
    if(!data){
        return data;
    }
    let success = data.success;
    let message = data.message;
    let config = response.config
    if(!config){
        return data;
    }
    //响应类型如果是后端返回的文件则单独处理
    let responseType = config.responseType
    if(responseType && responseType == 'blob'){
        handleDownLoad(response);
        return;
    }
    if(success === true){
        return data;
    }
    //console.log('data:'+JSON.stringify(data))
    if(bizConstants.NEED_LOGIN_CODE.indexOf(data.code) != -1){
        Message.error({
            message:message,
            duration:2000
        });
        router.push(bizConstants.LOGIN_PAGE);
        return data;
    }
    if(data.code != 200){
        Message.error({
            message:`发生业务异常:`+message,
            duration:2000
        });
        return data;
    }
};
export default request;

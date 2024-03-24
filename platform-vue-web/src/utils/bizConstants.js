import baseUtils from './baseUtils'
import Api from "@/services";
import { Message } from 'element-ui';
//当前长整时间
export const CURRENT_LONG_TIME = 'api/v1/platform/currenLongTime'
//顶级节点
export const TOP_MENU_NODE = '0'
//登录界面路由
export const LOGIN_PAGE = '/LoginPage'
//腾讯地图KEY
export const TC_MAP_KEY = 'BSVBZ-SMTKV-TUPPE-5QAL4-HBOSZ-WLFMS'
//登录请求地址
export const LOGIN_REQUEST_URL = '/authUserLogin'
export const pickerOptions = {
  shortcuts: [{
    text: '今天',
    onClick(picker) {
      picker.$emit('pick', new Date());
    }
  }, {
    text: '昨天',
    onClick(picker) {
      const date = new Date();
      date.setTime(date.getTime() - 3600 * 1000 * 24);
      picker.$emit('pick', date);
    }
  }, {
    text: '一周前',
    onClick(picker) {
      const date = new Date();
      date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
      picker.$emit('pick', date);
    }
  }]
}
//需要重新登陆的Code 由后端决定
export const NEED_LOGIN_CODE = [
  '501',
  '0000007',
  '0000012',
  '0000011',
  '0000013',
  '0000008',
]
//订单类型
export const orderTypeOptions = [
  {
    'text':'课程',
    'value': 'SALES_ITEM'
  },
]
//订单支付方式
export const payTypeOptions = [
  {
    'text':'支付宝',
    'value': 'aliPay'
  },
  {
    'text':'微信',
    'value': 'weChatPay'
  },
  {
    'text':'免费',
    'value': 'free'
  },
]
//订单状态
export const orderStatusOptions = [
  {
    'text':'未支付',
    'value': 'NOT_PAY'
  },
  {
    'text':'支付成功',
    'value': 'PAY_SUCCESS'
  },
  {
    'text':'订单关闭',
    'value': 'CLOSED'
  },
  {
    'text':'订单退款',
    'value': 'REFUND'
  },
  {
    'text':'退款失败',
    'value': 'REFUND_FAIL'
  },
  {
    'text':'订单完结',
    'value': 'FINISH'
  }
]
//权限类型
export const permissionTypeOptions = [
  {
    'text':'页面',
    'value': 1
  },
  {
    'text':'按钮',
    'value': 2
  },
  {
    'text':'文件夹',
    'value': 3
  },
  {
    'text':'其他资源',
    'value': 4
  },
]
//是和否选项
export const yesOrNoOptions = [
  {
    'text':'正常',
    'value': 1
  },
  {
    'text':'禁用',
    'value': 2
  },
]
//用户状态
export const authStatusOptions = [
  {
    'text':'正常',
    'value': 1
  },
  {
    'text':'禁用',
    'value': 2
  },
]
//用户性别类型
export const authUserGenderOptions = [
  {
    'text':'男性',
    'value':1
  },
  {
    'text':'女性',
    'value':2
  },
]
//是和否类型
export const booleanOptions = [
  {
    'text':'否',
    'value':false
  },
  {
    'text':'是',
    'value':true
  },
]
//是否查询所有
export const needAllStatusOptions = [
  {
    'text':'是',
    'value':'true'
  },
  {
    'text':'否',
    'value':'false'
  },
]
//物品是否热门
export const itemHotStatusOptions = [
  {
    'text':'热门',
    'value':'HOT'
  },
  {
    'text':'非热门',
    'value':'UN_HOT'
  },
]
//APP的板块名称
export const itemStatusOptions = [
  {
    'text':'上架',
    'value': 1
  },
  {
    'text':'下架',
    'value': 2
  },
]
//APP的板块名称
export const panelOptions = [
  {
    'text':'主页',
    'value':'MAIN'
  },
  {
    'text':'页面内',
    'value':'PAGE'
  },
]
export const sketchColumns = [
  {
    title: '简略信息',
    dataIndex: 'sketchContent',
    align:'center',
  }
]
export const tagColumns = [
  {
    title: '简略信息',
    dataIndex: 'tagContent',
    align:'center',
  }
]
//当前地图中心点
export const platformMapCenter = [
  104.068823, 30.657167
]
//分页查询对比条件
export const contrastFactorOption = [
  {
    'text':'等于',
    'value':'EQUALS',
  },
  {
    'text':'大于',
    'value':'GREATER_THAN',
  },
  {
    'text':'大于等于',
    'value':'GREATER_THAN_OR_EQUAL_TO',
  },
  {
    'text':'小于等于',
    'value':'LESS_THAN_OR_EQUAL_TO',
  },
  {
    'text':'小于',
    'value':'LESS_THAN',
  }
]
export const dataBeginAndEndTimeFormat = 'YYYY-MM' //时间格式
export const methodHandler = {
  async yearOption(){
    let optionArray = new Array();
    for(let index = 1900;index<=2100;index++) {
      let option = {
        'text': index,
        'value': index
      }
      optionArray.push(option);
    }
    return optionArray
  },
  async currenLongTime(){
    let result = await getRequest(CURRENT_LONG_TIME,{});
    let data = result.data;
    if(data){
      return data
    }else{
      return ''
    }
  },
  //所有角色
  async allRoleOptions(){
    let result = await Api.queryAllAuthRole({

    }).then((res) => {
      if (res.success) {
        return res.data
      }else{
        this.$message.error(res.message || res.msg || "服务器异常");
      }
    })
    let data = result;
    //console.log('data:'+JSON.stringify(data))
    let resultArray = new Array();
    if(data && data.length>0){
      data.map(item => {
        let option = {
          'text':item.roleName,
          'value':item.authRoleId
        }
        resultArray.push(option);
      })
    }
    return resultArray;
  },
  //根据类型查询系统字典信息
  async queryPlatformDictByType(dictType){
    let result = await Api.queryDictByType({
      dictType:dictType
    }).then((res) => {
      if (res.success) {
        return res.data
      }else{
        Message.error(res.message || res.msg || "服务器异常");
      }
    })
    let data = result;
    //console.log('data:'+JSON.stringify(data))
    let resultArray = new Array();
    if(data && data.length>0){
      data.map(item => {
        let option = {
          'text':item.dictValue,
          'value':item.platformDictId
        }
        resultArray.push(option);
      })
    }
    return resultArray;
  },
  //查询所有的前端用户
  async queryAuthAppUser() {
    try {
      let authAppUserOptions = await Api.queryAuthAppUser({

      }).then(async (res) => {
        if (res.success) {
          if (baseUtils.isNull(res)) {
            return new Array();
          }
          let data = res.data
          if (baseUtils.isNull(data)) {
            return new Array();
          }
          let authAppUserOptions = new Array();
          data.map((item) => {
            let options = {
              'text': item.userName,
              'value': item.authAppUserId
            }
            authAppUserOptions.push(options)
          })
          return authAppUserOptions;
        } else {
          Message.error('服务器异常');
          return new Array();
        }
      });
      return authAppUserOptions;
    } catch (error) {
      Message.error(error.message || error.msg || "服务器异常");
      return new Array();
    }
  },
  async authUserOptions(){
    let result = await Api.queryAllAuthUser({

    }).then((res) => {
      if (res.success) {
        return res.data
      }else{
        Message.error(res.message || res.msg || "服务器异常");
      }
    })
    let data = result;
    //console.log('data:'+JSON.stringify(data))
    let resultArray = new Array();
    if(data && data.length>0){
      data.map(item => {
        let option = {
          'text':item.userName,
          'value':item.authUserId
        }
        resultArray.push(option);
      })
    }
    return resultArray;
  },
}
export default {
  LOGIN_PAGE:LOGIN_PAGE,
  LOGIN_REQUEST_URL:LOGIN_REQUEST_URL,
  NEED_LOGIN_CODE:NEED_LOGIN_CODE,
  TOP_MENU_NODE:TOP_MENU_NODE,
  permissionTypeOptions:permissionTypeOptions,
  authStatusOptions:authStatusOptions,
  yesOrNoOptions:yesOrNoOptions,
  authUserGenderOptions:authUserGenderOptions,
  booleanOptions:booleanOptions,
  orderStatusOptions:orderStatusOptions,
  payTypeOptions:payTypeOptions,
  orderTypeOptions:orderTypeOptions,
  platformMapCenter:platformMapCenter,
  contrastFactorOption:contrastFactorOption,
  dataBeginAndEndTimeFormat:dataBeginAndEndTimeFormat,
  TC_MAP_KEY:TC_MAP_KEY,
  authUserOptions:methodHandler.authUserOptions,
  queryPlatformDictByType:methodHandler.queryPlatformDictByType,
  yearOption:methodHandler.yearOption,
  allRoleOptions:methodHandler.allRoleOptions,
  queryAuthAppUser:methodHandler.queryAuthAppUser,
  currenLongTime:methodHandler.currenLongTime,
  sketchColumns:sketchColumns,
  tagColumns:tagColumns,
  panelOptions:panelOptions,
  itemStatusOptions:itemStatusOptions,
  pickerOptions:pickerOptions,
  itemHotStatusOptions:itemHotStatusOptions,
  needAllStatusOptions:needAllStatusOptions,
  handleImageUrl:methodHandler.handleImageUrl,
};


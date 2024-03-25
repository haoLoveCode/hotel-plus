const httpUrl = process.env.BASE_API
const globalServices = {
    //-------------腾讯地图 start-------------------
    /*腾讯地图地点建议*/
    tcMapPlaceSuggestion: {
        method: 'GET',
        url: `${httpUrl}/api/v1/platform/queryPlace`,
    },
    //-------------腾讯地图 end-------------------
    //⽣成base64图形验证码
    base64captchaCode: {
        method: 'post',
        url: `${httpUrl}/api/v1/authUser/base64captchaCode`,
        needToken:false,
    },
    //登录
    authUserLogin: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authUser/authUserLogin`,
        needToken:true,
    },
    //拿到登录后返回的数据
    queryLoginMeta: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authUser/queryLoginMeta`,
        needToken:true,
    },
    //分页系统用户
    queryAuthUserByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authUser/queryByPage`,
        needToken:true,
    },
    //批量删除
    batchDeleteAuthUser: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authUser/batchDeleteItem`,
        needToken:true,
    },
    //新增系统用户
    addAuthUserItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authUser/addItem`,
        needToken:true,
    },
    //编辑系统用户
    editAuthUserItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/authUser/updateItem`,
        needToken:true,
    },
    //查询所有用户信息
    queryAllAuthUser: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authUser/queryAllAuthUser`,
        needToken:true,
    },
    //根据字典类型查询字典数据
    queryDictByType: {
        method: 'GET',
        url: `${httpUrl}/api/v1/platformDict/queryDictByType`,
        needToken:false,
    },
    queryPlatformDictByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/platformDict/queryByPage`,
        needToken:true,
    },
    batchDeletePlatformDict: {
        method: 'POST',
        url: `${httpUrl}/api/v1/platformDict/batchDeleteItem`,
        needToken:true,
    },
    addPlatformDictItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/platformDict/addItem`,
        needToken:true,
    },
    editPlatformDictItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/platformDict/updateItem`,
        needToken:true,
    },
    // 退出
    logOut: {
        method: 'post',
        url: `${httpUrl}/api/v1/authUser/logOut`,
        needToken:true,
    },
    //根据用户信息查询用户的角色信息
    queryRoleByAuthUserId: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authUser/queryRoleByAuthUserId`,
        needToken:true,
    },
    // 查询登录信息
    queryLoginUserMeta: {
        method: 'get',
        url: `${httpUrl}/api/v1/authUser/queryLoginUserMeta`,
        needToken:true,
    },
    //系统权限相关模块
    queryAuthPermissionByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authPermission/queryByPage`,
        needToken:true,
    },
    //查询权限树集合
    queryPermissionNodeList: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authPermission/queryPermissionNodeList`,
        needToken:true,
    },
    //查询权限树集合
    queryPermissionTreeList: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authPermission/queryPermissionTreeList`,
        needToken:true,
    },
    //根据主键ID查询菜单信息
    queryPermissionByMainId: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authPermission/queryPermissionByMainId`,
        needToken:true,
    },
    //查询所有权限树集合
    queryAllPermissionNodeList: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authPermission/queryAllPermissionNodeList`,
        needToken:true,
    },
    //批量删除权限
    batchDeleteAuthPermission: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authPermission/batchDeleteItem`,
        needToken:true,
    },
    //新增权限
    addAuthPermissionItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authPermission/addItem`,
        needToken:true,
    },
    //编辑权限
    editAuthPermissionItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/authPermission/updateItem`,
        needToken:true,
    },
    //系统角色相关API
    queryAuthRoleByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authRole/queryByPage`,
        needToken:true,
    },
    //批量删除角色
    batchDeleteAuthRole: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authRole/batchDeleteItem`,
        needToken:true,
    },
    //新增角色
    addAuthRoleItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authRole/addItem`,
        needToken:true,
    },
    //编辑角色
    editAuthRoleItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/authRole/updateItem`,
        needToken:true,
    },
    //查询所有角色信息
    queryAllAuthRole: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authRole/queryAllAuthRole`,
        needToken:true,
    },
    //查询角色具备的权限信息
    queryRolePermission: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authRole/queryRolePermission`,
        needToken:true,
    },
    //单个文件上传
    uploadSingleFile: {
        method: 'post',
        jsonStatus: true,
        url: `${httpUrl}/api/platformFile/uploadFile`,
        needToken:true,
    },
    //系统日志API
    queryPlatformApiLogByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/platformApiLog/queryByPage`,
        needToken:true,
    },
    batchDeletePlatformApiLog: {
        method: 'POST',
        url: `${httpUrl}/api/v1/platformApiLog/batchDeleteItem`,
        needToken:true,
    },
    //统计系统访问量
    statisticsVisitCount: {
        method: 'POST',
        url: `${httpUrl}/api/v1/platformApiLog/statisticsVisitCount`,
        needToken:true,
    },
    //查询系统角色对应的用户数量
    queryAuthRoleCount: {
        method: 'POST',
        url: `${httpUrl}/api/v1/statistics/queryAuthRoleCount`,
        needToken:true,
    },
    //单个文件删除
    deleteFileApi: {
        method: 'DELETE',
        jsonStatus: true,
        url: `${httpUrl}/api/platformFile/deleteFileByFileName`,
        needToken:true,
    },

    //--------------------------------
    /*查询所有首页轮播图*/
    queryAllMainSwiper: {
        method: 'GET',
        url: `${httpUrl}/api/v1/mainSwiper/queryAllMainSwiper`,
        needToken:true,
    },
    /*分页查询首页轮播图*/
    queryMainSwiperByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/mainSwiper/queryByPage`,
        needToken:true,
    },
    /*新增首页轮播图*/
    addMainSwiperItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/mainSwiper/addItem`,
        needToken:true,
    },
    /*编辑首页轮播图*/
    editMainSwiperItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/mainSwiper/updateItem`,
        needToken:true,
    },
    /*批量删除首页轮播图*/
    batchDeleteMainSwiper: {
        method: 'POST',
        url: `${httpUrl}/api/v1/mainSwiper/batchDeleteItem`,
        needToken:true,
    },
    /*查询所有APP认证用户*/
    queryAuthAppUser: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authAppUser/queryAuthAppUser`,
        needToken:true,
    },
    /*分页查询APP认证用户*/
    queryAuthAppUserByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authAppUser/queryByPage`,
        needToken:true,
    },
    /*新增APP认证用户*/
    addAuthAppUserItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authAppUser/addItem`,
        needToken:true,
    },
    /*编辑APP认证用户*/
    editAuthAppUserItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/authAppUser/updateItem`,
        needToken:true,
    },
    /*批量删除APP认证用户*/
    batchDeleteAuthAppUser: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authAppUser/batchDeleteItem`,
        needToken:true,
    },
    /*导出APP认证用户*/
    exportAuthAppUserItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authAppUser/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //--------------------------------
    //---------------公告信息----start-------------------
    /*分页查询公告信息*/
    queryNoticeDataByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/noticeData/queryByPage`,
        needToken:true,
    },
    /*查询公告信息*/
    queryNoticeData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/noticeData/queryNoticeData`,
        needToken:true,
    },
    /*查询单个公告信息*/
    queryOneNoticeData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/noticeData/queryOneNoticeData`,
        needToken:true,
    },
    /*新增公告信息*/
    addNoticeDataItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/noticeData/addItem`,
        needToken:true,
    },
    /*编辑公告信息*/
    editNoticeDataItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/noticeData/updateItem`,
        needToken:true,
    },
    /*批量删除公告信息*/
    batchDeleteNoticeData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/noticeData/batchDeleteItem`,
        needToken:true,
    },
    /*导出公告信息*/
    exportNoticeDataItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/noticeData/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------公告信息----end-------------------
    //---------------交易订单信息----start-------------------
    /*分页查询交易订单信息*/
    queryTradeOrderByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/tradeOrder/queryByPage`,
        needToken:true,
    },
    /*查询交易订单信息*/
    queryTradeOrder: {
        method: 'POST',
        url: `${httpUrl}/api/v1/tradeOrder/queryTradeOrder`,
        needToken:true,
    },
    /*查询单个交易订单信息*/
    queryOneTradeOrder: {
        method: 'POST',
        url: `${httpUrl}/api/v1/tradeOrder/queryOneTradeOrder`,
        needToken:true,
    },
    /*新增交易订单信息*/
    addTradeOrderItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/tradeOrder/addItem`,
        needToken:true,
    },
    /*编辑交易订单信息*/
    editTradeOrderItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/tradeOrder/updateItem`,
        needToken:true,
    },
    /*批量删除交易订单信息*/
    batchDeleteTradeOrder: {
        method: 'POST',
        url: `${httpUrl}/api/v1/tradeOrder/batchDeleteItem`,
        needToken:true,
    },
    /*导出交易订单信息*/
    exportTradeOrderItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/tradeOrder/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------交易订单信息----end-------------------
    //---------------订单-收货地址关联信息----start-------------------
    /*查询订单-收货地址关联信息*/
    queryOrderAddress: {
        method: 'POST',
        url: `${httpUrl}/api/v1/orderAddress/queryOrderAddress`,
        needToken:true,
    },
    /*编辑订单-收货地址关联信息*/
    editOrderAddressItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/orderAddress/updateItem`,
        needToken:true,
    },
    /*批量删除订单-收货地址关联信息*/
    batchDeleteOrderAddress: {
        method: 'POST',
        url: `${httpUrl}/api/v1/orderAddress/batchDeleteItem`,
        needToken:true,
    },
    //---------------订单-收货地址关联信息----end-------------------
    //---------------房间类型信息----start-------------------
    /*分页查询房间类型信息*/
    queryRoomTypeByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomType/queryByPage`,
        needToken:true,
    },
    /*查询房间类型信息*/
    queryRoomType: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomType/queryRoomType`,
        needToken:true,
    },
    /*查询单个房间类型信息*/
    queryOneRoomType: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomType/queryOneRoomType`,
        needToken:true,
    },
    /*新增房间类型信息*/
    addRoomTypeItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomType/addItem`,
        needToken:true,
    },
    /*编辑房间类型信息*/
    editRoomTypeItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/roomType/updateItem`,
        needToken:true,
    },
    /*批量删除房间类型信息*/
    batchDeleteRoomType: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomType/batchDeleteItem`,
        needToken:true,
    },
    /*导出房间类型信息*/
    exportRoomTypeItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomType/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------房间类型信息----end-------------------
    //---------------房间信息----start-------------------
    /*分页查询房间信息*/
    queryRoomDataByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomData/queryByPage`,
        needToken:true,
    },
    /*查询房间信息*/
    queryRoomData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomData/queryRoomData`,
        needToken:true,
    },
    /*查询单个房间信息*/
    queryOneRoomData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomData/queryOneRoomData`,
        needToken:true,
    },
    /*新增房间信息*/
    addRoomDataItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomData/addItem`,
        needToken:true,
    },
    /*编辑房间信息*/
    editRoomDataItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/roomData/updateItem`,
        needToken:true,
    },
    /*批量删除房间信息*/
    batchDeleteRoomData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomData/batchDeleteItem`,
        needToken:true,
    },
    /*导出房间信息*/
    exportRoomDataItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomData/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------房间信息----end-------------------
    //---------------房间图片信息----start-------------------
    /*分页查询房间图片信息*/
    queryRoomImgByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomImg/queryByPage`,
        needToken:true,
    },
    /*查询房间图片信息*/
    queryRoomImg: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomImg/queryRoomImg`,
        needToken:true,
    },
    /*查询单个房间图片信息*/
    queryOneRoomImg: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomImg/queryOneRoomImg`,
        needToken:true,
    },
    /*新增房间图片信息*/
    addRoomImgItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomImg/addItem`,
        needToken:true,
    },
    /*编辑房间图片信息*/
    editRoomImgItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/roomImg/updateItem`,
        needToken:true,
    },
    /*批量删除房间图片信息*/
    batchDeleteRoomImg: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomImg/batchDeleteItem`,
        needToken:true,
    },
    /*导出房间图片信息*/
    exportRoomImgItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomImg/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------房间图片信息----end-------------------
    //---------------客户身份信息----start-------------------
    /*分页查询客户身份信息*/
    queryGuestIdentifyByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/guestIdentify/queryByPage`,
        needToken:true,
    },
    /*查询客户身份信息*/
    queryGuestIdentify: {
        method: 'POST',
        url: `${httpUrl}/api/v1/guestIdentify/queryGuestIdentify`,
        needToken:true,
    },
    /*查询单个客户身份信息*/
    queryOneGuestIdentify: {
        method: 'POST',
        url: `${httpUrl}/api/v1/guestIdentify/queryOneGuestIdentify`,
        needToken:true,
    },
    /*新增客户身份信息*/
    addGuestIdentifyItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/guestIdentify/addItem`,
        needToken:true,
    },
    /*编辑客户身份信息*/
    editGuestIdentifyItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/guestIdentify/updateItem`,
        needToken:true,
    },
    /*批量删除客户身份信息*/
    batchDeleteGuestIdentify: {
        method: 'POST',
        url: `${httpUrl}/api/v1/guestIdentify/batchDeleteItem`,
        needToken:true,
    },
    /*导出客户身份信息*/
    exportGuestIdentifyItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/guestIdentify/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------客户身份信息----end-------------------
    //---------------房间预订信息----start-------------------
    /*分页查询房间预订信息*/
    queryRoomBookingByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomBooking/queryByPage`,
        needToken:true,
    },
    /*查询房间预订信息*/
    queryRoomBooking: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomBooking/queryRoomBooking`,
        needToken:true,
    },
    /*查询单个房间预订信息*/
    queryOneRoomBooking: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomBooking/queryOneRoomBooking`,
        needToken:true,
    },
    /*新增房间预订信息*/
    addRoomBookingItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomBooking/addItem`,
        needToken:true,
    },
    /*编辑房间预订信息*/
    editRoomBookingItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/roomBooking/updateItem`,
        needToken:true,
    },
    /*批量删除房间预订信息*/
    batchDeleteRoomBooking: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomBooking/batchDeleteItem`,
        needToken:true,
    },
    /*导出房间预订信息*/
    exportRoomBookingItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomBooking/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------房间预订信息----end-------------------
    //---------------客房入住信息----start-------------------
    /*分页查询客房入住信息*/
    queryRoomCheckInByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckIn/queryByPage`,
        needToken:true,
    },
    /*查询客房入住信息*/
    queryRoomCheckIn: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckIn/queryRoomCheckIn`,
        needToken:true,
    },
    /*查询单个客房入住信息*/
    queryOneRoomCheckIn: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckIn/queryOneRoomCheckIn`,
        needToken:true,
    },
    /*新增客房入住信息*/
    addRoomCheckInItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckIn/addItem`,
        needToken:true,
    },
    /*编辑客房入住信息*/
    editRoomCheckInItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/roomCheckIn/updateItem`,
        needToken:true,
    },
    /*批量删除客房入住信息*/
    batchDeleteRoomCheckIn: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckIn/batchDeleteItem`,
        needToken:true,
    },
    /*导出客房入住信息*/
    exportRoomCheckInItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckIn/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------客房入住信息----end-------------------
    //---------------客房退房信息----start-------------------
    /*分页查询客房退房信息*/
    queryRoomCheckOutByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckOut/queryByPage`,
        needToken:true,
    },
    /*查询客房退房信息*/
    queryRoomCheckOut: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckOut/queryRoomCheckOut`,
        needToken:true,
    },
    /*查询单个客房退房信息*/
    queryOneRoomCheckOut: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckOut/queryOneRoomCheckOut`,
        needToken:true,
    },
    /*新增客房退房信息*/
    addRoomCheckOutItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckOut/addItem`,
        needToken:true,
    },
    /*编辑客房退房信息*/
    editRoomCheckOutItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/roomCheckOut/updateItem`,
        needToken:true,
    },
    /*批量删除客房退房信息*/
    batchDeleteRoomCheckOut: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckOut/batchDeleteItem`,
        needToken:true,
    },
    /*导出客房退房信息*/
    exportRoomCheckOutItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/roomCheckOut/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------客房退房信息----end-------------------
    //---------------投诉建议信息----start-------------------
    /*分页查询投诉建议信息*/
    queryFeedbackDataByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/feedbackData/queryByPage`,
        needToken:true,
    },
    /*查询投诉建议信息*/
    queryFeedbackData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/feedbackData/queryFeedbackData`,
        needToken:true,
    },
    /*查询单个投诉建议信息*/
    queryOneFeedbackData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/feedbackData/queryOneFeedbackData`,
        needToken:true,
    },
    /*新增投诉建议信息*/
    addFeedbackDataItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/feedbackData/addItem`,
        needToken:true,
    },
    /*编辑投诉建议信息*/
    editFeedbackDataItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/feedbackData/updateItem`,
        needToken:true,
    },
    /*批量删除投诉建议信息*/
    batchDeleteFeedbackData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/feedbackData/batchDeleteItem`,
        needToken:true,
    },
    /*导出投诉建议信息*/
    exportFeedbackDataItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/feedbackData/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------投诉建议信息----end-------------------

};
export default globalServices;

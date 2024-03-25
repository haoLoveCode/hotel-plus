const httpUrl = process.env.BASE_API
const globalServices = {
    //-------------腾讯地图 start-------------------
    /*腾讯地图地点建议*/
    tcMapPlaceSuggestion: {
        method: 'GET',
        url: `${httpUrl}/api/v1/platform/queryPlace`,
    },
    //-------------腾讯地图 end-------------------
    //-------------订单 start-------------------
    /*查询当前用户的所有订单信息*/
    queryOrderList: {
        method: 'POST',
        url: `${httpUrl}/api/v1/tradeOrder/queryOrderList`,
    },
    /*分页查询当前用户的所有订单信息*/
    queryOrderListByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/tradeOrder/queryByPage`,
    },
    /*更新订单状态*/
    setOrderStatus: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/tradeOrder/setOrderStatus`,
    },
    //-------------订单 end-------------------
    //⽣成base64图形验证码
    base64captchaCode: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authUser/base64captchaCode`,
        needToken:false,
    },
    //验证Token
    verifyToken: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authAppUser/verifyToken`,
        needToken:false,
    },
    // 退出
    logOut: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authAppUser/logOut`,
        needToken:true,
    },
    // 用户登录
    userLogin: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authAppUser/userLogin`,
        needToken:false,
    },
    /*网页用户注册*/
    webUserReg: {
        method: 'POST',
        url: `${httpUrl}/api/v1/authAppUser/webUserReg`,
        needToken:false,
    },
    /*更新网页用户*/
    updateAppAuthUser: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/authAppUser/updateAppAuthUser`,
        needToken:false,
    },
    /*查询当前APP用户的数据*/
    currentUserMeta: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authAppUser/currentUserMeta`,
        needToken:true,
    },
    //单个文件上传
    uploadSingleFile: {
        method: 'post',
        jsonStatus: true,
        url: `${httpUrl}/api/platformFile/uploadFile`,
        needToken:true,
    },
    //单个文件删除
    deleteFileApi: {
        method: 'DELETE',
        jsonStatus: true,
        url: `${httpUrl}/api/platformFile/deleteFileByFileName`,
        needToken:true,
    },
    //根据字典类型查询字典数据
    queryDictByType: {
        method: 'GET',
        url: `${httpUrl}/api/v1/platformDict/queryDictByType`,
        needToken:false,
    },
    //查询所有用户信息
    queryAllAuthUser: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authUser/queryAllAuthUser`,
        needToken:true,
    },
    //--------------轮播图-start------------------
    /*查询所有首页轮播图*/
    queryAllMainSwiper: {
        method: 'GET',
        url: `${httpUrl}/api/v1/mainSwiper/queryAllMainSwiper`,
        needToken:false,
    },
    /*查询所有APP认证用户*/
    queryAuthAppUser: {
        method: 'GET',
        url: `${httpUrl}/api/v1/authAppUser/queryAuthAppUser`,
        needToken:false,
    },
    //--------------轮播图-end------------------
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
    //---------------公告信息----end-------------------
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

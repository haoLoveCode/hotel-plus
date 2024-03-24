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
    //---------------商品类型信息----start-------------------
    /*分页查询商品类型信息*/
    querySalesItemTypeByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItemType/queryByPage`,
        needToken:true,
    },
    /*查询商品类型信息*/
    querySalesItemType: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItemType/querySalesItemType`,
        needToken:true,
    },
    /*查询单个商品类型信息*/
    queryOneSalesItemType: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItemType/queryOneSalesItemType`,
        needToken:true,
    },
    /*新增商品类型信息*/
    addSalesItemTypeItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItemType/addItem`,
        needToken:true,
    },
    /*编辑商品类型信息*/
    editSalesItemTypeItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/salesItemType/updateItem`,
        needToken:true,
    },
    /*批量删除商品类型信息*/
    batchDeleteSalesItemType: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItemType/batchDeleteItem`,
        needToken:true,
    },
    /*导出商品类型信息*/
    exportSalesItemTypeItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItemType/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------商品类型信息----end-------------------
    //---------------商品信息----start-------------------
    /*分页查询商品信息*/
    querySalesItemByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItem/queryByPage`,
        needToken:true,
    },
    /*查询商品信息*/
    querySalesItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItem/querySalesItem`,
        needToken:true,
    },
    /*查询单个商品信息*/
    queryOneSalesItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItem/queryOneSalesItem`,
        needToken:true,
    },
    /*新增商品信息*/
    addSalesItemItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItem/addItem`,
        needToken:true,
    },
    /*编辑商品信息*/
    editSalesItemItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/salesItem/updateItem`,
        needToken:true,
    },
    /*批量删除商品信息*/
    batchDeleteSalesItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItem/batchDeleteItem`,
        needToken:true,
    },
    /*导出商品信息*/
    exportSalesItemItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItem/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------商品信息----end-------------------
    //---------------商品图片信息----start-------------------
    /*查询商品图片信息*/
    querySalesItemImg: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItemImg/querySalesItemImg`,
        needToken:true,
    },
    //---------------商品图片信息----end-------------------
    //---------------收货地址信息----start-------------------
    /*分页查询收货地址信息*/
    queryTakeAddressByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/takeAddress/queryByPage`,
        needToken:true,
    },
    /*查询收货地址信息*/
    queryTakeAddress: {
        method: 'POST',
        url: `${httpUrl}/api/v1/takeAddress/queryTakeAddress`,
        needToken:true,
    },
    /*查询单个收货地址信息*/
    queryOneTakeAddress: {
        method: 'POST',
        url: `${httpUrl}/api/v1/takeAddress/queryOneTakeAddress`,
        needToken:true,
    },
    /*新增收货地址信息*/
    addTakeAddressItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/takeAddress/addItem`,
        needToken:true,
    },
    /*编辑收货地址信息*/
    editTakeAddressItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/takeAddress/updateItem`,
        needToken:true,
    },
    /*批量删除收货地址信息*/
    batchDeleteTakeAddress: {
        method: 'POST',
        url: `${httpUrl}/api/v1/takeAddress/batchDeleteItem`,
        needToken:true,
    },
    /*导出收货地址信息*/
    exportTakeAddressItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/takeAddress/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------收货地址信息----end-------------------
    //---------------购物车信息----start-------------------
    /*分页查询购物车信息*/
    queryBasketDataByPage: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/queryByPage`,
        needToken:true,
    },
    /*查询购物车信息*/
    queryBasketData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/queryBasketData`,
        needToken:true,
    },
    /*查询单个购物车信息*/
    queryOneBasketData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/queryOneBasketData`,
        needToken:true,
    },
    /*新增购物车信息*/
    addBasketDataItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/addItem`,
        needToken:true,
    },
    /*编辑购物车信息*/
    editBasketDataItem: {
        method: 'PUT',
        url: `${httpUrl}/api/v1/basketData/updateItem`,
        needToken:true,
    },
    /*批量删除购物车信息*/
    batchDeleteBasketData: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/batchDeleteItem`,
        needToken:true,
    },
    /*导出购物车信息*/
    exportBasketDataItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/exportData`,
        needToken:true,
        responseType: 'blob',
    },
    //---------------购物车信息----end-------------------
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

};
export default globalServices;

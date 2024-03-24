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
    //---------------商品类型信息----start-------------------
    /*查询商品类型信息*/
    querySalesItemType: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItemType/querySalesItemType`,
        needToken:true,
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
    //---------------商品信息----end-------------------
    //---------------商品图片信息----start-------------------
    /*查询商品图片信息*/
    querySalesItemImg: {
        method: 'POST',
        url: `${httpUrl}/api/v1/salesItemImg/querySalesItemImg`,
        needToken:true,
    },
    //---------------商品图片信息----end-------------------
    //---------------购物车信息----start-------------------
    /*购物车业务处理*/
    handleBasketBiz: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/handleBasketBiz`,
        needToken:true,
    },
    /*查询当前用户购物车的商品信息*/
    queryBasketItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/queryBasketItem`,
        needToken:true,
    },
    /*查询当前用户购物车的单个商品信息*/
    queryOneBasketItem: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/queryOneBasketItem`,
        needToken:true,
    },
    /*商品购物车批量下单*/
    basketItemOrdering: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/basketItemOrdering`,
        needToken:true,
    },
    /*商品单个信息下单*/
    orderingOne: {
        method: 'POST',
        url: `${httpUrl}/api/v1/basketData/orderingOne`,
        needToken:true,
    },
    //---------------购物车信息----start-------------------
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
    //---------------收货地址信息----end-------------------
};
export default globalServices;

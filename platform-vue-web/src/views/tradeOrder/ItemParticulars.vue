<template>
  <el-dialog
    title=""
    width="45%"
    :center="true"
    :visible.sync="particularsVisible">
    <div class="particulars-view">
      <div class="descriptions-title-view">
        交易订单信息详情
      </div>
      <div class="descriptions-view">
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">商品信息:</el-tag>
            </div>
            <div class="descriptions-value">
              {{handleTypeByValue(particularsData.itemId,roomDataOptions)}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">下单用户信息:</el-tag>
            </div>
            <div class="descriptions-value">
              {{handleTypeByValue(particularsData.authAppUserId,authAppUserOptions)}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">订单类型:</el-tag>
            </div>
            <div class="descriptions-value">
              {{handleTypeByValue(particularsData.orderType,orderTypeOptions)}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">订单备注:</el-tag>
            </div>
            <div class="descriptions-value">
              {{particularsData.orderRemark}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">支付订单号:</el-tag>
            </div>
            <div class="descriptions-value">
              {{particularsData.outTradeNo}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">订单价格:</el-tag>
            </div>
            <div class="descriptions-value">
              {{particularsData.orderAmount}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">支付方式:</el-tag>
            </div>
            <div class="descriptions-value">
              {{handleTypeByValue(particularsData.payType,payTypeOptions)}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">订单额外数据:</el-tag>
            </div>
            <div class="descriptions-value">
              {{particularsData.extraData}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              <el-tag size="medium">订单状态:</el-tag>
            </div>
            <div class="descriptions-value">
              {{handleTypeByValue(particularsData.orderStatus,orderStatusOptions)}}
            </div>
          </div>
        </div>
      </div>
      <el-divider content-position="center" v-if="false">收货地址在线地图</el-divider>
      <div class="map-address-view">
        {{takeAddressData.addressDetailed}}
      </div>
      <div class="map-view">
        <div id="addressMapView"></div>
      </div>
    </div>
    <el-dialog append-to-body :visible.sync="previewVisible" title="图片预览">
      <img width="100%" :src="previewImageUrl" alt=""/>
    </el-dialog>
    <span slot="footer" class="dialog-footer">
      <el-button
        @click="handleCancel"
        size="mini">
        关闭
      </el-button>
    </span>
  </el-dialog>
</template>
<script>
import Api from "@/services";
import baseUtils from '@/utils/baseUtils'
let map, markerLayer, ap;
export default {
  name: "ItemParticulars",
  components: {
    Api: Api,
    baseUtils: baseUtils,
  },
  data() {
    return {
      //-----------------
      orderStatusOptions:this.$bizConstants.orderStatusOptions,
      payTypeOptions:this.$bizConstants.payTypeOptions,
      orderTypeOptions:this.$bizConstants.orderTypeOptions,
      authAppUserOptions:[],
      roomDataOptions: [],
      takeAddressData:{},
      areaSelectData: [],
      //设置联动层级(0-省市联动/1-省市区联动/2-省市区街道联动)
      areaLevel: 2,
      position: [
        {
          longitude: 0,//经度
          latitude: 0,//纬度
          city: ''
        }
      ],
      placeOptions: [],
      //-----------------
      authUserOptions: [],
      particularsVisible: false,
      particularsData: {},
      size: '',
      previewImageUrl: '',
      previewVisible: false,
    }
  },
  methods: {
    destroyMap() {
      //销毁地图
      if (map) {
        //销毁地图
        map.destroy();
        map = null;
      }
    },
    //设置当前位置定位坐标
    setCurrentLocation(position) {
      console.log('position' + JSON.stringify(position));
      this.position.latitude = position.latitude;
      this.position.longitude = position.longitude;
      this.position.city = position.city;
      setTimeout(() => {
        this.setMapData();
      }, 100);
    },
    /**
     * 位置信息在地图上展示
     */
    setMapData() {
      //如果地图存在则不进行初始化
      if (map) {
        console.log('不执行初始化地图')
        return;
      }
      console.log('addressMapView:'+document.getElementById('addressMapView'))
      //定义地图中心点坐标
      let center = new TMap.LatLng(this.position.latitude, this.position.longitude)
      //定义map变量，调用 TMap.Map() 构造函数创建地图
      map = new TMap.Map(document.getElementById('addressMapView'), {
        center: center,//设置地图中心点坐标
        zoom: 17.2,   //设置地图缩放级别
        pitch: 43.5,  //设置俯仰角
        rotation: 45    //设置地图旋转角度
      });
      //创建并初始化MultiMarker
      markerLayer = new TMap.MultiMarker({
        map: map,  //指定地图容器
        //样式定义
        styles: {
          //创建一个styleId为"myStyle"的样式（styles的子属性名即为styleId）
          "myStyle": new TMap.MarkerStyle({
            "width": 25,  // 点标记样式宽度（像素）
            "height": 35, // 点标记样式高度（像素）
            //因为JS是引入在index.html的，所以此处路径从根目录下开始
            //"src": './static/icons/marker.svg',  //图片路径
            //焦点在图片中的像素位置，一般大头针类似形式的图片以针尖位置做为焦点，圆形点以圆心位置为焦点
            "anchor": {x: 16, y: 32}
          })
        },
        //点标记数据数组
        geometries: [{
          "id": "1",   //点标记唯一标识，后续如果有删除、修改位置等操作，都需要此id
          "styleId": 'markerStyle',  //指定样式id
          "position": new TMap.LatLng(this.position.latitude, this.position.longitude),  //点标记坐标位置
          "properties": {//自定义属性
            "title": "marker1"
          }
        }
        ]
      });
      console.log(map)
    },
    async queryRoomData() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      try {
        Api.queryRoomData({}).then((res) => {
          if (res.success) {
            console.log('res:' + JSON.stringify(res))
            if (this.$isNull(res)) {
              return;
            }
            let data = res.data
            if (this.$isNull(data)) {
              return;
            }
            this.roomDataOptions = new Array();
            data.map((item) => {
              let options = {
                'text': item.roomNo,
                'value': item.roomDataId
              }
              this.roomDataOptions.push(options)
            })
            console.log('this.roomDataOptions:' + JSON.stringify(this.roomDataOptions))
            loading.close();
          } else {
            loading.close();
            this.$message.error('服务器异常');
          }
        });
      } catch (error) {
        loading.close();
        this.$message.error(error.message || error.msg || "服务器异常");
      }
    },
    handlePreView(url) {
      if (this.$isNull(url)) {
        return;
      }
      this.previewImageUrl = url
      this.previewVisible = true
    },
    handleTypeByValue(value, optionList) {
      //console.log('optionList:'+JSON.stringify(optionList))
      if (this.$isNull(value)) {
        console.log('返回空')
        return '无'
      }
      if (this.$isNull(optionList)) {
        console.log('optionList返回空')
        return '';
      }
      let result =
        optionList.find(item => item.value == value);
      //console.log('result:'+JSON.stringify(result))
      if (!this.$isNull(result)) {
        return result.text
      } else {
        return '无'
      }
    },
    showParticulars(data) {
      console.log('data:' + JSON.stringify(data))
      if (!this.$isNull(data)) {
        this.particularsData = {
          ...data
        }
      }
      this.particularsVisible = true;
    },
    async init(data) {
      this.authAppUserOptions = await this.$bizConstants.queryAuthAppUser();
      await this.queryRoomData();
      this.authUserOptions = await this.$bizConstants.authUserOptions()
      this.showParticulars(data);
    },
    handleCancel() {
      this.clearAll();
    },
    clearAll() {
      this.destroyMap();
      this.particularsVisible = false
    },
  },
  created() {

  }
}
</script>

<style scoped lang="scss">
.particulars-view {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: center;
  align-content: center;
  .descriptions-title-view {
    font-weight: bold;
    font-size: 20px;
    margin-bottom: 10px;
  }
  .map-address-view {
    margin:20px 0px;
    width: 100%;
    word-break: break-all;
  }
  .map-view {
    width: 100%;
    border: 1px solid #ea0f6d;
  }
  .descriptions-view {
    display: flex;
    flex-direction: column;
    align-items: flex-start;
    justify-content: flex-start;
    align-content: center;
    width: 100%;
    .descriptions-item {
      display: flex;
      flex-direction: row;
      align-items: flex-start;
      justify-content: space-around;
      .descriptions-item-view {
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: center;
        font-size: 15px;
        .descriptions-title {
          font-weight: bold;
          padding: 10px 5px;
        }
        .descriptions-value {
          font-weight: bold;
          padding: 10px 5px;
          word-break: break-all;
        }
      }
    }
    .table-view {
      width: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: flex-start;
      flex-wrap: wrap;
      .table-data-view {
        width: 100%;
      }
      .table-footer-view {

      }
    }
  }
}
</style>

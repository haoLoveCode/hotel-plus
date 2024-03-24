<template>
  <el-dialog title="编辑收货地址信息"
             :center="true"
             @close="handleCancel"
             :visible.sync="editVisible">
    <div class="update-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="130px"
          class="update-body-form"
      >
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="用户信息" prop="authAppUserId">
              <el-select
                  v-model="submitData.authAppUserId"
                  :clearable="true"
                  placeholder="请选择用户信息">
                <el-option
                    v-for="item in authAppUserOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="收货人姓名" prop="takerName">
              <el-input
                  v-model="submitData.takerName"
                  placeholder="请填写-收货人姓名"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="收货人手机号" prop="takerPhone">
              <el-input
                  v-model="submitData.takerPhone"
                  placeholder="请填写-收货人手机号"
                  maxlength="11"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="是否默认" prop="defaultStatus">
              <el-select
                  v-model="submitData.defaultStatus"
                  :clearable="true"
                  placeholder="请选择是否默认">
                <el-option
                    v-for="item in yesOrNoOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="text-area-view">
          <div class="update-item-view">
            <el-form-item label="详细地址" prop="addressDetailed">
              <el-select
                  style="width: 100%"
                  v-model="submitData.addressDetailed"
                  :clearable="true"
                  filterable
                  remote
                  reserve-keyword
                  @change="handlePlaceChange"
                  :remote-method="handlePlaceSearch"
                  :loading="placeSearchLoading"
                  placeholder="请选择详细地址">
                <el-option
                    v-for="item in placeOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
      </el-form>
      <el-divider content-position="center">在线地图</el-divider>
      <div class="map-view">
        <div id="updateDataMap"></div>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button
        @click="handleCancel"
        size="mini">
        取 消
      </el-button>
      <el-button
        type="primary"
        @click="handleSubmit"
        size="mini">
        提 交
      </el-button>
    </span>
  </el-dialog>
</template>

<script>
import Api from "@/services";
import baseUtils from '@/utils/baseUtils'
import UploadImg from "@/components/UploadImg";
let map, markerLayer, ap;
export default {
  components: {
    UploadImg: UploadImg
  },
  name: "ItemEdit",
  data() {
    return {
      //-----------------
      TC_MAP_KEY: this.$bizConstants.TC_MAP_KEY,
      placeSearchLoading: false,
      authAppUserOptions:[],
      yesOrNoOptions: this.$bizConstants.yesOrNoOptions,
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
      title: "编辑",
      editVisible: false,
      submitData: {
        authAppUserId: '',
        takerName: '',
        takerPhone: '',
        provinceName: '',
        cityName: '',
        districtName: '',
        postCode: '',
        placeLongitude: '',
        placeLatitude: '',
        addressDetailed: '',
        defaultStatus: '',
      },
      validatorRules: {
        authAppUserId: [
          {
            required: true,
            message: '请选择用户信息',
            trigger: 'change'
          }
        ],
        takerName: [
          {
            required: true,
            message: '请规范填写收货人姓名',
            trigger: 'blur'
          }
        ],
        takerPhone: [
          {
            required: true,
            message: '请规范填写收货人手机号',
            trigger: 'blur'
          }
        ],
        provinceName: [
          {
            required: true,
            message: '请规范填写省份名称',
            trigger: 'blur'
          }
        ],
        cityName: [
          {
            required: true,
            message: '请规范填写城市名称',
            trigger: 'blur'
          }
        ],
        districtName: [
          {
            required: true,
            message: '请规范填写区名称',
            trigger: 'blur'
          }
        ],
        postCode: [
          {
            required: true,
            message: '请规范填写邮编',
            trigger: 'blur'
          }
        ],
        placeLongitude: [
          {
            required: true,
            message: '请规范填写地点经度',
            trigger: 'blur'
          }
        ],
        placeLatitude: [
          {
            required: true,
            message: '请规范填写地点纬度',
            trigger: 'blur'
          }
        ],
        addressDetailed: [
          {
            required: true,
            message: '请规范填写详细地址',
            trigger: 'blur'
          }
        ],
        defaultStatus: [
          {
            required: true,
            message: '请选择是否默认',
            trigger: 'change'
          }
        ],
      }
    };
  },
  methods: {
    //设置区域
    async setAreaData(data) {
      if (!data) {
        return;
      }
      if (!data.provinceName) {
        return;
      }
      if (!data.cityName) {
        return;
      }
      if (!data.districtName) {
        return;
      }
      this.areaSelectData.push(String(data.provinceName));
      this.areaSelectData.push(String(data.cityName));
      this.areaSelectData.push(String(data.districtName));
      console.log('this.areaSelectData:' + JSON.stringify(this.areaSelectData))
    },
    //处理地点选择发生变化
    handlePlaceChange(value) {
      //console.log('handlePlaceChange:'+JSON.stringify(value));
      //console.log('this.placeOptions:'+JSON.stringify(this.placeOptions));
      let item = this.placeOptions.find((item) => item.value == value);
      if (!item) {
        return;
      }
      console.log('item:' + JSON.stringify(item));
      let location = item.location
      if (!location) {
        return;
      }
      //设置地图中心点和地图的标记点
      this.updateMarker(location.lat, location.lng);
      this.changeCenter(location.lat, location.lng);
      //设置提交的信息
      this.submitData.placeLatitude = location.lat;
      this.submitData.placeLongitude = location.lng;
      this.submitData.provinceName = item.province;
      this.submitData.cityName = item.city;
      this.submitData.districtName = item.district;
      this.submitData.postCode = item.adcode;
      this.submitData.addressDetailed = item.address;
    },
    //处理远程搜索
    handlePlaceSearch(value) {
      //console.log('handlePlaceSearch:'+value);
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      try {
        Api.tcMapPlaceSuggestion({
          key: this.TC_MAP_KEY,
          keyword: value
        }).then((res) => {
          //console.log('res:' + JSON.stringify(res))
          if (res.success) {
            //console.log('res:' + JSON.stringify(res))
            if (this.$isNull(res)) {
              loading.close();
              return;
            }
            let data = res.data
            if (this.$isNull(data) || data.length === 0) {
              this.placeOptions = new Array();
              loading.close();
              return;
            }
            this.placeOptions = new Array();
            data.map((item) => {
              if(!item){
                return;
              }
              let options = {
                'text': item.address,
                'value': item.id,
                'location': item.location,
                'adcode': item.adcode,
                'province': item.province,
                'city': item.city,
                'district': item.district,
                'address': item.address,
                'category': item.category,
              }
              this.placeOptions.push(options)
            })
            //console.log('this.placeOptions:' + JSON.stringify(this.placeOptions))
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
    destroyMap() {
      //销毁地图
      if (map) {
        //销毁地图
        map.destroy();
        map = null;
      }
    },
    /**
     * /定位获得当前位置信息
     */
    getCurrentLocation() {
      let geolocation = new qq.maps.Geolocation(this.TC_MAP_KEY, "名字");
      geolocation.getLocation(this.setCurrentLocation, this.showErr);
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
    //修改地图中心点
    changeCenter(latitude, longitude) {
      if (!map) {
        return;
      }
      // 设置中心点
      map.setCenter(new TMap.LatLng(latitude, longitude))
    },
    //修改点标记
    updateMarker(latitude, longitude) {
      if (!markerLayer) {
        return;
      }
      markerLayer.updateGeometries([
        {
          "styleId": "markerStyle",
          "id": "1",
          "position": new TMap.LatLng(latitude, longitude),
          "properties": {//自定义属性
            "title": "marker1"
          }
        }
      ])
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
      //定义地图中心点坐标
      let center = new TMap.LatLng(this.position.latitude, this.position.longitude)
      //定义map变量，调用 TMap.Map() 构造函数创建地图
      map = new TMap.Map(document.getElementById('updateDataMap'), {
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
    //定位失败再请求定位，测试使用
    showErr() {
      console.log("定位失败，请重试！");
      this.getCurrentLocation();
    },
    //处理展示
    async showEdit(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {
        this.submitData = {
          ...data
        }
      }
      await this.init(data);
      this.editVisible = true;
    },
    async setOtherData(data) {
      this.authAppUserOptions = await this.$bizConstants.queryAuthAppUser();
      await this.setAreaData(data);
      //设置地图中心点和地图的标记点
      let position = {
        longitude:Number(data.placeLongitude),
        latitude:Number(data.placeLatitude),
      }
      this.setCurrentLocation(position);
      //this.getCurrentLocation();
    },
    //处理初始化
    async init(data) {
      await this.setOtherData(data);
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      loading.close();
    },
    clearAll() {
      console.log('触发清除所有')
      this.destroyMap();
      this.submitData = {
        authAppUserId: '',
        takerName: '',
        takerPhone: '',
        provinceName: '',
        cityName: '',
        districtName: '',
        postCode: '',
        placeLongitude: '',
        placeLatitude: '',
        addressDetailed: '',
        defaultStatus: '',
      }
    },
    handleCancel() {
      this.editVisible = false
      this.clearAll();
    },
    //处理提交
    handleSubmit() {
      this.$refs.submitForm.validate((valid) => {
        const params = {};
        if (valid) {
          Object.assign(params, this.submitData);
          console.log("params", params);
        } else {
          this.$message.error('验证异常');
          return;
        }
        const loading = this.$loading({
          lock: true,
          text: "正在提交...",
        });
        try {
          Api.editTakeAddressItem(params).then((res) => {
            if (res.success) {
              this.editVisible = false;
              this.$emit("handleSuccess");
              this.$message({
                message: "操作成功!",
                type: "success",
              });
              this.clearAll()
            } else {
              this.$message.error('服务器异常');
            }
          });
          loading.close();
        } catch (error) {
          this.clearAll()
          loading.close();
          this.$message.error(error.message || error.msg || "服务器异常");
        }
      });
    },
  },
};
</script>
<style scoped lang="scss">
.update-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  .map-view {
    width: 80%;
    border: 1px solid #ea0f6d;
  }
  .update-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;
    .update-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: auto;
      .update-item-view {

      }
      .upload-item-view {
        .upload-item {

        }
      }
    }
    .text-area-view {
      width: 100%;
      .update-item-view {

      }
    }
    .dynamic-body-div {
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-items: flex-start;
      align-content: center;
      width: auto;
      border: 1px solid #575e57;
      margin: 2px 0px;
      width: 100%;
      .dynamic-row-view {
        display: flex;
        flex-direction: row;
        justify-content: flex-start;
        align-items: flex-start;
        align-content: center;
        width: auto;
        .dynamic-item-view {

        }
        .remove-button-view {
          margin: 10px 10px;
        }
      }
    }
  }
}
</style>

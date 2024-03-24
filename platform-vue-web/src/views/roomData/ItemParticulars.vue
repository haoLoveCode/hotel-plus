<template>
  <el-dialog
    title=""
    width="45%"
    :center="true"
    :visible.sync="particularsVisible">
    <div class="particulars-view">
      <div class="descriptions-title-view">
        房间信息详情
      </div>
      <div class="descriptions-view">
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              房间类型:
            </div>
            <div class="descriptions-value">
              {{handleTypeByValue(particularsData.roomTypeId,roomTypeOptions)}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              房间状态:
            </div>
            <div class="descriptions-value">
              {{handleTypeByValue(particularsData.roomStatus,itemStatusOptions)}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              房间展示标题:
            </div>
            <div class="descriptions-value">
              {{particularsData.roomTitle}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              房间简介:
            </div>
            <div class="descriptions-value">
              {{particularsData.briefData}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              房间编号:
            </div>
            <div class="descriptions-value">
              {{particularsData.roomNo}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              房间图片:
            </div>
            <div class="descriptions-value">
              {{particularsData.mainImg}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              房间详情图片:
            </div>
            <div class="descriptions-value">
              <el-tag size="medium" @click="viewRoomImgDataList(particularsData)">点击查看</el-tag>
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              房间楼层:
            </div>
            <div class="descriptions-value">
              {{particularsData.roomFloor}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              价格:
            </div>
            <div class="descriptions-value">
              {{particularsData.unitPrice}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              房间面积:
            </div>
            <div class="descriptions-value">
              {{particularsData.roomArea}}
            </div>
          </div>
        </div>
        <div class="descriptions-item">
          <div class="descriptions-item-view">
            <div class="descriptions-title">
              床位数量:
            </div>
            <div class="descriptions-value">
              {{particularsData.bedNum}}
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--商品图片查看弹窗-->
    <el-dialog
        title="商品图片"
        :visible.sync="roomImgListVisible"
        width="80%">
      <div class="table-view">
        <el-table
            :data="roomImgList"
            border
            :fit="true">
          <el-table-column
              prop="id"
              label="编号"
              header-align="center"
              align="center"
              width="50">
          </el-table-column>
          <el-table-column
              prop="imgUrl"
              label="图片名称"
              header-align="center"
              align="center"
          >
          </el-table-column>
          <el-table-column
              prop="imgUrl"
              label="图片查看"
              header-align="center"
              align="center"
          >
            <template v-slot="scope">
              <img v-if="scope.row.imgUrl" @click="handlePreView(handleImageUrl(scope.row.imgUrl))"
                   :src="handleImageUrl(scope.row.imgUrl)"
                   style="width: 30px;height: 30px;border-radius: 5px;"/>
              <el-tag v-else size="medium">暂无</el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
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

export default {
  name: "ItemParticulars",
  components: {
    Api: Api,
    baseUtils: baseUtils,
  },
  data() {
    return {
      //-----------------
      roomImgList: [],
      roomImgListVisible: false,
      roomTypeOptions: [],
      itemStatusOptions: [
        {
          'text':'闲置',
          'value': 1
        },
        {
          'text':'已预订',
          'value': 2
        },
        {
          'text':'维护中',
          'value': 3
        },
        {
          'text':'已入住',
          'value': 4
        },
        {
          'text':'已退住',
          'value': 5
        },
      ],
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
    //查看房间图片
    async viewRoomImgDataList(data) {
      let roomImgList = data.imgList;
      if (!roomImgList || roomImgList.length == 0) {
        this.$message.error('暂无图片');
      }
      this.roomImgList = new Array();
      roomImgList.map((item, index) => {
        console.log(`第${index}个元素为 ${item}`);
        this.roomImgList.push({
          imgUrl: item,
          id: index + 1
        })
      });
      this.roomImgListVisible = true;
    },
    async queryRoomType() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      try {
        Api.queryRoomType({}).then((res) => {
          if (res.success) {
            console.log('res:' + JSON.stringify(res))
            if (this.$isNull(res)) {
              return;
            }
            let data = res.data
            if (this.$isNull(data)) {
              return;
            }
            this.roomTypeOptions = new Array();
            data.map((item) => {
              let options = {
                'text': item.typeName,
                'value': item.roomTypeId
              }
              this.roomTypeOptions.push(options)
            })
            console.log('this.roomTypeOptions:' + JSON.stringify(this.roomTypeOptions))
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
      await this.queryRoomType();
      this.authUserOptions = await this.$bizConstants.authUserOptions()
      this.showParticulars(data);
    },
    handleCancel() {
      this.particularsVisible = false
      this.clearAll();
    },
    clearAll() {
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

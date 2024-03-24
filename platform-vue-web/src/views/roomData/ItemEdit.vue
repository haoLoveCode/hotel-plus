<template>
  <el-dialog
      title="编辑房间信息"
      :center="true"
      @close="handleCancel"
      :visible.sync="editVisible">
    <div class="data-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="130px"
          class="data-body-form"
      >
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="房间类型" prop="roomTypeId">
              <el-select
                  v-model="submitData.roomTypeId"
                  :clearable="true"
                  placeholder="请选择-房间类型信息">
                <el-option
                    v-for="(item,index) in roomTypeOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="房间状态" prop="roomStatus">
              <el-select
                  v-model="submitData.roomStatus"
                  :clearable="true"
                  placeholder="请选择-房间状态信息">
                <el-option
                    v-for="(item,index) in itemStatusOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="房间展示标题" prop="roomTitle">
              <el-input
                  v-model="submitData.roomTitle"
                  placeholder="请填写-房间展示标题"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="房间简介" prop="briefData">
              <el-input
                  v-model="submitData.briefData"
                  placeholder="请填写-房间简介"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="房间编号" prop="roomNo">
              <el-input
                  v-model="submitData.roomNo"
                  placeholder="请填写-房间编号"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="房间图片" prop="mainImg">
              <el-input
                  v-model="submitData.mainImg"
                  placeholder="请填写-房间图片"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="房间楼层" prop="roomFloor">
              <el-input
                  v-model="submitData.roomFloor"
                  placeholder="请填写-房间楼层"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="价格" prop="unitPrice">
              <el-input
                  v-model="submitData.unitPrice"
                  placeholder="请填写-价格"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="房间面积" prop="roomArea">
              <el-input
                  v-model="submitData.roomArea"
                  placeholder="请填写-房间面积"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="data-body-div">
          <div class="data-item-view">
            <el-form-item label="床位数量" prop="bedNum">
              <el-input
                  v-model="submitData.bedNum"
                  placeholder="请填写-床位数量"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
      </el-form>
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
import Editor from '@/components/editor/Editor';
export default {
  components: {
    UploadImg: UploadImg,
    Editor: Editor,
  },
  name: "ItemEdit",
  data() {
    return {
      //-----------------
      editorContent:'',
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
      title: "编辑",
      editVisible: false,
      submitData: {
        roomTypeId: '',
        roomStatus: '',
        roomTitle: '',
        briefData: '',
        roomNo: '',
        mainImg: '',
        roomFloor: '',
        unitPrice: '',
        roomArea: '',
        bedNum: '',
        imgList:[],
      },
      validatorRules: {
        roomTypeId: [
          {
            required: true,
            message: '请选择-房间类型',
            trigger: 'change'
          }
        ],
        roomStatus: [
          {
            required: true,
            message: '请选择-房间状态',
            trigger: 'change'
          }
        ],
        roomTitle: [
          {
            required: true,
            message: '请规范填写-房间展示标题',
            trigger: 'blur'
          }
        ],
        briefData: [
          {
            required: true,
            message: '请规范填写-房间简介',
            trigger: 'blur'
          }
        ],
        roomNo: [
          {
            required: true,
            message: '请规范填写-房间编号',
            trigger: 'blur'
          }
        ],
        mainImg: [
          {
            required: true,
            message: '请规范填写-房间图片',
            trigger: 'blur'
          }
        ],
        roomFloor: [
          {
            required: true,
            message: '请规范填写-房间楼层',
            trigger: 'blur'
          }
        ],
        unitPrice: [
          {
            required: true,
            message: '请规范填写-价格',
            pattern: new RegExp(baseUtils.numberPattern(), "g"),
            trigger: 'blur'
          }
        ],
        roomArea: [
          {
            required: true,
            message: '请规范填写-房间面积',
            pattern: new RegExp(baseUtils.numberPattern(), "g"),
            trigger: 'blur'
          }
        ],
        bedNum: [
          {
            required: true,
            message: '请规范填写-床位数量',
            pattern: new RegExp(baseUtils.numberPattern(), "g"),
            trigger: 'blur'
          }
        ],
      }
    };
  },
  methods: {
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
    //处理展示主图
    async setMainImgUrl(data) {
      if (!data) {
        return;
      }
      let mainImg = data.mainImg
      if (!mainImg) {
        return;
      }
      let mainImgUid = baseUtils.randomString()
      this.mainImgList = this.$isNull(this.mainImgList) ?
          new Array() : this.mainImgList
      if (this.$isNull(mainImg)) {
        return;
      }
      let item = {
        status: 'success',
        name: mainImg,
        percentage: 100,
        uid: mainImgUid,
        raw: {
          uid: mainImgUid
        },
        url: this.handleImageUrl(mainImg)
      }
      this.mainImgList.push(item)
    },
    //处理展示详情图片
    async setItemImgUrl(data) {
      if (!data) {
        return;
      }
      console.log('setSaleItemImgUrl:' + JSON.stringify(data))
      let imgList = data.imgList
      if (!imgList || imgList.length === 0) {
        return;
      }
      console.log('imgList:' + JSON.stringify(imgList))
      console.log(' this.imgList:' + JSON.stringify(this.imgList))
      this.imgList = this.$isNull(this.imgList) ?
          new Array() : this.imgList
      imgList.map((item) => {
        let imgUid = baseUtils.randomString()
        let imgItem = {
          status: 'success',
          name: item,
          percentage: 100,
          uid: imgUid,
          raw: {
            uid: imgUid
          },
          url: this.handleImageUrl(item)
        }
        this.imgList.push(imgItem)
      })
      console.log(' this.imgList:' + JSON.stringify(this.imgList))
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
      await this.setItemImgUrl(data);
      await this.setMainImgUrl(data);
      await this.queryRoomType();
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
      this.submitData = {
        roomTitle: '',
        briefData: '',
        roomNo: '',
        mainImg: '',
        roomFloor: '',
        unitPrice: '',
        roomArea: '',
        bedNum: '',
        imgList:[],
      }
      this.editorContent = '';
      this.mainImgList = [];
      this.imgList = [];
      //清除图片
      if (this.$baseUtils.isNull(this.$refs.uploadMainImgRef)) {
        this.$refs.uploadMainImgRef.clearFiles()
      }
      //清除图片
      if (this.$baseUtils.isNull(this.$refs.salesItemImgRef)) {
        this.$refs.salesItemImgRef.clearFiles()
      }
    },
    handleCancel() {
      this.editVisible = false
      this.clearAll();
    },
    //提交之前的处理措施
    async beforeSubmit(){
      let imgList = this.imgList;
      console.log('imgList:'+JSON.stringify(imgList))
      if (!imgList || imgList.length === 0) {
        return;
      }
      this.submitData.imgList = new Array();
      imgList.map((item) => {
        this.submitData.imgList.push(item.name);
      })
    },
    //处理提交
    async handleSubmit() {
      await this.beforeSubmit();
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
          Api.editRoomDataItem(params).then((res) => {
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
.data-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  .data-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;
    .data-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: auto;
      .data-item-view {

      }
      .upload-item-view {
        .upload-item {

        }
      }
    }
    .text-area-view {
      width: 100%;
      .data-item-view {

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

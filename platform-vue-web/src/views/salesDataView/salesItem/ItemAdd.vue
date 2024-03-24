<template>
  <el-dialog title="新增商品信息"
             :center="true"
             @close="handleCancel"
             :visible.sync="addVisible">
    <div class="add-body">
      <el-form
        ref="submitForm"
        :model="submitData"
        :rules="validatorRules"
        label-width="130px"
        class="add-body-form"
      >
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="商品类型" prop="typeItemId">
              <el-select
                  v-model="submitData.typeItemId"
                  :clearable="true"
                  placeholder="请选择商品类型">
                <el-option
                    v-for="item in salesItemTypeOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="发布人" prop="publisherId">
              <el-select
                  v-model="submitData.publisherId"
                  :clearable="true"
                  placeholder="请选择发布人">
                <el-option
                    v-for="item in authAppUserOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
        </div>
        <div class="text-area-view">
          <div class="add-item-view">
            <el-form-item label="商品名称" prop="itemName">
              <el-input
                  type="textarea"
                  :rows="5"
                  v-model="submitData.itemName"
                  placeholder="请填写-商品名称"
                  maxlength="20"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="text-area-view">
          <div class="add-item-view">
            <el-form-item label="商品标题" prop="itemTitle">
              <el-input
                  type="textarea"
                  :rows="5"
                  v-model="submitData.itemTitle"
                  placeholder="请填写-商品标题"
                  maxlength="200"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="text-area-view">
          <div class="add-item-view">
            <el-form-item label="备注信息" prop="remarkData">
              <el-input
                  type="textarea"
                  :rows="2"
                  v-model="submitData.remarkData"
                  placeholder="请填写-备注信息"
                  maxlength="200"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="销售价格" prop="salePrice">
              <el-input
                  v-model="submitData.salePrice"
                  placeholder="请填写-销售价格"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="原价" prop="originalPrice">
              <el-input
                  v-model="submitData.originalPrice"
                  placeholder="请填写-原价"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="商品状态" prop="itemStatus">
              <el-select
                  v-model="submitData.itemStatus"
                  :clearable="true"
                  placeholder="请选择商品状态">
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
        <el-divider content-position="center">请填写详情介绍</el-divider>
        <div class="editor-div">
          <Editor @change="getEditorContent" :value="editorContent" :isClear="true"></Editor>
        </div>
        <el-divider content-position="center">上传商品图片</el-divider>
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="商品图片:">
              <div class="upload-item-view">
                <UploadImg
                    class="upload-item"
                    ref="uploadMainImgRef"
                    :uploadApi="uploadUrl"
                    :fileList="mainImgList"
                    @subBack="uploadMainImgSuccess"
                    @handleFileDelete="handleMainImgDelete"
                    :limit="1"
                    :headers="uploadHeaders"
                />
              </div>
            </el-form-item>
          </div>
        </div>
        <el-divider content-position="center">上传商品详情图片</el-divider>
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="商品详情图片:">
              <div class="upload-item-view">
                <UploadImg
                    class="upload-item"
                    ref="salesItemImgRef"
                    :uploadApi="uploadUrl"
                    :fileList="imgList"
                    @subBack="uploadSalesImgSuccess"
                    @handleFileDelete="handleSalesImgDelete"
                    :limit="5"
                    :headers="uploadHeaders"
                />
              </div>
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
  name: "ItemAdd",
  data() {
    return {
      //-----------------
      editorContent: '',
      authAppUserOptions:[],
      salesItemTypeOptions:[],
      yesOrNoOptions:[
        {
          'text':'上架',
          'value': 1
        },
        {
          'text':'下架',
          'value': 2
        },
      ],
      //-----------------
      title: "新增",
      addVisible: false,
      submitData: {
        typeItemId: '',
        publisherId: '',
        itemName: '',
        itemSummary: '',
        itemTitle: '',
        salePrice: '',
        mainImg: '',
        itemStatus: '',
        remarkData: '',
        imgList:[],
      },
      mainImgList:[],
      imgList:[],
      uploadUrl: process.env.BASE_API + `/api/platformFile/uploadFile`,
      deleteFileUrl: process.env.BASE_API + `/api/platformFile/deleteFileByFileName`,
      uploadHeaders: {
        token: localStorage.getItem('token') || ''
      },
      validatorRules: {
        typeItemId: [
          {
            required: true,
            message: '请选择商品类型',
            trigger: 'change'
          }
        ],
        publisherId: [
          {
            required: true,
            message: '请选择发布人',
            trigger: 'change'
          }
        ],
        itemName: [
          {
            required: true,
            message: '请规范填写商品名称',
            trigger: 'blur'
          }
        ],
        itemSummary: [
          {
            required: true,
            message: '请规范填写商品简介',
            trigger: 'blur'
          }
        ],
        itemTitle: [
          {
            required: true,
            message: '请规范填写商品标题',
            trigger: 'blur'
          }
        ],
        salePrice: [
          {
            required: true,
            message: '请规范填写销售价格',
            pattern: new RegExp(baseUtils.numberPattern(), "g"),
            trigger: 'blur'
          }
        ],
        originalPrice: [
          {
            required: true,
            message: '请规范填写原价格',
            pattern: new RegExp(baseUtils.numberPattern(), "g"),
            trigger: 'blur'
          }
        ],
        mainImg: [
          {
            required: true,
            message: '请规范填写商品图片',
            trigger: 'blur'
          }
        ],
        itemStatus: [
          {
            required: true,
            message: '请选择商品状态',
            trigger: 'change'
          }
        ],
        remarkData: [
          {
            required: true,
            message: '请规范填写备注信息',
            trigger: 'blur'
          }
        ],
      }
    };
  },
  methods: {
    //拿到富文本内容
    getEditorContent(textValue) {
      this.editorContent = textValue
      console.log('textValue : ' + textValue)
      console.log('editorContent : ' + this.editorContent)
    },
    // 上传图片回调
    uploadSalesImgSuccess(fileName, file) {
      if (this.$isNull(fileName)) {
        return
      }
      if (fileName.length === 0) {
        return;
      }
      if (this.$isNull(file)) {
        return
      }
      //随机数
      let uuId = baseUtils.randomString();
      //文件名称
      let fileNameItem = {
        status: 'success',
        name: fileName,
        percentage: 100,
        uid: uuId,
        raw: {
          uid: uuId
        },
        url: this.handleImageUrl(fileName)
      }
      this.imgList.push(fileNameItem);
      console.log('上传成功后的文件集合:' + JSON.stringify(this.imgList))
    },
    async handleSalesImgDelete(fileName) {
      console.log('删除文件fileName:' + fileName);
      if (this.$isNull(fileName)) {
        return
      }
      const index = this.imgList.findIndex((item) => {
        return item.name == fileName
      })
      this.imgList.splice(index, 1);
      const res = await Api.deleteFileApi({
        name: fileName
      });
      console.log('删除文件结果:' + JSON.stringify(res))
      if (res.success) {
        console.log('删除成功')
      }
      console.log('删除后的文件集合:' + this.imgList);
    },
    // 上传图片回调
    uploadMainImgSuccess(fileName, file) {
      if (this.$isNull(fileName)) {
        return
      }
      if (fileName.length === 0) {
        return;
      }
      if (this.$isNull(file)) {
        return
      }
      //随机数
      let uuId = baseUtils.randomString();
      //文件名称
      let fileNameItem = {
        status: 'success',
        name: fileName,
        percentage: 100,
        uid: uuId,
        raw: {
          uid: uuId
        },
        url: this.handleImageUrl(fileName)
      }
      this.mainImgList.push(fileNameItem);
      this.submitData.mainImg = fileName
      console.log('上传成功后的文件集合:' + JSON.stringify(this.mainImgList))
    },
    async handleMainImgDelete(fileName) {
      console.log('删除文件fileName:' + fileName);
      if (this.$isNull(fileName)) {
        return
      }
      const index = this.mainImgList.findIndex((item) => {
        return item.name == fileName
      })
      this.mainImgList.splice(index, 1);
      const res = await Api.deleteFileApi({
        name: fileName
      });
      console.log('删除文件结果:' + JSON.stringify(res))
      if (res.success) {
        console.log('删除成功')
      }
      console.log('删除后的文件集合:' + this.mainImgList);
      this.submitData.mainImg = '';
    },
    async querySalesItemType() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      try {
        Api.querySalesItemType({}).then((res) => {
          if (res.success) {
            console.log('res:' + JSON.stringify(res))
            if (this.$isNull(res)) {
              return;
            }
            let data = res.data
            if (this.$isNull(data)) {
              return;
            }
            this.salesItemTypeOptions = new Array();
            data.map((item) => {
              let options = {
                'text': item.typeName,
                'value': item.salesItemTypeId
              }
              this.salesItemTypeOptions.push(options)
            })
            console.log('this.salesItemTypeOptions:' + JSON.stringify(this.salesItemTypeOptions))
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
    //处理展示
    showAdd(data) {
      console.log('data:' + JSON.stringify(data))
      if (data) {

      }
      this.init();
      this.addVisible = true;
    },
    async setOtherData() {
      await this.querySalesItemType();
      this.authAppUserOptions = await this.$bizConstants.queryAuthAppUser();
    },
    //处理初始化
    async init() {
      await this.setOtherData();
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
        typeItemId: '',
        publisherId: '',
        itemName: '',
        itemSummary: '',
        itemTitle: '',
        salePrice: '',
        mainImg: '',
        itemStatus: '',
        remarkData: '',
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
      this.addVisible = false
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
      let imgList = this.submitData.imgList;
      console.log('imgList:'+JSON.stringify(imgList))
      if (!imgList || imgList.length === 0) {
        this.$message.error('请上传商品详情图片');
        return;
      }
      this.$refs.submitForm.validate((valid) => {
        const params = {};
        if (valid) {
          if (!this.$isNull(this.editorContent)) {
            this.submitData.itemSummary = this.editorContent
          } else {
            this.$message.error('请填写详情内容');
          }
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
          Api.addSalesItemItem(params).then((res) => {
            if (res.success) {
              this.addVisible = false;
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
.add-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  .add-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;
    .editor-div{
      width: 100%;
      height: auto;
    }
    .add-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: auto;
      .add-item-view {

      }
      .upload-item-view {
        .upload-item {

        }
      }
    }
    .text-area-view {
      width: 100%;
      .add-item-view {
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

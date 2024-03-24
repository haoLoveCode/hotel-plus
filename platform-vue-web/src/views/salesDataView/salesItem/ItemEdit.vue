<template>
  <el-dialog title="编辑商品信息"
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
          <div class="update-item-view">
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
          <div class="update-item-view">
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
          <div class="update-item-view">
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
          <div class="update-item-view">
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
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="销售价格" prop="salePrice">
              <el-input
                  v-model="submitData.salePrice"
                  placeholder="请填写-销售价格"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="原价" prop="originalPrice">
              <el-input
                  v-model="submitData.originalPrice"
                  placeholder="请填写-原价"
                  maxlength="10"
                  show-word-limit>
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
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
        <div class="update-body-div">
          <div class="update-item-view">
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
        <div class="update-body-div">
          <div class="update-item-view">
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
  name: "ItemEdit",
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
      title: "编辑",
      editVisible: false,
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
    //处理展示主图
    async setSaleMainImgUrl(data) {
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
    async setSaleItemImgUrl(data) {
      if (!data) {
        return;
      }
      console.log('setAnimalImgUrl:' + JSON.stringify(data))
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
        this.editorContent = data.itemSummary;
      }
      await this.init(data);
      this.editVisible = true;
    },
    async setOtherData(data) {
      await this.setSaleMainImgUrl(data);
      await this.setSaleItemImgUrl(data);
      await this.querySalesItemType();
      this.authAppUserOptions = await this.$bizConstants.queryAuthAppUser();
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
          Api.editSalesItemItem(params).then((res) => {
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
  .editor-div{
    width: 100%;
    height: auto;
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

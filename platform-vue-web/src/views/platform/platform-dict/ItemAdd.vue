<template>
    <el-dialog title="新增字典信息" :center="true" :visible.sync="addVisible">
        <div class="add-body">
            <el-form
                ref="submitForm"
                :model="submitData"
                :rules="validatorRules"
                label-width="100px"
                class="add-body-form"
            >
                <div class="add-body-div">
                    <div class="add-item-view">
                        <el-form-item label="字典类型" prop="dictType">
                            <el-input
                                v-model="submitData.dictType"
                                placeholder="请填写-业务字典类型">
                            </el-input>
                        </el-form-item>
                    </div>
                    <div class="add-item-view">
                        <el-form-item label="键" prop="dictKey">
                            <el-input
                                v-model="submitData.dictKey"
                                placeholder="请填写-键">
                            </el-input>
                        </el-form-item>
                    </div>
                    <div class="add-item-view">
                        <el-form-item label="值" prop="dictValue">
                            <el-input
                                v-model="submitData.dictValue"
                                placeholder="请填写-值">
                            </el-input>
                        </el-form-item>
                    </div>
                </div>
                <div class="add-body-div">
                    <div class="add-item-view">
                        <el-form-item label="字典排序" prop="sortIndex">
                            <el-input
                                v-model="submitData.sortIndex"
                                placeholder="请填写-排序字段">
                            </el-input>
                        </el-form-item>
                    </div>
                    <div class="add-item-view">
                        <el-form-item label="字典备注" prop="dictRemark">
                            <el-input
                                v-model="submitData.dictRemark"
                                placeholder="请填写-字典备注">
                            </el-input>
                        </el-form-item>
                    </div>
                </div>
                <el-divider v-if="false" content-position="center">上传图片</el-divider>
                <div class="add-body-div" v-if="false">
                    <div class="add-item-view">
                        <el-form-item label="封面图:">
                            <div class="upload-item-view">
                                <UploadImg
                                    class="upload-item"
                                    ref="uploadImgCover"
                                    :uploadApi="uploadUrl"
                                    :fileList="submitData.coverImageList"
                                    @subBack="uploadCoverImageSuccess"
                                    @handleFileDelete="uploadCoverImageDelete"
                                    :limit="1"
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
                size="mini"
                @click="handleCancel">
                取 消
            </el-button>
            <el-button
                type="primary"
                size="mini"
                @click="handleSubmit">
                提 交
            </el-button>
        </span>
    </el-dialog>
</template>
<script>
import Api from "@/services";
import baseUtils from '@/utils/baseUtils'
import UploadImg from "@/components/UploadImg";

export default {
    components: {
        UploadImg: UploadImg
    },
    name: "ItemAdd",
    data() {
        return {
            title: "新增",
            addVisible: false,
            roleList: [],
            editId: null,
            submitData: {
                dictType: '',
                dictKey: '',
                dictValue: '',
                sortIndex: '',
                dictRemark: '',
            },
            uploadUrl: process.env.BASE_API + `/api/platformFile/uploadFile`,
            deleteFileUrl: process.env.BASE_API + `/api/platformFile/deleteFileByFileName`,
            uploadHeaders: {
                token: localStorage.getItem('token') || ''
            },
            validatorRules: {
                dictType: [
                    {required: true, message: '请规范填写业务字典类型', trigger: 'blur'}
                ],
                dictKey: [
                    {required: true, message: '请规范填写键', trigger: 'blur'}
                ],
                dictValue: [
                    {required: true, message: '请规范填写值', trigger: 'blur'}
                ],
                sortIndex: [
                    {
                        required: true,
                        message: '请规范填写排序字段',
                        pattern: new RegExp(baseUtils.numberPattern(), "g"),
                        trigger: 'blur'
                    }
                ],
                dictRemark: [
                    {required: true, message: '请规范填写字典备注', trigger: 'blur'}
                ],
            }
        };
    },
    methods: {
        // 上传图片回调
        uploadCoverImageSuccess(resp, file) {
            console.log(resp);
            console.log(file);
            if (this.$isNull(resp)) {
                return
            }
            if (!resp.success) {
                return;
            }
            let data = resp.data
            console.log('上传图片结果:' + JSON.stringify(file));
            console.log('上传图片结果:' + JSON.stringify(resp));
            this.submitData.coverImageUrl = data.fileName
            //console.log('fileItem:'+JSON.stringify(fileItem))
            this.submitData.coverImageList.push(file);
            console.log(this.submitData.coverImageUrl);
        },
        async uploadCoverImageDelete(url) {
            console.log(url);
            if (this.$isNull(url)) {
                return
            }
            const index = this.submitData.coverImageList.findIndex((el) => {
                return el === url
            })
            this.submitData.coverImageList.splice(index, 1);
            const res = await Api.deleteFileApi({
                name: this.submitData.coverImageUrl
            });
            console.log('删除图片结果:' + JSON.stringify(res))
            if (res.success) {
                console.log('删除成功')
            }
            this.submitData.coverImageUrl = ''
            console.log(this.submitData.coverImageUrl);
            console.log(index);
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
            /*try {
              const res = await Api.roleListAll();
              if (res.success) {
                this.roleList = res.items;
              }
              loading.close();
            } catch (error) {
              this.$message.error(error.message || error.msg || "服务器异常");
              loading.close();
            }*/
        },
        clearAll() {
            console.log('触发清除所有')
            this.submitData = {
                dictType: '',
                dictKey: '',
                dictValue: '',
                sortIndex: '',
                dictRemark: '',
            }
            //清除图片
            /*if(this.$baseUtils.isNull(this.$refs.uploadImgCover)){
              this.$refs.uploadImgCover.clearFiles()
            }
            if(this.$baseUtils.isNull(this.$refs.uploadImgBgImage)){
              this.$refs.uploadImgBgImage.clearFiles()
            }*/
        },
        handleCancel() {
            this.addVisible = false
            this.clearAll();
        },
        //处理提交
        handleSubmit() {
            this.$refs.submitForm.validate((valid) => {
                const params = {};
                if (valid) {
                    Object.assign(params, this.submitData);
                } else {
                    this.$message.error('验证异常');
                    return;
                }
                const loading = this.$loading({
                    lock: true,
                    text: "正在提交...",
                });
                try {
                    Api.addPlatformDictItem(params).then((res) => {
                        if (res.success) {
                            this.addVisible = false;
                            this.$emit("handleSuccess");
                            this.$message({
                                message: "操作成功!",
                                type: "success",
                            });
                        } else {
                            this.$message.error('服务器异常');
                        }
                        this.clearAll()
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

    .add-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;

      .add-item-view {

      }

      .upload-item-view {
        .upload-item {
        }
      }
    }
  }
}
</style>

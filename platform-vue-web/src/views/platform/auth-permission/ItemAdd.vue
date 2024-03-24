<template>
  <el-dialog title="新增系统权限" :center="true" :visible.sync="addVisible">
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
            <div class="add-item-view">
              <el-form-item label="权限名称" prop="permissionName">
                <el-input
                    v-model="submitData.permissionName"
                    placeholder="请填写-权限名称">
                </el-input>
              </el-form-item>
            </div>
          </div>
          <div class="add-item-view">
            <div class="add-item-view">
              <el-form-item label="权限路由" prop="permissionPath">
                <el-input
                    v-model="submitData.permissionPath"
                    placeholder="请填写-权限路由">
                </el-input>
              </el-form-item>
            </div>
          </div>
          <div class="add-item-view">
            <el-form-item label="权限编码" prop="permissionCode">
              <el-input
                  v-model="submitData.permissionCode"
                  placeholder="请填写-权限编码">
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="权限类型" prop="permissionType">
              <el-select
                  v-model="submitData.permissionType"
                  :clearable="true"
                  @change="handlePermissionTypeChange"
                  placeholder="请选择权限类型">
                <el-option
                    v-for="item in permissionTypeOptions"
                    :key="item.value"
                    :label="item.text"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="排序" prop="sortIndex">
              <el-input
                  v-model="submitData.sortIndex"
                  placeholder="请填写-排序">
              </el-input>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="备注" prop="permissionRemark">
              <el-input
                  v-model="submitData.permissionRemark"
                  placeholder="请填写-备注">
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="add-body-div">
          <div class="add-item-view">
            <el-form-item label="上级权限" prop="parentId">
              <el-button
                  type="text"
                  @click="parentPermVisible = true"
                  size="mini">
                {{ submitData.parentPermissionName }}
              </el-button>
            </el-form-item>
          </div>
          <div class="add-item-view">
            <el-form-item label="图标" prop="permissionIcon">
              <el-button
                  v-if="$isNull(submitData.permissionIcon)" type="text"
                  @click="permIconVisible = true" size="mini">选择图标
              </el-button>
              <i v-else :class="submitData.permissionIcon" @click="permIconVisible = true"
                 style="font-size: 30px;color: #589df8;"></i>
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
    <el-dialog
        title="选择图标"
        :visible.sync="permIconVisible"
        :append-to-body="true"
        width="80%"
        :before-close="handleMenuIconCancel">
      <div class="menu-icon-view">
        <div class="icon-item-view" @click="handleMenuIconSelect(item)" v-for="(item,index) in iconList"
             :key="index">
          <i :class="item" style="margin:3px 3px;font-size: 20px;color: #589df8;"></i>
        </div>
      </div>
    </el-dialog>
    <el-dialog
        title="系统所有权限"
        :visible.sync="parentPermVisible"
        width="30%"
        append-to-body
        :before-close="handleParentPermSelect">
      <el-tree
          :data="permissionTreeNode"
          show-checkbox
          node-key="authPermissionId"
          ref="permissionTree"
          :default-checked-keys="[]"
          highlight-current
          :check-strictly="true"
          :props="permissionTreeNodeProps">
      </el-tree>
      <span slot="footer" class="dialog-footer">
          <el-button
              @click="handleParentPermSelect"
              size="mini">
              关 闭
          </el-button>
          <el-button
              type="primary"
              @click="handleParentPermSelect"
              size="mini">
              确 定
          </el-button>
      </span>
    </el-dialog>
  </el-dialog>
</template>

<script>
import Api from "@/services";
import baseUtils from '@/utils/baseUtils'
import iconList from '@/utils/iconList'
import UploadImg from "@/components/UploadImg";

export default {
  components: {
    UploadImg: UploadImg
  },
  name: "ItemAdd",
  data() {
    return {
      iconList: iconList.ICON_LIST,
      permissionTypeOptions: this.$bizConstants.permissionTypeOptions,
      title: "新增菜单权限",
      addVisible: false,
      permIconVisible: false,
      parentPermVisible: false,
      parentNodeDisable: false,
      permissionNodeOptions: [],
      permissionTreeNodeProps: {
        children: 'children',
        label: 'permissionName'
      },
      permissionTreeNode: [],
      submitData: {
        parentId: '',
        parentPermissionName: '上级菜单',
        permissionType: '',
        sortIndex: '',
        permissionIcon: '',
        permissionName: '',
        permissionPath: '',
        permissionCode: '',
        permissionRemark: '',
      },
      uploadUrl: process.env.BASE_API + `/api/platformFile/uploadFile`,
      deleteFileUrl: process.env.BASE_API + `/api/platformFile/deleteFileByFileName`,
      uploadHeaders: {
        token: localStorage.getItem('token') || ''
      },
      validatorRules: {
        parentId: [
          {required: false, message: '请规范填写上级权限', trigger: 'change'}
        ],
        permissionType: [
          {required: true, message: '请规范填写权限类型', trigger: 'change'}
        ],
        sortIndex: [
          {
            required: true,
            message: '请规范填写排序',
            pattern: new RegExp(baseUtils.numberPattern(), "g"),
            trigger: 'blur'
          }
        ],
        permissionCode: [
          {required: true, message: '请规范填写权限编码', trigger: 'blur'}
        ],
        permissionName: [
          {required: true, message: '请规范填写权限名称', trigger: 'blur'}
        ],
        permissionPath: [
          {required: true, message: '请规范填写权限路由', trigger: 'blur'}
        ],
        permissionRemark: [
          {required: true, message: '请规范填写备注', trigger: 'blur'}
        ],
        authPermission: [
          {required: true, message: '请规范填写业务主键ID', trigger: 'blur'}
        ],
      }
    };
  },
  methods: {
    handleMenuIconCancel() {
      this.permIconVisible = false
    },
    handleMenuIconSelect(data) {
      if (!this.$isNull(data)) {
        this.submitData.permissionIcon = data
      }
      this.permIconVisible = false
    },
    beforeMenuIconSelect() {
      this.permIconVisible = false
    },
    handleParentPermSelect() {
      let parentNode = this.$refs.permissionTree.getCheckedNodes()
      if (!parentNode || parentNode.length == 0) {
        this.parentPermVisible = false
        this.setDefaultParentPerm();
        return;
      }
      if (parentNode.length > 1) {
        this.$message.error(`上级菜单只能选择1个`)
        return;
      }
      //选择第一个 因为上级菜单只能选择1个
      let selectedItem = parentNode[0]
      this.submitData.parentId = selectedItem.authPermissionId
      this.submitData.parentPermissionName = selectedItem.permissionName
      this.parentPermVisible = false
    },
    setDefaultParentPerm() {
      this.submitData.parentId = ''
      this.submitData.parentPermissionName = '上级菜单'
    },
    handlePermissionTypeChange(value) {
      this.submitData.permissionType = value
    },
    async queryPermissionTreeList() {
      const res = await Api.queryPermissionTreeList({});
      if (res.success) {
        let data = res.data;
        if (data) {
          let that = this
          that.permissionTreeNode = data
        }
      }
    },
    async queryNodeList() {
      const res = await Api.queryAllPermissionNodeList({});
      if (res.success) {
        let data = res.data;
        if (data) {
          let that = this
          data.map(item => {
            let option = {
              'text': item.permissionName,
              'value': item.authPermissionId
            }
            that.permissionNodeOptions.push(option);
          })
        }
      }
    },
    //处理展示
    showAdd(data) {
      if (data) {
        this.submitData.parentId = data.authPermissionId
      }
      this.init();
      this.addVisible = true;
    },
    async setOtherData() {
      await this.queryPermissionTreeList();
      await this.queryNodeList();
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
        parentId: '',
        permissionType: '',
        sortIndex: '',
        permissionName: '',
        permissionPath: '',
        permissionCode: '',
        permissionRemark: '',
        permissionIcon: '',
      }
      this.setDefaultParentPerm();
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
          Api.addAuthPermissionItem(params).then((res) => {
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
.menu-icon-view {
  margin: 10px 50px;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-items: flex-start;
  flex-wrap: wrap;
  border-radius: 10px;
  border: 2px solid #5187cc;
  //height: auto;
  .icon-item-view {
    //background-color: #5187cc;
    //border: 1px solid red;
    margin: 3px 3px;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    align-items: flex-end;
  }
}

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

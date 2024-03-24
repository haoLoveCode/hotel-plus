<template>
  <el-dialog title="编辑系统角色" :center="true" :visible.sync="editVisible">
    <div class="update-body">
      <el-form
          ref="submitForm"
          :model="submitData"
          :rules="validatorRules"
          label-width="100px"
          class="update-body-form"
      >
        <div class="update-body-div">
          <div class="update-item-view">
            <el-form-item label="角色名称" prop="roleName">
              <el-input
                  v-model="submitData.roleName"
                  placeholder="请填写-角色名称">
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="角色编码" prop="roleCode">
              <el-input
                  v-model="submitData.roleCode"
                  placeholder="请填写-角色编码">
              </el-input>
            </el-form-item>
          </div>
          <div class="update-item-view">
            <el-form-item label="描述信息" prop="roleRemark">
              <el-input
                  v-model="submitData.roleRemark"
                  placeholder="请填写-描述信息">
              </el-input>
            </el-form-item>
          </div>
        </div>
        <div class="tree-body-div">
          <div class="tree-body-title">
              权限信息
          </div>
          <div class="tree-text-view">
            请选择角色对应的权限信息
          </div>
        </div>
        <el-divider content-position="center">系统权限信息</el-divider>
        <div class="tree-data-view">
          <div class="tree-item-data">
            <el-tree
                ref="permissionTree"
                @check-change="handleCheckChange"
                :data="permissionTreeList"
                show-checkbox
                :check-strictly="true"
                node-key="authPermissionId"
                :default-checked-keys="rolePermissionIdList"
                :props="treeProps">
            </el-tree>
          </div>
        </div>
        <el-dialog
            title="编辑系统角色"
            :center="true"
            :visible.sync="permissionVisible">
        </el-dialog>
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

export default {
  components: {
    UploadImg: UploadImg
  },
  name: "ItemEdit",
  data() {
    return {
      //------------------------------------
      treeProps: {
        children: 'children',
        label: 'permissionName'
      },
      //权限树集合
      permissionTreeList: [],
      //角色具备的权限ID信息
      rolePermissionIdList: [],
      //角色具备的权限名称信息
      permissionNameList: [],
      //权限类型
      permissionTypeOptions: this.$bizConstants.permissionTypeOptions,
      //------------------------------------
      title: "编辑",
      permissionVisible: false,
      editVisible: false,
      submitData: {
        roleName: '',
        roleCode: '',
        roleRemark: '',
        authPermissionList: [],
      },
      uploadUrl: process.env.BASE_API + `/api/platformFile/uploadFile`,
      deleteFileUrl: process.env.BASE_API + `/api/platformFile/deleteFileByFileName`,
      uploadHeaders: {
        token: localStorage.getItem('token') || ''
      },
      validatorRules: {
        roleName: [
          {required: true, message: '请规范填写角色名称', trigger: 'blur'}
        ],
        roleCode: [
          {required: true, message: '请规范填写角色编码', trigger: 'blur'}
        ],
        roleRemark: [
          {required: true, message: '请规范填写描述信息', trigger: 'blur'}
        ],
      }
    };
  },
  methods: {
    handleTypeByValue(value, optionList) {
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
      if (!this.$isNull(result)) {
        return result.text
      } else {
        return '无'
      }
    },
    handleCheckChange(data, checked, indeterminate) {
      console.log('data:' + JSON.stringify(data))
      console.log('checked:' + JSON.stringify(checked))
      console.log('indeterminate:' + JSON.stringify(indeterminate))
    },
    //拿到选中的Key
    getTreeSelected() {
      let selectedKeys = this.$refs.permissionTree.getCheckedKeys();
      console.log('选中的keys:' + JSON.stringify(selectedKeys))
      return selectedKeys
    },
    //查询角色具备的权限信息
    async queryRolePermission() {
      const res = await Api.queryRolePermission({
        authRoleId: this.submitData.authRoleId
      });
      if (res.success) {
        let data = res.data;
        if (data) {
          let rolePermissionIdList = new Array();
          let permissionNameList = new Array();
          data.map(item => {
            rolePermissionIdList.push(item.authPermissionId)
            permissionNameList.push(item.permissionName)
          })
          this.rolePermissionIdList = rolePermissionIdList
          this.permissionNameList = permissionNameList
        }
      }
    },
    //分页查询集合
    queryPageList() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      //查询顶级节点
      Api.queryPermissionTreeList({
        parentId: this.$bizConstants.TOP_MENU_NODE
      }).then((res) => {
        let data = res.data
        if (res.success) {
          if (data) {
            data = data
            data.map(item => {
              item.id = item.authPermissionId
            })
            if (this.permissionTreeList.length > 0) {
              console.log('清空权限')
              this.permissionTreeList.splice(0, this.permissionTreeList.length)
              console.log('清空权限:' + this.permissionTreeList.length)
            }
            this.permissionTreeList = data;
          }
        } else {
          this.$message.error(res.message || res.msg || "服务器异常");
        }
        loading.close();
      }).catch((error) => {
        console.log(error);
        this.$message.error(error.message || error.msg || "服务器异常");
        loading.close();
      });
    },
    //处理展示
    showEdit(data) {
      //console.log('data:'+JSON.stringify(data))
      if (data) {
        this.submitData = {
          ...data
        }
      }
      this.init();
      this.editVisible = true;
    },
    async setOtherData() {
      this.queryPageList();
      await this.queryRolePermission();
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
        roleName: '',
        roleCode: '',
        roleRemark: '',
        authPermissionList: [],
      }
      this.rolePermissionIdList.splice(0, this.rolePermissionIdList.length)
      this.permissionNameList.splice(0, this.permissionNameList.length)
      this.permissionTreeList.splice(0, this.permissionTreeList.length)
    },
    handleCancel() {
      this.editVisible = false
      this.clearAll();
    },
    //处理提交
    handleSubmit() {
      this.$refs.submitForm.validate((valid) => {
        this.submitData.authPermissionList = this.getTreeSelected();
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
          Api.editAuthRoleItem(params).then((res) => {
            if (res.success) {
              this.editVisible = false;
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
.update-body {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-content: center;
  align-items: center;
  width: 100%;
  .update-body-form {
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-content: center;
    align-items: flex-start;
    width: 100%;
    .tree-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      width: 100%;
      margin-left: 20px;
      font-weight: bold;
      .tree-body-title{
        margin: 20px 10px;
      }
      .tree-text-view{

      }
    }
    .tree-data-view {
      width: 100%;
      border: 1px solid #a8aeb6;
      display: flex;
      flex-direction: column;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      .tree-item-data{
        width: 80%;
      }
    }
    .update-body-div {
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-content: center;
      align-items: center;
      .update-item-view {
        .permission-view {
          display: flex;
          flex-direction: row;
          justify-content: flex-start;
          flex-wrap: wrap;
          .permission-item {
            margin: 2px 2px;
          }
        }
      }
      .upload-item-view {
        .upload-item {

        }
      }
    }
  }
}
</style>

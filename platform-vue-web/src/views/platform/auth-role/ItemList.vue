<template>
  <div class="page-view">
    <div class="page-search-view">
      <el-form :inline="true" ref="pageForm">
        <div class="search-param-view">
          <div class="search-view">
            <div class="search-item-view">
              <el-form-item label="角色名称">
                <el-input
                    v-model="searchData.roleName"
                    placeholder="角色名称"
                ></el-input>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item label="角色编码">
                <el-input
                    v-model="searchData.roleCode"
                    placeholder="角色编码"
                ></el-input>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item label="描述信息">
                <el-input
                    v-model="searchData.roleRemark"
                    placeholder="描述信息"
                ></el-input>
              </el-form-item>
            </div>
          </div>
          <div class="search-view">
            <div class="search-item-view">
              <el-form-item>
                <el-button
                    @click="handleSearch"
                    icon="search"
                    type="primary"
                    size="mini">
                  搜索
                </el-button>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item>
                <el-button
                    type="primary"
                    size="mini"
                    @click="resetSearchForm">
                  重置
                </el-button>
              </el-form-item>
            </div>
            <div class="search-item-view" v-if="isAuth('authRole:add')">
              <el-form-item>
                <el-button
                    type="primary"
                    size="mini"
                    @click="handleAdd">
                  新增
                </el-button>
              </el-form-item>
            </div>
            <div class="search-item-view" v-if="isAuth('authRole:batchDelete')">
              <el-form-item>
                <el-button
                    type="danger"
                    size="mini"
                    @click="showDeleteBatchModal">
                  批量删除
                </el-button>
              </el-form-item>
            </div>
          </div>
        </div>
      </el-form>
    </div>
    <el-table
        :data="dataList"
        border
        row-key="id"
        lazy
        @selection-change="handleSelectionChange"
        size="mini"
    >
      <el-table-column
          type="selection"
          fixed="left"
          width="55">
      </el-table-column>
      <el-table-column
          prop="id"
          label="标识"
          fixed="left"
          width="60"
          align="center">
        <template v-slot="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column
          prop="roleName"
          label="角色名称"
          header-align="center"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="roleCode"
          label="角色编码"
          header-align="center"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="roleRemark"
          label="描述信息"
          header-align="center"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="operatorId"
          label="操作人"
          header-align="center"
          align="center"
          width="200"
      >
        <template v-slot="scope">
          <el-tag size="medium">{{ handleTypeByValue(scope.row.operatorId, authUserOptions) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          label="操作"
          fixed="right"
          width="250"
          header-align="center"
          align="center">
        <template v-slot="scope">
          <div class="operation-view">
            <div class="operation-button-view" v-if="isAuth('authRole:view')">
              <el-button
                  @click="handleViewParticulars(scope.row)"
                  icon="el-icon-search"
                  type="primary"
                  size="mini">
                查看
              </el-button>
            </div>
            <div class="operation-button-view" v-if="isAuth('authRole:edit')">
              <el-button
                  @click="handleUpdate(scope.row)"
                  icon="el-icon-edit"
                  type="warning"
                  size="mini">
                编辑
              </el-button>
            </div>
            <div class="operation-button-view" v-if="isAuth('authRole:deleteOne')">
              <el-button
                  @click="showDeleteOneModal(scope.row)"
                  :loading="deleteButtonLoading"
                  icon="el-icon-delete"
                  type="danger"
                  size="mini">
                删除
              </el-button>
            </div>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination-view">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :background="true"
          :current-page="paginationData.currentPage"
          :page-sizes="[5, 10, 60, 80, 100, 200, 300]"
          :page-size="paginationData.itemsPerPage"
          layout="total, sizes, prev, pager, next, jumper"
          :total="paginationData.totalCount">
      </el-pagination>
    </div>
    <!--删除确认框-->
    <el-dialog
        title="提示"
        :visible.sync="deleteVisible"
        width="30%">
      <span>需要删除该条么?</span>
      <span slot="footer" class="dialog-footer">
          <el-button
              @click="handleDeleteCancel"
              size="mini">
              取 消
          </el-button>
          <el-button
              type="primary"
              @click="handleDeleteOne"
              :loading="deleteButtonLoading"
              size="mini">
              确 定
          </el-button>
      </span>
    </el-dialog>
    <!--批量删除确认框-->
    <el-dialog
        title="提示"
        :visible.sync="batchDeleteVisible"
        width="30%">
          <span style="font-weight: bold;color: #ee0620;font-size: 20px;">
            需要删除选择的{{ multipleSelection.length }}条数据么?
          </span>
      <span slot="footer" class="dialog-footer">
          <el-button
              @click="handleDeleteBatchCancel"
              size="mini">
              取 消
          </el-button>
          <el-button
              type="primary"
              @click="handleDeleteBatch"
              :loading="batchDeleteButtonLoading"
              size="mini">
              确 定
          </el-button>
      </span>
    </el-dialog>
    <!--数据新增-->
    <ItemAdd ref="itemAdd"
             @handleSuccess="handleAddSuccess">
    </ItemAdd>
    <!--数据更新-->
    <ItemEdit ref="itemEdit"
              @handleSuccess="handleEditSuccess">
    </ItemEdit>
    <!--详情信息-->
    <ItemParticulars
        ref="itemParticulars">
    </ItemParticulars>
  </div>
</template>

<script>
import pageTable from "@/components/pageTable/pageTable.vue";
import Api from "@/services";
import MixinWx from "@/mixin/authUser";
import ItemAdd from "./ItemAdd.vue";
import ItemEdit from "./ItemEdit.vue";
import ItemParticulars from "./ItemParticulars.vue";

export default {
  components: {
    pageTable,
    ItemAdd: ItemAdd,
    ItemEdit: ItemEdit,
    ItemParticulars: ItemParticulars,
  },
  mixins: [MixinWx],
  data() {
    return {
      authUserOptions: [],
      deleteVisible: false,
      batchDeleteVisible: false,
      needDeleteItem: {},
      previewImageUrl: '',
      previewVisible: false,
      deleteButtonLoading: false,
      batchDeleteButtonLoading: false,
      paginationData: {
        itemsPerPage: 10,
        totalCount: 0,
        currentPage: 1
      },
      multipleSelection: [],
      name: "",
      dataList: [],
      searchData: {
        roleName: '',
        roleCode: '',
        roleRemark: '',
      },
      uploadHeaders: {
        token: localStorage.getItem('token') || ''
      }
    };
  },
  created() {
    this.init();
  },
  methods: {
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
    // 重置控件
    resetSearchForm() {
      this.searchData = {
        roleName: '',
        roleCode: '',
        roleRemark: '',
      };
      this.queryPageList();
    },
    handleViewParticulars(data) {
      this.$refs.itemParticulars.init(data)
    },
    //点击取消删除
    handleDeleteCancel() {
      this.needDeleteItem = {}
      this.deleteVisible = false;
    },
    //点击取消删除
    handleDeleteBatchCancel() {
      this.batchDeleteVisible = false;
    },
    //点击批量删除
    handleDeleteBatch() {
      this.batchDelete()
    },
    //点击删除
    handleDeleteOne() {
      this.deleteOne(this.needDeleteItem)
    },
    //展示批量删除弹窗
    showDeleteBatchModal() {
      let multipleSelection = this.multipleSelection
      if (this.$isNull(multipleSelection)) {
        this.$message.error(`请选择需要删除的数据`);
        return;
      }
      if (multipleSelection.length == 0) {
        this.$message.error(`请选择需要删除的数据`);
        return;
      }
      this.batchDeleteVisible = true
    },
    //展示删除弹窗
    showDeleteOneModal(data) {
      this.needDeleteItem = {
        ...data
      }
      this.deleteVisible = true
    },
    handlePreView(url) {
      if (this.$isNull(url)) {
        return;
      }
      this.previewImageUrl = url
      this.previewVisible = true
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.paginationData.itemsPerPage = val
      this.queryPageList();
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`);
      this.paginationData.currentPage = val
      this.queryPageList();
    },
    handleSelectionChange(val) {
      console.log('val:' + JSON.stringify(val))
      this.multipleSelection = val;
    },
    //处理单个删除
    deleteOne(data) {
      //console.log('data:'+JSON.stringify(data))
      let mainIdList = new Array();
      mainIdList.push(data.authRoleId)
      this.deleteButtonLoading = true
      Api.batchDeleteAuthRole({
        mainIdList: mainIdList
      }).then((res) => {
        //console.log('res:'+JSON.stringify(res))
        if (res.success) {
          this.handleSearch()
        } else {
          this.$message.error(res.message || res.msg || "服务器异常");
        }
        this.needDeleteItem = {}
        this.deleteButtonLoading = false
        this.deleteVisible = false;
      })
    },
    //处理批量删除
    batchDelete() {
      this.batchDeleteButtonLoading = true
      let multipleSelection = this.multipleSelection;
      if (this.$isNull(multipleSelection)) {
        this.batchDeleteButtonLoading = false
        return;
      }
      if (multipleSelection.length == 0) {
        this.batchDeleteButtonLoading = false
        return;
      }
      let mainIdList = new Array();
      multipleSelection.map(item => {
        mainIdList.push(item.authRoleId)
      })
      console.log('mainIdList:' + JSON.stringify(mainIdList))
      if (this.$isNull(mainIdList)) {
        this.$message({
          message: '请选择需要删除的数据',
          type: 'success'
        });
        this.batchDeleteButtonLoading = false
        return;
      }
      if (mainIdList.length == 0) {
        this.$message({
          message: '请选择需要删除的数据',
          type: 'success'
        });
        this.batchDeleteButtonLoading = false
        return;
      }
      Api.batchDeleteAuthRole({
        mainIdList: mainIdList
      }).then((res) => {
        console.log('res:' + JSON.stringify(res))
        if (res.success) {
          this.handleSearch()
        } else {
          this.$message.error(res.message || res.msg || "服务器异常");
        }
        this.batchDeleteVisible = false
        this.batchDeleteButtonLoading = false
      })
    },
    //处理新增成功
    handleAddSuccess() {
      this.queryPageList();
    },
    //处理编辑成功
    handleEditSuccess() {
      this.queryPageList();
    },
    //分页查询集合
    queryPageList() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      Api.queryAuthRoleByPage({
        ...this.searchData,
        itemsPerPage: this.paginationData.itemsPerPage,
        currentPage: this.paginationData.currentPage,
      }).then((res) => {
        //console.log('res:'+JSON.stringify(res))
        let data = res.data
        //console.log('data:'+JSON.stringify(data))
        if (res.success) {
          if (data) {
            this.paginationData.totalCount = data.totalCount
            this.paginationData.itemsPerPage = data.itemsPerPage
            this.paginationData.currentPage = data.currentPage
            data = data.data

            this.dataList = data;
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
    // 初始化数据
    async init() {
      this.authUserOptions = await this.$bizConstants.authUserOptions()
      this.queryPageList();
    },
    // 搜索事件
    handleSearch() {
      this.queryPageList();
    },
    // 处理更新
    handleUpdate(data) {
      if (data) {
        let editData = {
          ...data
        }
        console.log('editData:' + JSON.stringify(editData))
        this.$refs.itemEdit.showEdit(editData);
      } else {
        this.$refs.itemEdit.showEdit({});
      }
    },
    // 处理新增
    handleAdd() {
      this.$refs.itemAdd.showAdd({});
    },
    showDialogImport(type, data) {
      //this.$refs.ItemImport.showDialog(type, data);
    },
  },
};
</script>
<style scoped lang="scss">
.page-view {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  padding: 20px;

  .page-search-view {
    margin: 20px 20px;

    .search-param-view {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      justify-content: flex-start;

      .search-view {
        display: flex;
        flex-direction: row;
        align-items: flex-start;
        justify-content: flex-start;

        .search-item-view {

        }
      }
    }
  }

  .operation-view {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    flex-wrap: wrap;

    .operation-button-view {
      margin: 2px 2px;
    }
  }

  .pagination-view {
    margin-top: 20px;
    margin-bottom: 20px;
    margin-left: 20px;
    //border: 2px solid #15de0f;
  }
}
</style>

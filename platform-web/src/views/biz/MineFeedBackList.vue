<template>
  <div class="page-view">
    <div class="page-search-view">
      <el-form :inline="true" ref="pageForm">
        <div class="search-param-view">
          <div class="search-view">
            <div class="search-item-view">
              <el-form-item label="内容标题">
                <el-input
                    v-model="searchData.dataTitle"
                    placeholder="内容标题"
                ></el-input>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item label="内容">
                <el-input
                    v-model="searchData.dataValue"
                    placeholder="内容"
                ></el-input>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item label="提交人">
                <el-select
                    v-model="searchData.submitterId"
                    :clearable="true"
                    placeholder="请选择提交人">
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
          <div class="search-view">
            <div class="search-item-view">
              <el-form-item label="处理状态">
                <el-select
                    v-model="searchData.handleStatus"
                    :clearable="true"
                    placeholder="请选择处理状态">
                  <el-option
                      v-for="item in handleOptions"
                      :key="item.value"
                      :label="item.text"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item label="备注">
                <el-input
                    v-model="searchData.remarkData"
                    placeholder="备注"
                ></el-input>
              </el-form-item>
            </div>
          </div>
          <div class="search-view">
            <div class="search-item-view">
              <el-form-item>
                <el-button
                    size="mini"
                    @click="handleSearch"
                    icon="search"
                    type="primary">
                  搜索
                </el-button>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item>
                <el-button
                    size="mini"
                    type="primary"
                    @click="resetSearchForm">
                  重置
                </el-button>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item>
                <el-button
                    size="mini"
                    type="primary"
                    @click="handleAdd">
                  提交建议
                </el-button>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item>
                <el-button
                    size="mini"
                    type="danger"
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
          align="center"
          width="80">
        <template v-slot="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column
          prop="dataTitle"
          label="内容标题"
          header-align="center"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="dataValue"
          label="内容"
          header-align="center"
          align="center"
      >
      </el-table-column>
      <el-table-column
          prop="submitterId"
          label="提交人"
          header-align="center"
          align="center"
      >
        <template v-slot="scope">
          {{handleTypeByValue(scope.row.submitterId,authAppUserOptions)}}
        </template>
      </el-table-column>
      <el-table-column
          prop="handleStatus"
          label="处理状态"
          header-align="center"
          align="center"
      >
        <template v-slot="scope">
          <el-tag size="medium">{{handleTypeByValue(scope.row.handleStatus,handleOptions)}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="remarkData"
          label="备注"
          header-align="center"
          align="center"
      >
      </el-table-column>
      <el-table-column
          label="操作"
          fixed="right"
          width="250"
          header-align="center"
          align="center">
        <template v-slot="scope">
          <div class="operation-view">
            <div class="operation-button-view">
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
    <el-dialog :visible.sync="previewVisible" title="图片预览">
      <img width="100%" :src="previewImageUrl" alt=""/>
    </el-dialog>
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
    <!--富文本查看弹窗-->
    <el-dialog
        append-to-body
        title="富文本内容"
        :visible.sync="richTextVisible"
        width="50%">
      <div v-html="richText"></div>
    </el-dialog>
    <!--批量删除确认框-->
    <el-dialog
        title="提示"
        :visible.sync="batchDeleteVisible"
        width="30%">
      <span style="font-weight: bold;color: #ee0620;font-size: 20px;">
        需要删除选择的{{multipleSelection.length}}条数据么?
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
  </div>
</template>

<script>
import pageTable from "@/components/pageTable/pageTable.vue";
import Api from "@/services";
import MixinWx from "@/mixin/authUser";

export default {
  components: {
    pageTable,
  },
  mixins: [MixinWx],
  data() {
    return {
      //-----------------
      authAppUserOptions:[],
      handleOptions: [
        {
          'text':'已提交',
          'value':1
        },
        {
          'text':'已处理',
          'value':2
        },
      ],
      userData:{},
      //-----------------
      feedbackDataOptions: [],
      richTextVisible: false,
      richText: ``,
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
      dataList: [],
      searchData: {
        dataTitle: '',
        dataValue: '',
        submitterId: '',
        handleStatus: '',
        remarkData: '',
      },
    };
  },
  created() {
    this.init();
  },
  methods: {
    //查询当前用户信息
    async currentUserMeta() {
      await Api.currentUserMeta({

      }).then(async (res) => {
        if (!res.success) {
          return;
        }
        if (this.$isNull(res)) {
          return;
        }
        let data = res.data
        if (this.$isNull(data)) {
          return;
        }
        data.avatarUrl = await this.handleImageUrl(data.avatarUrl);
        this.userData = {...data};
        console.log('当前用户信息:' + JSON.stringify(this.userData))
      });
    },
    async showRichText(richText) {
      console.log(richText)
      this.richText = richText
      this.richTextVisible = true
    },
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
    // 重置控件
    resetSearchForm() {
      this.searchData = {
        dataTitle: '',
        dataValue: '',
        submitterId: '',
        handleStatus: '',
        remarkData: '',
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
      let mainIdList = new Array();
      mainIdList.push(data.feedbackDataId)
      this.deleteButtonLoading = true
      Api.batchDeleteFeedbackData({
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
        mainIdList.push(item.feedbackDataId)
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
      Api.batchDeleteFeedbackData({
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
    //分页查询集合
    queryPageList() {
      const loading = this.$loading({
        lock: true,
        text: "正在请求。。。",
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.8)'
      });
      let params = {
        ...this.searchData,
        itemsPerPage: this.paginationData.itemsPerPage,
        currentPage: this.paginationData.currentPage,
      }
      //用户ID
      params.submitterId = this.userData.authAppUserId;
      Api.queryFeedbackDataByPage({
        ...params
      }).then((res) => {
        //console.log('res:'+JSON.stringify(res))
        let data = res.data
        //console.log('data:'+JSON.stringify(data))
        if (res.success) {
          if (data) {
            //console.log('data:'+JSON.stringify(data))
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
      await this.currentUserMeta();
      this.authAppUserOptions = await this.$bizConstants.queryAuthAppUser();
      //await this.queryFeedbackData();
      this.queryPageList();
    },
    // 搜索事件
    handleSearch() {
      this.queryPageList();
    },
    // 处理新增
    handleAdd() {
      this.$router.push({path: "/submitFeedBack"});
    },
  },
};
</script>
<style scoped lang="scss">
.page-view{
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  justify-content: flex-start;
  padding: 20px;
  .page-search-view{
    margin: 20px 20px;
    .search-param-view{
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      justify-content: flex-start;
      .search-view{
        display: flex;
        flex-direction: row;
        align-items: flex-start;
        justify-content: flex-start;
        .search-item-view{

        }
      }
    }
  }
  .operation-view{
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    flex-wrap: wrap;
    .operation-button-view{
      margin: 2px 2px;
    }
  }
  .pagination-view{
    margin-top: 20px;
    margin-bottom: 20px;
    margin-left: 20px;
    //border: 2 px solid #15de0f;
  }
  .table-view{
    width: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    flex-wrap: wrap;
    .table-data-view{
      width: 100%;
    }
    .table-footer-view{

    }
  }
}
</style>

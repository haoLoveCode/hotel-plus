<template>
  <div class="page-view">
    <div class="page-search-view">
      <el-form :inline="true" ref="pageForm">
        <div class="search-param-view">
          <div class="search-view">
            <div class="search-item-view">
              <el-form-item label="商品类型">
                <el-select
                    v-model="searchData.typeItemId"
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
            <div class="search-item-view">
              <el-form-item label="发布人">
                <el-select
                    v-model="searchData.publisherId"
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
            <div class="search-item-view">
              <el-form-item label="商品名称">
                <el-input
                    v-model="searchData.itemName"
                    placeholder="商品名称"
                ></el-input>
              </el-form-item>
            </div>
          </div>
          <div class="search-view">
            <div class="search-item-view">
              <el-form-item label="商品简介">
                <el-input
                  v-model="searchData.itemSummary"
                  placeholder="商品简介"
                ></el-input>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item label="商品标题">
                <el-input
                  v-model="searchData.itemTitle"
                  placeholder="商品标题"
                ></el-input>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item label="销售价格">
                <el-input
                  v-model="searchData.salePrice"
                  placeholder="销售价格"
                ></el-input>
              </el-form-item>
            </div>
            <div class="search-item-view">
              <el-form-item label="商品状态">
                <el-select
                    v-model="searchData.itemStatus"
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
            <div class="search-item-view" v-if="isAuth('salesItem:add')">
              <el-form-item>
                <el-button
                  size="mini"
                  type="primary"
                  @click="handleAdd">
                  新增
                </el-button>
              </el-form-item>
            </div>
            <div class="search-item-view" v-if="isAuth('salesItem:add')">
              <el-form-item>
                <el-button
                    size="mini"
                    type="primary"
                    @click="handleCopy">
                  复制数据
                </el-button>
              </el-form-item>
            </div>
            <div class="search-item-view" v-if="isAuth('salesItem:export')">
              <el-form-item>
                <el-button
                  size="mini"
                  @click="handleExport"
                  icon="search"
                  type="primary">
                  导出
                </el-button>
              </el-form-item>
            </div>
            <div class="search-item-view" v-if="isAuth('salesItem:batchDelete')">
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
        prop="typeItemId"
        label="商品类型"
        header-align="center"
        align="center"
      >
        <template v-slot="scope">
          {{handleTypeByValue(scope.row.typeItemId,salesItemTypeOptions)}}
        </template>
      </el-table-column>
      <el-table-column
          prop="itemName"
          label="商品名称"
          header-align="center"
          align="center"
          width="200"
      >
      </el-table-column>
      <el-table-column
          prop="mainImg"
          label="商品图片"
          header-align="center"
          align="center"
      >
        <template v-slot="scope">
          <img v-if="scope.row.mainImg" @click="handlePreView(handleImageUrl(scope.row.mainImg))"
               :src="handleImageUrl(scope.row.mainImg)"
               style="width: 30px;height: 30px;border-radius: 5px;"/>
          <img v-else
               src="~@static/icons/main-logo.svg"" style="width: 30px;height: 30px;border-radius: 5px;"/>
        </template>
      </el-table-column>
      <el-table-column
        prop="publisherId"
        label="发布人"
        header-align="center"
        align="center"
      >
        <template v-slot="scope">
          {{handleTypeByValue(scope.row.publisherId,authAppUserOptions)}}
        </template>
      </el-table-column>
      <el-table-column
          prop="imgList"
          label="商品图片"
          header-align="center"
          align="center"
          width="100"
      >
        <template v-slot="scope">
          <el-tag size="medium" @click="viewSaleItemImgDataList(scope.row)">点击查看</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="itemSummary"
        label="商品简介"
        header-align="center"
        align="center"
        width="100"
      >
        <template v-slot="scope">
          <el-tag size="medium" @click="showRichText(scope.row.itemSummary)">点击查看</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="itemTitle"
        label="商品标题"
        header-align="center"
        align="center"
        width="100"
      >
        <template v-slot="scope">
          <el-tag size="medium" @click="showRichText(scope.row.itemTitle)">点击查看</el-tag>
        </template>
      </el-table-column>
      <el-table-column
          prop="originalPrice"
          label="原价"
          header-align="center"
          align="center"
      >
      </el-table-column>
      <el-table-column
        prop="salePrice"
        label="销售价格"
        header-align="center"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="itemStatus"
        label="商品状态"
        header-align="center"
        align="center"
      >
        <template v-slot="scope">
          {{handleTypeByValue(scope.row.itemStatus,yesOrNoOptions)}}
        </template>
      </el-table-column>
      <el-table-column
          prop="remarkData"
          label="备注信息"
          header-align="center"
          align="center"
      >
        <template v-slot="scope">
          <el-tag size="medium" @click="showRichText(scope.row.remarkData)">点击查看</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="operatorId"
        label="创建人"
        header-align="center"
        align="center"
      >
        <template v-slot="scope">
          <el-tag size="medium">{{handleTypeByValue(scope.row.operatorId,authUserOptions)}}</el-tag>
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
            <div class="operation-button-view" v-if="isAuth('salesItem:view')">
              <el-button
                @click="handleViewParticulars(scope.row)"
                icon="el-icon-search"
                type="primary"
                size="mini">
                查看
              </el-button>
            </div>
            <div class="operation-button-view" v-if="isAuth('salesItem:edit')">
              <el-button
                @click="handleUpdate(scope.row)"
                icon="el-icon-edit"
                type="warning"
                size="mini">
                编辑
              </el-button>
            </div>
            <div class="operation-button-view" v-if="isAuth('salesItem:deleteOne')">
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
    <!--商品图片查看弹窗-->
    <el-dialog
        title="商品图片"
        :visible.sync="saleItemImgListVisible"
        width="80%">
      <div class="table-view">
        <el-table
            :data="saleItemImgList"
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
    <!--数据新增-->
    <ItemAdd ref="itemAdd"
         @handleSuccess="handleAddSuccess">
    </ItemAdd>
    <!--数据复制-->
    <ItemCopy ref="itemCopy"
         @handleSuccess="handleCopySuccess">
    </ItemCopy>
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
  import ItemCopy from "./ItemCopy.vue";
  import ItemEdit from "./ItemEdit.vue";
  import ItemParticulars from "./ItemParticulars.vue";

  export default {
    components: {
      pageTable,
      ItemAdd: ItemAdd,
      ItemCopy: ItemCopy,
      ItemEdit: ItemEdit,
      ItemParticulars: ItemParticulars,
    },
    mixins: [MixinWx],
    data() {
      return {
        //-----------------
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
        saleItemImgList: [],
        saleItemImgListVisible: false,
        //-----------------
        salesItemOptions: [],
        richTextVisible: false,
        richText: ``,
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
        dataList: [],
        searchData: {
          typeItemId: '',
          publisherId: '',
          itemName: '',
          itemSummary: '',
          itemTitle: '',
          salePrice: '',
          originalPrice: '',
          mainImg: '',
          itemStatus: '',
          remarkData: '',
        },
      };
    },
    created() {
      this.init();
    },
    methods: {
      //查看商品图片
      async viewSaleItemImgDataList(data) {
        let saleItemImgList = data.imgList;
        if (!saleItemImgList || saleItemImgList.length == 0) {
          this.$message.error('暂无图片');
        }
        this.saleItemImgList = new Array();
        saleItemImgList.map((item, index) => {
          console.log(`第${index}个元素为 ${item}`);
          this.saleItemImgList.push({
            imgUrl: item,
            id: index + 1
          })
        });
        this.saleItemImgListVisible = true;
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
      async showRichText(richText) {
        console.log(richText)
        this.richText = richText
        this.richTextVisible = true
      },
      async handleSalesItemChange(value) {
        this.searchData.salesItemId = value
      },
      async querySalesItem() {
        const loading = this.$loading({
          lock: true,
          text: "正在请求。。。",
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.8)'
        });
        try {
          Api.querySalesItem({}).then((res) => {
            if (res.success) {
              console.log('res:' + JSON.stringify(res))
              if (this.$isNull(res)) {
                return;
              }
              let data = res.data
              if (this.$isNull(data)) {
                return;
              }
              this.salesItemOptions = new Array();
              data.map((item) => {
                let options = {
                  'text': item.itemName,
                  'value': item.salesItemId
                }
                this.salesItemOptions.push(options)
              })
              console.log('this.salesItemOptions:' + JSON.stringify(this.salesItemOptions))
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
          typeItemId: '',
          publisherId: '',
          itemName: '',
          itemSummary: '',
          itemTitle: '',
          salePrice: '',
          originalPrice: '',
          mainImg: '',
          itemStatus: '',
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
        mainIdList.push(data.salesItemId)
        this.deleteButtonLoading = true
        Api.batchDeleteSalesItem({
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
          mainIdList.push(item.salesItemId)
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
        Api.batchDeleteSalesItem({
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
      //处理复制成功
      handleCopySuccess() {
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
        let params = {
          ...this.searchData,
          itemsPerPage: this.paginationData.itemsPerPage,
          currentPage: this.paginationData.currentPage,
        }
        Api.querySalesItemByPage({
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
        this.authUserOptions = await this.$bizConstants.authUserOptions();
        await this.querySalesItemType();
        this.authAppUserOptions = await this.$bizConstants.queryAuthAppUser();
        //await this.querySalesItem();
        this.queryPageList();
      },
      //处理导出
      handleExport() {
        const loading = this.$loading({
          lock: true,
          text: "正在请求。。。",
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.8)'
        });
        Api.exportSalesItemItem({
          ...this.searchData,
          itemsPerPage: this.paginationData.itemsPerPage,
          currentPage: this.paginationData.currentPage,
        }).then((res) => {

          loading.close();
        }).catch((error) => {
          console.log(error);
          this.$message.error(error.message || error.msg || "服务器异常");
          loading.close();
        });
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
      // 处理复制
      handleCopy() {
        let multipleSelection = this.multipleSelection;
        if (this.$isNull(multipleSelection)) {
          this.$message.error(`请选择需要复制的数据`);
          return;
        }
        if (multipleSelection.length == 0) {
          this.$message.error(`请选择需要复制的数据`);
          return;
        }
        if (multipleSelection.length > 1) {
          this.$message.error(`只能选择一条需要复制的数据`);
          return;
        }
        //return;
        //从中选择一个
        let data = multipleSelection.find((item) => !this.$baseUtils.isNull(item));
        //console.log('data:' + JSON.stringify(data))
        let copyData = {
          ...data
        }
        //console.log('copyData:' + JSON.stringify(copyData))
        this.$refs.itemCopy.showCopy(copyData);
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

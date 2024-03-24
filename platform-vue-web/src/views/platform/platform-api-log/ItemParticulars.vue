<template>
    <el-dialog title="" :center="true" :visible.sync="particularsVisible">
        <div class="particulars-view">
            <div class="descriptions-title-view">
                系统日志详情
            </div>
            <div class="descriptions-view">
                <div class="descriptions-item">
                    <div class="descriptions-item-view">
                        <div class="descriptions-title">
                            操作者用户名:
                        </div>
                        <div class="descriptions-value">
                            {{ particularsData.userName }}
                        </div>
                    </div>
                    <div class="descriptions-item-view">
                        <div class="descriptions-title">
                            操作内容:
                        </div>
                        <div class="descriptions-value">
                            {{ particularsData.operation }}
                        </div>
                    </div>
                    <div class="descriptions-item-view">
                        <div class="descriptions-title">
                            耗费时间:
                        </div>
                        <div class="descriptions-value">
                            {{ particularsData.operationTime }}
                        </div>
                    </div>
                    <div class="descriptions-item-view">
                        <div class="descriptions-title">
                            操作方法:
                        </div>
                        <div class="descriptions-value">
                            {{ particularsData.method }}
                        </div>
                    </div>
                </div>
                <div class="descriptions-item">
                    <div class="descriptions-item-view">
                        <div class="descriptions-title">
                            方法参数:
                        </div>
                        <div class="descriptions-value">
                            {{ particularsData.params }}
                        </div>
                    </div>
                    <div class="descriptions-item-view">
                        <div class="descriptions-title">
                            操作地点:
                        </div>
                        <div class="descriptions-value">
                            {{ particularsData.location }}
                        </div>
                    </div>
                    <div class="descriptions-item-view">
                        <div class="descriptions-title">
                            IP地址:
                        </div>
                        <div class="descriptions-value">
                            {{ particularsData.requestIp }}
                        </div>
                    </div>
                </div>
            </div>
        </div>
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
            particularsVisible: false,
            particularsData: {},
            size: '',
        }
    },
    methods: {
        showParticulars(data) {
            console.log('data:' + JSON.stringify(data))
            if (!this.$isNull(data)) {
                this.particularsData = {
                    ...data
                }
            }
            this.particularsVisible = true;
        },
        init(data) {
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
  }
}
</style>

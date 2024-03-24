<template>
  <div class="page-view">
    <div id="chartSearchDiv" class="chart-search-div" style="height: 130px">
      <div class="chart-text-view">系统访问量</div>
      <div class="chart-search-item">
        <div class="search-item">
          <el-date-picker
              v-model="queryParams.beginTime"
              type="datetime"
              placeholder="请选择开始时间"
              align="right"
              :value-format="timeFormat"
              :picker-options="pickerOptions"
          >
          </el-date-picker>
        </div>
        <div class="search-item">
          <el-date-picker
              v-model="queryParams.endTime"
              type="datetime"
              placeholder="请选择结束时间"
              align="right"
              :value-format="timeFormat"
              :picker-options="pickerOptions"
          >
          </el-date-picker>
        </div>
      </div>
    </div>
    <div class="chart-view">
      <div id="visitCountContainer" class="chart-item">
      </div>
    </div>
  </div>
</template>
<script>
import { Chart } from '@antv/g2';
import moment from 'moment'
import Api from "@/services";
moment.locale('zh-cn')
import baseUtils from '@/utils/baseUtils'
export default {
  name: 'VisitCountChart',
  components: {
    Chart:Chart,
    baseUtils:baseUtils,
    moment:moment,
  },
  data () {
    return {
      //--------------------------------
      pickerOptions:this.$commonOptions.pickerOptions,
      timeFormat:'yyyy-MM-dd', //时间格式
      //--------------------------------
      chart:null,
      data: [],
      queryParams: {
        endTime:'',
        beginTime:'',
      },
    }
  },
  mounted () {
    this.initChart()
  },
  created () {

  },
  destroyed () {

  },
  computed:{

  },
  methods: {
    moment, //时间处理
    //时间发生改变
    async changeTime(value){
      console.log('changeTime:'+value)
      await this.statisticsCount();
    },
    //统计访问量
    async statisticsCount(){
      if(!this.$isNull(this.queryParams.beginTime)){
        moment(this.queryParams.beginTime).format(this.timeFormat);
      }
      if(!this.$isNull(this.queryParams.endTime)){
        moment(this.queryParams.endTime).format(this.endTime);
      }
      Api.statisticsVisitCount({
        ...this.queryParams
      }).then((res) => {
        console.log('res:'+JSON.stringify(res))
        let data = res.data
        this.data.splice(0,this.data.length);
        if (res.success) {
          if(data && data.length > 0){
            data.map(item => {
              this.data.push(item)
            })
          }else{
            this.data = new Array();
          }
          console.log('this.data : '+JSON.stringify(this.data))
          let chart = this.chart
          console.log('chart:'+chart)
          if(chart){
            //立即改变数据
            chart.changeData(this.data);
          }
        }else{
          this.$message.error(res.message || res.msg || "服务器异常");
        }
      }).catch((error) => {
        console.log(error);
        this.$message.error(error.message || error.msg || "服务器异常");
      });
    },
    async initChart() {
      await this.statisticsCount();
      console.log('初始化表格')
      /*let chart = new Chart({
        container: 'visitCountContainer',
        autoFit: true,
        height: 300,
        width:300
      });
      chart.data(this.data);
      chart.scale('visitCount', {
        alias: '访问量',
        nice: true,
      });
      chart.axis('createTime', {
        tickLine: null
      });
      chart.tooltip({
        showMarkers: false
      });
      chart.interaction('active-region');
      chart.interval().position('createTime*visitCount');
      chart.render();*/

      //此处是曲线的设置
      console.log(this.data)
      let chart = new Chart({
        container: 'visitCountContainer',
        autoFit: true,
        height: 300,
        width:300
      });
      chart.data(this.data);
      chart.scale({
        createTime: {
          range: [0, 1],
        },
        visitCount: {
          min: 0,
          nice: true,
        },
      });

      chart.tooltip({
        showCrosshairs: true, // 展示 Tooltip 辅助线
        shared: true,
      });

      chart.line().position('createTime*visitCount').label('visitCount');
      chart.point().position('createTime*visitCount');

      chart.render();
      this.chart = chart
    }
  }
}
</script>

<style scoped lang="scss">
@import "./style.scss";
.page-view{
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  .chart-search-div{
    padding: 24px;
    background: #14749b;
    border: 1px solid #d9d9d9;
    border-radius: 6px;
    margin-bottom: 30px;
    width:100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .chart-text-view{
      text-align: center;
      display: block;
      margin-bottom: 10px;
      font-weight:bold;
      font-size: 20px;
      color: #f5f4f9;
    }
    .chart-search-item{
      display: flex;
      flex-direction: row;
      justify-content: space-around;
      align-items: center;
      width: 100%;
      .search-item{

      }
    }
  }
  .chart-view{
    //border: 1px solid #52c421;
    width: 100%;
    .chart-item{

    }
  }
}
</style>

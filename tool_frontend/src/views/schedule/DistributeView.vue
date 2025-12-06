<!--Vehicle allocation management page-->
<template>
  <div style="height: 6vh;background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">车辆分配</span>
  </div>
  <!-- 申请单搜索卡片 -->
  <el-card style="margin:20px;height: 70px;">
    <el-form :inline="true">
      <el-form-item label="工单申请人">
        <el-input placeholder="请输入申请人" style="width:220px;"
                  v-model="search.username" @keydown.enter.prevent="loadApplication"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="primary" @click="loadApplication">查询</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <!-- 工单信息列表 -->
  <el-card  style="margin:20px;">
    <el-radio-group style="margin-bottom: 15px;" v-model="type">
      <el-radio-button value="50" size="large" @change="loadApplication">待分配</el-radio-button>
      <el-radio-button value="60" size="large" @change="loadApplication">已分配</el-radio-button>
    </el-radio-group>
    <el-table :data="appTableData">
      <el-table-column label="编号" type="index" width="100" align="center"></el-table-column>
      <el-table-column label="工单申请人" prop="username" align="center"></el-table-column>
      <el-table-column label="申请时间" prop="createTime" align="center"></el-table-column>
      <el-table-column label="出发地" prop="departureAddr" align="center"></el-table-column>
      <el-table-column label="目的地" prop="destinationAddr" align="center"></el-table-column>
      <el-table-column label="用车事由" prop="reason" align="center"></el-table-column>
      <el-table-column label="使用开始时间" prop="startTime" align="center"></el-table-column>
      <el-table-column label="使用结束时间" prop="endTime" align="center"></el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template #default="scope">
          <!--只有"已审核"才显示分配用车按钮  只有"分配用车"才显示还车按钮-->
          <el-button type="primary" link v-if="type==50" @click="loadGeo(scope.row.id)">分配用车</el-button>
          <!--注意:我们现在在申请表表格中,这里的scope.row表示当前申请单的完整对象,既包含申请单id,也包含车辆id-->
          <el-button type="primary" link v-if="type==60" @click="back(scope.row)">还车</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- 围栏车辆概览弹窗 -->
  <el-dialog title="围栏列表" v-model="dialogVisible" style="padding:40px;" :before-close="handleClose">
    <el-form>
      <el-row :gutter="10">
        <el-col :span="8">
          <el-form-item label="围栏名称:">
            <el-input placeholder="请输入" v-model="searchGeofenceForm.name" @keydown.enter="loadGeo"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="围栏状态">
            <el-select placeholder="请选择" v-model="searchGeofenceForm.status" @change="loadGeo">
              <el-option label="启用" value="1"/>
              <el-option label="禁用" value="0"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4"></el-col>
        <el-col :span="4">
          <el-form-item>
            <el-button-group>
              <el-button @click="resetSearchGeo">重置</el-button>
              <el-button type="primary" @click="loadGeo">查询</el-button>
            </el-button-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-table style="margin:20px 0" :data="geoTableData">
      <el-table-column label="编号" type="index" width="80" align="center"></el-table-column>
      <el-table-column label="围栏名称" prop="name" width="200" align="center"></el-table-column>
      <el-table-column label="车辆总数" prop="totalNum" width="200" align="center"></el-table-column>
      <el-table-column label="可用车辆" prop="availableNum" align="center"></el-table-column>
      <el-table-column label="围栏状态" prop="status" align="center" :formatter="geofenceStatusFormatter"></el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="showVehicle(scope.row.id)">查看车辆</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>

  <!-- 分配车辆弹窗 -->
  <el-dialog title="分配车辆" style="padding:40px;" v-model="vehicleDialogVisible">
    <el-table style="margin:20px 0" :data="vehicleTableData">
      <el-table-column label="编号" type="index" width="80" align="center"></el-table-column>
      <el-table-column label="车辆品牌" prop="brand" width="200" align="center"></el-table-column>
      <el-table-column label="车牌号" prop="license" width="200" align="center"></el-table-column>
      <el-table-column label="车辆类型" prop="type" align="center"></el-table-column>
      <el-table-column label="车辆状态" prop="status" align="center"></el-table-column>
      <el-table-column label="操作" width="100" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="distribute(scope.row.id)">分配</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import qs from "qs";
import {ElMessage} from "element-plus";
//用于控制围栏车辆概览弹窗是否可见
const dialogVisible = ref(false);
//用于控制分配车辆弹窗是否可见
const vehicleDialogVisible = ref(false);

const type = ref(50);
const search = ref({
  username: "",
});
const appTableData = ref([]);
const loadApplication = () => {
  search.value.status = type.value;
  let data = qs.stringify(search.value);
  axios.get(BASE_URL+'/v1/application/select?'+data).then((response)=>{
    if(response.data.code==2000){
      appTableData.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
onMounted(()=>{
  loadApplication();
})

const resetSearch = () => {
  search.value.username = "";
  loadApplication();
}

const searchGeofenceForm = ref({
  name: "",
  status: "",
});
const geoTableData = ref([]);

//定义全局变量用来保存当前申请单的id,在分配车辆时使用
var applicationId;
const loadGeo=(id)=>{
  //将当前申请单id赋值给全局变量
  applicationId = id;

  dialogVisible.value = true;
  let data = qs.stringify(searchGeofenceForm.value);
  axios.get(BASE_URL+'/v1/geofence/select?'+data).then((response)=>{
    if(response.data.code==2000){
      geoTableData.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
const resetSearchGeo = () => {
  searchGeofenceForm.value = {};
  loadGeo();
}
//对围栏状态显示的值进行转换
const geofenceStatusFormatter = (row, column,cellValue,index) => {
  if(cellValue==1){
    cellValue = '启用';
  }else if(cellValue==0){
    cellValue = '禁用'
  }
  return cellValue;
}
//处理关闭围栏弹窗的方法
const handleClose = ()=>{
  searchGeofenceForm.value = {};
  dialogVisible.value = false;
}

//定义数组用来保存加载出来的围栏车辆数据
const vehicleTableData = ref([]);
//定义方法加载指定围栏上的车辆数据
const showVehicle = (id)=>{
  vehicleDialogVisible.value = true;
  axios.get(BASE_URL+'/v1/vehicle/select?geofenceId='+id).then((response)=>{
    if(response.data.code==2000){
      vehicleTableData.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

//分配车辆方法 给指定申请单分配指定的车辆 applicationId是全局变量,在loadGeo调用时已赋值
const distribute = (vehicleId)=>{
  axios.post(BASE_URL+'/v1/application/distribute/'+applicationId+'/'+vehicleId)
      .then((response)=>{
        if (response.data.code==2000){
          dialogVisible.value = false;//关闭围栏列表弹窗
          vehicleDialogVisible.value = false;//关闭车辆列表弹窗
          loadApplication();//分配后重新加载申请单数据
          ElMessage.success('车辆分配成功!');
        }else {
          ElMessage.error(response.data.msg);
        }
      })
}

//还车方法
//row代表调用方法时传过来的整行申请单数据
//row.id就是applicationId row.vehicleId就是当前申请单的车辆id
const back = (row)=>{
  console.log(row.id,typeof row.id);
  console.log(row.vehicleId,typeof row.vehicleId);
  axios.post(BASE_URL+'/v1/application/back/'+row.id+'/'+row.vehicleId)
      .then((response)=>{
        if (response.data.code==2000){
          loadApplication();//还车后重新加载申请单数据
          ElMessage.success('还车成功!');
        }else {
          ElMessage.error(response.data.msg);
        }
      })
}
</script>

<style scoped>

</style>

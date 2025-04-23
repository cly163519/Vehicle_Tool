<!--电子围栏页-->
<template>
  <div style="height: 6vh;background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">围栏管理</span>
    <el-button type="primary" style="float:right;margin-top:13px;"
    @click="router.push('/geofenceMap')">新建围栏</el-button>
  </div>
<!--围栏查询卡片-->
<el-card style="margin:20px;height:70px;">
  <el-form :inline="true">
    <el-form-item label="围栏名称">
      <el-input placeholder="请输入" style="width:220px;"
      v-model="geoSearchForm.name" @keydown.enter="loadGeo"></el-input>
    </el-form-item>
    <el-form-item label="围栏状态" style="width:290px;">
      <el-select placeholder="请选择" v-model="geoSearchForm.status" @change="loadGeo">
        <el-option label="启用" value="1"></el-option>
        <el-option label="禁用" value="0"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button @click="resetSearch">重置</el-button>
      <el-button type="primary" @click="loadGeo">查询</el-button>
    </el-form-item>
  </el-form>
</el-card>
<!--围栏表格数据-->
<el-card style="margin:20px;">
  <el-table stripe :data="geoTableData">
    <el-table-column label="编号" prop="id" type="index" align="center" width="100"></el-table-column>
    <el-table-column label="围栏名称" prop="name" align="center"></el-table-column>
    <el-table-column label="围栏坐标点" prop="position" align="center" show-overflow-tooltip="true" width="500"></el-table-column>
    <el-table-column label="创建时间" prop="createTime" align="center"></el-table-column>
    <el-table-column label="围栏状态" align="center">
      <template #default="scope">
        <el-switch inactive-value="0" active-value="1" v-model="scope.row.status"
        @change="changeStatus(scope.row.id,scope.row.status)"></el-switch>
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button type="primary" link @click="loadVehicle(scope.row.id)">管理车辆</el-button>
        <el-button type="primary" link @click="deleteGeo(scope.row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
</el-card>
<!--已绑定车辆弹窗-->
<el-dialog title="已绑定车辆列表" v-model="bindVehicleDialogVisible" style="padding:40px;">
  <el-button type="primary" style="margin:10px 0;" @click="showUnbindVehicleList">添加绑定车辆</el-button>
    <el-table :data="bindVehicleArr">
      <el-table-column prop="brand" label="车辆品牌" align="center"></el-table-column>
      <el-table-column prop="license" label="车牌号" align="center"></el-table-column>
      <el-table-column prop="type" label="车辆类型" align="center"></el-table-column>
      <el-table-column label="操作" fixed="right" width="200" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="removeBindVehicle(scope.row.id)">移除</el-button>
        </template>
      </el-table-column>
    </el-table>
</el-dialog>
<!--添加可绑定车辆列表弹窗 -->
<el-dialog title="可绑定车辆列表" v-model="AddBindVehicleDialogVisible" :before-close="handleClose">
  <el-table :data="unBindVehicleArr">
    <el-table-column label="编号" type="index" align="center" width="80px;"></el-table-column>
    <el-table-column prop="brand" label="车辆品牌" align="center"></el-table-column>
    <el-table-column prop="license" label="车牌号" align="center"></el-table-column>
    <el-table-column prop="type" label="车辆类型" align="center"></el-table-column>
    <el-table-column prop="status" label="车辆状态" align="center"></el-table-column>
    <el-table-column label="操作" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="bindNewVehicle(scope.row.id)">绑定</el-button>
      </template>
    </el-table-column>
  </el-table>
</el-dialog>
</template>

<script setup>
import router from "@/router";
import {onMounted,ref} from "vue";
import {ElMessage} from "element-plus";
import axios from "axios";
import qs from "qs";

//已绑定车辆列表弹窗
const bindVehicleDialogVisible = ref(false);
//可绑定车辆列表弹窗
const AddBindVehicleDialogVisible = ref(false);

const geoSearchForm = ref({name:'', status:''})
const geoTableData = ref([]);

const loadGeo = () =>{
  /*
  作用是将 geoSearchForm.value 中包含的查询条件对象序列化为一个 URL 查询字符串。
  这个字符串可以附加在 URL 的末尾，用于发送带有查询参数的 GET 请求。
  比如，如果 geoSearchForm.value 的内容是 { name: 'test', status: 'active' }，
  那么 data 的值将是 'name=test&status=active'。
   */
  let data = qs.stringify(geoSearchForm.value);
  axios.get(BASE_URL+'/v1/geofence/select?'+data).then((response)=>{
    if(response.data.code === 2000){
      geoTableData.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
onMounted(()=>{
  loadGeo();
})

const resetSearch = ()=>{
  geoSearchForm.value = {name:'',status:''};
  loadGeo();
}
var geoId;

const bindVehicleArr = ref([]);
const loadVehicle = (geofenceId)=>{
  bindVehicleDialogVisible.value = true;
  geoId = geofenceId;
  axios.get(BASE_URL+'/v1/vehicle/select?geofenceId='+geofenceId).then((response)=>{
    if(response.data.code === 2000){
      bindVehicleArr.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
const removeBindVehicle = (id) =>{
  axios.post(BASE_URL+'/v1/vehicle/unbind/'+id).then((response)=>{
    if(response.data.code === 2000){
      ElMessage.success('车辆解绑成功');
      loadVehicle(geoId);
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
const unBindVehicleArr = ref([]);
const showUnbindVehicleList = () =>{
  AddBindVehicleDialogVisible.value = true;
  axios.get(BASE_URL+'/v1/vehicle/select?geofenceBindStatus=0').then((response)=>{
    if(response.data.code === 2000){
      unBindVehicleArr.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
const bindNewVehicle = (id)=>{
  axios.post(BASE_URL+'/v1/vehicle/bind/'+geoId+'/'+id).then((response)=>{
    if(response.data.code === 2000){
      ElMessage.success('车辆绑定成功');
      showUnbindVehicleList();
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
const handleClose = ()=>{
  loadVehicle(geoId);
  AddBindVehicleDialogVisible.value=false;
}
const changeStatus = (id,status)=>{
  let data = qs.stringify({id:id,status:status});
  axios.post(BASE_URL+'/v1/geofence/save',data).then((response)=>{
    if(response.data.code ===2000){
      ElMessage.success('修改状态成功');
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
const deleteGeo = (id) =>{
  if(confirm('是否删除该围栏?')){
    axios.post(BASE_URL+'/v1/geofence/delete/'+id).then((response)=>{
      if(response.data.code === 2000){
        ElMessage.success('删除围栏成功');
      }else{
        ElMessage.error('请先移除已绑定车辆');
      }
    })
  }
}

</script>




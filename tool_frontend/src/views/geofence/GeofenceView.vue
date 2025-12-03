<!-- Electric geoFence page -->
<template>
  <div style="height: 6vh;background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">Fence management</span>
    <el-button type="primary" style="float:right;margin-top:13px;"
    @click="router.push('/geofenceMap')">Add new fence</el-button>
  </div>
<!-- Fence searching card -->
<el-card style="margin:20px;height:70px;">
  <el-form :inline="true">
    <el-form-item label="Fence name">
      <el-input placeholder="Please enter" style="width:220px;"
      v-model="geoSearchForm.name" @keydown.enter="loadGeo"></el-input>
    </el-form-item>
    <el-form-item label="Fence status" style="width:290px;">
      <el-select placeholder="Please select" v-model="geoSearchForm.status" @change="loadGeo">
        <el-option label="Enable" value="1"></el-option>
        <el-option label="Disable" value="0"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button @click="resetSearch">Reset</el-button>
      <el-button type="primary" @click="loadGeo">Search</el-button>
    </el-form-item>
  </el-form>
</el-card>
<!--Fence tabel data-->
<el-card style="margin:20px;">
  <el-table stripe :data="geoTableData">
    <el-table-column label="Number" prop="id" type="index" align="center" width="100"></el-table-column>
    <el-table-column label="Fence name" prop="name" align="center"></el-table-column>
    <el-table-column label="Fence coordinate" prop="position" align="center" show-overflow-tooltip="true" width="500"></el-table-column>
    <el-table-column label="Creation time" prop="createTime" align="center"></el-table-column>
    <el-table-column label="Fence status" align="center">
      <template #default="scope">
        <el-switch inactive-value="0" active-value="1" v-model="scope.row.status"
        @change="changeStatus(scope.row.id,scope.row.status)"></el-switch>
      </template>
    </el-table-column>
    <el-table-column label="Operation" align="center">
      <template #default="scope">
        <el-button type="primary" link @click="loadVehicle(scope.row.id)">Manage vehicle</el-button>
        <el-button type="primary" link @click="deleteGeo(scope.row.id)">Delete</el-button>
      </template>
    </el-table-column>
  </el-table>
</el-card>
<!-- Vehicle bounded pop-up window -->
<el-dialog title="Bounded vehicle list" v-model="bindVehicleDialogVisible" style="padding:40px;">
  <el-button type="primary" style="margin:10px 0;" @click="showUnbindVehicleList">Add bounding vehicle</el-button>
    <el-table :data="bindVehicleArr">
      <el-table-column prop="brand" label="Vehicle brand" align="center"></el-table-column>
      <el-table-column prop="license" label="Vehicle number" align="center"></el-table-column>
      <el-table-column prop="type" label="Vehicle type" align="center"></el-table-column>
      <el-table-column label="operation" fixed="right" width="200" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="removeBindVehicle(scope.row.id)">Remove</el-button>
        </template>
      </el-table-column>
    </el-table>
</el-dialog>
<!--Add a pop-up window to the list of vehicles that can be bound -->
<el-dialog title="List of vehicles that can be bound" v-model="AddBindVehicleDialogVisible" :before-close="handleClose">
  <el-table :data="unBindVehicleArr">
    <el-table-column label="Code" type="index" align="center" width="80px;"></el-table-column>
    <el-table-column prop="brand" label="Vehicle brand" align="center"></el-table-column>
    <el-table-column prop="license" label="License number" align="center"></el-table-column>
    <el-table-column prop="type" label="Vehicle type" align="center"></el-table-column>
    <el-table-column prop="status" label="Vehicle status" align="center"></el-table-column>
    <el-table-column label="operations" align="center">
      <template #default="scope">
        <el-button link type="primary" @click="bindNewVehicle(scope.row.id)">Bound</el-button>
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

//List of bound vehicles pop-up window
const bindVehicleDialogVisible = ref(false);
//Available vehicle list pop-up
const AddBindVehicleDialogVisible = ref(false);

const geoSearchForm = ref({name:'', status:''})
const geoTableData = ref([]);

const loadGeo = () =>{

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
      ElMessage.success('Vehicle unbinding successful');
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
      ElMessage.success('Vehicle binding successful');
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
      ElMessage.success('Status changed successfully');
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
const deleteGeo = (id) =>{
  if(confirm('Should the fence be deleted??')){
    axios.post(BASE_URL+'/v1/geofence/delete/'+id).then((response)=>{
      if(response.data.code === 2000){
        ElMessage.success('Fence removed successfully');
      }else{
        ElMessage.error('Please remove the bound vehicle first.');
      }
    })
  }
}

</script>




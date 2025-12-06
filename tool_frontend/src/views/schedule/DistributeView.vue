<!--Vehicle allocation management page-->
<template>
  <div style="height: 6vh;background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">Vehicle allocation</span>
  </div>
  <!-- Application form search card -->
  <el-card style="margin:20px;height: 70px;">
    <el-form :inline="true">
      <el-form-item label="Applicant">
        <el-input placeholder="Please enter applicant" style="width:220px;"
                  v-model="search.username" @keydown.enter.prevent="loadApplication"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch">Reset</el-button>
        <el-button type="primary" @click="loadApplication">Research</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <!-- Application form list -->
  <el-card  style="margin:20px;">
    <el-radio-group style="margin-bottom: 15px;" v-model="type">
      <el-radio-button value="50" size="large" @change="loadApplication">Pending allocation</el-radio-button>
      <el-radio-button value="60" size="large" @change="loadApplication">Allocated</el-radio-button>
    </el-radio-group>
    <el-table :data="appTableData">
      <el-table-column label="Number" type="index" width="100" align="center"></el-table-column>
      <el-table-column label="Applicant" prop="username" align="center"></el-table-column>
      <el-table-column label="Application time" prop="createTime" align="center"></el-table-column>
      <el-table-column label="Departure" prop="departureAddr" align="center"></el-table-column>
      <el-table-column label="Destination" prop="destinationAddr" align="center"></el-table-column>
      <el-table-column label="Reason for vehicle use" prop="reason" align="center"></el-table-column>
      <el-table-column label="Start time" prop="startTime" align="center"></el-table-column>
      <el-table-column label="End time" prop="endTime" align="center"></el-table-column>
      <el-table-column label="Operation" width="100" align="center">
        <template #default="scope">
          <!--The "Assign a vehicle" button is only displayed when the application is "Approved". The "Return a vehicle" button is only displayed when a vehicle has been assigned-->
          <el-button type="primary" link v-if="type==50" @click="loadGeo(scope.row.id)">Vehicle allocation</el-button>
          <!--Note: In the application form, scope.row represents the complete object of the current application, including both the application ID and the vehicle ID-->
          <el-button type="primary" link v-if="type==60" @click="back(scope.row)">Return vehicle</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- Fence vehicle overview pop-up -->
  <el-dialog title="Fence list" v-model="dialogVisible" style="padding:40px;" :before-close="handleClose">
    <el-form>Fence vehicle overview pop-up
      <el-row :gutter="10">
        <el-col :span="8">
          <el-form-item label="Fence name: ">
            <el-input placeholder="Please enter" v-model="searchGeofenceForm.name" @keydown.enter="loadGeo"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="Fence status">
            <el-select placeholder="Please choice" v-model="searchGeofenceForm.status" @change="loadGeo">
              <el-option label="Enable" value="1"/>
              <el-option label="Disable" value="0"/>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4"></el-col>
        <el-col :span="4">
          <el-form-item>
            <el-button-group>
              <el-button @click="resetSearchGeo">Reset</el-button>
              <el-button type="primary" @click="loadGeo">Search</el-button>
            </el-button-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <el-table style="margin:20px 0" :data="geoTableData">
      <el-table-column label="Number" type="index" width="80" align="center"></el-table-column>
      <el-table-column label="Fence name" prop="name" width="200" align="center"></el-table-column>
      <el-table-column label="Total vehicle" prop="totalNum" width="200" align="center"></el-table-column>
      <el-table-column label="Available vehicle" prop="availableNum" align="center"></el-table-column>
      <el-table-column label="Fence status" prop="status" align="center" :formatter="geofenceStatusFormatter"></el-table-column>
      <el-table-column label="Operation" width="100" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="showVehicle(scope.row.id)">Check vehicle</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-dialog>

  <!-- Allocation vehicle pop-up window -->
  <el-dialog title="Allocation vehicle" style="padding:40px;" v-model="vehicleDialogVisible">
    <el-table style="margin:20px 0" :data="vehicleTableData">
      <el-table-column label="Number" type="index" width="80" align="center"></el-table-column>
      <el-table-column label="Vehicle brand" prop="brand" width="200" align="center"></el-table-column>
      <el-table-column label="Vehicle number" prop="license" width="200" align="center"></el-table-column>
      <el-table-column label="Vehicle type" prop="type" align="center"></el-table-column>
      <el-table-column label="Vehicle status" prop="status" align="center"></el-table-column>
      <el-table-column label="Operation" width="100" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="distribute(scope.row.id)">Allocation</el-button>
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
// Used to control whether the fenced vehicle overview pop-up is visible
const dialogVisible = ref(false);
// Used to control whether the vehicle allocation pop-up is visible.
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

// Define a global variable to store the ID of the current application form, which will be used when allocating vehicles
var applicationId;
const loadGeo=(id)=>{
  // Assign the current application ID to a global variable.
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
// Convert the value displayed for the fence status
const geofenceStatusFormatter = (row, column,cellValue,index) => {
  if(cellValue==1){
    cellValue = 'Enable';
  }else if(cellValue==0){
    cellValue = 'Disable'
  }
  return cellValue;
}
// How to handle the "Close Fence" pop-up
const handleClose = ()=>{
  searchGeofenceForm.value = {};
  dialogVisible.value = false;
}

// Define an array to store the loaded fence vehicle data
const vehicleTableData = ref([]);
// Define a method to load vehicle data on a specified fence
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

//The vehicle allocation method assigns a specified vehicle to a specified application form. 
  //`applicationId` is a global variable and was assigned a value when `loadGeo` is called.
const distribute = (vehicleId)=>{
  axios.post(BASE_URL+'/v1/application/distribute/'+applicationId+'/'+vehicleId)
      .then((response)=>{
        if (response.data.code==2000){
          dialogVisible.value = false; //Close the fence list pop-up
          vehicleDialogVisible.value = false;// Close the vehicle list pop-up
          loadApplication();//Reload application data after allocation
          ElMessage.success('Vehicle allocation successful!');
        }else {
          ElMessage.error(response.data.msg);
        }
      })
}

//Return vehicle
const back = (row)=>{
  console.log(row.id,typeof row.id);
  console.log(row.vehicleId,typeof row.vehicleId);
  axios.post(BASE_URL+'/v1/application/back/'+row.id+'/'+row.vehicleId)
      .then((response)=>{
        if (response.data.code==2000){
          loadApplication();//Reload the application data after returning the car.
          ElMessage.success('Car returned successfully!');
        }else {
          ElMessage.error(response.data.msg);
        }
      })
}
</script>
<style scoped>
</style>

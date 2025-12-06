<!-- Vehicle Management Page -->
<template>
  <div style="height: 6vh;background-color:#fcf9f9;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">Vehicle management</span>
    <el-button @click="dialogVisible=true" type="primary" style="float:right;margin-top:13px;">New vehicles</el-button>
  </div>
<!--  New vehicles pop-up window -->
  <el-dialog :title="dialogTitle" v-model="dialogVisible"
             :before-close="handleClose" style="width:1000px;padding:40px;">
    <el-form label-width="80px" label-position="top">
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="Vehicle brand">
            <el-input placeholder="Please enter content" v-model="saveVehicleForm.brand"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Vehicle number">
            <el-input placeholder="Please enter content" v-model="saveVehicleForm.license"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="Vehicle type">
            <el-input placeholder="Please enter content" v-model="saveVehicleForm.model"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Vehicle Identification Number">
            <el-input placeholder="Please enter content" v-model="saveVehicleForm.code"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="Vehicle displacement">
            <el-select placeholder="Please select" v-model="saveVehicleForm.displacement">
              <el-option label="1.6" value="1"></el-option>
              <el-option label="2.5" value="2"></el-option>
              <el-option label="4" value="3"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Vehicle type">
            <el-select placeholder="Please select" v-model="saveVehicleForm.type">
              <el-option label="Car"></el-option>
              <el-option label="Truck"></el-option>
              <el-option label="Bus"></el-option>
              <el-option label="Trailer"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="Vehicle color">
            <el-select placeholder="Please select" v-model="saveVehicleForm.color">
              <el-option label="Black" value="10"></el-option>
              <el-option label="White" value="20"></el-option>
              <el-option label="Blue" value="30"></el-option>
              <el-option label="Grey" value="40"></el-option>
              <el-option label="Silver" value="50"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Mileage">
            <el-input placeholder="Please enter content" v-model="saveVehicleForm.kilometers"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gtter="30">
        <el-col :span="12">
          <el-form-item label="Purchase time">
            <el-date-picker type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
            v-model="saveVehicleForm.buyTime"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Registration time">
            <el-date-picker type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
            v-model="saveVehicleForm.regTime"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="Purchase price">
            <el-input placeholder="Please enter your content" v-model="saveVehicleForm.price"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Battery Type">
            <el-select placeholder="Please select" v-model="saveVehicleForm.batteryType">
              <el-option label="Lead-acid batteries" value="10"></el-option>
              <el-option label="Nickel-metal hydride batteries" value="20"></el-option>
              <el-option label="Sodium-sulfur batteries" value="30"></el-option>
              <el-option label="Secondary lithium batteries" value="40"></el-option>
              <el-option label="Air battery" value="50"></el-option>
              <el-option label="Ternary lithium battery" value="60"></el-option>
              <el-option label="Alkaline fuel cells" value="70"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">Cancle</el-button>
      <el-button type="primary" @click="saveVehicle">Enter</el-button>
    </template>
  </el-dialog>
  <!-- Vehicle search card -->
  <el-card style="margin:20px;height:70px;">
    <el-form :inline="true">
      <el-form-item label="Vehicle card">
        <el-input placeholder="Please enter content" style="width:220px;"
                  v-model="searchVehicleForm.brand" @keydown.enter="loadVehicle"></el-input>
      </el-form-item>
      <el-form-item label="Vehicle number" style="width:290px;">
        <el-input placeholder="Please enter content" style="width:220px;"
                  v-model="searchVehicleForm.license" @keydown.enter="loadVehicle"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch">Reset</el-button>
        <el-button type="primary" @click="loadVehicle">Search</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <!--Vehicle search -->
  <el-card style="margin:20px;">
    <el-table :data="vehicleArr">
      <el-table-column type="index" label="Number" width="80" align="center"></el-table-column>
      <el-table-column prop="brand" label="Vehicle brand" align="center"></el-table-column>
      <el-table-column prop="license" label="Vehicle number" align="center"></el-table-column>
      <el-table-column prop="code" label="Vehicle identification code" align="center"></el-table-column>
      <el-table-column prop="type" label="Vehicle type" align="center"
                       :formatter="vehicleTypeFormatter"></el-table-column>
      <el-table-column prop="price" label="Price" align="center"></el-table-column>
      <el-table-column prop="buyTime" label="Purchase time" align="center"></el-table-column>
      <el-table-column prop="regTime" label="Registration time" align="center"></el-table-column>
      <el-table-column prop="batteryType" label="Battery type" align="center"
                       :formatter="batteryTypeFormatter"></el-table-column>
      <el-table-column prop="status" label="Vehicle status" align="center"
                       :formatter="vehicleStatusFormatter"></el-table-column>
      <el-table-column label="Operation" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="editVehicle(scope.row.id)">Edit</el-button>
          <el-button link type="primary" size="small" @click="deleteVehicle(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import {onMounted, ref} from "vue";
import qs from "qs";
import axios from "axios";
import {ElMessage} from "element-plus";

const dialogVisible = ref(false);
const dialogTitle = ref('Add vehicle');

const vehicleArr = ref([]);
const searchVehicleForm = ref({brand:'',license:''});

const loadVehicle = () =>{
  let data = qs.stringify(searchVehicleForm.value);
  console.log(data);
  axios.get(BASE_URL+'/v1/vehicle/select?'+data).then((response)=>{
    if(response.data.code === 2000){
      vehicleArr.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

onMounted(()=>{
  loadVehicle();
})

const resetSearch = ()=> {
  searchVehicleForm.value = {};
  loadVehicle();
}

const vehicleTypeFormatter = (row,column,cellValue,index)=>{
  if(cellValue==10){
    cellValue='Car';
  }else if(cellValue==20){
    cellValue='Truck';
  }else if(cellValue==30){
    cellValue='Bus';
  }else if(cellValue==40){
    cellValue='Trailer';
  }
  return cellValue;
}

const vehicleStatusFormatter = (row,column,cellValue,index)=>{
  if(cellValue==1){
    cellValue='Available';
  }else if(cellValue==2){
    cellValue='Occupied;'
  }
  return cellValue;
}

const batteryTypeFormatter = (row,column,cellValue,index) => {
  if(cellValue==10){
    cellValue='Lead-acid batteries';
  }else if(cellValue==20){
    cellValue='Nickel-metal hydride batteries';
  }else if(cellValue==30){
    cellValue= 'Sodium-sulfur batteries';
  }else if (cellValue==40){
    cellValue= 'Secondary lithium batteries';
  }else if (cellValue==50){
    cellValue= 'Air battery';
  }else if (cellValue==60){
    cellValue= 'Ternary lithium battery';
  }else if (cellValue==70){
    cellValue= 'Alkaline fuel cells';
  }
  return cellValue;
}

const saveVehicleForm = ref({
  brand:'',
  license:'',
  model:'',
  code:'',
  displacement:'',
  type:'',
  color:'',
  kilometers:'',
  buyTime:'',
  regTime:'',
  price:'',
  batteryType:''
});

const saveVehicle = ()=>{
  let data = qs.stringify(saveVehicleForm.value);
  axios.post(BASE_URL+'/v1/vehicle/save',data).then((response)=>{
    if(response.data.code===2000){
      ElMessage.success('Save success!');
      saveVehicleForm.value = {};
      dialogVisible.value = false;
      loadVehicle();
    }else{
      ElMessage.error('License plate number or vehicle identification number cannot be duplicated.');
    }
  })
}

const handleClose = () =>{
  if(confirm('Close?')){
    dialogVisible.value = false;
    saveVehicleForm.value = {};
  }
}

const editVehicle = (id) =>{
  dialogVisible.value = true;
  dialogTitle.value = 'Edit vehicle';
  //Use the `get` function to complete the editing process. Once editing is complete, 
  //saving the code doesn't require rewriting the code, as the functionality is already included in the `saveVehicle` function above.
  axios.get(BASE_URL+'/v1/vehicle/select?id='+id).then((response)=>{
    if(response.data.code === 2000){
      saveVehicleForm.value = response.data.data[0];
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

const deleteVehicle = (id) =>{
  if(confirm('Confirm deletion?')){
    axios.post(BASE_URL+'/v1/vehicle/delete/'+id).then((response)=>{
      if(response.data.code === 2000){
        ElMessage.success('Delete successful');
        loadVehicle();
      }else{
        ElMessage.error(response.data.msg);
      }
    })
  }
}


</script>



<!--Dict management page-->
<template>
  <div style="background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">Dice management</span>
  </div>
  <!-- Dict data searching card -->
  <el-card style="margin:20px;height: 70px;">
    <el-form :inline="true">
      <el-form-item label="Dict name">
        <el-input placeholder="Please enter" style="width:220px;" v-model="searchForm.name"
                  @keydown.enter="selectDict()"></el-input>
      </el-form-item>
      <el-form-item label="Dict code">
        <el-input placeholder="Please enter" style="width:220px;" v-model="searchForm.code"
                  @keydown.enter="selectDict()"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch()">Reset</el-button>
        <el-button type="primary" @click="selectDict()">Search</el-button>
      </el-form-item>
    </el-form>
  </el-card>
  <el-card style="margin:20px;">
    <div style="margin:10px 0px 40px 10px;">
      Dict list
      <el-button type="primary" style="float: right;margin-top:-3px;" @click="addDict">Add new dict</el-button>
    </div>
    <el-table :data="dictArr">
      <el-table-column type="index" label="Code" width="80" align="center"/>
      <el-table-column prop="name" label="Dict name" align="center"/>
      <el-table-column prop="code" label="Dict code" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="showDictOption(scope.row.id)">{{ scope.row.code }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="Remark" align="center"/>
      <el-table-column prop="createTime" label="Creation time" align="center"/>
      <el-table-column label="Operation" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="editDict(scope.row.id)">Edit</el-button>
          <el-button link type="primary" size="small" @click="deleteDict(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- Save dict pop-up window -->
  <el-dialog :title="dialogTitle" v-model="dialogVisible" style="width:1000px;padding:40px;"
             :before-close="handleClose">
    <el-form label-width="80px" label-position="top">
      <el-form-item label="Dict name">
        <el-input placeholder="Please enter" v-model="saveDictForm.name"></el-input>
      </el-form-item>
      <el-form-item label="Dict code">
        <el-input placeholder="Please enter" v-model="saveDictForm.code"></el-input>
      </el-form-item>
      <el-form-item label="Remark">
        <el-input placeholder="Please enter" v-model="saveDictForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose()">Cancle</el-button>
      <el-button type="primary" @click="saveDict()">Save</el-button>
    </template>
  </el-dialog>
</template>
<script setup>
import {onMounted, ref} from "vue";
import router from "@/router";
import qs from "qs";
import axios from "axios";
import {ElMessage} from "element-plus";

const dialogVisible = ref(false);
const dialogTitle = ref('Add dict');

const dictArr = ref([
  {name: 'Vehicle color', code: 'vehicle_color', remark: 'Manage vehicle color', createTime: '2024-04-28'},
  {name: 'Battery type', code: 'battery_type', remark: 'Manage battery type', createTime: '2024-04-28'},
  {name: 'Vehicle type', code: 'vehicle_type', remark: 'Manage vehicle type', createTime: '2024-04-28'},
  {name: 'Status', code: 'general_status', remark: 'Manage enable/disable status', createTime: '2024-04-28'},
  {name: 'Application status', code: 'application_status', remark: 'Manage application status', createTime: '2024-04-28'},
  {name: 'Review status', code: 'audit_status', remark: 'Management audit form status', createTime: '2024-04-28'}
]);

onMounted(() => {
  selectDict();
})
//Query all dictionary information
const searchForm = ref({name: '', code: '',});
const selectDict = () => {
  let data = qs.stringify(searchForm.value);
  console.log(data);
  axios.get(BASE_URL+'/v1/dict/select?' + data).then((response) => {
    if (response.data.code == 2000) {
      dictArr.value = response.data.data;
    } else {
      ElMessage(response.data.msg);
    }
  })
}
//Reset user search information
const resetSearch = () => {
  searchForm.value = {};
  selectDict();
}
//Add a dictionary
const addDict = ()=>{
  dialogVisible.value=true;
  dialogTitle.value = 'Add dictionary';
}
//Save dictionary information
const saveDictForm = ref({name: '', code: '', remark: ''});
const saveDict = () => {
  dialogVisible.value = true;
  let data = qs.stringify(saveDictForm.value);
  axios.post(BASE_URL+'/v1/dict/save', data).then((response) => {
    if (response.data.code == 2000) {
      ElMessage.success('Succeed!');
      dialogVisible.value = false;
      saveDictForm.value = {};
      selectDict();
    } else {
      ElMessage.error('Failed!');
    }
  })
}
//Edit dictionary information
const editDict = (id) => {
  dialogTitle.value = 'Edit dictionary information';
  dialogVisible.value = true;
  axios.get(BASE_URL+'/v1/dict/select?id=' + id).then((response) => {
    if (response.data.code == 2000) {
      //The return value is a list
      saveDictForm.value = response.data.data[0];
    } else {
      ElMessage.error(response.data.msg);
    }
  })
}

//Implement the function of the cancel button
const handleClose = () => {
  if (confirm('Do you want to close this window?')) {
    //After the user confirms the closure, close both the approval pop-up and the rejection confirmation pop-up.
    dialogVisible.value = false;
    //When canceling, all entered data is cleared, and the form returns to its initial state.
    saveDictForm.value={};
  }
}

//Delete dictionary
const deleteDict = (id) => {
  if (confirm('Are you sure you want to delete this dictionary??')) {
    axios.post(BASE_URL+'/v1/dict/delete/' + id).then((response) => {
      if (response.data.code === 2000) {
        ElMessage.success('Delete succeed!');
        selectDict();
      } else {
        ElMessage.error(response.data.msg);
      }
    })
  }
}

const showDictOption = (id) => {
  router.push('/dictOption?id=' + id);
}
</script>
<style scoped>
</style>




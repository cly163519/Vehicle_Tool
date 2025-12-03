<!--Dict page-->
<template>
  <div style="background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">Dict management</span>
    <el-button @click="goBack()" type="primary" style="float:right;margin-top:13px;">return</el-button>
  </div>
  <!-- Dict items search card -->
  <el-card style="margin:20px;height: 70px;">
    <el-form :inline="true">
      <el-form-item label="Dict items name">
        <el-input placeholder="Please enter.." style="width:220px;" v-model="searchForm.label"
                  @keydown.enter.prevent="selectDictOption"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch()">Reset</el-button>
        <el-button type="primary" @click="selectDictOption">Search</el-button>
      </el-form-item>
    </el-form>
  </el-card>
  <el-card style="margin:20px;">
    <div style="margin:10px 0px 40px 10px;">
      Dict items list
      <el-button type="primary" style="float: right;margin-top:-3px;" @click="dialogVisible=true">Add dict item</el-button>
    </div>
    <el-table :data="dictOptionArr">
      <el-table-column type="index" label="number" width="80" align="center"/>
      <el-table-column prop="label" label="dict item name" align="center"/>
      <el-table-column prop="value" label="dict item value" align="center"/>
      <el-table-column prop="sort" label="dict item order" align="center"/>
      <el-table-column prop="remark" label="remarks" align="center" width="320"/>
      <el-table-column prop="createTime" label="create time" align="center"/>
      <el-table-column label="operation" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="editDictOption(scope.row.id)">edit</el-button>
          <el-button type="primary" link @click="deleteDictOption(scope.row.id)">delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <!-- Save dict item pop-up window -->
  <el-dialog :title="dialogTitle" v-model="dialogVisible" style="width:1000px;padding:40px;"
             :before-close="handleClose">
    <el-form label-width="80px" label-position="top">
      <el-form-item label="dict item name">
        <el-input placeholder="please enter" v-model="saveDictOptionForm.label"></el-input>
      </el-form-item>
      <el-form-item label="dict item value">
        <el-input placeholder="please enter" v-model="saveDictOptionForm.value"></el-input>
      </el-form-item>
      <el-form-item label="dict item order">
        <el-input placeholder="please enter" v-model="saveDictOptionForm.sort"></el-input>
      </el-form-item>
      <el-form-item label="remark">
        <el-input placeholder="please enter" v-model="saveDictOptionForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose()">cancel</el-button>
      <el-button type="primary" @click="saveDictOption()">save</el-button>
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
const dialogTitle = ref('add new dict item');
const dictId = new URLSearchParams(window.location.search).get('id');

const dictOptionArr = ref([
  {label: 'car', value: 10, sort: 1, remark: 'Vehicle type', createTime: '2024-04-28'},
  {label: 'bus', value: 20, sort: 2, remark: 'Vehicle type', createTime: '2024-04-28'},
  {label: 'truck', value: 30, sort: 3, remark: 'Vehicle type', createTime: '2024-04-28'},
  {label: 'trailer', value: 40, sort: 4, remark: 'Vehicle type', createTime: '2024-04-28'}
]);

onMounted(() => {
  selectDictOption();
})
//Query all dictionary entries
const searchForm = ref({dictId:dictId,label: ''});
const selectDictOption = () => {
  //This is empty, console.log(new URLSearchParams(window.location.search).get('id'));
  let data = qs.stringify(searchForm.value);
  console.log(data);
  axios.get(BASE_URL+'/v1/dictoption/select?' + data).then((response) => {
    if (response.data.code == 2000) {
      dictOptionArr.value = response.data.data;
      console.log(response.data.data);
    } else {
      ElMessage(response.data.msg);
    }
  })
}
//Reset user's searching info
const resetSearch = () => {
  console.log('Guess if it will call [something] as soon as you enter.')
  searchForm.value = {dictId:dictId,label: ''};
  selectDictOption();
}

//Save dict item info
const saveDictOptionForm = ref({dictId:dictId, label: '', value: '', sort: '', remark: ''});
const saveDictOption = () => {
  dialogVisible.value = true;
  let data = qs.stringify(saveDictOptionForm.value);
  axios.post(BASE_URL+'/v1/dictoption/save', data).then((response) => {
    if (response.data.code == 2000) {
      ElMessage.success('Succeed!');
      dialogVisible.value = false;
      //Note that the dictionary ID here cannot be cleared; otherwise, multiple dictionary items cannot be added consecutively at once.
      saveDictOptionForm.value = {dictId:dictId, label: '', value: '', sort: '', remark: ''};
      selectDictOption();
    } else {
      ElMessage.error('Failed!');
    }
  })
}
//Edit dict item info
const editDictOption = (id) => {
  dialogTitle.value = 'Edit dict item';
  dialogVisible.value = true;
  axios.get(BASE_URL+'/v1/dictoption/select?id=' + id).then((response) => {
    if (response.data.code == 2000) {
      //Note!!! The return value is a list, remember to add [0].
      saveDictOptionForm.value = response.data.data[0];
    } else {
      ElMessage.error(response.data.msg);
    }
  })
}

//Implement the function of the cancel button
const handleClose = () => {
  if (confirm('Close window??')) {
    dialogVisible.value = false;
    //This command will clear the form data in the pop-up window for adding dictionary items; otherwise, 
    //clicking the pop-up window will display the data of the dictionary items that were just deleted.
    saveDictOptionForm.value = {dictId:dictId, label: '', value: '', sort: '', remark: ''};
  }
}

//Delete dict item
const deleteDictOption = (id) => {
  if (confirm('Are you sure you want to delete this dictionary?')) {
    axios.post(BASE_URL+'/v1/dictoption/delete/' + id).then((response) => {
      if (response.data.code === 2000) {
        ElMessage.success('Delete success!');
        selectDictOption();
      } else {
        ElMessage.error(response.data.msg);
      }
    })
  }
}

//To make the "back" button clickable, return to the previous page, the "dictionary list page".
const goBack = () => {
  window.history.go(-1)
}
</script>
<style scoped>
</style>




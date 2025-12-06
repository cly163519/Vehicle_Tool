<!--User Management Page-->
<template>
  <div style="height: 6vh;background-color:#fffbfb;padding: 10px 20px;">
    <span style="font-size:20px;line-height: 60px;float: left;">User management</span>
    <el-button @click="dialogVisible=true" type="primary"
               style="float: right;margin-top:10px;">Create user
    </el-button>
  </div>
  <!-- Create/edit user pop-up window -->
  <!-- :before-close="handleClose"Call the handleClose method before closing to prevent the pop-up from closing even when clicking outside the window  -->
  <el-dialog :title="dialogTitle" v-model="dialogVisible"
             :before-close="handleClose"
             style="width: 1000px;padding:40px;">
    <el-form label-position="top" label-width="80px">
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="username">
            <el-input placeholder="Please enter username" v-model="saveUserForm.username"
                      :disabled="saveUserForm.id!=null"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Phone number">
            <el-input placeholder="Please enter phone number" v-model="saveUserForm.phone"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="email">
            <el-input placeholder="Please enter email address" v-model="saveUserForm.email"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Age">
            <el-input placeholder="Please enter age" v-model="saveUserForm.age"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="6">
          <el-form-item label="Rank">
            <el-select placeholder="Please select" v-model="saveUserForm.level" @change="loadLeader">
              <!--              <el-option label="Staff" value="10"></el-option>-->
              <!--              <el-option label="Manager" value="20"></el-option>-->
              <!--              <el-option label="Director" value="30"></el-option>-->
              <!--              <el-option label="President" value="40"></el-option>-->
              <el-option v-for="item in levelOptions"
                         :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="Direct supervisor">
            <el-select placeholder="Please select" v-model="saveUserForm.parentId">
              <!--              <el-option label="shaoyun" value="1"></el-option>-->
              <!--              <el-option label="mike" value="2"></el-option>-->
              <el-option v-for="item in leaderOptions"
                         :label="item.username" :value="item.id"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="gender">
            <el-radio-group v-model="saveUserForm.gender">
              <el-radio label="Male" border value="1" style="margin: 0;"></el-radio>
              <el-radio label="Female" border value="0"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="User status">
            <el-radio-group v-model="saveUserForm.status">
              <el-radio label="Enable" border value="1" style="margin: 0;"></el-radio>
              <el-radio label="Disable" border value="0"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">Cancle</el-button>
      <el-button type="primary" @click="saveUser">Save</el-button>
    </template>
  </el-dialog>

  <!-- User query card -->
  <el-card style="margin: 20px;height: 70px;">
    <el-form :inline="true" style="float: left;">
      <el-form-item label="User Search" >
        <el-input placeholder="Please enter username" style="width: 220px;"
                  v-model="searchUserForm.username" @keydown.enter="loadUser"></el-input>
      </el-form-item>
      <el-form-item label="User status">
        <el-select placeholder="Please enter user status" style="width: 290px;"
                   v-model="searchUserForm.status" @change="loadUser">
          <el-option label="Enable" value="1"></el-option>
          <el-option label="Disable" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch">Reset</el-button>
        <el-button type="primary" @click="loadUser">Search</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <!-- User information form -->
  <el-card style="margin: 20px;">
    <el-table :data="userArr">
      <el-table-column type="index" label="Number" width="80" align="center"></el-table-column>
      <el-table-column prop="username" label="User name" align="center"></el-table-column>
      <el-table-column prop="phone" label="Phone number" align="center"></el-table-column>
      <el-table-column prop="createTime" label="Joining time" align="center"></el-table-column>
      <el-table-column prop="status" label="User status" align="center">
        <template #default="scope">
          <el-switch @change="changeStatus(scope.row.id,scope.row.status)" active-value="1" inactive-value="0" v-model="scope.row.status" :disabled="scope.row.level==40"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="Operation" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" link
                     :disabled="scope.row.level==40"
                     @click="resetPassword(scope.row.id)">Password reset</el-button>
          <el-button type="primary" size="small" link
                     :disabled="scope.row.level==40"
                     @click="editUser(scope.row.id)">Edit</el-button>
          <el-button type="primary" size="small" link
                     :disabled="scope.row.level==40" @click="deleteUser(scope.row.id)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";
import qs from "qs";

//Define a variable to control whether the new user pop-up appears
const dialogVisible = ref(false);
//Define a variable to store the pop-up title.
const dialogTitle = ref('Create user');

//Define an array to store employee job level options
const levelOptions = ref([
  {label:'staff',value:'10'},
  {label:'manager',value:'20'},
  {label:'director',value:'30'},
  {label:'president',value:'40'}
]);
//Define an array to store data of direct supervisors
const leaderOptions = ref([
  {username:'shaoyun',id:'1'},
  {username:'mike',id:'2'}
]);
//Define a method to obtain data from direct supervisors.
const loadLeader = ()=>{
  //Optimization: If you have already selected a direct supervisor and want to reselect a job level, 
  //you need to clear the previous direct supervisor array and the selected direct supervisor data.
  leaderOptions.value = [];
  saveUserForm.value.parentId = '';
  //Retrieve the currently selected job level. The direct supervisor's job level is the current user's job level + 10. Note that this should be converted to a number type.
  let level = parseInt(saveUserForm.value.level)+10;
  //Send a request to the backend to retrieve data
  axios.get(BASE_URL+'/v1/user/select?level='+level).then((response)=>{
    if(response.data.code == 2000){
      leaderOptions.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

//Define an object to collect form information from the add/modify employee pop-up window.
const saveUserForm = ref({
  username:'',
  phone:'',
  email:'',
  age:'',
  level:'',
  parentId:'',
  gender:'',
  status:''
});

//Define a method to save users
const saveUser = ()=>{
  let data = qs.stringify(saveUserForm.value);
  console.log(data);
  axios.post(BASE_URL+'/v1/user/save',data)
      .then((response)=>{
        if(response.data.code == 2000){
          ElMessage.success('Saved successfully!');
          dialogVisible.value = false;
          saveUserForm.value = {};//Clear the form data that has already been entered into the database; otherwise, the pop-up window will display the previous values ​​again.
          //After adding/updating a user, the user data needs to be reloaded.
          loadUser();
        }else{
          ElMessage.error(response.data.msg);
        }
      })
}

//Methods for closing pop-ups
const handleClose = ()=>{
  if(confirm('Are you sure you want to close this window?')){
    //After the user confirms the closure, the pop-up window will close
    dialogVisible.value = false;
    //Clear the entered form data
    saveUserForm.value = {};
  }
}
//Define an array to store the user list data
const userArr = ref([
  {username:'ss',phone:'18811112222',createTime:'2022-01-01',status:'1'},
  {username:'yy',phone:'18811113333',createTime:'2022-03-03',status:'0'}
]);
//Define an object to store query conditions
const searchUserForm = ref({username:'',status:''});
//Define a method for loading user data
const loadUser = ()=>{
  let data = qs.stringify(searchUserForm.value);
  console.log(data);
  //If there are search criteria, the search will be performed according to those criteria; otherwise, all employee information will be searched by default.
  axios.get(BASE_URL+'/v1/user/select?'+data).then((response)=>{
    //If the backend returns a status code of 2000, it means the process was successful
    if(response.data.code==2000){
      //Store the successfully retrieved data into an array bound to the table
      userArr.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
//Method to be called immediately after the page loads
onMounted(()=>{
  // Load employee data
  loadUser();
})

// Reset search information
const resetSearch = ()=>{
  // Clear the previously entered search terms
  searchUserForm.value = {};
  // Reload all user data
  loadUser();
}

// Reset password (RESTful style)
const resetPassword = (userId)=>{
  axios.post(BASE_URL+'/v1/user/update/password/'+userId).then((response)=>{
    if(response.data.code == 2000){
      ElMessage.success('Password reset successful!');
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

//Edit staff
const editUser = (id)=>{
  //Open the pop-up window and change the title
  dialogVisible.value = true;
  dialogTitle.value = 'Edit staff';
  // Send a request to the backend: Retrieve information for a specified employee based on their employee ID
  axios.get(BASE_URL+'/v1/user/select?id='+id).then((response)=>{
    if(response.data.code == 2000){
      //Note: The backend returns a List<UserVO> list! So here is data[0]
      saveUserForm.value = response.data.data[0];
      // Get optional direct supervisor data
      let level = parseInt(saveUserForm.value.level)+10;
      // Send a request to the backend to retrieve data
      axios.get(BASE_URL+'/v1/user/select?level='+level).then((response)=>{
        if(response.data.code == 2000){
          leaderOptions.value = response.data.data;
        }else{
          ElMessage.error(response.data.msg);
        }
      })
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

//Edit user's status
const changeStatus = (userId,status)=>{
  axios.post(BASE_URL+'/v1/user/update/status/'+userId+'/'+status)
      .then((response)=>{
        if(response.data.code==2000){
          ElMessage.success('Edit successful!');
        }else{
          ElMessage.error(response.data.msg);
          loadUser();
        }
      })
}

//Delete staff
const deleteUser = (userId)=>{
  if(confirm('Are you sure you want to delete this employee?')){
    axios.post(BASE_URL+'/v1/user/delete/'+userId).then((response)=>{
      if(response.data.code==2000){
        ElMessage.success('Delete successful!');
        //After successful deletion, reload the user list
        loadUser();
      }else{
        ElMessage.error(response.data.msg);
      }
    })
  }
}
</script>
<style scoped>
</style>

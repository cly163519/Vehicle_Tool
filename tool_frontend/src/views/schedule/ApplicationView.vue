<!-- Vehicle application page -->
<template>
  <div style="height: 6vh;background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">Application Form List</span>
    <el-button type="primary" style="float:right;margin-top:13px;"
               @click="addApplicationDialogVisible=true"
               :disabled="user.parentId==null">Apply for a car</el-button>
    <!--  v-if="user.parentId!=null If the person is the CEO (without a superior rank), no car application is required; the button can be disabled or hidden -->
  </div>
  <!-- Application for car rental pop-up window -->
  <el-dialog title="Create application form" v-model="addApplicationDialogVisible"
             style="width:1000px;padding:40px;" :before-close="handleClose">
    <el-form label-width="80px" label-position="top">
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="Car user">
            <el-input readonly :value="user.username"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Usage time">
            <el-date-picker
                type="datetimerange"
                range-separator="to"
                start-placeholder="Start date"
                end-placeholder="End date"
                value-format="YYYY-MM-DD HH:mm:ss"
                format="YYYY-MM-DD HH:mm:ss"
                v-model="times"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="Vehicle departure point">
            <el-input placeholder="Please enter" v-model="addForm.departureAddr"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Vehicle destination">
            <el-input placeholder="Please enter" v-model="addForm.destinationAddr"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="Reason for vehicle use">
            <el-input placeholder="Please enter" type="textarea" resize="none" :rows="3" v-model="addForm.reason"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Remark">
            <el-input placeholder="Please enter" type="textarea" resize="none" :rows="3" v-model="addForm.remark"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="Driver lisence">
            <el-upload
                v-model:file-list="fileList"
                v-model:action="actionUrl"
                name="file"
                limit="1"
                list-type="picture-card"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemove"
            >
              <el-icon>
                <Plus/>
              </el-icon>
            </el-upload>
            <el-dialog v-model="dialogVisible">
              <img w-full :src="dialogImageUrl"/>
            </el-dialog>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="Approver">
            <el-row :gutter="10">
              <el-col :span="12" v-if="auditUserOpts.length > 0">
                <el-tag type="primary">
                  <el-icon style="margin-right:5px;position:relative;top:2px;">
                    <User/>
                  </el-icon>
                  <span>{{ auditUserOpts[0].username }}</span>
                </el-tag>
              </el-col>
              <el-col :span="12" v-if="auditUserOpts.length > 1">
                <el-tag type="primary">
                  <el-icon style="margin-right:5px;position:relative;top:2px;">
                    <User/>
                  </el-icon>
                  <span>{{ auditUserOpts[1].username }}</span>
                </el-tag>
              </el-col>
            </el-row>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">Cancel</el-button>
      <el-button type="primary" @click="addApplication">Enter</el-button>
    </template>
  </el-dialog>

  <!-- Car Use Application List Search Card -->
  <el-card style="margin: 20px;">
    <el-form style="padding-top:10px;">
      <el-row :gutter="30">
        <el-col :span="5">
          <el-form-item label="Departure">
            <el-input placeholder="Enter departure" v-model="searchApplication.departureAddr"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="Destination">
            <el-input placeholder="Enter destination" v-model="searchApplication.destinationAddr"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="Application status">
            <el-select placeholder="Please select" v-model="searchApplication.status">
              <!-- <el-option label="Initiated" value="10"></el-option>-->
              <!-- <el-option label="Cancel" value="20"></el-option>-->
              <!-- <el-option label="Under review" value="30"></el-option>-->
              <!-- <el-option label="Reject" value="40"></el-option>-->
              <!-- <el-option label="Approved" value="50"></el-option>-->
              <!-- <el-option label="Vehicle allocation" value="60"></el-option>-->
              <!-- <el-option label="Closed" value="70"></el-option>-->
              <el-option v-for="item in auditOptions"
                         :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item>
            <el-button @click="resetSearch">Reset</el-button>
            <el-button type="primary" @click="loadApplication">Research</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-card>
  <!-- Vehicle Use Application Form -->
  <el-card style="margin:20px">
    <el-table :data="tableData">
      <el-table-column type="index" width="80" align="center" prop="id" label="Number"></el-table-column>
      <el-table-column align="center" prop="username" label="Applicant"></el-table-column>
      <el-table-column align="center" prop="departureAddr" label="Departure"></el-table-column>
      <el-table-column align="center" prop="destinationAddr" label="Destination"></el-table-column>
      <el-table-column align="center" prop="reason" label="Reason for using the vehicle"></el-table-column>
      <el-table-column align="center" prop="auditUsernameList" label="Reviewer"></el-table-column>
      <el-table-column align="center" prop="startTime" label="Use start time"></el-table-column>
      <el-table-column align="center" prop="endTime" label="Use end time"></el-table-column>
      <el-table-column align="center" prop="status" label="Application for status"
                       :formatter="appStatusFormatter"></el-table-column>
      <el-table-column label="Operation" width="120" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" link
                     :disabled="scope.row.status!=10" @click="cancel(scope.row.id)">Cancel</el-button>
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
//Define variables to control whether the application form pop-up appears
const addApplicationDialogVisible = ref(false);

//Get currently logged-in user information
const user = ref(getUser());
//Define an array to store the approvers (including their names) from the newly added application forms.
const auditUserOpts = ref([]);
//Define an array to store the approver ID corresponding to each new application form, for use in database entry.
const auditUserIdList = ref([]);
//Define the method for loading approvers
const loadAuditUser = ()=>{
  //Get the parent ID of the currently logged-in user
  let parentId = user.value.parentId;
  //Retrieve the corresponding leader information based on the parent ID.
  axios.get(BASE_URL+'/v1/user/select/audit/'+parentId).then((response)=>{
    if(response.data.code==2000){
      auditUserOpts.value = response.data.data;
      // Retrieve each approver from auditUserOpts and obtain their ID.
      // Store them sequentially in the auditUserIdList array, and send them to the backend along with any new application.
      for(let i = 0; i<auditUserOpts.value.length ;i++){
        auditUserIdList.value.push(auditUserOpts.value[i].id);
      }
    }
  })
}

onMounted(()=>{
  //Since the direct supervisor data of the current applicant (login user) is fixed, 
  //it only needs to be requested once when the page loads, and there is no need to make repeated requests.
  loadAuditUser();
})
//The user ID is retrieved from localstorage; it's the currently logged-in username. Since input is not allowed, two-way binding is unnecessary to retrieve the input value.
//The approver is a fixed value retrieved from the backend, so two-way binding is also unnecessary to retrieve the input value.
//Define a variable to store the "usage time" in the form. Note: This is an array containing the start and end times.
const times = ref([]);
//Define an object to store application form data.
const addForm = ref({
  departureAddr: '', //Departure
  destinationAddr: '', //Destination
  reason: '', //Reason for using car
  remark:'' //Remark
});
//Define a method to send a new application request
const addApplication = ()=>{
  if(fileList.value.length==0){
    ElMessage.error('Upload image!');
    return;
  }
  // Get the path of the uploaded image from the upload image component. fileList.value[0] represents the first image object
 //.response.data is returned by our uploadController /2024/06/18/UUID.suffix
  addForm.value.imgUrl = fileList.value[0].response.data;//Driver's license image path
  addForm.value.userId = user.value.id;//Applicant's id
  addForm.value.username = user.value.username;//Applicant's name
  addForm.value.startTime = times.value[0];//Start time
  addForm.value.endTime = times.value[1];//End time
  //Note: The approver ID data also needs to be provided to the backend, 
  //because the data in the approval table needs to be updated accordingly for transactional additions
  addForm.value.auditUserIdList = auditUserIdList.value;//Reviewer id list
  console.log(addForm.value);
  let data = qs.stringify(addForm.value);
  axios.post(BASE_URL+'/v1/application/save',data).then((response)=>{
    if(response.data.code==2000){
      ElMessage.success('Succeed!');
      addApplicationDialogVisible.value = false;
      fileList.value = [];//Clear image upload component
      times.value = [];//Clear start and end times
      addForm.value = {};//Clear other items
      //After adding a new application, all application data also need to be reloaded.
      loadApplication();
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
//How to handle closing pop-ups
const handleClose = ()=>{
  if(confirm('Close the pop-up window?')){
    addApplicationDialogVisible.value = false;
    fileList.value = [];
    times.value = [];
    addForm.value = {};
  }
}

/**Image upload related code begins**/
const fileList = ref([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
//Define a variable to store the address of the backend interface to which the image is uploaded.
const actionUrl = ref(BASE_URL + '/v1/file/upload');
//Remove image
const handleRemove = (uploadFile, uploadFiles) => {
  console.log(uploadFile, uploadFiles);
  //1.The path to the image to be deleted is obtained; `uploadFile` is the image file object we want to delete.
  let imgUrl = uploadFile.response.data;
  //2.Send a request to the backend to delete the image.
  axios.post(BASE_URL+'/v1/file/remove?imgUrl='+imgUrl)
      .then((response)=>{
        if (response.data.code==2000){
          ElMessage.success('Delete succeed!');
        }
      })
}
//Preview after uploading the image
const handlePictureCardPreview = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url
  dialogVisible.value = true;
  console.log(uploadFile);
  //console.log(fileList.value[0].response.data);
}
/**The code related to image upload ends here**/

//Prepare an array object to store the retrieved application form data.
const tableData = ref([]);
//The preparation object is used to store the search criteria.
const searchApplication = ref({
  departureAddr:'',
  destinationAddr:'',
  status:''
});
//Define a method for loading application forms.
const loadApplication = ()=>{
  let data = qs.stringify(searchApplication.value);
  axios.get(BASE_URL+'/v1/application/select?'+data).then((response)=>{
    if(response.data.code==2000){
      tableData.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
//Define a method to reset search criteria
const resetSearch = ()=>{
  searchApplication.value = {};
  loadApplication();
}
//The application form loads as soon as you enter the page.
onMounted(()=>{
  loadApplication();
})

//Define a function to convert the display format of the application form status.
const appStatusFormatter = (row,column,cellValue,index)=>{
  if(cellValue==10){
    cellValue = 'Initiated';
  }else if(cellValue==20){
    cellValue = 'Cancel';
  }else if(cellValue==30){
    cellValue = 'Under review';
  }else if(cellValue==40){
    cellValue = 'Reject';
  }else if(cellValue==50){
    cellValue = 'Approved';
  }else if(cellValue==60){
    cellValue = 'Vehicle allocation';
  }else if(cellValue==70){
    cellValue = 'End';
  }
  return cellValue;
}

//Use an array to maintain the options in the search card state selector.
const auditOptions = ref([
  {label:'Initiated',value:'10'},
  {label:'Cancel',value:'20'},
  {label:'Under review',value:'30'},
  {label:'Reject',value:'40'},
  {label:'Approved',value:'50'},
  {label:'Vehicle allocation',value:'60'},
  {label:'End',value:'70'}
])

//Cancel vehicle use application
const cancel = (id)=>{
  axios.post(BASE_URL+'/v1/application/cancel/'+id).then((response)=>{
    if (response.data.code==2000){
      ElMessage.success('Cancel!');
      //After updating the current application status, it is also necessary to reload the application data
      loadApplication();
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
</script>
<style scoped>
</style>

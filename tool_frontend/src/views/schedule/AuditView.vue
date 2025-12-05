<!--Application audit page-->
<template>
  <div style="height: 6vh;background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">Audit list</span>
  </div>
  <!-- Audit search card -->
  <el-card style="margin:20px;height: 70px;">
    <el-form :inline="true">
      <el-form-item label="Vehicle user">
        <el-input placeholder="Please enter the vehicle user's name." style="width:220px;"
                  @keydown.enter.prevent="loadAudit" v-model="search.username"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch">Reset</el-button>
        <el-button type="primary" @click="loadAudit">Research</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <!-- Audit body -->
  <el-card style="margin:20px;">
    <!--  Audit status item  -->
    <el-radio-group style="margin-bottom: 15px;"
                    v-model="type" @change="loadAudit">
      <el-radio-button value="10" size="large">Wait for review by me</el-radio-button>
      <el-radio-button value="20" size="large">Wait for review by others</el-radio-button>
      <el-radio-button value="30" size="large">Reviewed</el-radio-button>
      <el-radio-button value="40" size="large">Reject</el-radio-button>
    </el-radio-group>
    <!-- Audit list -->
    <el-table :data="auditArr">
      <el-table-column label="number" prop="id" align="center" width="55" type="index"></el-table-column>
      <el-table-column label="Vehicle user" prop="username" align="center" width="110"></el-table-column>
      <el-table-column label="Start time" prop="startTime"  align="center"></el-table-column>
      <el-table-column label="End time" prop="endTime"  align="center"></el-table-column>
      <el-table-column label="Reason for vehicle use" prop="reason"  align="center"></el-table-column>
      <el-table-column label="Reviewer" prop="auditUsernameList"  align="center"></el-table-column>
      <el-table-column label="Departure" prop="departureAddr"  align="center"></el-table-column>
      <el-table-column label="Destination" prop="destinationAddr"  align="center"></el-table-column>
      <el-table-column label="Operation" width="100" align="center" v-if="type==10||type==40" :key="audit">
        <template #default="scope">
          <!-- Approval 10: Displayed under the "Pending My Review" tab, requires approval from the currently logged-in user. -->
          <el-button type="primary" link v-if="type==10" @click="auditing(scope.row.id)">Approval</el-button>
          <!-- View 40: Under the Rejection tab, you can view the reasons for rejection of rejected applications. -->
          <el-button type="primary" link v-if="type==40" @click="auditing(scope.row.id)">Check</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- Approval/View pop-up window -->
  <el-dialog :title="dialogTitle" v-model="auditDialogVisible">
    <el-descriptions direction="horizontal" :column="2" border>
      <el-descriptions-item label="Car user">{{ auditDialogData.username }}</el-descriptions-item>
      <el-descriptions-item label="Reason for vehicle use">{{ auditDialogData.reason }}</el-descriptions-item>
      <el-descriptions-item label="Use start time">{{ auditDialogData.startTime }}</el-descriptions-item>
      <el-descriptions-item label="Use end time">{{ auditDialogData.endTime }}</el-descriptions-item>
      <el-descriptions-item label="Vehicle departure point">{{ auditDialogData.departureAddr }}</el-descriptions-item>
      <el-descriptions-item label="Vehicle destination">{{ auditDialogData.destinationAddr }}</el-descriptions-item>
      <el-descriptions-item label="Driver's license image">
        <img :src="BASE_URL+auditDialogData.imgUrl" style="width:150px;">
      </el-descriptions-item>
      <el-descriptions-item label="Remark">{{ auditDialogData.remark }}</el-descriptions-item>
      <!--  The reason for rejection will only be displayed after clicking "Reject"  -->
      <el-descriptions-item label="Rejection reason"
                            v-if="auditDialogData.auditStatus==40">{{ auditDialogData.rejectReason }}</el-descriptions-item>
    </el-descriptions>
    <!-- The three action buttons below will only be displayed when I review them   -->
    <template #footer v-if="auditDialogData.auditStatus==10">
      <el-button @click="auditDialogVisible=false">Cancel</el-button>
      <el-button type="primary" plain @click="rejectInnerDialogVisible=true">Reject</el-button>
      <el-button type="primary" @click="auditPass">Pass</el-button>
    </template>
  </el-dialog>

  <!-- Rejection Reason Pop-up -->
  <el-dialog title="Rejected. (See details)" v-model="rejectInnerDialogVisible" style="margin-top: 37vh;"
             :before-close="rejectConfirm">
    <el-descriptions direction="horizontal" border>
      <el-descriptions-item label="Rejection reason">
        <el-input placeholder="Enter rejection reason" v-model="rejectReason"></el-input>
      </el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <el-button type="primary" plain @click="rejectConfirm">Cancel</el-button>
      <el-button type="primary" @click="auditReject">Enter</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import {onMounted, ref} from "vue";
import qs from "qs";
import axios from "axios";
import {ElMessage} from "element-plus";

// Control Approval Pop-up Title
const dialogTitle = ref("Details pending approval");
//Control whether the approval pop-up window is displayed
const auditDialogVisible = ref(false);
//Control whether the pop-up window for the reason for rejection is displayed
const rejectInnerDialogVisible = ref(false);

// Define an array to store the approval form data
const auditArr = ref([]);
// Define an object to store query conditions
const search = ref({
  username:'', // Search for the user's name in the card
  auditUserId:'', // Preparation: Next, we need to check the approval data of the currently logged-in user
  auditStatus:'' // Preparation: Next, we need to check the approval data for the currently logged-in user's approval status
});
// Get currently logged-in user data
const user = ref(getUser());
// Set variables to be two-way bound to the approval status radio button above
const type = ref(10);

// Define a method for loading approval form data
const loadAudit = ()=>{
  search.value.auditUserId = user.value.id;
  search.value.auditStatus = type.value;
  let data = qs.stringify(search.value);
  axios.get(BASE_URL+'/v1/audit/select?'+data).then((response)=>{
    if(response.data.code==2000){
      auditArr.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
// Define a method to reset search criteria
const resetSearch = ()=>{
  // Simply clear the names of vehicle users that are visible to other users
  search.value.username = '';
  loadAudit();
}

onMounted(()=>{
  loadAudit();
})

// Define an object to store the approval pop-up data
const auditDialogData = ref({
  username:'', //Car user
  reason:'', //Reason for vehicle use
  startTime:'', //Start of use time
  endTime:'', //End of use time
  departureAddr:'', //Vehicle departure point
  destinationAddr:'', //Vehicle Destination
  imgUrl:'', //Driver license image
  remark:'' //Remark
})

//Click the "Approve" button, and based on the passed approval ID, request the detailed information of the current approval form
const auditing = (id)=>{
  //Display Approval + Rejection Pop-up
  auditDialogVisible.value = true;
  axios.get(BASE_URL+'/v1/audit/select?id='+id).then((response)=>{
    if(response.data.code==2000){
      auditDialogData.value = response.data.data[0];
      //Set the pop-up title based on the status of the requested approval form
      if(auditDialogData.value.auditStatus==10){
        dialogTitle.value = 'Details pending approval';
      }else{
        dialogTitle.value = 'Rejection Details';
      }
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

//Approval form passed
const auditPass = ()=>{
  //Change the current approval status to "Approved"
  auditDialogData.value.auditStatus = 30;
  let data = qs.stringify(auditDialogData.value);
  axios.post(BASE_URL+'/v1/audit/update',data).then((response)=>{
    if(response.data.code==2000){
      ElMessage.success('Approval has been granted!');
      auditDialogVisible.value = false;//Close the approval pop-up window
      auditDialogData.value = {};//Clear the approval data just now
      loadAudit();//Reload approval data
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
//Reason for rejection when saving variable definition
const rejectReason = ref('');
//Define a function to handle approval rejection operations.
const auditReject = ()=>{
  if(!rejectReason.value || rejectReason.value.trim()==''){
    ElMessage.error('The reason for rejection cannot be empty!');
    return;
  }
  //After confirming the rejection, close both pop-up windows
  rejectInnerDialogVisible.value = false;
  auditDialogVisible.value = false;
  //Change the current approval status to "Rejected"
  auditDialogData.value.auditStatus = 40;
  //Set rejection reason
  auditDialogData.value.rejectReason = rejectReason.value;
  let data = qs.stringify(auditDialogData.value);
  axios.post(BASE_URL+'/v1/audit/update',data).then((response)=>{
    if(response.data.code==2000){
      ElMessage.success('Rejection complete!');
      auditDialogData.value = {};//Clear the approval data just now
      rejectReason.value = '';//Clear the reason for rejection
      loadAudit();//Reload approval data
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

//Close the pop-up window for handling rejection reasons
const rejectConfirm = ()=>{
  if(confirm('Are you sure you want to close this window??')){
    //After the user confirms the closure, the inner rejection reason pop-up window will close, and the entered rejection reason will be cleared
    rejectInnerDialogVisible.value = false;
    rejectReason.value = '';
  }
}
</script>
<style>
</style>

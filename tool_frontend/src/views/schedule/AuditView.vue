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

//控制审批弹窗标题
const dialogTitle = ref("待审批详情");
//控制审批弹窗是否显示
const auditDialogVisible = ref(false);
//控制驳回原因弹窗是否显示
const rejectInnerDialogVisible = ref(false);

//定义数组用来保存审批表格数据
const auditArr = ref([]);
//定义对象用来保存查询条件
const search = ref({
  username:'',//搜索卡片中的用车人姓名
  auditUserId:'',//预先准备 接下来要查当前登录用户的审批数据
  auditStatus:''//预先准备 接下来要查当前登录用户对应审批状态下的审批数据
});
//获取当前登录的用户数据
const user = ref(getUser());
//设置变量与上方审批状态单选组进行双向绑定
const type = ref(10);

//定义加载审批单数据方法
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
//定义重置搜索条件方法
const resetSearch = ()=>{
  //清空用户看得见的用车人姓名即可
  search.value.username = '';
  loadAudit();
}

onMounted(()=>{
  loadAudit();
})

//定义对象用来保存审批弹窗数据
const auditDialogData = ref({
  username:'',//用车人
  reason:'',//用车事由
  startTime:'',//使用开始时间
  endTime:'',//使用结束时间
  departureAddr:'',//车辆出发地
  destinationAddr:'',//车辆目的地
  imgUrl:'',//驾照图片
  remark:''//备注
})

//点击审批按钮,根据传过来的审批id,请求当前审批单的详细信息
const auditing = (id)=>{
  //显示审批+驳回弹窗
  auditDialogVisible.value = true;
  axios.get(BASE_URL+'/v1/audit/select?id='+id).then((response)=>{
    if(response.data.code==2000){
      auditDialogData.value = response.data.data[0];
      //根据请求回来的审批单状态设置弹窗标题
      if(auditDialogData.value.auditStatus==10){
        dialogTitle.value = '待审批详情';
      }else{
        dialogTitle.value = '驳回详情';
      }
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

//审批单通过
const auditPass = ()=>{
  //将当前审批单状态改为"已通过"
  auditDialogData.value.auditStatus = 30;
  let data = qs.stringify(auditDialogData.value);
  axios.post(BASE_URL+'/v1/audit/update',data).then((response)=>{
    if(response.data.code==2000){
      ElMessage.success('审批已通过!');
      auditDialogVisible.value = false;//关闭审批弹窗
      auditDialogData.value = {};//清空刚刚的审批数据
      loadAudit();//重新加载审批数据
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
//定义变量保存驳回原因
const rejectReason = ref('');
//定义函数处理审批驳回操作
const auditReject = ()=>{
  if(!rejectReason.value || rejectReason.value.trim()==''){
    ElMessage.error('驳回原因不能为空!');
    return;
  }
  //确认驳回后,关闭两层弹窗
  rejectInnerDialogVisible.value = false;
  auditDialogVisible.value = false;
  //将当前审批单状态改为"驳回"
  auditDialogData.value.auditStatus = 40;
  //设置驳回原因
  auditDialogData.value.rejectReason = rejectReason.value;
  let data = qs.stringify(auditDialogData.value);
  axios.post(BASE_URL+'/v1/audit/update',data).then((response)=>{
    if(response.data.code==2000){
      ElMessage.success('驳回完成!');
      auditDialogData.value = {};//清空刚刚的审批数据
      rejectReason.value = '';//清空驳回原因
      loadAudit();//重新加载审批数据
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

//处理驳回原因弹窗关闭
const rejectConfirm = ()=>{
  if(confirm('确认关闭本窗口吗?')){
    //用户确认关闭后,关闭内层驳回原因弹窗,并且清空已输入的驳回原因
    rejectInnerDialogVisible.value = false;
    rejectReason.value = '';
  }
}
</script>
<style>
</style>

<!--用车申请页-->
<template>
  <div style="height: 6vh;background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">申请单列表</span>
    <el-button type="primary" style="float:right;margin-top:13px;"
               @click="addApplicationDialogVisible=true"
               :disabled="user.parentId==null">申请用车</el-button>
    <!--  v-if="user.parentId!=null 如果是总裁(没有上级的职级)无需申请用车 可以禁用或隐藏按钮 -->
  </div>
  <!-- 申请用车弹窗 -->
  <el-dialog title="创建申请单" v-model="addApplicationDialogVisible"
             style="width:1000px;padding:40px;" :before-close="handleClose">
    <el-form label-width="80px" label-position="top">
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="用车人">
            <el-input readonly :value="user.username"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="使用时间">
            <el-date-picker
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD HH:mm:ss"
                format="YYYY-MM-DD HH:mm:ss"
                v-model="times"
            ></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="车辆出发地">
            <el-input placeholder="请输入" v-model="addForm.departureAddr"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车辆目的地">
            <el-input placeholder="请输入" v-model="addForm.destinationAddr"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="用车事由">
            <el-input placeholder="请输入" type="textarea" resize="none" :rows="3" v-model="addForm.reason"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="备注">
            <el-input placeholder="请输入" type="textarea" resize="none" :rows="3" v-model="addForm.remark"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="驾照">
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
          <el-form-item label="审批人">
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
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="addApplication">确定</el-button>
    </template>
  </el-dialog>

  <!-- 用车申请列表 搜索卡片 -->
  <el-card style="margin: 20px;">
    <el-form style="padding-top:10px;">
      <el-row :gutter="30">
        <el-col :span="5">
          <el-form-item label="出发地">
            <el-input placeholder="请输入出发地" v-model="searchApplication.departureAddr"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="目的地">
            <el-input placeholder="请输入目的地" v-model="searchApplication.destinationAddr"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item label="申请单状态">
            <el-select placeholder="请选择" v-model="searchApplication.status">
              <!--              <el-option label="已发起" value="10"></el-option>-->
              <!--              <el-option label="撤销" value="20"></el-option>-->
              <!--              <el-option label="审核中" value="30"></el-option>-->
              <!--              <el-option label="驳回" value="40"></el-option>-->
              <!--              <el-option label="已通过" value="50"></el-option>-->
              <!--              <el-option label="分配用车" value="60"></el-option>-->
              <!--              <el-option label="工单结束" value="70"></el-option>-->
              <el-option v-for="item in auditOptions"
                         :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="5">
          <el-form-item>
            <el-button @click="resetSearch">重置</el-button>
            <el-button type="primary" @click="loadApplication">查询</el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </el-card>
  <!-- 用车申请表格 -->
  <el-card style="margin:20px">
    <el-table :data="tableData">
      <el-table-column type="index" width="80" align="center" prop="id" label="编号"></el-table-column>
      <el-table-column align="center" prop="username" label="申请人"></el-table-column>
      <el-table-column align="center" prop="departureAddr" label="出发地"></el-table-column>
      <el-table-column align="center" prop="destinationAddr" label="目的地"></el-table-column>
      <el-table-column align="center" prop="reason" label="用车原因"></el-table-column>
      <el-table-column align="center" prop="auditUsernameList" label="审批人"></el-table-column>
      <el-table-column align="center" prop="startTime" label="使用开始时间"></el-table-column>
      <el-table-column align="center" prop="endTime" label="使用结束时间"></el-table-column>
      <el-table-column align="center" prop="status" label="申请单状态"
                       :formatter="appStatusFormatter"></el-table-column>
      <el-table-column label="操作" width="120" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" link
                     :disabled="scope.row.status!=10" @click="cancel(scope.row.id)">撤销</el-button>
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
//定义变量控制创建申请单弹窗是否出现
const addApplicationDialogVisible = ref(false);

//获取当前登录的用户信息
const user = ref(getUser());
//定义数组用来保存新增申请表单中的审批人(含姓名)
const auditUserOpts = ref([]);
//定义数组用来保存新增申请单时,对应的审批人id,入库使用
const auditUserIdList = ref([]);
//定义加载审批人的方法
const loadAuditUser = ()=>{
  //获取当前登录用户的上级id
  let parentId = user.value.parentId;
  //根据父级id查询对应的领导信息
  axios.get(BASE_URL+'/v1/user/select/audit/'+parentId).then((response)=>{
    if(response.data.code==2000){
      auditUserOpts.value = response.data.data;
      //依次取出auditUserOpts中的每个审批人,获取审批人的id
      //并依次存入auditUserIdList数组中,在新增申请时一起发给后端
      for(let i = 0; i<auditUserOpts.value.length ;i++){
        auditUserIdList.value.push(auditUserOpts.value[i].id);
      }
    }
  })
}

onMounted(()=>{
  //因为当前申请人(登录人)的直属领导数据是固定的,所以只需要在页面加载时请求一次即可,无需重复请求
  loadAuditUser();
})
//用车人是从localstorage中获取的,是当前登录的用户名,不允许输入,就不用双向绑定获取输入的值了
//审批人是从后端查出来的固定值，所以也不用双向绑定获取输入的值了
//定义变量用来保存表单中的"使用时间",注意:这是一个数组，包含开始时间与结束时间
const times = ref([]);
//定义对象用来保存申请单表单数据
const addForm = ref({
  departureAddr: '', //出发地
  destinationAddr: '', //目的地
  reason: '', //用车事由
  remark:'' //备注信息
});
//定义方法发送新增申请单请求
const addApplication = ()=>{
  if(fileList.value.length==0){
    ElMessage.error('请上传图片!');
    return;
  }
  //从upload图片上传组件中获取上传图片的路径 fileList.value[0]表示第一张图片对象
  //.response.data 是我们uploadController返回的 /2024/06/18/UUID.suffix
  addForm.value.imgUrl = fileList.value[0].response.data;//驾照图片路径
  addForm.value.userId = user.value.id;//申请人id
  addForm.value.username = user.value.username;//申请人姓名
  addForm.value.startTime = times.value[0];//开始时间
  addForm.value.endTime = times.value[1];//结束时间
  //注意:审批人id数据也需要一起给到后端,因为事务性新增,审批表的数据也需要一起更新!
  addForm.value.auditUserIdList = auditUserIdList.value;//审批人id集合
  console.log(addForm.value);
  let data = qs.stringify(addForm.value);
  axios.post(BASE_URL+'/v1/application/save',data).then((response)=>{
    if(response.data.code==2000){
      ElMessage.success('申请成功!');
      addApplicationDialogVisible.value = false;
      fileList.value = [];//图片上传组件清空
      times.value = [];//起止时间清空
      addForm.value = {};//其他项清空
      //新增申请单后也需要重新加载所有申请单数据
      loadApplication();
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
//处理关闭弹窗的方法
const handleClose = ()=>{
  if(confirm('确认关闭弹窗吗?')){
    addApplicationDialogVisible.value = false;
    fileList.value = [];//图片上传组件清空
    times.value = [];//起止时间清空
    addForm.value = {};//其他项清空
  }
}


/**图片上传相关代码开始**/
const fileList = ref([]);
const dialogImageUrl = ref('');
const dialogVisible = ref(false);
//定义变量用来保存图片上传给哪个后端接口地址
const actionUrl = ref(BASE_URL + '/v1/file/upload');
//移除图片
const handleRemove = (uploadFile, uploadFiles) => {
  console.log(uploadFile, uploadFiles);
  //1.得到要删除的图片路径 uploadFile就是我们要删除的那个图片文件对象
  let imgUrl = uploadFile.response.data;
  //2.给后端发请求,删除图片
  axios.post(BASE_URL+'/v1/file/remove?imgUrl='+imgUrl)
      .then((response)=>{
        if (response.data.code==2000){
          ElMessage.success('删除成功!');
        }
      })
}
//图片上传后预览
const handlePictureCardPreview = (uploadFile) => {
  dialogImageUrl.value = uploadFile.url
  dialogVisible.value = true;
  console.log(uploadFile);
  //console.log(fileList.value[0].response.data);
}
/**图片上传相关代码结束**/

//准备数组对象用来保存查出来的申请单数据
const tableData = ref([]);
//准备对象用来保存搜索条件
const searchApplication = ref({
  departureAddr:'',
  destinationAddr:'',
  status:''
});
//定义加载申请单的方法
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
//定义重置搜索条件的方法
const resetSearch = ()=>{
  searchApplication.value = {};
  loadApplication();
}
//一进入页面就加载申请单
onMounted(()=>{
  loadApplication();
})

//定义申请单状态显示格式转换函数
const appStatusFormatter = (row,column,cellValue,index)=>{
  if(cellValue==10){
    cellValue = '已发起';
  }else if(cellValue==20){
    cellValue = '撤销';
  }else if(cellValue==30){
    cellValue = '审核中';
  }else if(cellValue==40){
    cellValue = '驳回';
  }else if(cellValue==50){
    cellValue = '已通过';
  }else if(cellValue==60){
    cellValue = '分配用车';
  }else if(cellValue==70){
    cellValue = '工单结束';
  }
  return cellValue;
}

//使用数组维护搜索卡片状态选择器中的选项
const auditOptions = ref([
  {label:'已发起',value:'10'},
  {label:'撤销',value:'20'},
  {label:'审核中',value:'30'},
  {label:'驳回',value:'40'},
  {label:'已通过',value:'50'},
  {label:'分配用车',value:'60'},
  {label:'工单结束',value:'70'}
])

//撤销用车申请
const cancel = (id)=>{
  axios.post(BASE_URL+'/v1/application/cancel/'+id).then((response)=>{
    if (response.data.code==2000){
      ElMessage.success('撤销成功!');
      //更新完当前申请单状态后,也需要重新加载申请单数据
      loadApplication();
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
</script>

<style scoped>
</style>
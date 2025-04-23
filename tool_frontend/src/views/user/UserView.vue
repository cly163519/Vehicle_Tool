<!--用户管理页-->
<template>
  <div style="height: 6vh;background-color:#fffbfb;padding: 10px 20px;">
    <span style="font-size:20px;line-height: 60px;float: left;">用户管理</span>
    <el-button @click="dialogVisible=true" type="primary"
               style="float: right;margin-top:10px;">新建用户
    </el-button>
  </div>
  <!-- 新建/编辑用户弹窗 -->
  <!-- :before-close="handleClose"
  在关闭之前调用handleClose方法,避免点击窗口外部也能关弹窗  -->
  <el-dialog :title="dialogTitle" v-model="dialogVisible"
             :before-close="handleClose"
             style="width: 1000px;padding:40px;">
    <el-form label-position="top" label-width="80px">
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="用户名">
            <el-input placeholder="请输入用户名" v-model="saveUserForm.username"
                      :disabled="saveUserForm.id!=null"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="手机号">
            <el-input placeholder="请输入手机号" v-model="saveUserForm.phone"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="邮箱">
            <el-input placeholder="请输入邮箱" v-model="saveUserForm.email"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="年龄">
            <el-input placeholder="请输入年龄" v-model="saveUserForm.age"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="6">
          <el-form-item label="职级">
            <el-select placeholder="请选择" v-model="saveUserForm.level" @change="loadLeader">
              <!--              <el-option label="职员" value="10"></el-option>-->
              <!--              <el-option label="经理" value="20"></el-option>-->
              <!--              <el-option label="总监" value="30"></el-option>-->
              <!--              <el-option label="总裁" value="40"></el-option>-->
              <el-option v-for="item in levelOptions"
                         :label="item.label" :value="item.value"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="直属领导">
            <el-select placeholder="请选择" v-model="saveUserForm.parentId">
              <!--              <el-option label="shaoyun" value="1"></el-option>-->
              <!--              <el-option label="mike" value="2"></el-option>-->
              <el-option v-for="item in leaderOptions"
                         :label="item.username" :value="item.id"></el-option><!-- 这里为什么不用 :label="item.label" :value="item.value"-->
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="4">
          <el-form-item label="性别">
            <el-radio-group v-model="saveUserForm.gender">
              <el-radio label="男" border value="1" style="margin: 0;"></el-radio>
              <el-radio label="女" border value="0"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="用户状态">
            <el-radio-group v-model="saveUserForm.status">
              <el-radio label="启用" border value="1" style="margin: 0;"></el-radio>
              <el-radio label="禁用" border value="0"></el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="saveUser">保存</el-button>
    </template>
  </el-dialog>

  <!-- 用户查询卡片 -->
  <el-card style="margin: 20px;height: 70px;">
    <el-form :inline="true" style="float: left;">
      <el-form-item label="用户搜索" >
        <el-input placeholder="输入用户名" style="width: 220px;"
                  v-model="searchUserForm.username" @keydown.enter="loadUser"></el-input>
      </el-form-item>
      <el-form-item label="用户状态">
        <el-select placeholder="请选择用户状态" style="width: 290px;"
                   v-model="searchUserForm.status" @change="loadUser">
          <el-option label="启用" value="1"></el-option>
          <el-option label="禁用" value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="primary" @click="loadUser">查询</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <!-- 用户信息表格 -->
  <el-card style="margin: 20px;">
    <el-table :data="userArr">
      <el-table-column type="index" label="编号" width="80" align="center"></el-table-column>
      <el-table-column prop="username" label="用户名" align="center"></el-table-column>
      <el-table-column prop="phone" label="手机号" align="center"></el-table-column>
      <el-table-column prop="createTime" label="加入时间" align="center"></el-table-column>
      <el-table-column prop="status" label="用户状态" align="center">
        <template #default="scope">
          <el-switch @change="changeStatus(scope.row.id,scope.row.status)" active-value="1" inactive-value="0" v-model="scope.row.status" :disabled="scope.row.level==40"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button type="primary" size="small" link
                     :disabled="scope.row.level==40"
                     @click="resetPassword(scope.row.id)">密码重置</el-button>
          <el-button type="primary" size="small" link
                     :disabled="scope.row.level==40"
                     @click="editUser(scope.row.id)">编辑</el-button>
          <el-button type="primary" size="small" link
                     :disabled="scope.row.level==40" @click="deleteUser(scope.row.id)">删除</el-button>
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

//定义变量用来控制新增用户弹窗是否出现
const dialogVisible = ref(false);
//定义变量用来保存弹窗标题
const dialogTitle = ref('新建用户');

//定义数组保存员工职级选项
const levelOptions = ref([
  {label:'职员',value:'10'},
  {label:'经理',value:'20'},
  {label:'总监',value:'30'},
  {label:'总裁',value:'40'}
]);
//定义数组保存直属领导数据
const leaderOptions = ref([
  {username:'shaoyun',id:'1'},
  {username:'mike',id:'2'}
]);
//定义方法获取直属领导数据
const loadLeader = ()=>{
  //优化:如果已经勾选了直属领导,想再重新选择职级的话,需要将之前的直属领导数组与已选择的直属领导数据清空
  leaderOptions.value = [];
  saveUserForm.value.parentId = '';
  //拿到当前用户选择的职级,直属领导的职级为当前用户的职级+10 注意转为number类型
  let level = parseInt(saveUserForm.value.level)+10;
  //给后端发请求查数据
  axios.get(BASE_URL+'/v1/user/select?level='+level).then((response)=>{
    if(response.data.code == 2000){
      leaderOptions.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}


//定义对象收集新增/修改员工弹窗的表单信息
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

//定义保存用户的方法
const saveUser = ()=>{
  //如果向后端发请求的参数是一个对象,前端可以使用qs将参数对象转为查询字符串的格式发送
  //格式:username=tom&phone=18811112222&email=tom@qq.com&age=12&level=20&parentId=1&gender=1&status=1
  //这样在前端直接处理好,后端不需要考虑前端传过来的是否为对象,也不用加@RequestBody注解了
  let data = qs.stringify(saveUserForm.value);
  console.log(data);
  axios.post(BASE_URL+'/v1/user/save',data)
      .then((response)=>{
        if(response.data.code == 2000){
          ElMessage.success('保存成功!');
          dialogVisible.value = false;//关闭弹窗
          saveUserForm.value = {};//清空已入库的表单数据，否则再次打开弹窗还是之前的值
          //新增/更新用户后,都需要重新加载用户数据
          loadUser();
        }else{
          ElMessage.error(response.data.msg);
        }
      })
}

//处理弹窗关闭的方法
const handleClose = ()=>{
  if(confirm('确定关闭本窗口吗?')){
    //用户确认关闭后,关闭弹窗
    dialogVisible.value = false;
    //清空已输入的表单数据
    saveUserForm.value = {};
  }
}
//定义数组用来保存用户列表数据
const userArr = ref([
  {username:'ss',phone:'18811112222',createTime:'2022-01-01',status:'1'},
  {username:'yy',phone:'18811113333',createTime:'2022-03-03',status:'0'}
]);
//定义对象用来保存查询条件
const searchUserForm = ref({username:'',status:''});
//定义加载用户数据的方法
const loadUser = ()=>{
  let data = qs.stringify(searchUserForm.value);
  console.log(data);
  //如果有查询条件,就根据条件查,没有查询条件,默认查全部员工信息
  axios.get(BASE_URL+'/v1/user/select?'+data).then((response)=>{
    //如果后端返回的状态码是2000,说明成功了
    if(response.data.code==2000){
      //把成功请求回来的数据装到与表格绑定的数组中
      userArr.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
//页面加载完毕立即调用的方法
onMounted(()=>{
  //加载员工数据
  loadUser();
})

//重置搜索信息
const resetSearch = ()=>{
  //清空之前输入的搜索内容
  searchUserForm.value = {};
  //重新加载所有的用户数据
  loadUser();
}

//重置密码 restful风格
const resetPassword = (userId)=>{
  axios.post(BASE_URL+'/v1/user/update/password/'+userId).then((response)=>{
    if(response.data.code == 2000){
      ElMessage.success('密码重置成功!');
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

//编辑员工
const editUser = (id)=>{
  //打开弹窗并修改标题
  dialogVisible.value = true;
  dialogTitle.value = '编辑员工';
  //给后端发请求:根据员工id查指定员工的信息
  axios.get(BASE_URL+'/v1/user/select?id='+id).then((response)=>{
    if(response.data.code == 2000){
      //注意:后端返回的是List<UserVO> list! 所以这里是data[0]
      saveUserForm.value = response.data.data[0];
      //获取可选直属领导数据
      let level = parseInt(saveUserForm.value.level)+10;
      //给后端发请求查数据
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

//修改员工状态
const changeStatus = (userId,status)=>{
  axios.post(BASE_URL+'/v1/user/update/status/'+userId+'/'+status)
      .then((response)=>{
        if(response.data.code==2000){
          ElMessage.success('修改成功!');
        }else{
          ElMessage.error(response.data.msg);
          //如果数据库没修改成功的话，需要将页面当前用户的状态s
          //刷新为修改前的状态，也就是重新加载下用户列表即可
          loadUser();
        }
      })
}

//删除员工
const deleteUser = (userId)=>{
  if(confirm('您确认要删除该员工吗?')){
    axios.post(BASE_URL+'/v1/user/delete/'+userId).then((response)=>{
      if(response.data.code==2000){
        ElMessage.success('删除成功!');
        //删除成功后,重新加载用户列表
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
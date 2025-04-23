<!--字典管理页-->
<template>
  <div style="background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">字典管理</span>
  </div>
  <!-- 字典数据搜索卡片 -->
  <el-card style="margin:20px;height: 70px;">
    <el-form :inline="true">
      <el-form-item label="字典名称">
        <el-input placeholder="请输入" style="width:220px;" v-model="searchForm.name"
                  @keydown.enter="selectDict()"></el-input>
      </el-form-item>
      <el-form-item label="字典编码">
        <el-input placeholder="请输入" style="width:220px;" v-model="searchForm.code"
                  @keydown.enter="selectDict()"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch()">重置</el-button>
        <el-button type="primary" @click="selectDict()">搜索</el-button>
      </el-form-item>
    </el-form>
  </el-card>
  <el-card style="margin:20px;">
    <div style="margin:10px 0px 40px 10px;">
      字典列表
      <el-button type="primary" style="float: right;margin-top:-3px;" @click="addDict">新增字典</el-button>
    </div>
    <el-table :data="dictArr">
      <el-table-column type="index" label="编号" width="80" align="center"/>
      <el-table-column prop="name" label="字典名称" align="center"/>
      <el-table-column prop="code" label="字典编码" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="showDictOption(scope.row.id)">{{ scope.row.code }}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column prop="remark" label="备注信息" align="center"/>
      <el-table-column prop="createTime" label="创建时间" align="center"/>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="editDict(scope.row.id)">编辑</el-button>
          <el-button link type="primary" size="small" @click="deleteDict(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

  <!-- 保存字典弹窗 -->
  <el-dialog :title="dialogTitle" v-model="dialogVisible" style="width:1000px;padding:40px;"
             :before-close="handleClose">
    <el-form label-width="80px" label-position="top">
      <el-form-item label="字典名称">
        <el-input placeholder="请输入" v-model="saveDictForm.name"></el-input>
      </el-form-item>
      <el-form-item label="字典编码">
        <el-input placeholder="请输入" v-model="saveDictForm.code"></el-input>
      </el-form-item>
      <el-form-item label="备注信息">
        <el-input placeholder="请输入" v-model="saveDictForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose()">取消</el-button>
      <el-button type="primary" @click="saveDict()">保存</el-button>
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
const dialogTitle = ref('新增字典');

const dictArr = ref([
  {name: '车身颜色', code: 'vehicle_color', remark: '管理汽车颜色', createTime: '2024-04-28'},
  {name: '电池类型', code: 'battery_type', remark: '管理电池类型', createTime: '2024-04-28'},
  {name: '车辆类型', code: 'vehicle_type', remark: '管理车辆类型', createTime: '2024-04-28'},
  {name: '状态', code: 'general_status', remark: '管理启用禁用状态', createTime: '2024-04-28'},
  {name: '申请单状态', code: 'application_status', remark: '管理申请单状态', createTime: '2024-04-28'},
  {name: '审核单状态', code: 'audit_status', remark: '管理审核单状态', createTime: '2024-04-28'}
]);

onMounted(() => {
  selectDict();
})
//查询所有字典信息
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
//重置用户搜索信息
const resetSearch = () => {
  searchForm.value = {};
  selectDict();
}
//新增字典
const addDict = ()=>{
  dialogVisible.value=true;
  dialogTitle.value = '新增字典';
}
//保存字典信息
const saveDictForm = ref({name: '', code: '', remark: ''});
const saveDict = () => {
  dialogVisible.value = true;
  let data = qs.stringify(saveDictForm.value);
  axios.post(BASE_URL+'/v1/dict/save', data).then((response) => {
    if (response.data.code == 2000) {
      ElMessage.success('保存成功!');
      dialogVisible.value = false;
      saveDictForm.value = {};
      selectDict();
    } else {
      ElMessage.error('保存失败!');
    }
  })
}
//编辑字典信息
const editDict = (id) => {
  dialogTitle.value = '编辑字典';
  dialogVisible.value = true;
  axios.get(BASE_URL+'/v1/dict/select?id=' + id).then((response) => {
    if (response.data.code == 2000) {
      //注意！！！返回值是个list,记得加[0]
      saveDictForm.value = response.data.data[0];
    } else {
      ElMessage.error(response.data.msg);
    }
  })
}

//实现取消按钮的功能
const handleClose = () => {
  if (confirm('是否关闭本窗口?')) {
    //用户确认关闭后,将审批弹窗与确认驳回弹窗一起关掉
    dialogVisible.value = false;
    //取消时把填的数据都清空，表单回复到初始状态
    saveDictForm.value={};
  }
}

//删除字典
const deleteDict = (id) => {
  if (confirm('您确认要删除此字典吗?')) {
    axios.post(BASE_URL+'/v1/dict/delete/' + id).then((response) => {
      if (response.data.code === 2000) {
        ElMessage.success('删除成功!');
        selectDict();
      } else {
        ElMessage.error(response.data.msg);
      }
    })
  }
}

//点击不同的字典编码展示该字典下的具体字典项,携带当前字典的id跳转至字典项页即可
const showDictOption = (id) => {
  router.push('/dictOption?id=' + id);
}
</script>

<style scoped>

</style>




<!--字典项页-->
<template>
  <div style="background-color:#fff;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">字典项管理</span>
    <el-button @click="goBack()" type="primary" style="float:right;margin-top:13px;">返回</el-button>
  </div>
  <!-- 字典项搜索卡片 -->
  <el-card style="margin:20px;height: 70px;">
    <el-form :inline="true">
      <el-form-item label="字典项名称">
        <el-input placeholder="请输入" style="width:220px;" v-model="searchForm.label"
                  @keydown.enter.prevent="selectDictOption"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch()">重置</el-button>
        <el-button type="primary" @click="selectDictOption">搜索</el-button>
      </el-form-item>
    </el-form>
  </el-card>
  <el-card style="margin:20px;">
    <div style="margin:10px 0px 40px 10px;">
      字典项列表
      <el-button type="primary" style="float: right;margin-top:-3px;" @click="dialogVisible=true">新增字典项</el-button>
    </div>
    <el-table :data="dictOptionArr">
      <el-table-column type="index" label="编号" width="80" align="center"/>
      <el-table-column prop="label" label="字典项名称" align="center"/>
      <el-table-column prop="value" label="字典项值" align="center"/>
      <el-table-column prop="sort" label="字典项排序" align="center"/>
      <el-table-column prop="remark" label="备注信息" align="center" width="320"/>
      <el-table-column prop="createTime" label="创建时间" align="center"/>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button type="primary" link @click="editDictOption(scope.row.id)">编辑</el-button>
          <el-button type="primary" link @click="deleteDictOption(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
  <!-- 保存字典项弹窗 -->
  <el-dialog :title="dialogTitle" v-model="dialogVisible" style="width:1000px;padding:40px;"
             :before-close="handleClose">
    <el-form label-width="80px" label-position="top">
      <el-form-item label="字典项名称">
        <el-input placeholder="请输入" v-model="saveDictOptionForm.label"></el-input>
      </el-form-item>
      <el-form-item label="字典项值">
        <el-input placeholder="请输入" v-model="saveDictOptionForm.value"></el-input>
      </el-form-item>
      <el-form-item label="字典项排序">
        <el-input placeholder="请输入" v-model="saveDictOptionForm.sort"></el-input>
      </el-form-item>
      <el-form-item label="备注信息">
        <el-input placeholder="请输入" v-model="saveDictOptionForm.remark"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose()">取消</el-button>
      <el-button type="primary" @click="saveDictOption()">保存</el-button>
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
const dialogTitle = ref('新增字典项');
const dictId = new URLSearchParams(window.location.search).get('id');

const dictOptionArr = ref([
  {label: '轿车', value: 10, sort: 1, remark: '车辆类型', createTime: '2024-04-28'},
  {label: '客车', value: 20, sort: 2, remark: '车辆类型', createTime: '2024-04-28'},
  {label: '货车', value: 30, sort: 3, remark: '车辆类型', createTime: '2024-04-28'},
  {label: '挂车', value: 40, sort: 4, remark: '车辆类型', createTime: '2024-04-28'}
]);

onMounted(() => {
  selectDictOption();
})
//查询所有字典项信息
const searchForm = ref({dictId:dictId,label: ''});
const selectDictOption = () => {
  //这的是空的，没传过来console.log(new URLSearchParams(window.location.search).get('id'));
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
//重置用户搜索信息
const resetSearch = () => {
  console.log('猜猜会不会一进来就调用')
  searchForm.value = {dictId:dictId,label: ''};
  selectDictOption();
}

//保存字典项信息
const saveDictOptionForm = ref({dictId:dictId, label: '', value: '', sort: '', remark: ''});
const saveDictOption = () => {
  dialogVisible.value = true;
  let data = qs.stringify(saveDictOptionForm.value);
  axios.post(BASE_URL+'/v1/dictoption/save', data).then((response) => {
    if (response.data.code == 2000) {
      ElMessage.success('保存成功!');
      dialogVisible.value = false;
      //注意这里的字典id不能清空,否则无法一次性连续添加多个字典项
      saveDictOptionForm.value = {dictId:dictId, label: '', value: '', sort: '', remark: ''};
      selectDictOption();
    } else {
      ElMessage.error('保存失败!');
    }
  })
}
//编辑字典项信息
const editDictOption = (id) => {
  dialogTitle.value = '编辑字典项';
  dialogVisible.value = true;
  axios.get(BASE_URL+'/v1/dictoption/select?id=' + id).then((response) => {
    if (response.data.code == 2000) {
      //注意！！！返回值是个list,记得加[0]
      saveDictOptionForm.value = response.data.data[0];
    } else {
      ElMessage.error(response.data.msg);
    }
  })
}

//实现取消按钮的功能
const handleClose = () => {
  if (confirm('是否关闭本窗口?')) {
    dialogVisible.value = false;
    //此句会清空新增字典项弹窗里的表单数据，否则点击弹窗显示的是刚删除掉的字典项数据
    saveDictOptionForm.value = {dictId:dictId, label: '', value: '', sort: '', remark: ''};
  }
}

//删除字典项
const deleteDictOption = (id) => {
  if (confirm('您确认要删除此字典吗?')) {
    axios.post(BASE_URL+'/v1/dictoption/delete/' + id).then((response) => {
      if (response.data.code === 2000) {
        ElMessage.success('删除成功!');
        selectDictOption();
      } else {
        ElMessage.error(response.data.msg);
      }
    })
  }
}

//为返回按钮实现点击返回至上一页"字典列表页"
const goBack = () => {
  window.history.go(-1)
}
</script>

<style scoped>

</style>




<!--车辆管理页-->
<template>
  <div style="height: 6vh;background-color:#fcf9f9;padding:10px 20px;">
    <span style="line-height:60px;font-size:20px;">车辆管理</span>
    <el-button @click="dialogVisible=true" type="primary" style="float:right;margin-top:13px;">新建车辆</el-button>
  </div>
<!--  新增车辆弹窗-->
  <el-dialog :title="dialogTitle" v-model="dialogVisible"
             :before-close="handleClose" style="width:1000px;padding:40px;">
    <el-form label-width="80px" label-position="top">
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="车辆品牌">
            <el-input placeholder="请输入内容" v-model="saveVehicleForm.brand"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车辆牌号">
            <el-input placeholder="请输入内容" v-model="saveVehicleForm.license"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="车辆型号">
            <el-input placeholder="请输入内容" v-model="saveVehicleForm.model"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车辆识别码">
            <el-input placeholder="请输入内容" v-model="saveVehicleForm.code"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="车辆排量">
            <el-select placeholder="请选择" v-model="saveVehicleForm.displacement">
              <el-option label="1.6" value="1"></el-option>
              <el-option label="2.5" value="2"></el-option>
              <el-option label="4" value="3"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车辆类型">
            <el-select placeholder="请选择" v-model="saveVehicleForm.type">
              <el-option label="轿车"></el-option>
              <el-option label="货车"></el-option>
              <el-option label="客车"></el-option>
              <el-option label="挂车"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="车身颜色">
            <el-select placeholder="请选择" v-model="saveVehicleForm.color">
              <el-option label="黑" value="10"></el-option>
              <el-option label="白" value="20"></el-option>
              <el-option label="蓝" value="30"></el-option>
              <el-option label="灰" value="40"></el-option>
              <el-option label="银" value="50"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="里程数">
            <el-input placeholder="请输入内容" v-model="saveVehicleForm.kilometers"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gtter="30">
        <el-col :span="12">
          <el-form-item label="购买时间">
            <el-date-picker type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
            v-model="saveVehicleForm.buyTime"></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="上牌时间">
            <el-date-picker type="date" format="YYYY-MM-DD" value-format="YYYY-MM-DD"
            v-model="saveVehicleForm.regTime"></el-date-picker>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="30">
        <el-col :span="12">
          <el-form-item label="购买价格">
            <el-input placeholder="请输入内容" v-model="saveVehicleForm.price"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="电池类型">
            <el-select placeholder="请选择" v-model="saveVehicleForm.batteryType">
              <el-option label="铅酸蓄电池" value="10"></el-option>
              <el-option label="镍氢电池" value="20"></el-option>
              <el-option label="钠硫电池" value="30"></el-option>
              <el-option label="二次锂电池" value="40"></el-option>
              <el-option label="空气电池" value="50"></el-option>
              <el-option label="三元锂电池" value="60"></el-option>
              <el-option label="碱性燃料电池" value="70"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="saveVehicle">确定</el-button>
    </template>
  </el-dialog>
  <!--车辆搜索卡片-->
  <el-card style="margin:20px;height:70px;">
    <el-form :inline="true">
      <el-form-item label="车辆品牌">
        <el-input placeholder="请输入内容" style="width:220px;"
                  v-model="searchVehicleForm.brand" @keydown.enter="loadVehicle"></el-input>
      </el-form-item>
      <el-form-item label="车牌号" style="width:290px;">
        <el-input placeholder="请输入内容" style="width:220px;"
                  v-model="searchVehicleForm.license" @keydown.enter="loadVehicle"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetSearch">重置</el-button>
        <el-button type="primary" @click="loadVehicle">查询</el-button>
      </el-form-item>
    </el-form>
  </el-card>

  <!--车辆列表-->
  <el-card style="margin:20px;">
    <el-table :data="vehicleArr">
      <el-table-column type="index" label="编号" width="80" align="center"></el-table-column>
      <el-table-column prop="brand" label="车辆品牌" align="center"></el-table-column>
      <el-table-column prop="license" label="车牌号" align="center"></el-table-column>
      <el-table-column prop="code" label="车辆识别码" align="center"></el-table-column>
      <el-table-column prop="type" label="车辆类型" align="center"
                       :formatter="vehicleTypeFormatter"></el-table-column>
      <el-table-column prop="price" label="购买价格" align="center"></el-table-column>
      <el-table-column prop="buyTime" label="购买时间" align="center"></el-table-column>
      <el-table-column prop="regTime" label="上牌时间" align="center"></el-table-column>
      <el-table-column prop="batteryType" label="电池类型" align="center"
                       :formatter="batteryTypeFormatter"></el-table-column>
      <el-table-column prop="status" label="车辆状态" align="center"
                       :formatter="vehicleStatusFormatter"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button link type="primary" size="small" @click="editVehicle(scope.row.id)">编辑</el-button>
          <el-button link type="primary" size="small" @click="deleteVehicle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>
</template>

<script setup>
import {onMounted, ref} from "vue";
import qs from "qs";
import axios from "axios";
import {ElMessage} from "element-plus";

const dialogVisible = ref(false);
const dialogTitle = ref('新增车辆');

const vehicleArr = ref([]);
const searchVehicleForm = ref({brand:'',license:''});

const loadVehicle = () =>{
  let data = qs.stringify(searchVehicleForm.value);
  console.log(data);
  axios.get(BASE_URL+'/v1/vehicle/select?'+data).then((response)=>{
    if(response.data.code === 2000){
      vehicleArr.value = response.data.data;
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

onMounted(()=>{
  loadVehicle();
})

const resetSearch = ()=> {
  searchVehicleForm.value = {};
  loadVehicle();
}

const vehicleTypeFormatter = (row,column,cellValue,index)=>{
  if(cellValue==10){
    cellValue='轿车';
  }else if(cellValue==20){
    cellValue='货车';
  }else if(cellValue==30){
    cellValue='客车';
  }else if(cellValue==40){
    cellValue='挂车';
  }
  return cellValue;
}

const vehicleStatusFormatter = (row,column,cellValue,index)=>{
  if(cellValue==1){
    cellValue='空闲';
  }else if(cellValue==2){
    cellValue='占用;'
  }
  return cellValue;
}

const batteryTypeFormatter = (row,column,cellValue,index) => {
  if(cellValue==10){
    cellValue='铅酸蓄电池';
  }else if(cellValue==20){
    cellValue='镍氢电池';
  }else if(cellValue==30){
    cellValue= '钠硫电池';
  }else if (cellValue==40){
    cellValue= '二次锂电池';
  }else if (cellValue==50){
    cellValue= '空气电池';
  }else if (cellValue==60){
    cellValue= '三元锂电池';
  }else if (cellValue==70){
    cellValue= '碱性燃料电池';
  }
  return cellValue;
}

const saveVehicleForm = ref({
  brand:'',
  license:'',
  model:'',
  code:'',
  displacement:'',
  type:'',
  color:'',
  kilometers:'',
  buyTime:'',
  regTime:'',
  price:'',
  batteryType:''
});

const saveVehicle = ()=>{
  let data = qs.stringify(saveVehicleForm.value);
  axios.post(BASE_URL+'/v1/vehicle/save',data).then((response)=>{
    if(response.data.code===2000){
      ElMessage.success('保存成功');
      saveVehicleForm.value = {};
      dialogVisible.value = false;
      loadVehicle();
    }else{
      ElMessage.error('车牌号或车辆识别码不能重复');
    }
  })
}

const handleClose = () =>{
  if(confirm('确认关闭?')){
    dialogVisible.value = false;
    saveVehicleForm.value = {};
  }
}

const editVehicle = (id) =>{
  dialogVisible.value = true;
  dialogTitle.value = '编辑车辆';
//完成编辑功能用get,编辑完成保存不必再重新代码,功能已经包含在上面saveVehicle中.
  axios.get(BASE_URL+'/v1/vehicle/select?id='+id).then((response)=>{
    if(response.data.code === 2000){
      saveVehicleForm.value = response.data.data[0];
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}

const deleteVehicle = (id) =>{
  if(confirm('确认删除?')){
    axios.post(BASE_URL+'/v1/vehicle/delete/'+id).then((response)=>{
      if(response.data.code === 2000){
        ElMessage.success('删除成功');
        loadVehicle();
      }else{
        ElMessage.error(response.data.msg);
      }
    })
  }
}


</script>



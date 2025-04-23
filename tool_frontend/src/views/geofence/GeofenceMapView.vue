<!--围栏地图页-->
<template>
<div style="height:6vh;background-color:#fff;padding:10px 20px;">
  <span style="line-height:60px;font-size:20px;">新增围栏</span>
    <el-button type="primary" style="float:right;margin-top:13px;"
                @click="router.push('/geofence')">返回
    </el-button>
</div>
  <!--第6步:创建地图容器元素-->
  <div id="mapContainer" style="width:100%;height:84vh;"></div>

  <!--9.新增电子围栏弹窗-->
  <el-dialog title="围栏信息" v-model="dialogVisible" :before-close="closeDialog">
    <el-form label-width="80px">
      <el-form-item label="围栏名称">
        <el-input v-model="geofence.name" placeholder="请输入围栏名称"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="closeDialog">取消</el-button>
      <el-button type="primary" @click="saveGeofence">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import router from "@/router";
import {nextTick,onMounted,ref} from "vue";
import {DrawScene,DrawControl,OperateEventType} from 'bmap-draw';
//import {createRef,useEffect} from 'react';
import {ElMessage} from "element-plus";
import qs from "qs";
import axios from "axios";

//8.定义电子围栏对象,用于给后端新增时发送
const geofence = ref({
  name:'',
  position:{}
})
var map;


onMounted(()=>{
  nextTick(()=>{
    let map = new BMapGL.Map("mapContainer");
    let point = new BMapGL.Point(116.4074,39.9024);
    map.centerAndZoom(point,15);

    const scene = new DrawScene(map);
    const drawControl = new DrawControl(scene,{
      enableTips:false,
      anchor: BMAP_ANCHOR_TOP_RIGHT,
      drawingItems:[
          'circle',
          'rectangle'
      ]
    });
    scene.addEventListener(OperateEventType.COMPLETE,(event) => {
      dialogVisible.value = true;
      let overlay = event.target.overlay;
      if(overlay instanceof BMapGL.Circle){
        geofence.value.position.type = 'circle';
        geofence.value.position.radius = overlay.getRadius();
        geofence.value.position.latitude = overlay.getCenter().lat;
        geofence.value.position.longitude = overlay.getCenter().lng;
      }else{
        let recPoints = [];
        for(let i=0;i<4;i++){
          recPoints.push(overlay.points[i].latLng.lng+'-'+overlay.points[i].latLng.lat);
        }
        geofence.value.position.type = 'rectangle';
        geofence.value.position.recPoints = recPoints.join(',');
      }
      console.log(geofence.value);
    })

    map.addControl(drawControl);
  })
})

const dialogVisible = ref(false);
const saveGeofence = ()=>{
  if(!geofence.value.name || geofence.value.name.trim().length == 0){
    ElMessage.error('围栏名称不能为空');
    return;
  }
  geofence.value.position = JSON.stringify(geofence.value.position);
  let data = qs.stringify(geofence.value);
  axios.post(BASE_URL+'/v1/geofence/save',data).then((response)=>{
    if(response.data.code === 2000){
      ElMessage.success('保存成功');
      dialogVisible.value = false;
      router.push('/geofence');
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
const closeDialog = ()=>{
  if(confirm('确认取消新增围栏?')){
    dialogVisible.value = false;
    map.clearOverlays();
  }
}

</script>



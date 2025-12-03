<!-- Fence Map Page -->
<template>
<div style="height:6vh;background-color:#fff;padding:10px 20px;">
  <span style="line-height:60px;font-size:20px;">Add new fence</span>
    <el-button type="primary" style="float:right;margin-top:13px;"
                @click="router.push('/geofence')">Return
    </el-button>
</div>
  <!--  Step 6: Create map container elements -->
  <div id="mapContainer" style="width:100%;height:84vh;"></div>

  <!--9.Add electric fence pop-up window -->
  <el-dialog title="Fence info" v-model="dialogVisible" :before-close="closeDialog">
    <el-form label-width="80px">
      <el-form-item label="fence name">
        <el-input v-model="geofence.name" placeholder="Please enter fence name"></el-input>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="closeDialog">Cancel</el-button>
      <el-button type="primary" @click="saveGeofence">Enter</el-button>
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

//8.Define an electronic fence object to send when adding new data to the backend.
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
    ElMessage.error('The fence name cannot be empty.');
    return;
  }
  geofence.value.position = JSON.stringify(geofence.value.position);
  let data = qs.stringify(geofence.value);
  axios.post(BASE_URL+'/v1/geofence/save',data).then((response)=>{
    if(response.data.code === 2000){
      ElMessage.success('Succeed');
      dialogVisible.value = false;
      router.push('/geofence');
    }else{
      ElMessage.error(response.data.msg);
    }
  })
}
const closeDialog = ()=>{
  if(confirm('Confirm cancellation of adding fence?')){
    dialogVisible.value = false;
    map.clearOverlays();
  }
}

</script>



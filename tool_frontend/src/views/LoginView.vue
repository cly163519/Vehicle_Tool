<!--Login page-->
<template>
  <div style="height:100vh;background-image:linear-gradient(135deg,#1352F2,#3C82F5);overflow:hidden;">
    <el-container style="padding:30px;">
      <el-header>
        <el-row :gutter="10">
          <el-col :span="3">
            <img src="/imgs/login/logo.png" style="height: 50px;">
          </el-col>
          <el-col :span="1">
            <img src="/imgs/login/auto.png" style="height: 50px;">
          </el-col>
          <el-col :span="1">
            <img src="/imgs/login/verticalLine.png" style="height: 50px;">
          </el-col>
          <el-col :span="19">
            <span style="color:#fff;font-size:30px;font-weight:bold;">Vehicle Operation Management System</span>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-row :gutter="40">
          <el-col :span="16">
            <img src="/imgs/login/bg.png" style="width:100%;margin-top:60px;">
          </el-col>
          <el-col :span="8">
            <el-card style="margin-top:80px;">
              <h3 style="text-align:center;">User login</h3>
              <el-form label-position="top">
                <el-form-item label="Username">
                  <el-input placeholder="Please enter username" v-model="user.username"></el-input>
                </el-form-item>
                <el-form-item label="Password">
                  <el-input placeholder="Please enter password" type="password" v-model="user.password"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" style="width:100%;" @click="login">Login</el-button>
                </el-form-item>
              </el-form>
              <div style="color:#666;text-align:center;font-size:14px;padding:10px 0;">
                (Test account:tom Password:123456)
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import {ref} from "vue";
import axios from "axios";
import {ElMessage} from "element-plus";
import router from "@/router";

const user = ref({username:'',password:''});
const login = ()=>{
  //console.log(user.value);
  //Send login request to the backend
  axios.post(BASE_URL+'/v1/user/login',user.value)
      .then((response)=>{
        //Callback function: This code is executed after the backend server responds.
        //response contains the response content; we obtain the server's response data through response.data.
        console.log(response.data);
        if(response.data.code === 2000){
          ElMessage.success('Succeed!');
          //localStorage can only store key-value pairs of string type.
          console.log(response.data);//Returned by backend JsonResult
          console.log(response.data.data);//The data in JsonResult is userVO
          console.log(JSON.stringify(response.data.data));//Convert JS objects to JSON strings
          //After successful login, store the current user information returned by the backend in localStorage.
          localStorage.setItem('user',JSON.stringify(response.data.data));

          //After successful login, you will be redirected to the homepage. The router/index.js configuration will redirect to the /user user list page.
          router.push('/');
        }else{
          ElMessage.error(response.data.msg);
          //Login failed, clear input box
          user.value = {username:'',password:''};
        }
      })
}
</script>


<!--登录页-->
<template>
  <div style="height:100vh;">
    <el-container style="padding:30px;">
      <el-header>
        <el-row :gutter="10">
          <el-col :span="5">
            <img src="/imgs/login/logo.png" style="height: 70px;">
          </el-col>
          <el-col :span="19">
            <span style="color:#180101;font-size:30px;font-weight:bold;">保险代理人智慧展业系统</span>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-row :gutter="40">
          <el-col :span="16">
            <img src="imgs/login/bg.jpeg" style="width:100%;margin-top:20px;">
          </el-col>
          <el-col :span="8">
            <el-card style="margin-top:80px;">
              <h3 style="text-align:center;">用户登录</h3>
              <el-form label-position="top">
              <el-form-item label="用户名">
                <el-input placeholder="请输入用户名" v-model="user.username"></el-input>
              </el-form-item>
              <el-form-item label="密码">
                <el-input placeholder="请输入密码" type="password" v-model="user.password"></el-input>
              </el-form-item>
                <el-form-item>
                  <el-button type="primary" style="width:100%;" @click="login">登录</el-button>
                </el-form-item>
              </el-form>

            </el-card>
          </el-col>
        </el-row>
      </el-main>
    </el-container>

  </div>
</template>

<script setup>
import {ref} from "vue";
import axios from 'axios';

const user = ref({username:'',password:''});
const login = ()=>{
  console.log(user.value);

  axios.post('http://localhost:8080/v1/user/login',user.value)
      .then((response)=>{
        console.log(response.data);
      });
}

</script>



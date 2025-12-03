<!-- Parent page: Controls the header and left menu bar shared by all pages except the login page. -->
<template>
  <!--display: flex;This indicates that flex layout is being used on the parent element.
  Effect: All child elements are horizontally positioned, and the parent element automatically fills the height according to the actual height of the child elements after horizontal positioning. -->
  <div style="display: flex;">
    <!-- The left-side menu bar is collapsible. When collapsed, its width is 64px; when not collapsed, its width is 208px -->
    <div :style="{width:( isCollapse ? '64px' : '208px' )}">
      <!-- Menu bar
      width: 100%; The width fills the entire width of the left menu bar, 64px or 208px.
      height: 100vh; The height fills the entire screen height, fixed at 100vh. -->
      <el-menu
          background-color="#3C82F5"
          text-color="#fff"
          style="width:100%;height:100vh;"
          active-text-color="#ff0"
          :collapse="isCollapse"
          :collapse-transition="false"
          router
          unique-opened
          @select="selectMenu"
      >
        <!-- Top bar of the left-hand menu -->
        <el-row style="padding-top:10px;">
          <el-col :span="2"></el-col>
          <el-col :span="7" style="padding-left:7px;">
            <el-avatar src="/imgs/admin/logo.png" style="width: 35px;height: 35px;"></el-avatar>
          </el-col>
          <el-col :span="isCollapse ? 0 : 13">
            <router-link to="/" style="color:#fff;text-decoration:none;font-weight:bold;position:relative;top:8px;left:2px;">
              Vehicle Tool
            </router-link>
          </el-col>
        </el-row>

        <el-sub-menu index="1">
          <template #title>
            <el-icon style="font-size:21px;">
              <User/>
            </el-icon>
            <span>User management</span>
          </template>
          <el-menu-item index="/user">User list</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="2">
          <template #title>
            <el-icon style="font-size:21px;">
              <Van/>
            </el-icon>
            <span>Vehicle management</span>
          </template>
          <el-menu-item index="/vehicle">Basic info</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="3">
          <template #title>
            <el-icon style="font-size:21px;">
              <Place/>
            </el-icon>
            <span>Dispatch Management</span>
          </template>
          <el-menu-item index="/application">Application list</el-menu-item>
          <el-menu-item index="/audit">Dispatch review</el-menu-item>
          <el-menu-item index="/distribute">Vehicle allocation</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="4">
          <template #title>
            <el-icon style="font-size:21px;">
              <Location/>
            </el-icon>
            <span>Electric fence</span>
          </template>
          <el-menu-item index="/geofence">Fence management</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="5">
          <template #title>
            <el-icon style="font-size:21px;">
              <MessageBox/>
            </el-icon>
            <span>Data dictionary</span>
          </template>
          <el-menu-item index="/dict">Dictionary Management</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>
    <!-- The main content on the right always fills the remaining width -->
    <div :style="{width:(isCollapse ? 'calc(100% - 64px)' : 'calc(100% - 208px)')}">
      <!-- Top right bar -->
      <el-header style="padding-top:22px;height:10vh;background-color:#fff;">
        <!-- Collapse icon and logout  -->
        <el-row :gutter="10">
          <el-col :span="18">
            <el-icon style="font-size:20px;float:left;" @click="changeCollapsed">
              <Expand/>
            </el-icon>
          </el-col>
          <el-col :span="6" style="position: relative;">
            <el-dropdown trigger="click" style="position:fixed;right:20px;">
              <span style="font-size:19px;font-weight: bold;">
                {{ user.username }}
                <el-icon><arrow-down/></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="logout">Log out</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-col>
        </el-row>
        <!-- Breadcrumb navigation -->
        <el-breadcrumb separator="/" style="position:relative;top:20px;">
          <el-breadcrumb-item>Home page</el-breadcrumb-item>
          <el-breadcrumb-item v-for="item in breadCrumb">{{item}}</el-breadcrumb-item>
        </el-breadcrumb>
      </el-header>
      <!-- The main content on the right, excluding the top bar and breadcrumbs, is a variable area; write your content in the corresponding child page!
      height:90vh; 90vh overflow-y: auto; Automatically display scrollbar when vertical overflow occurs-->
      <el-main style="padding:0;height:90vh;overflow-y: auto;">
        <router-view/>
      </el-main>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";

//Get currently logged-in user information
const user = ref(getUser());

//Define a variable to control whether the left-side menu bar is collapsed
const isCollapse = ref(false);
//Define a method to toggle whether the left-side menu bar is collapsed
const changeCollapsed = () => {
  isCollapse.value = !isCollapse.value;
}

// Set variables to maintain the value of the breadcrumb navigation; the default value is the user list interface.
// If it's the first time opening, localStorage.getItem('breadcrumb') won't retrieve a value, so the default value will be ["User Management", "User List"].
// If it's not the first time opening, and there's a value in localStorage, then whatever value is retrieved will be displayed.
const breadCrumb = ref(
    JSON.parse(localStorage.getItem('breadcrumb') || '["User management","User list"]'));
// Prepare an object to maintain the mapping between the corresponding page and the breadcrumb navigation
// Prepare an array to store the values ​​of the breadcrumb navigation
let map = {
  "/user": ['user management','user list'],
  "/vehicle": ['vehicle management','basic info'],
  "/geofence": ['electric fence','fence management'],
  "/application": ['dispatch management','application list'],
  "/audit": ['dispatch management','dispatch review'],
  "/distribute": ['dispatch management','vehicle allocation'],
  "/dict": ['data dict','dict management']
};To resolve the issue of lost breadcrumb values ​​upon page refresh: store them in localStorage.
//Triggered when a menu item is selected; index indicates which menu item was selected
const selectMenu = (index)=>{
  //Set the value of the breadcrumb navigation based on the selected menu item
  breadCrumb.value = map[index];
  //To resolve the issue of lost breadcrumb values ​​upon page refresh: store them in localStorage.
  localStorage.setItem("breadcrumb",JSON.stringify(breadCrumb.value));
}

//Log out
const logout = ()=>{
  if(confirm('Log out?')){
    //Clear user and breadcrumb information stored in localStorage upon exit
    localStorage.removeItem('user');
    localStorage.removeItem('breadcrumb');
    //After logging out, you will be redirected to the login page
    window.location.href = '/login';
    //It is also necessary to clear the values ​​that have already been retrieved
    user.value = '';
  }
}
</script>

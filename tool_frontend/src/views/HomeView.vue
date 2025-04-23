<!--父级页面:控制除了登录页以外所有页面共有的头部与左侧菜单栏-->
<template>
  <!--display: flex;表示给父元素使用弹性布局
  效果:所有子元素横置,且父元素会根据子元素横置后的实际高度自动填充高度 -->
  <div style="display: flex;">
    <!-- 左侧菜单栏 可折叠 折叠时宽度为64px 不折叠时宽度为208px -->
    <div :style="{width:( isCollapse ? '64px' : '208px' )}">
      <!-- 菜单栏
      width:100%;宽度占满左侧菜单栏宽度 64px或 208px
      height:100vh; 高度占满整个屏幕高度，固定就是100vh -->
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
        <!-- 左侧菜单栏的顶部条 -->
        <el-row style="padding-top:10px;">
          <el-col :span="2"></el-col>
          <el-col :span="7" style="padding-left:7px;">
            <el-avatar src="/imgs/admin/logo.png" style="width: 35px;height: 35px;"></el-avatar>
          </el-col>
          <el-col :span="isCollapse ? 0 : 13">
            <router-link to="/"
                         style="color:#fff;text-decoration:none;font-weight:bold;position:relative;top:8px;left:2px;">
              智慧车辆
            </router-link>
          </el-col>
        </el-row>

        <el-sub-menu index="1">
          <template #title>
            <el-icon style="font-size:21px;">
              <User/>
            </el-icon>
            <span>用户管理</span>
          </template>
          <el-menu-item index="/user">用户列表</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="2">
          <template #title>
            <el-icon style="font-size:21px;">
              <Van/>
            </el-icon>
            <span>车辆管理</span>
          </template>
          <el-menu-item index="/vehicle">基本信息</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="3">
          <template #title>
            <el-icon style="font-size:21px;">
              <Place/>
            </el-icon>
            <span>调度管理</span>
          </template>
          <el-menu-item index="/application">申请列表</el-menu-item>
          <el-menu-item index="/audit">调度审核</el-menu-item>
          <el-menu-item index="/distribute">车辆分配</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="4">
          <template #title>
            <el-icon style="font-size:21px;">
              <Location/>
            </el-icon>
            <span>电子围栏</span>
          </template>
          <el-menu-item index="/geofence">围栏管理</el-menu-item>
        </el-sub-menu>
        <el-sub-menu index="5">
          <template #title>
            <el-icon style="font-size:21px;">
              <MessageBox/>
            </el-icon>
            <span>数据字典</span>
          </template>
          <el-menu-item index="/dict">字典管理</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>
    <!-- 右侧主体内容 永远占满剩余宽度 -->
    <div :style="{width:(isCollapse ? 'calc(100% - 64px)' : 'calc(100% - 208px)')}">
      <!-- 右侧顶部条 -->
      <el-header style="padding-top:22px;height:10vh;background-color:#fff;">
        <!--折叠图标与退出登录-->
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
                  <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </el-col>
        </el-row>
        <!-- 面包屑导航 -->
        <el-breadcrumb separator="/" style="position:relative;top:20px;">
          <el-breadcrumb-item>首页</el-breadcrumb-item>
          <el-breadcrumb-item v-for="item in breadCrumb">{{item}}</el-breadcrumb-item>
        </el-breadcrumb>
      </el-header>
      <!-- 右侧主体内容 除顶部条与面包屑的部分都是可变区域,在对应的子级页面里写!
      height:90vh;高度是固定的90vh overflow-y: auto;垂直方向溢出自动显示滚动条-->
      <el-main style="padding:0;height:90vh;overflow-y: auto;">
        <router-view/>
      </el-main>
    </div>
  </div>
</template>

<script setup>
import {ref} from "vue";

//获取当前登录的用户信息
const user = ref(getUser());

//定义变量控制左侧菜单栏是否折叠
const isCollapse = ref(false);
//定义方法切换左侧菜单栏是否折叠
const changeCollapsed = () => {
  isCollapse.value = !isCollapse.value;
}

//设置变量来维护面包屑导航的值,默认为用户列表界面
//如果第1次打开,localStorage.getItem('breadcrumb')取不到值,则默认值为["用户管理","用户列表"]
//如果不是第1次开打开,localStorage里有值,取到什么值,就显示什么值
const breadCrumb = ref(
    JSON.parse(localStorage.getItem('breadcrumb') || '["用户管理","用户列表"]'));
//准备对象用来维护对应页面与面包屑导航的对应关系
//准备数组用来存面包屑导航的值
let map = {
  "/user": ['用户管理','用户列表'],
  "/vehicle": ['车辆管理','基本信息'],
  "/geofence": ['电子围栏','围栏管理'],
  "/application": ['调度管理','申请列表'],
  "/audit": ['调度管理','调度审核'],
  "/distribute": ['调度管理','车辆分配'],
  "/dict": ['数据字典','字典管理']
};
//选择菜单项时触发,index是选了哪一个菜单项
const selectMenu = (index)=>{
  //根据选择的菜单项设置面包屑导航的值
  breadCrumb.value = map[index];
  //解决刷新页面面包屑值丢失的问题:存到localStorage中
  localStorage.setItem("breadcrumb",JSON.stringify(breadCrumb.value));
}

//退出登录功能
const logout = ()=>{
  if(confirm('您确认要退出吗?')){
    //退出时清空localStorage中存的用户与面包屑信息
    localStorage.removeItem('user');
    localStorage.removeItem('breadcrumb');
    //退出后去到登录页
    window.location.href = '/login';
    //也需要把已经取出来的值清空
    user.value = '';
  }
}



</script>
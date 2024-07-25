import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
    redirect:'/user',
    children:[
      {path: '/user', component: () => import('../views/user/UserView.vue')},
      {path: '/vehicle', component: () => import('../views/insurance_policy/InsurancePolicyView.vue')},
      {path: '/geofence', component: () => import('../views/insurance_plan/GeneratePlanView.vue')},
      {path: '/geofenceMap', component: () => import('../views/insurance_plan/MakePlanView.vue')},
      {path: '/application', component: () => import('../views/customer/CustomerView.vue')}

    ]
  },
  {path: '/login', component: () => import('../views/LoginView')}
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
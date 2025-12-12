import { createRouter, createWebHistory } from 'vue-router'

import LandingView from "@/views/LandingView.vue";
import BoardView from "@/views/BoardView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: LandingView
    },
    {
      path: '/board',
      name: 'board',
      component: BoardView,
      props: true,
    }
  ],
})

export default router

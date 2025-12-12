<script setup>
import {onMounted, onUnmounted, ref} from "vue";
import {useBoardStore} from "@/stores/boardStore.js";
import {formatRelativeTime} from "@/utils/timeFormatter.js";

const store = useBoardStore()

const refreshKey = ref(0)
let interval;

onMounted(() => {
    interval = setInterval(() => {
        refreshKey.value++;
    }, 30_000);
})

onUnmounted(() => {
    if (interval) clearInterval(interval);
})

function getMethodColor(method) {
  const map = {
    GET: 'blue',
    POST: 'green',
    PUT: 'orange',
    DELETE: 'red',
    PATCH: 'purple'
  }
  return map[method] || 'grey'
}

function getStatusColor(status) {
  if (status >= 200 && status < 300) return 'text-green'
  if (status >= 400 && status < 500) return 'text-orange'
  if (status >= 500) return 'text-red'
  return 'text-grey'
}
</script>

<template>
    <v-navigation-drawer permanent width="450" color="surface" class="border-e">
        <div class="d-flex align-center px-4 py-3 border-b" style="height: 64px;">
            <v-icon icon="mdi-history" color="grey" class="mr-2"></v-icon>
            <span class="text-subtitle-2 font-weight-bold text-medium-emphasis">REQUESTS</span>
            <v-spacer></v-spacer>
            <v-chip size="small" color="primary" variant="flat">
                {{ store.requests.length > 0 ? store.requests.length : 0 }}
            </v-chip>
        </div>

        <v-list lines="two" class="pa-0">
            <v-list-item 
                v-if="store.requests.length === 0"
                value="placeholder" 
                color="primary"
                class="border-b"
                disabled
            >
            Received requests goes here
            </v-list-item>

            <v-list-item 
                v-for="(req, index) in store.requests"
                :key="index"
                :value="req"
                class="border-b request-item"
            >
                <template v-slot:prepend>
                <v-chip 
                    label 
                    size="small" 
                    :color="getMethodColor(req.method)" 
                    class="font-weight-bold mr-2 text-uppercase"
                    style="min-width: 60px; justify-content: center;"
                >
                    {{ req.method }}
                </v-chip>
                </template>

                <v-list-item-title class="font-monospace text-body-2 font-weight-medium">
                {{ req.path }}
                </v-list-item-title>
                
                <v-list-item-subtitle class="d-flex justify-space-between mt-1 align-center">
                <span :class="getStatusColor(req.status)" class="font-weight-bold text-caption">
                    {{ req.status || '---' }}
                </span>
                <span :key="refreshKey" class="text-caption text-disabled font-monospace">
                    {{ formatRelativeTime(req.timestamp) }}
                </span>
                </v-list-item-subtitle>
            </v-list-item>
        </v-list>
    </v-navigation-drawer>
</template>

<style scoped>
.font-monospace {
  font-family: 'Roboto Mono', monospace !important;
}
.border-b {
  border-bottom: 1px solid rgba(255, 255, 255, 0.12) !important;
}
.border-e {
  border-right: 1px solid rgba(255, 255, 255, 0.12) !important;
}
</style>
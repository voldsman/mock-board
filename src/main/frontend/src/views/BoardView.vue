<script setup>
import {onBeforeUnmount, onMounted} from 'vue'

import {useBoardStore} from '@/stores/boardStore'
import TopBar from '@/components/TopBar.vue'
import RequestHistory from '@/components/RequestHistory.vue'
import {boardApi} from "@/services/api.js";
import router from "@/router/index.js";

const store = useBoardStore()

onMounted(async () => {
    let sessionId = history.state?.sessionId;
    if (!sessionId) {
        sessionId = await boardApi.startSession();
    }

    if (!sessionId) {
        await store.reset();
        await router.push('/');
        return;
    }

    store.setSessionAndConnect(sessionId);
})

onBeforeUnmount(() => {
    store.disconnect()
})
</script>

<template>
    <div class="fill-height">
        <TopBar />

        <RequestHistory />

        <v-main class="bg-background">
            <v-container fluid class="pa-6">
                <div class="d-flex flex-column align-center justify-center h-50 text-grey">
                    <v-icon size="64" class="mb-4">mdi-view-dashboard-outline</v-icon>
                    <h3 class="text-h5 font-weight-light">Dashboard Area</h3>
                    <p>Rules grid will go here</p>
                </div>
            </v-container>
        </v-main>
    </div>
</template>
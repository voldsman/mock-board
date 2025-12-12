<script setup>
import {onBeforeUnmount, onMounted} from 'vue'
import {useBoardStore} from '@/stores/boardStore'
import {boardApi} from "@/services/api.js";
import router from "@/router/index.js";
import TopBar from '@/components/TopBar.vue'
import RequestHistory from '@/components/RequestHistory.vue'
import RequestViewer from "@/components/RequestViewer.vue";

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
    <div class="d-flex flex-column min-vh-100">
        <TopBar />
        <div class="d-flex flex-grow-1" style="height: calc(100vh - 57px); overflow: hidden;">
            <RequestHistory />

            <main class="flex-grow-1 p-0 position-relative" style="background-color: #352945;">

                <RequestViewer v-if="store.selectedRequest" />

                <div v-else class="h-100 d-flex flex-column align-items-center justify-content-center text-secondary">
                    <i class="bi bi-grid-3x3-gap display-1 mb-4"></i>
                    <h3 class="h5 fw-light">Dashboard Area</h3>
                    <p class="text-muted">Rules grid will go here</p>
                    <button class="btn btn-outline-primary mt-3" @click="boardApi.startSession()">
                        Test Logic
                    </button>
                </div>

            </main>
        </div>
    </div>
</template>
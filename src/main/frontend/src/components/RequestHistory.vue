<script setup>
import {onMounted, onUnmounted, ref} from "vue";
import {useBoardStore} from "@/stores/boardStore.js";
import {formatRelativeTime} from "@/utils/timeFormatter.js";
import {getMethodColor, getStatusColor} from "@/utils/colors.js";

const store = useBoardStore();
const refreshKey = ref(0);
let interval;

onMounted(() => {
    interval = setInterval(() => { refreshKey.value++; }, 30_000);
});

onUnmounted(() => {
    if (interval) clearInterval(interval);
});

function handleSelect(req) {
    store.selectRequest(req);
}
</script>

<template>
    <div class="d-flex flex-column bg-surface border-end" style="width: 450px; min-width: 450px; height: calc(100vh - 57px);"> <div class="d-flex align-items-center px-4 py-3 border-bottom" style="height: 64px;">
        <i class="bi bi-clock-history text-secondary me-2"></i>
        <span class="text-uppercase fw-bold text-secondary small">REQUESTS</span>
        <div class="ms-auto">
            <span class="badge bg-primary rounded-pill">{{ store.requests.length }}</span>
        </div>
    </div>

        <div class="flex-grow-1 overflow-auto">
            <div class="list-group list-group-flush">
                <div v-if="store.requests.length === 0" class="text-center py-5 text-secondary">
                    Waiting for requests...
                </div>

                <div v-for="(req, index) in store.requests"
                     :key="index"
                     class="list-group-item list-group-item-action bg-surface border-bottom cursor-pointer"
                     :class="{ 'active-item': store.selectedRequest === req }"
                     @click="handleSelect(req)"
                >
                    <div class="d-flex align-items-start gap-2 mb-2">
                        <span class="badge text-uppercase fw-bold" :class="`bg-${getMethodColor(req.method)}`" style="min-width: 60px;">
                            {{ req.method }}
                        </span>
                        <div class="flex-grow-1">
                            <div class="font-monospace small fw-medium text-light">{{ req.path }}</div>
                        </div>
                    </div>
                    <div class="d-flex justify-content-between align-items-center mt-2">
                        <span :class="getStatusColor(req.status)" class="fw-bold small">{{ req.status || '---' }}</span>
                        <span :key="refreshKey" class="text-secondary small font-monospace">
                            {{ formatRelativeTime(req.timestamp) }}
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.bg-surface {
    background-color: #3f3951;
}
.list-group-item:hover {
    cursor: pointer;
}
.active-item {
    background-color: #4a4359 !important;
    border-left: 4px solid #0d6efd !important;
}
</style>
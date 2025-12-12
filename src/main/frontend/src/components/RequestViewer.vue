<script setup>
import {useBoardStore} from "@/stores/boardStore.js";
import {getMethodColor, getStatusColor} from "@/utils/colors.js";
import formatJson from "@/utils/formatJson.js";

const store = useBoardStore();
</script>

<template>
    <div class="h-100 d-flex flex-column text-light">
        <div class="p-4 border-bottom d-flex justify-content-between align-items-center bg-surface">
            <div class="d-flex align-items-center gap-3">
                <span class="badge text-uppercase fw-bold fs-6"
                      :class="`bg-${getMethodColor(store.selectedRequest?.method)}`">
                    {{ store.selectedRequest?.method }}
                </span>
                <span class="font-monospace fs-5">{{ store.selectedRequest?.path }}</span>
            </div>
            <button class="btn btn-outline-secondary btn-sm" @click="store.clearSelectedRequest">
                <i class="bi bi-x-lg"></i> Close
            </button>
        </div>

        <div class="px-4 py-3 border-bottom bg-dark">
            <div class="row g-3">
                <div class="col-md-3">
                    <small class="text-secondary d-block mb-1">Status</small>
                    <span class="badge fw-bold" :class="`bg-${getStatusColor(store.selectedRequest?.status)}`">
                        {{ store.selectedRequest?.status || '---' }}
                    </span>
                </div>
                <div class="col-md-3">
                    <small class="text-secondary d-block mb-1">Content-Type</small>
                    <span class="font-monospace text-light small">{{ store.selectedRequest?.contentType || 'N/A' }}</span>
                </div>
                <div class="col-md-3">
                    <small class="text-secondary d-block mb-1">Time</small>
                    <span class="font-monospace text-light small">{{ new Date(store.selectedRequest?.timestamp).toLocaleTimeString() }}</span>
                </div>
            </div>
        </div>

        <div class="flex-grow-1 overflow-auto">
            <div class="row g-0 h-100">
                <div class="col-md-6 border-end p-4">
                    <h6 class="text-secondary text-uppercase small mb-3">
                        <i class="bi bi-box-arrow-in-down me-2"></i>Request
                    </h6>
                    <h6 class="text-secondary text-uppercase small mt-4 mb-3">Headers</h6>
                    <div v-for="(value, key) in store.selectedRequest?.headers" :key="key"
                         class="mb-2 small font-monospace border-bottom border-secondary border-opacity-10 pb-1">
                        <span class="text-info fw-bold">{{ key }}:</span>
                        <span class="text-light ms-2 text-break">{{ value }}</span>
                    </div>
                    <h6 class="text-secondary text-uppercase small mt-4 mb-3">Body</h6>
                    <div class="bg-dark rounded p-3">
                        <pre class="text-light font-monospace small m-0" v-if="store.selectedRequest?.body">{{
                                formatJson(store.selectedRequest.body)
                            }}</pre>
                        <div v-else class="text-secondary">No body content</div>
                    </div>
                </div>

                <div class="col-md-6 border-end p-4">
                    <h6 class="text-secondary text-uppercase small mb-3">
                        <i class="bi bi-box-arrow-in-down me-2"></i>Response
                    </h6>
                    <h6 class="text-secondary text-uppercase small mt-4 mb-3">Headers</h6>
                    <div v-for="(value, key) in store.selectedRequest?.headers" :key="key"
                         class="mb-2 small font-monospace border-bottom border-secondary border-opacity-10 pb-1">
                        <span class="text-info fw-bold">{{ key }}:</span>
                        <span class="text-light ms-2 text-break">{{ value }}</span>
                    </div>
                    <h6 class="text-secondary text-uppercase small mt-4 mb-3">Body</h6>
                    <div class="bg-dark rounded p-3">
                        <pre class="text-light font-monospace small m-0">
                            Mock Body
                        </pre>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.bg-surface {
    background-color: #352945;
}
pre {
    white-space: pre-wrap;
    word-wrap: break-word;
}
</style>
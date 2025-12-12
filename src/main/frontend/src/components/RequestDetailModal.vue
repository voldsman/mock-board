<script setup>
import { ref, watch } from 'vue';
import {getMethodColor, getStatusColor} from "@/utils/colors.js";

const props = defineProps({
    request: {
        type: Object,
        default: null
    },
    show: {
        type: Boolean,
        default: false
    }
});

const emit = defineEmits(['close']);

const modalRef = ref(null);
let bsModal = null;

watch(() => props.show, (newVal) => {
    if (newVal && modalRef.value) {
        if (!bsModal) {
            bsModal = new bootstrap.Modal(modalRef.value);
        }
        bsModal.show();
    } else if (bsModal) {
        bsModal.hide();
    }
});

function handleClose() {
    emit('close');
}

function formatJson(obj) {
    try {
        return JSON.stringify(obj, null, 2);
    } catch {
        return String(obj);
    }
}
</script>

<template>
    <div
        ref="modalRef"
        class="modal fade"
        tabindex="-1"
        aria-hidden="true"
        @hidden.bs.modal="handleClose"
    >
        <div class="modal-dialog modal-xl modal-dialog-scrollable">
            <div class="modal-content bg-surface">
                <div class="modal-header border-bottom">
                    <h5 class="modal-title d-flex align-items-center gap-2">
                        <span
                            class="badge text-uppercase fw-bold"
                            :class="`bg-${getMethodColor(request?.method)}`"
                        >
                            {{ request?.method }}
                        </span>
                        <span class="font-monospace">{{ request?.path }}</span>
                    </h5>
                    <button
                        type="button"
                        class="btn-close btn-close-white"
                        @click="handleClose"
                    ></button>
                </div>

                <div class="modal-body p-0">
                    <!-- Top Info Bar -->
                    <div class="px-4 py-3 border-bottom bg-dark">
                        <div class="row g-3">
                            <div class="col-md-3">
                                <small class="text-secondary d-block mb-1">Status</small>
                                <span
                                    class="badge fw-bold"
                                    :class="`bg-${getStatusColor(request?.status)}`"
                                >
                                    {{ request?.status || '---' }}
                                </span>
                            </div>
                            <div class="col-md-3">
                                <small class="text-secondary d-block mb-1">Protocol</small>
                                <span class="font-monospace text-light">{{ request?.protocol || 'N/A' }}</span>
                            </div>
                            <div class="col-md-3">
                                <small class="text-secondary d-block mb-1">Content-Type</small>
                                <span class="font-monospace text-light small">{{ request?.contentType || 'N/A' }}</span>
                            </div>
                            <div class="col-md-3">
                                <small class="text-secondary d-block mb-1">Content-Length</small>
                                <span class="font-monospace text-light">{{ request?.contentLength || 0 }} bytes</span>
                            </div>
                        </div>

                        <div v-if="request?.query" class="mt-3">
                            <small class="text-secondary d-block mb-1">Query String</small>
                            <code class="text-info d-block">{{ request.query }}</code>
                        </div>
                    </div>

                    <!-- Two Column Layout -->
                    <div class="row g-0" style="min-height: 400px;">
                        <!-- Left: Request -->
                        <div class="col-md-6 border-end">
                            <div class="p-4">
                                <h6 class="text-secondary text-uppercase small mb-3">
                                    <i class="bi bi-box-arrow-in-down me-2"></i>Request
                                </h6>

                                <!-- Headers -->
                                <div class="mb-4">
                                    <div class="d-flex align-items-center mb-2">
                                        <small class="text-secondary fw-bold">HEADERS</small>
                                        <span class="badge bg-secondary ms-2">{{ Object.keys(request?.headers || {}).length }}</span>
                                    </div>
                                    <div class="bg-dark rounded p-3" style="max-height: 200px; overflow-y: auto;">
                                        <div
                                            v-for="(value, key) in request?.headers"
                                            :key="key"
                                            class="mb-2 small"
                                        >
                                            <span class="text-info font-monospace">{{ key }}:</span>
                                            <span class="text-light font-monospace ms-2">{{ value }}</span>
                                        </div>
                                        <div v-if="!request?.headers || Object.keys(request.headers).length === 0" class="text-secondary">
                                            No headers
                                        </div>
                                    </div>
                                </div>

                                <!-- Body -->
                                <div>
                                    <small class="text-secondary fw-bold d-block mb-2">BODY</small>
                                    <div class="bg-dark rounded p-3" style="max-height: 300px; overflow-y: auto;">
                                        <pre class="text-light font-monospace small m-0" v-if="request?.body">{{
                                                typeof request.body === 'object' ? formatJson(request.body) : request.body
                                            }}</pre>
                                        <div v-else class="text-secondary">No body content</div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Right: Response -->
                        <div class="col-md-6">
                            <div class="p-4">
                                <h6 class="text-secondary text-uppercase small mb-3">
                                    <i class="bi bi-box-arrow-up me-2"></i>Response
                                </h6>

                                <!-- Response Headers -->
                                <div class="mb-4">
                                    <div class="d-flex align-items-center mb-2">
                                        <small class="text-secondary fw-bold">HEADERS</small>
                                        <span class="badge bg-secondary ms-2">0</span>
                                    </div>
                                    <div class="bg-dark rounded p-3">
                                        <div class="text-secondary">No response headers</div>
                                    </div>
                                </div>

                                <!-- Response Body -->
                                <div>
                                    <small class="text-secondary fw-bold d-block mb-2">BODY</small>
                                    <div class="bg-dark rounded p-3" style="min-height: 200px;">
                                        <div class="text-secondary">No response body</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer border-top">
                    <button type="button" class="btn btn-secondary" @click="handleClose">
                        Close
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.bg-surface {
    background-color: #352945;
}
.font-monospace {
    font-family: 'Roboto Mono', monospace !important;
}
.modal-content {
    border: 1px solid rgba(255, 255, 255, 0.12);
}
pre {
    white-space: pre-wrap;
    word-wrap: break-word;
}
</style>
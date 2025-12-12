<script setup>
import {useBoardStore} from "@/stores/boardStore";
import router from "@/router/index.js";

const store = useBoardStore()

function copyUrl() {
    if (store.webhookUrl) {
        navigator.clipboard.writeText(store.webhookUrl)
        alert("Copied!")
    }
}

const handleReset = async () => {
    await store.reset();
    await router.push("/");
}
</script>

<template>
    <nav class="navbar bg-surface border-bottom shadow-sm">
        <div class="container-fluid px-4">
            <span class="navbar-brand mb-0 h1">
                <span class="text-primary">&lt;/&gt;</span> MockBoard
            </span>

            <div class="d-flex align-items-center flex-grow-1 mx-5" style="max-width: 600px;">
                <div class="input-group">
                    <span class="input-group-text bg-dark border-secondary">
                        <i class="bi bi-globe"></i>
                    </span>
                    <input
                        type="text"
                        class="form-control bg-dark border-secondary text-light font-monospace"
                        :value="store.webhookUrl"
                        readonly
                        placeholder="Waiting for session..."
                    />
                    <button
                        class="btn btn-primary"
                        type="button"
                        @click="copyUrl"
                    >
                        Copy
                    </button>
                </div>
            </div>

            <div class="d-flex align-items-center gap-3">
                <span
                    class="badge rounded-pill"
                    :class="store.isConnected ? 'bg-success' : 'bg-danger'"
                >
                    {{ store.isConnected ? 'Connected' : 'Offline' }}
                </span>
                <button
                    class="btn btn-outline-danger btn-sm"
                    @click="handleReset"
                    title="Reset Session"
                >
                    <i class="bi bi-box-arrow-right"></i>
                </button>
            </div>
        </div>
    </nav>
</template>

<style scoped>
.navbar {
    background-color: #3f3951 !important;
}
.font-monospace {
    font-family: 'Roboto Mono', monospace !important;
}
</style>
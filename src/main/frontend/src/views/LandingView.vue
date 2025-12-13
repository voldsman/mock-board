<script setup>
import { useRouter } from 'vue-router';
import {boardApi} from "@/services/apiService.js";

const router = useRouter();

const handleStart = async () => {
    const sessionId = await boardApi.startSession();
    if (sessionId) {
        await router.push({
            path: '/board',
            state: { sessionId }
        });
    }
};
</script>

<template>
    <div class="min-vh-100 d-flex align-items-center justify-content-center">
        <button class="btn btn-primary btn-lg" @click="handleStart">
            <i class="bi bi-play-fill me-2"></i>Start Session
        </button>
    </div>
</template>
<script setup>
import { useRouter } from 'vue-router';
import {boardApi} from "@/services/api.js";

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
    <v-btn @click="handleStart">Start Session</v-btn>
</template>
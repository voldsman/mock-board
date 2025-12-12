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
    <v-app-bar density="default" elevation="1" color="surface">
        <v-app-bar-title class="fond-weight-bold ml-2" style="max-width: 200px;">
            <span class="text-primary">&lt;/&gt;</span> MockBoard
        </v-app-bar-title>

        <v-spacer></v-spacer>

        <div class="mt-5" style="width: 500px; max-width: 800px;">
            <v-text-field
                :model-value="store.webhookUrl"
                readonly
                variant="outlined"
                density="compact"
                bg-color="background"
                placeholder="Waiting for session..."
                prepend-inner-icon="mdi-web"
                class="font-monospace"
            >
                <template v-slot:append-inner>
                    <v-btn 
                        size="small"
                        color="primary"
                        variant="text"
                        @click="copyUrl"
                        >Copy</v-btn>
                </template>
            </v-text-field>
        </div>

        <v-spacer></v-spacer>

        <v-chip
            :color="store.isConnected ? 'success' : 'error'"
            size="small"
            class="mr-4 fond-weight-bold"
            variant="flat"
        >
        {{ store.isConnected ? 'Connected' : 'Offline' }}
        </v-chip>

        <v-btn icon="mdi-logout"
               size="small"
               color="error"
               variant="text"
               class="ml-2"
               title="Reset Session"
               @click="handleReset"
        ></v-btn>
    </v-app-bar>
</template>

<style scoped>
.font-monospace {
  font-family: 'Roboto Mono', monospace !important;
}
</style>
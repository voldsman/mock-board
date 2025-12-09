<script setup>
import { onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useBoardStore } from '@/stores/boardStore'
import TopBar from '@/components/TopBar.vue'

const route = useRoute()
const store = useBoardStore()

onMounted(() => {
    store.init(route.params.sessionId)
})
</script>

<template>
    <div class="vh-100 d-flex flex-column bg-dark text-light">
        <TopBar />

        <div class="container-fluid flex-grow-1 overflow-hidden">
            <div class="row h-100">

                <div class="col-md-3 border-end border-secondary p-0 d-flex flex-column">
                    <div class="p-2 border-bottom border-secondary bg-darker">
                        <small class="text-muted fw-bold">REQUESTS</small>
                    </div>
                    <div class="flex-grow-1 overflow-auto">
                        <div class="p-3 text-muted text-center small">
                            No requests yet...
                        </div>
                    </div>
                </div>

                <div class="col-md-9 bg-darker p-4">
                    <h3>Mock Rules</h3>
                    <p class="text-secondary">Rules will appear here.</p>
                </div>

            </div>
        </div>
    </div>
</template>

<style>
.bg-darker {
    background-color: #1e293b;
}
</style>
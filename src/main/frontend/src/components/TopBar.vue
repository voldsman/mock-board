<script setup>
import {useBoardStore} from "@/stores/boardStore.js";
import {storeToRefs} from "pinia";

const store = useBoardStore()
const {sessionId, isConnected, webhookUrl} = storeToRefs(store)

function copyUrl() {
  navigator.clipboard.writeText(webhookUrl.value)
  alert("Copied!")
}
</script>

<template>
  <nav class="navbar navbar-expand-md navbar-dark bg-dark border-bottom border-secondary"
       style="height: 60px;">

    <div class="container-fluid">
      <a class="navbar-brand fw-bold" href="#">
        <span class="text-primary">&lt;/&gt;</span> MockBoard
      </a>

      <div class="mx-auto w-50 d-none d-md-block">
        <div class="input-group input-group-sm">
          <span class="input-group-text bg-dark text-muted">URL</span>
          <input type="text" class="form-control bg-dark text-light border-secondary text-center font-monospace"
          :value="webhookUrl" readonly>
          <button class="btn btn-outline-primary" @click="copyUrl">Copy</button>
      </div>
    </div>

    <div class="d-flex align-items-center">
        <span class="badge rounded-pill me-3"
      :class="isConnected ? 'bg-success' : 'bg-danger'">
      {{ isConnected ? 'Connected' : 'Offline' }}
    </span>
    <span class="text-muted small font-monospace">ID: {{ sessionId?.substring(0,6) }}...</span>
  </div>
</div>
</nav>
</template>

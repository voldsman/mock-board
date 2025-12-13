import {defineStore} from "pinia";

import {boardApi} from "@/services/api.js";
import RequestData from "@/models/RequestData.js";

export const useBoardStore = defineStore("boardStore", {
    state: () => ({
        sessionId: null,
        isConnected: false,
        webhookUrl: '',
        requests: [],
        rules: [],

        selectedRequest: null,

        socket: null,
        heartbeatInterval: null,
        pongTimeout: null,
        reconnectTimer: null,
        reconnectAttempts: 0,
    }),

    actions: {
        setSessionAndConnect(sessionId) {
            this.sessionId = sessionId;
            this.webhookUrl = `https://mockboard.dev/m/${this.sessionId}`;
        },

        selectRequest(req) {
          this.selectedRequest = req;
        },

        clearSelectedRequest() {
            this.selectedRequest = null;
        },

        async reset() {
            await boardApi.reset();
            this.requests = [];
            this.selectedRequest = null;
            this.sessionId = null;
            this.webhookUrl = '';
        },

        attachRequestHistoryData(data) {
            const reqData = new RequestData();
            reqData.method = data.method;
            reqData.path = data.path;
            reqData.query = data.query;
            reqData.protocol = data.protocol;
            reqData.headers = data.headers;
            reqData.body = data.body??null;
            reqData.contentType = data.contentType;
            reqData.contentLength = data.contentLength;
            reqData.status = data.status;
            reqData.timestamp = data.timestamp;
            this.requests.unshift(reqData);
        }
    }
});
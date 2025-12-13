import {defineStore} from "pinia";

import {boardApi} from "@/services/apiService.js";
import RequestData from "@/models/RequestData.js";
import {sseService} from "@/services/sseService.js";

export const useBoardStore = defineStore("boardStore", {
    state: () => ({
        sessionId: null,
        isConnected: false,
        webhookUrl: '',
        requests: [],
        rules: [],
        selectedRequest: null,

        eventSource: null,
    }),

    actions: {
        async setSessionAndConnect(sessionId) {
            try {
                this.sessionId = sessionId;
                this.webhookUrl = `https://mockboard.dev/m/${this.sessionId}`;
                this.connectSse();
            } catch (e) {
                console.error("Failed to start session", e);
            }
        },
        connectSse() {
            if (!this.sessionId) {
                console.log("no session id");
                return;
            }

            sseService.connect(this.sessionId, {
                onOpen: () => {
                    this.isConnected = true;
                },
                onClose: () => {
                    this.isConnected = false;
                },
                onError: () => {
                    console.error('sse connection error')
                },
                onMaxReconnectFailed: () => {
                    console.error('failed to reconnect to sse')
                }
            });

            sseService.addEventListener('REQUEST_CAPTURED', this.handleRequestCaptured)
        },
        disconnectSse() {
            sseService.disconnect();
            this.isConnected = false;
        },

        selectRequest(req) {
          this.selectedRequest = req;
        },

        clearSelectedRequest() {
            this.selectedRequest = null;
        },

        async reset() {
            await boardApi.reset();
            this.disconnectSse();
            this.requests = [];
            this.selectedRequest = null;
            this.sessionId = null;
            this.webhookUrl = '';
        },

        handleRequestCaptured(event) {
            try {
                const data = JSON.parse(event.data);
                this.attachRequestHistoryData(data);
            } catch (e) {
                console.error("Failed to parse event data", e);
            }
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

            if (this.requests.length > 15) {
                this.requests.pop();
            }
        }
    }
});
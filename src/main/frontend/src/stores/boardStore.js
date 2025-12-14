import {defineStore} from "pinia";
import RequestData from "@/models/RequestData.js";
import {boardApi} from "@/services/apiService.js";
import {longPollingService} from "@/services/longPoolingService.js";

export const useBoardStore = defineStore("boardStore", {
    state: () => ({
        sessionId: null,
        isConnected: false,
        webhookUrl: '',
        requests: [],
        rules: [],
        selectedRequest: null,
    }),

    actions: {
        setSessionAndConnect(sessionId) {
            try {
                this.sessionId = sessionId;
                this.webhookUrl = `https://mockboard.dev/m/${this.sessionId}`;
                this.connect();
            } catch (e) {
                console.error("Failed to start session", e);
            }
        },

        connect() {
            if (!this.sessionId) {
                console.log("No session id");
                return;
            }

            longPollingService.addEventListener("REQUEST_CAPTURED", this.handleRequestCaptured.bind(this));

            longPollingService.connect(this.sessionId, {
                onConnected: (data) => {
                    console.log("Connected:", data);
                    this.isConnected = true;
                },
                onEvent: (event) => {
                    console.log("Event received:", event.type);
                },
                onError: (error) => {
                    console.error("Connection error:", error);
                },
                onMaxRetriesFailed: () => {
                    console.error("Failed to maintain connection");
                    this.isConnected = false;
                    // Optional: show user notification
                }
            });
        },

        handleRequestCaptured(event) {
            try {
                const data = event.data;
                console.log("Request captured:", data);
                this.attachRequestHistoryData(data);
            } catch (e) {
                console.error("Failed to process request data", e);
            }
        },

        disconnect() {
            longPollingService.disconnect();
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
            this.disconnect();
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
            reqData.body = data.body ?? null;
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
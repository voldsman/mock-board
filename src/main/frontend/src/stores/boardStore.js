import {defineStore} from "pinia";

import {boardApi} from "@/services/api.js";

export const useBoardStore = defineStore("boardStore", {
    state: () => ({
        sessionId: null,
        isConnected: false,
        webhookUrl: '',
        requests: [],
        rules: [],

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
            this.connectWs();
        },

        connectWs() {
            if (this.socket) {
                this.socket.close();
                this.socket = null;
            }

            if (this.reconnectTimer) {
                clearTimeout(this.reconnectTimer);
                this.reconnectTimer = null;
            }

            const protocol = location.protocol === 'https:' ? 'wss:' : 'ws:';
            const wsUrl = `${protocol}//${location.host}/ws/${this.sessionId}`;

            console.log('Connecting to WS...');
            this.socket = new WebSocket(wsUrl);

            this.socket.onopen = () => {
                console.log("WS Connected!");
                this.isConnected = true;
                this.reconnectAttempts = 0;
                this.startHeartbeat();
            };

            this.socket.onmessage = (event) => {
                if (event.data === "_pong") {
                    if (this.pongTimeout) {
                        clearTimeout(this.pongTimeout);
                        this.pongTimeout = null;
                    }
                    return;
                }

                try {
                    const data = JSON.parse(event.data);
                    this.requests.unshift(data);
                } catch (e) {
                    console.log("Message:", event.data);
                }
            };

            this.socket.onclose = (e) => {
                console.log('WS Closed', e.code);
                this.isConnected = false;
                this.stopHeartbeat();

                // Stop reconnecting after 5 attempts
                if (this.reconnectAttempts >= 5) {
                    console.log('Max reconnect attempts reached');
                    return;
                }

                this.reconnectAttempts++;
                this.scheduleReconnect();
            };

            this.socket.onerror = (err) => {
                console.error('WS Error', err);
            };
        },

        startHeartbeat() {
            this.stopHeartbeat();

            this.heartbeatInterval = setInterval(() => {
                if (this.socket?.readyState === WebSocket.OPEN) {
                    this.socket.send('_ping');

                    this.pongTimeout = setTimeout(() => {
                        console.log('Pong timeout, closing connection');
                        this.socket.close();
                    }, 5000);
                }
            }, 20000);
        },

        stopHeartbeat() {
            if (this.heartbeatInterval) {
                clearInterval(this.heartbeatInterval);
                this.heartbeatInterval = null;
            }
            if (this.pongTimeout) {
                clearTimeout(this.pongTimeout);
                this.pongTimeout = null;
            }
        },

        scheduleReconnect() {
            const delay = Math.min(2000 * this.reconnectAttempts, 10000);
            console.log(`Reconnecting in ${delay}ms... (attempt ${this.reconnectAttempts})`);

            this.reconnectTimer = setTimeout(async () => {
                const newSessionId = await this.startSession();
                if (newSessionId) {
                    this.sessionId = newSessionId;
                    this.webhookUrl = `https://mockboard.dev/m/${this.sessionId}`;
                    this.connectWs();
                } else {
                    if (this.reconnectAttempts < 5) {
                        this.reconnectAttempts++;
                        this.scheduleReconnect();
                    } else {
                        console.log('Max reconnect attempts reached');
                    }
                }
            }, delay);
        },

        disconnect() {
            console.log('Disconnecting...');
            if (this.socket) {
                this.socket.close(1000, 'Page navigation');
            }
            this.stopHeartbeat();
            if (this.reconnectTimer) {
                clearTimeout(this.reconnectTimer);
                this.reconnectTimer = null;
            }
            this.socket = null;
            this.isConnected = false;
        },

        async reset() {
            await boardApi.reset();
            this.disconnect();
            this.requests = [];
            this.sessionId = null;
            this.webhookUrl = '';
        }
    }
});
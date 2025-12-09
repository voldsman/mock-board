import {defineStore} from "pinia";
import axios from "axios";

export const useBoardStore = defineStore("boardStore", {
    state: () => ({
        sessionId: null,
        isConnected: false,
        webhookUrl: '',
        requests: [],
        rules: [],

        // ws connection
        socket: null,
        heartbeatInterval: null,
        pongTimeout: null,
        reconnectTimer: null,

        // retry
        reconnectAttempts: 0,
        maxRetries: 5
    }),
    getters: {
        isGivenUp: (state) => state.reconnectAttempts >= state.maxRetries && !state.isConnected,
    },
    actions: {
        init(id) {
            this.sessionId = id
            this.webhookUrl = `${globalThis.location.origin}/${id}`
            this.connectWs()
            // fetch rules
        },

        // ws
        connectWs() {
            if (this.socket && (this.socket.readyState === WebSocket.OPEN ||
                this.socket.readyState === WebSocket.CONNECTING)) {
                return
            }

            const protocol = globalThis.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const wsUrl = `${protocol}//${globalThis.location.host}/ws/${this.sessionId}`

            console.log('Connecting to WS...')
            this.socket = new WebSocket(wsUrl);
            this.socket.onopen = () => {
                console.log("WS Connected!");
                this.isConnected = true;
                this.reconnectAttempts = 0
                this.startHeartbeat()
            }

            this.socket.onmessage = (event) => {
                const msg = event.data
                if (msg === "_pong") {
                    this.handlePong()
                    return
                }

                try {
                    const data = JSON.parse(msg)
                    this.requests.unshift(data)
                } catch (e) {
                    console.error("System message: ", msg)
                }
            }

            this.socket.onclose = (e) => {
                if (e.code === 1000 || e.code === 1001) return

                console.warn('WS Closed', e.reason)
                this.cleanup()

                if (this.reconnectAttempts < this.maxRetries) {
                    const delay = Math.min(1000 * (2 ** this.reconnectAttempts), 10000)
                    console.log(`Reconnecting in ${delay}ms...`)

                    this.reconnectTimer = setTimeout(() => {
                        this.reconnectAttempts++
                        this.connectWs()
                    }, delay)
                } else {
                    console.error('Max reconnect attempts reached. Giving up.')
                }
            }

            this.socket.onerror = (err) => {
                console.error('WS Error', err)
                this.socket.close()
            }
        },
        startHeartbeat() {
            this.stopHeartbeat()
            this.heartbeatInterval = setInterval(() => {
                if (this.socket.readyState === WebSocket.OPEN) {
                    this.socket.send('_ping')

                    this.pongTimeout = setTimeout(() => {
                        console.error('Ping timed out. Connection stale. Force closing.')
                        this.socket.close()
                    }, 5000)
                }
            }, 20000)
        },
        handlePong() {
            if (this.pongTimeout) {
                clearTimeout(this.pongTimeout)
                this.pongTimeout = null
            }
        },
        manualReconnect() {
            this.reconnectAttempts = 0
            this.connectWs()
        },
        stopHeartbeat() {
            if (this.heartbeatInterval) clearInterval(this.heartbeatInterval)
            if (this.pongTimeout) clearTimeout(this.pongTimeout)
            this.heartbeatInterval = null
            this.pongTimeout = null
        },

        cleanup() {
            this.isConnected = false
            this.stopHeartbeat()
        },

        // api
        async fetchRules() {
            try {
                const res = await axios.get(`/api/board/${this.sessionId}/rules`)
                this.rules = res.data
            } catch (e) {
                console.error("Failed to load rules", e)
            }
        },

        async createRule(rule) {
            const res = await axios.post(`/api/board/${this.sessionId}/rules`, rule)
            this.rules.push(res.data)
        }
    }
});
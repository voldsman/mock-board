class BoardWS {

    constructor(url) {
        this.url = url;
        this.ws = null;

        this.connectTimeout = 1000;
        this.maxTimeout = 10000;
        this.heartbeatInterval = 20000;

        this.reconnectAttempts = 0;
        this.maxReconnectAttempts = 3;

        this.messageQueue = [];
        this.forcedClose = false;

        this.connect();
    }

    connect() {
        this.ws = new WebSocket(this.url);

        this.ws.onopen = () => {
            console.log("WS OPEN");

            // Reset backoff
            this.connectTimeout = 1000;

            // Flush queued messages
            this.messageQueue.forEach(m => this.ws.send(m));
            this.messageQueue = [];

            // Start heartbeat
            this.startHeartbeat();

            if (this.onopen) this.onopen();
        };

        this.ws.onmessage = (event) => {
            if (event.data === "_pong") return; // ignore
            if (this.onmessage) this.onmessage(event.data);
        };

        this.ws.onerror = (e) => {
            console.log("WS ERROR:", e);
            this.reconnectAttempts++;
        };

        this.ws.onclose = () => {
            console.log("WS CLOSED");

            if (this.forcedClose) return;

            this.stopHeartbeat();
            this.reconnect();
        };
    }

    reconnect() {
        if (this.reconnectAttempts > this.maxReconnectAttempts) {
            console.log("Reached max reconnect attempts: " + this.maxReconnectAttempts);
            return;
        }

        console.log("Reconnecting in", this.connectTimeout, "ms");

        setTimeout(() => {
            this.connectTimeout = Math.min(this.connectTimeout * 2, this.maxTimeout);
            this.connect();
        }, this.connectTimeout);
    }

    send(msg) {
        if (this.ws.readyState === WebSocket.OPEN) {
            this.ws.send(msg);
        } else {
            this.messageQueue.push(msg);
        }
    }

    startHeartbeat() {
        this.heartbeatTimer = setInterval(() => {
            if (this.ws.readyState === WebSocket.OPEN) {
                this.ws.send("_ping");
            }
        }, this.heartbeatInterval);
    }

    stopHeartbeat() {
        clearInterval(this.heartbeatTimer);
    }

    close() {
        this.forcedClose = true;
        this.stopHeartbeat();
        this.ws.close();
    }
}
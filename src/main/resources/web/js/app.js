$(document).ready(function(){
    console.log("Ready!");

    const uuid = window.MockBoard ? window.MockBoard.sessionId : "unknown";
    const ws = new BoardWS('ws://'+ location.host + '/ws/' + uuid);

    const messages = $("#messages");
    const input = $("#msgInput");

    ws.onopen = () => {
        append("🟢 Connected");
    };

    ws.onmessage = (data) => {
        append("Server: " + data);
    };

    $("#sendBtn").click(sendMsg);
    input.on("keypress", e => { if (e.which === 13) sendMsg(); });

    // $("#resetBtn").click(() => window.location.href = "/reset");

    function sendMsg() {
        const msg = input.val().trim();
        if (!msg) return;

        ws.send(msg);
        append("You: " + msg);

        input.val("");
    }

    function append(text) {
        messages.append($("<div>").text(text));
        messages.scrollTop(messages.prop("scrollHeight"));
    }
});
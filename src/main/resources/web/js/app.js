$(document).ready(function(){
    console.log("Ready!");

    const uuid = "123";
    const ws = new BoardWS('ws://localhost:8080/ws/' + uuid);

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
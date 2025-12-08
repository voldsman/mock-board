<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <title>MockBoard Session</title>

    <link href="/css/bootstrap-4.5.3.min.css" rel="stylesheet">
    <link href="/css/style.css?v=${version}" rel="stylesheet">
</head>
<body>
<h1>MockBoard Session</h1>

<div class="card p-3 mb-3">
    <div id="messages" style="height: 250px; overflow-y: auto; background:#222; padding:10px;"></div>
</div>

<div class="input-group">
    <input id="msgInput" type="text" class="form-control" placeholder="Type message...">
    <div class="input-group-append">
        <button id="sendBtn" class="btn btn-primary">Send</button>
    </div>
</div>

<script src="/js/jquery-3.5.1.slim.min.js"></script>
<script src="/js/bootstrap-4.5.3.bundle.min.js"></script>

<script src="/js/board-ws.js?v=${version}"></script>
<script src="/js/app.js?v=${version}"></script>
</body>
</html>
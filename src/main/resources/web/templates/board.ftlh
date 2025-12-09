<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>MockBoard Board</title>
    <link href="/css/bootstrap-4.5.3.min.css" rel="stylesheet">
    <link href="/css/style.css?v=${version}" rel="stylesheet">
</head>

<body class="d-flex flex-column vh-100">
    <#include "parts/topbar.ftlh">

    <div class="container-fluid flex-grow-1 overflow-hidden mb-3">
        <div class="row h-100">

            <#include "parts/history.ftlh">

            <#include "parts/rules_panel.ftlh">
        </div>
    </div>

    <#include "parts/request_preview.ftlh">
    <#include "parts/modal_rule.ftlh">

    <script src="/js/jquery-3.7.1.min.js"></script>
    <script src="/js/bootstrap-4.5.3.bundle.min.js"></script>
    <script src="/js/board-ws.js?v=${version}"></script>
    <script>
        globalThis.MockBoard = {
            sessionId: "${sessionId}",
            version: "${version}"
        }
    </script>
    <script src="/js/app.js?v=${version}"></script>
</body>
</html>
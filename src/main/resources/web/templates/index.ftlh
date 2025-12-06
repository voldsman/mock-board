<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>

    <style>
        body {
            font-family: sans-serif;
            padding: 40px;
        }
    </style>
</head>
<body>
<h1>${title}</h1>

<p>Hello, <b>${username}</b>!</p>

<#-- Conditional example -->
<#if username == "Voldpix">
    <p>You are the admin.</p>
</#if>
</body>
</html>

<!DOCTYPE html>
<html lang="en" data-bs-theme="dark">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>MockBoard | The Instant API Mock Server & Webhook Tester</title>
    <meta name="description" content="Generate instant fake HTTP endpoints for development. MockBoard is a free, no-login API simulator for frontend testing, webhooks, and prototyping.">
    <meta name="keywords" content="api mock, fake backend, http simulator, webhook tester, json generator, dev tools, api simulation, rest api mock">

    <meta property="og:type" content="website">
    <meta property="og:title" content="MockBoard - Instant FakeEnd">
    <meta property="og:description" content="Stop waiting for the backend. Mock APIs in seconds.">

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/style.css" rel="stylesheet">

    <script src="/js/alpine.min.js" defer></script>

    <script type="application/ld+json">
        {
            "@context": "https://schema.org",
            "@type": "SoftwareApplication",
            "name": "MockBoard",
            "applicationCategory": "DeveloperTool",
            "offers": { "@type": "Offer", "price": "0" }
        }
    </script>
</head>
<body>

<nav class="navbar navbar-expand-lg fixed-top" style="background: rgba(15, 23, 42, 0.8); backdrop-filter: blur(10px); border-bottom: 1px solid rgba(255,255,255,0.05);">
    <div class="container">
        <a class="navbar-brand fw-bold text-white d-flex align-items-center gap-2" href="/">
            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="text-primary" viewBox="0 0 16 16">
                <path d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm-5 6s-1 0-1-1 1-4 6-4 6 3 6 4 1 1 1 1H1zM11 3.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5zm.5 2.5a.5.5 0 0 0 0 1h4a.5.5 0 0 0 0-1h-4zm2 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2zm0 3a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1h-2z"/>
            </svg>
            MockBoard
        </a>
        <div class="d-flex">
            <a href="https://github.com/voldpix/mock-board" target="_blank" rel="noopener" class="btn btn-sm btn-github d-flex align-items-center gap-2">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
                    <path d="M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.87.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27.68 0 1.36.09 2 .27 1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.012 8.012 0 0 0 16 8c0-4.42-3.58-8-8-8z"/>
                </svg>
                <span>Star on GitHub</span>
                <svg xmlns="http://www.w3.org/2000/svg" width="14" height="14" fill="currentColor" class="text-warning" viewBox="0 0 16 16">
                    <path d="M3.612 15.443c-.386.198-.824-.149-.746-.592l.83-4.73L.173 6.765c-.329-.314-.158-.888.283-.95l4.898-.696L7.538.792c.197-.39.73-.39.927 0l2.184 4.327 4.898.696c.441.062.612.636.282.95l-3.522 3.356.83 4.73c.078.443-.36.79-.746.592L8 13.187l-4.389 2.256z"/>
                </svg>
            </a>
        </div>
    </div>
</nav>

<header class="hero-section text-center">
    <div class="glow-effect"></div>
    <div class="container">
        <div class="row justify-content-center">
            <div class="col-lg-8">
                <div class="d-inline-flex align-items-center gap-2 px-3 py-1 rounded-pill border border-primary border-opacity-25 bg-primary bg-opacity-10 text-primary mb-4">
                    <span class="badge bg-primary rounded-pill">Beta</span>
                    <span class="small fw-semibold">v1.0 is live</span>
                </div>

                <h1 class="display-3 fw-bold mb-4">
                    The Instant <span class="text-gradient">FakeEnd</span><br>
                    for Developers.
                </h1>

                <p class="lead text-secondary mb-5 px-lg-5">
                    Simulate HTTP responses, test webhooks, and prototype microservices.
                    No login. No database. Just a disposable URL that works instantly.
                </p>

                <div class="card glass-card mx-auto p-4 mb-5" style="max-width: 480px;">
                    <form action="/session" method="post" x-data="{ loading: false }" @submit.prevent="loading = true; setTimeout(() => $el.submit(), 1000)">
                        <button type="submit" class="btn btn-primary-glow btn-lg w-100 fw-bold py-3 text-white" :disabled="loading">
                            <span x-show="!loading">Start New Session ⚡</span>
                            <span x-show="loading" style="display: none;">
                                    <span class="spinner-border spinner-border-sm me-2"></span> Building Sandbox...
                                </span>
                        </button>
                        <div class="mt-3 d-flex justify-content-evenly text-muted small">
                            <span><span class="text-success">●</span> Free & Anonymous</span>
                            <span><span class="text-success">●</span> 60m Session (Extendable)</span>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <div class="row justify-content-center">
            <div class="col-lg-7">
                <div class="code-block p-3 text-start shadow-lg">
                    <div class="d-flex gap-2 mb-3 border-bottom border-secondary border-opacity-25 pb-2">
                        <div class="rounded-circle bg-danger" style="width:10px; height:10px;"></div>
                        <div class="rounded-circle bg-warning" style="width:10px; height:10px;"></div>
                        <div class="rounded-circle bg-success" style="width:10px; height:10px;"></div>
                    </div>
                    <div class="text-success font-monospace">
                        > curl -X POST https://mockboard.io/b/uuid/api/user <br>
                        > <span class="text-light">{ "id": 84, "status": "active" }</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</header>

<section class="py-2">
    <div class="container">
        <div class="row mb-5 text-center">
            <div class="col-12">
                <h2 class="fw-bold">Why use MockBoard?</h2>
                <p class="text-secondary">Designed for the modern stack.</p>
            </div>
        </div>

        <div class="row g-4 row-cols-1 row-cols-md-3">
            <div class="col">
                <div class="card glass-card h-100 p-4 border-0">
                    <div class="icon-square">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
                            <path d="M0 2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V2zm15 2h-4v3h4V4zm0 4h-4v3h4V8zm0 4h-4v3h3a1 1 0 0 0 1-1v-2zm-5 3v-3H6v3h4zm-5 0v-3H1v2a1 1 0 0 0 1 1h3zm-4-4h4V8H1v3zm0-4h4V4H1v3zm5-3v3h4V4H6zm4 8v3h4v-3h-4z"/>
                        </svg>
                    </div>
                    <h4 class="h5 fw-bold">Frontend Prototyping</h4>
                    <p class="text-secondary small mb-0">Build your React/Vue UI before the backend team finishes the API. Define the JSON structure you need and start coding.</p>
                </div>
            </div>
            <div class="col">
                <div class="card glass-card h-100 p-4 border-0">
                    <div class="icon-square">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
                            <path d="M8 15A7 7 0 1 1 8 1a7 7 0 0 1 0 14zm0 1A8 8 0 1 0 8 0a8 8 0 0 0 0 16z"/>
                            <path d="M10.97 4.97a.235.235 0 0 0-.02.022L7.477 9.417 5.384 7.323a.75.75 0 0 0-1.06 1.06L6.97 11.03a.75.75 0 0 0 1.079-.02l3.992-4.99a.75.75 0 0 0-1.071-1.05z"/>
                        </svg>
                    </div>
                    <h4 class="h5 fw-bold">Edge Case Testing</h4>
                    <p class="text-secondary small mb-0">How does your app handle a 500 error? Or a 401? Force specific status codes to test your error handling logic.</p>
                </div>
            </div>
            <div class="col">
                <div class="card glass-card h-100 p-4 border-0">
                    <div class="icon-square">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" fill="currentColor" viewBox="0 0 16 16">
                            <path d="M1.5 0A1.5 1.5 0 0 0 0 1.5v9A1.5 1.5 0 0 0 1.5 12H13v3l3-3V1.5a1.5 1.5 0 0 0-1.5-1.5h-13zM13 11H1.5a.5.5 0 0 1-.5-.5v-9a.5.5 0 0 1 .5-.5h12a.5.5 0 0 1 .5.5v9a.5.5 0 0 1-.5.5z"/>
                        </svg>
                    </div>
                    <h4 class="h5 fw-bold">Webhook Inspections</h4>
                    <p class="text-secondary small mb-0">Point Stripe, Slack, or GitHub webhooks to your MockBoard URL. Inspect the headers and payload in real-time.</p>
                </div>
            </div>
        </div>
    </div>
</section>

<section class="py-5 bg-black bg-opacity-25">
    <div class="container">
        <div class="row align-items-center">
            <div class="col-lg-6 mb-4 mb-lg-0">
                <h2 class="fw-bold mb-4">Zero Config.<br>Zero Friction.</h2>
                <ul class="list-unstyled">
                    <li class="d-flex mb-4">
                        <div class="text-primary fw-bold h4 me-3">01</div>
                        <div>
                            <h5 class="fw-bold">Start a Session</h5>
                            <p class="text-secondary small">One click generates a unique, private workspace.</p>
                        </div>
                    </li>
                    <li class="d-flex mb-4">
                        <div class="text-primary fw-bold h4 me-3">02</div>
                        <div>
                            <h5 class="fw-bold">Define Rules</h5>
                            <p class="text-secondary small">Map <code>/api/users</code> to return a JSON list of users.</p>
                        </div>
                    </li>
                    <li class="d-flex">
                        <div class="text-primary fw-bold h4 me-3">03</div>
                        <div>
                            <h5 class="fw-bold">Connect your App</h5>
                            <p class="text-secondary small">Point your localhost or staging app to the MockBoard URL.</p>
                        </div>
                    </li>
                </ul>
            </div>
            <div class="col-lg-6">
                <div class="code-block p-4 opacity-75" style="border-left: 4px solid var(--brand-secondary);">
                    <span class="text-secondary">// Server Response</span><br>
                    <span class="text-info">HTTP/1.1</span> <span class="text-warning">200 OK</span><br>
                    <span class="text-secondary">Content-Type:</span> application/json<br><br>
                    {<br>
                    &nbsp;&nbsp;"status": "success",<br>
                    &nbsp;&nbsp;"data": [<br>
                    &nbsp;&nbsp;&nbsp;&nbsp;{ "id": 1, "name": "Alice" }<br>
                    &nbsp;&nbsp;]<br>
                    }
                </div>
            </div>
        </div>
    </div>
</section>

<section class="py-5 container">
    <h3 class="fw-bold text-center mb-5">Frequently Asked Questions</h3>
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="accordion accordion-flush" id="faqAccordion">
                <div class="accordion-item bg-transparent border-bottom border-secondary border-opacity-25">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed bg-transparent text-white shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#faq1">
                            Is this really free?
                        </button>
                    </h2>
                    <div id="faq1" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                        <div class="accordion-body text-secondary">
                            Yes. MockBoard is a free tool for the developer community. We have no hidden tiers or login walls.
                        </div>
                    </div>
                </div>
                <div class="accordion-item bg-transparent border-bottom border-secondary border-opacity-25">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed bg-transparent text-white shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#faq2">
                            Can I save my mocks?
                        </button>
                    </h2>
                    <div id="faq2" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                        <div class="accordion-body text-secondary">
                            Sessions are temporary and expire after 60 minutes of inactivity. However, you can use the "Export Config" feature inside the app to save your work to a file and import it later.
                        </div>
                    </div>
                </div>
                <div class="accordion-item bg-transparent border-bottom border-secondary border-opacity-25">
                    <h2 class="accordion-header">
                        <button class="accordion-button collapsed bg-transparent text-white shadow-none" type="button" data-bs-toggle="collapse" data-bs-target="#faq3">
                            Is my data private?
                        </button>
                    </h2>
                    <div id="faq3" class="accordion-collapse collapse" data-bs-parent="#faqAccordion">
                        <div class="accordion-body text-secondary">
                            Yes. Your session URL is unique to you. We do not sell data or persist it to a disk. Once your session expires, all data is permanently wiped from the server.
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<footer class="py-4 mt-5 border-top border-secondary border-opacity-10 text-center">
    <div class="container text-secondary small">
        <p class="mb-1">&copy; 2025 MockBoard. All rights reserved.</p>
        <p>
            Powered by
            <span class="text-white">Java 21</span>,
            <span class="text-white">Javalin</span>, and
            <span class="text-white">Virtual Threads</span>.
        </p>
    </div>
</footer>

<script src="/js/bootstrap.bundle.min.js"></script>
</body>
</html>
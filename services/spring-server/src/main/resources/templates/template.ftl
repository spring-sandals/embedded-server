<#macro noauthentication title="Welcome">
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name=viewport content="width=device-width, initial-scale=1">
        <meta name="theme-color" content="#ff835c">
        <title>Spring Sandals</title>
        <link rel="stylesheet" href="/styles/reset.css">
        <link rel="stylesheet" href="/styles/site.css">
        <link rel="icon" type="image/svg" href="/images/favicon.svg">
        <meta name="title" property="og:title" content="Spring Sandals">
        <meta name="type" property="og:type" content="website">
        <meta name="description" property="og:description" content="Lightweight application using the Spring Framework">
    </head>
    <body>
    <header class="container">
        <h1>Spring Sandals</h1>
        <p>
            A kotlin-jetty-spring example with a small set of runtime dependencies.
        </p>
    </header>
    <#nested />
    </body>
    </html>
</#macro>
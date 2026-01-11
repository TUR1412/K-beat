<#-- Atomic design: page-level "template" wrapper (head + body start). -->
<#assign _assetsVersion = assetsVersion!'dev'>
<#assign _pageTitle = pageTitle!'K-beat'>
<#assign _pageDescription = pageDescription!'把每件事都打在节拍上：服务端渲染为主 + 渐进增强 + 持久化的待办应用'>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="color-scheme" content="light dark">
    <meta name="theme-color" content="#5b6cff">
    <meta name="description" content="${_pageDescription?html}">
    <title>${_pageTitle?html}</title>
    <link rel="icon" type="image/svg+xml" href="/assets/favicon.svg?v=${_assetsVersion?url}">
    <link rel="stylesheet" href="/assets/app.css?v=${_assetsVersion?url}">
    <script src="/assets/app.js?v=${_assetsVersion?url}" defer></script>
</head>
<body<#if bodyDataToday??> data-today="${bodyDataToday?html}"</#if><#if requestId??> data-request-id="${requestId?html}"</#if>>


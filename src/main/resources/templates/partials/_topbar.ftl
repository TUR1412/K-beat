<#-- Atomic design: organism-level topbar. -->
<#assign _topbarTitle = topbarTitle!'K-beat'>
<#assign _topbarSubtitle = topbarSubtitle!'把每件事都打在节拍上'>

<header class="topbar" role="banner">
    <div class="brand">
        <div class="brand-mark" aria-hidden="true">K</div>
        <div class="brand-text">
            <div class="brand-title">${_topbarTitle?html}</div>
            <div class="brand-subtitle">${_topbarSubtitle?html}</div>
        </div>
    </div>
    <div class="topbar-actions">
        <button id="themeToggle" class="icon-button" type="button" aria-label="切换主题" title="切换主题">
            <span class="icon-button__dot" aria-hidden="true"></span>
        </button>
    </div>
</header>


<#import "template.ftl" as layout>

<@layout.registrationLayout displayMessage=false; section>
<#if section == "form">
<main class="codegap-info-shell">
    <section class="codegap-info-card" aria-labelledby="expired-title">
        <div class="login-brand"><span class="brand-mark" aria-hidden="true"><svg viewBox="0 0 32 32"><path d="M17.9 3.5 7.3 17.1h7.4l-.6 11.4 10.6-14.2h-7.3l.5-10.8Z"></path></svg></span><span>Code<span class="brand-accent">Gap</span></span></div>
        <div class="info-icon info-icon-warning" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M12 8v5M12 17h.01"></path><path d="m10.3 3.8-8 14A2 2 0 0 0 4 20.8h16a2 2 0 0 0 1.7-3l-8-14a2 2 0 0 0-3.4 0Z"></path></svg></div>
        <h1 id="expired-title">This link has expired</h1>
        <p class="info-copy">${kcSanitize(message.summary)?no_esc}</p>
        <div class="info-actions"><a class="info-primary-btn" href="${url.loginUrl}">Back to login</a></div>
    </section>
</main>
</#if>
</@layout.registrationLayout>

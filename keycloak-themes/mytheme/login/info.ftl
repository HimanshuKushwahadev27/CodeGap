<#import "template.ftl" as layout>

<@layout.registrationLayout displayMessage=false; section>
<#if section == "form">
<main class="codegap-info-shell">
    <section class="codegap-info-card" aria-labelledby="info-title">
        <div class="login-brand">
            <span class="brand-mark" aria-hidden="true"><svg viewBox="0 0 32 32"><path d="M17.9 3.5 7.3 17.1h7.4l-.6 11.4 10.6-14.2h-7.3l.5-10.8Z"></path></svg></span>
            <span>Code<span class="brand-accent">Gap</span></span>
        </div>
        <#if message.type == "success">
            <div class="info-icon" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="m5 12 4 4L19 6"></path><circle cx="12" cy="12" r="9"></circle></svg></div>
            <h1 id="info-title">Your email has been verified</h1>
        <#elseif message.type == "error">
            <div class="info-icon info-icon-error" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M12 8v5M12 17h.01"></path><path d="m10.3 3.8-8 14A2 2 0 0 0 4 20.8h16a2 2 0 0 0 1.7-3l-8-14a2 2 0 0 0-3.4 0Z"></path></svg></div>
            <h1 id="info-title"><#if messageHeader??>${kcSanitize(messageHeader)?no_esc}<#else>We could not confirm your email</#if></h1>
        <#else>
            <div class="info-icon info-icon-neutral" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M12 16v-4M12 8h.01"></path><circle cx="12" cy="12" r="9"></circle></svg></div>
            <h1 id="info-title"><#if messageHeader??>${kcSanitize(messageHeader)?no_esc}<#else>Confirm your email address</#if></h1>
        </#if>
        <p class="info-copy">${kcSanitize(message.summary)?no_esc}</p>
        <div class="info-actions">
            <#if pageRedirectUri?has_content>
                <a class="info-primary-btn" href="${pageRedirectUri}">Confirm email</a>
            <#elseif actionUri?has_content>
                <a class="info-primary-btn" href="${actionUri}">Continue</a>
            <#elseif client.baseUrl?has_content>
                <a class="info-primary-btn" href="${client.baseUrl}">Continue</a>
            </#if>
            <p class="info-secondary">Wrong account? <a href="${url.loginUrl}">Back to login</a></p>
        </div>
        <#if skipLink?? && skipLink>
            <p class="info-skip"><a href="${pageRedirectUri}">Skip this step</a></p>
        </#if>
    </section>
</main>
</#if>
</@layout.registrationLayout>

<#import "template.ftl" as layout>

<@layout.registrationLayout displayMessage=false; section>
<#if section == "form">
<main class="codegap-info-shell">
    <section class="codegap-info-card codegap-form-card" aria-labelledby="reset-title">
        <div class="login-brand">
            <span class="brand-mark" aria-hidden="true"><svg viewBox="0 0 32 32"><path d="M17.9 3.5 7.3 17.1h7.4l-.6 11.4 10.6-14.2h-7.3l.5-10.8Z"></path></svg></span>
            <span>Code<span class="brand-accent">Gap</span></span>
        </div>
        <div class="info-icon info-icon-neutral" aria-hidden="true"><svg viewBox="0 0 24 24"><path d="M7 10V7a5 5 0 0 1 10 0v3M5 10h14v11H5V10Z"></path></svg></div>
        <h1 id="reset-title">Reset your password</h1>
        <p class="info-copy">Enter your email or username and we'll send you a reset link.</p>

        <#if message?has_content>
            <div class="login-alert login-alert-${message.type}" role="alert">
                <span class="alert-icon" aria-hidden="true">i</span>
                <span>${kcSanitize(message.summary)?no_esc}</span>
            </div>
        </#if>

        <form id="kc-reset-password-form" class="info-form" action="${url.loginAction}" method="post">
            <div class="form-field">
                <label for="username">Email or username</label>
                <div class="input-wrap">
                    <svg class="input-icon" viewBox="0 0 24 24" aria-hidden="true"><path d="M4 5h16v14H4zM4 6l8 6 8-6"></path></svg>
                    <input type="text" id="username" name="username" value="${(login.username!'')}" autofocus autocomplete="username" required>
                </div>
            </div>
            <button class="info-primary-btn" type="submit">Send reset link</button>
        </form>
        <p class="info-secondary">Remember your password? <a href="${url.loginUrl}">Back to login</a></p>
    </section>
</main>
</#if>
</@layout.registrationLayout>
